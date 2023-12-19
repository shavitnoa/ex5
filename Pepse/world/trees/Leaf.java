package world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import util.ColorSupplier;
import world.Block;

import java.awt.*;
import java.util.Random;


public class Leaf extends GameObject {
    private static final float FALL_SPEED = 30;
    private static final float DEATH_TIME_BOUND = 5;
    private static final float ANGLE_TRANSITION_TIME = 4;
    private static final float WIDTH_TRANSITION_TIME = 4;
    private static final float HORIZONTAL_TRANSITION_TIME = 1;
    private static final float INITIAL_ANGLE = -8f;
    private static final float FINAL_ANGLE = 8f;
    private static final float FINAL_WIDTH = 1.3f;
    private static final float INITIAL_WIDTH = 1f;
    private static final float INITIAL_HORIZONTAL = -40f;
    private static final float FINAL_HORIZONTAL = 40f;
    private final float DILAY_TIME_BOUND = 5;
    private final float LIFE_TIME_BOUND = 200;


    private static final Color BASE_LEAF_COLOR = new Color(100, 180, 50);
    private static final Vector2 leafDimensions = new Vector2(Block.SIZE, Block.SIZE);
    private static final RectangleRenderable leafRenderable =
            new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAF_COLOR));
    private final Random random = new Random();
    private final Vector2 originalTopLeftCorner;
    private final GameObjectCollection gameObjects;
    private final int staticLeavesLayer;
    private final int fallingLeavesLayer;
    private Transition<Float> horizontalTransition;
    private Transition<Float> angleTransition;
    private Transition<Float> widthTransition;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner      Position of the object, in window coordinates (pixels).
     *                           Note that (0,0) is the top-left corner of the window.
     * @param fallingLeavesLayer
     */
    public Leaf(Vector2 topLeftCorner, int staticLeavesLayer, int fallingLeavesLayer, GameObjectCollection gameObjects) {
        super(topLeftCorner, leafDimensions, new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAF_COLOR)));
        this.originalTopLeftCorner = topLeftCorner;
        this.gameObjects = gameObjects;
        this.staticLeavesLayer = staticLeavesLayer;
        this.fallingLeavesLayer = fallingLeavesLayer;

        createTransitions();
        startCycleLife();
    }
    private void createTransitions(){
        this.angleTransition= new Transition<Float>(this,
                this.renderer()::setRenderableAngle,
                INITIAL_ANGLE, FINAL_ANGLE, Transition.CUBIC_INTERPOLATOR_FLOAT, ANGLE_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        this.widthTransition = new Transition<Float>(this,
                (width)->this.setDimensions(leafDimensions.multX(width)),
                INITIAL_WIDTH, FINAL_WIDTH, Transition.LINEAR_INTERPOLATOR_FLOAT, WIDTH_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        this.horizontalTransition = new Transition<Float>(
                this,
                this.transform()::setVelocityX,
                INITIAL_HORIZONTAL, FINAL_HORIZONTAL, Transition.CUBIC_INTERPOLATOR_FLOAT, HORIZONTAL_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }
    private void startCycleLife(){
        stopMoving();
        changeLayer(this.fallingLeavesLayer, this.staticLeavesLayer);
        this.setTopLeftCorner(this.originalTopLeftCorner);
        this.renderer().setOpaqueness(1);

        new ScheduledTask(this, random.nextFloat(DILAY_TIME_BOUND), false, this::shaking);
        new ScheduledTask(this, random.nextFloat(LIFE_TIME_BOUND), false, this::falling);


    }
    private void shaking(){
        this.addComponent(this.angleTransition);
        this.addComponent(this.widthTransition);
    }

    private void falling(){

        changeLayer(this.staticLeavesLayer, this.fallingLeavesLayer);
        this.transform().setVelocity(Vector2.DOWN.mult(FALL_SPEED));
        this.addComponent(this.horizontalTransition);
        this.renderer().fadeOut(10, () -> new ScheduledTask(this, random.nextFloat(DEATH_TIME_BOUND),
                false, this::startCycleLife));

    }

    private void changeLayer(int currentLayer, int desiredLayer) {
        this.gameObjects.removeGameObject(this, currentLayer);
        this.gameObjects.addGameObject(this, desiredLayer);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        stopMoving();
    }
    private void stopMoving(){
        this.removeComponent(this.horizontalTransition);
        this.removeComponent(this.angleTransition);
        this.removeComponent(this.widthTransition);
        this.setVelocity(Vector2.ZERO);
    }
}
