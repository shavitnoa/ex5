package world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {
    // CONSTANTS
    private static final float SUN_RADIUS = 120;
    private static final float FINAL_ANGLE = 360f;
    private static final float START_ANGLE = 0f;
    private static final float X_ELLIPSE_CENTER_FACTOR = 0.5f;
    private static final float Y_ELLIPSE_CENTER_FACTOR = 0.65f;
    private static final float STRETCH_ELLIPSE_FACTOR = 0.6f;

    private static final Color SUN_COLOR = new Color(238, 223, 140, 255);

    // --------------------------------- STRINGS FOR GAME ---------------------------------
    private static final String SUN = "sun";
    public static GameObject create(
            Vector2 windowDimensions,
            float cycleLength,
            GameObjectCollection gameObjects,
            int layer){

        OvalRenderable sunRenderable = new OvalRenderable(SUN_COLOR);
        GameObject sun = new GameObject(Vector2.ZERO, new Vector2(SUN_RADIUS, SUN_RADIUS), sunRenderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN);
        gameObjects.addGameObject(sun, layer);

        sunCycleTransition(windowDimensions, cycleLength, sun);

        return sun;

    }
    /*
   Responsible for the cycle transition of the sun.
    */
    private static void sunCycleTransition(Vector2 windowDimensions, float cycleLength, GameObject sun) {
        Vector2 centerOfSun =windowDimensions.multX(X_ELLIPSE_CENTER_FACTOR).multY(Y_ELLIPSE_CENTER_FACTOR);
        Vector2 xStretchForEllipse = Vector2.RIGHT.mult((windowDimensions.x()*STRETCH_ELLIPSE_FACTOR));
        Vector2 yStretchForEllipse = Vector2.UP.mult((windowDimensions.y()*STRETCH_ELLIPSE_FACTOR));

        new Transition<Float>(sun,
                angle -> sun.setCenter(
                        centerOfSun.add(xStretchForEllipse.mult((float) Math.sin(Math.toRadians(angle)))).
                                add(yStretchForEllipse.mult((float) Math.cos(Math.toRadians(angle))))),
                START_ANGLE, FINAL_ANGLE, Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_LOOP, null);


    }
    static float getSunRadius(){
        return SUN_RADIUS;
    }
}
