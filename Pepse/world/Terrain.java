package world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import util.ColorSupplier;

import java.awt.*;

public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    /**
     * the relative size of the window that the ground is going to cover
     */
    private static final float TERRAIN_HEIGHT = 0.7f;
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final float groundHeightAtX0;

    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer,
                   Vector2 windowDimensions) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.groundHeightAtX0 = windowDimensions.y()*TERRAIN_HEIGHT;
    }
    public float groundHeightAt(float x) {
//        float z = (float) (groundHeightAtX0 + (Math.sin(x / 5)) * 3 * Block.SIZE);
//        return z - z % Block.SIZE;
        return groundHeightAtX0;
    }
    public void createInRange(int minX, int maxX) {
        minX -= minX % Block.SIZE;
        maxX += maxX % Block.SIZE;
//       maxX = (int) (Math.ceil((double) maxX / Block.SIZE)*Block.SIZE);
        for (int x = minX; x < maxX ; x+=Block.SIZE) {
            for (int i = 0; i < TERRAIN_DEPTH; i++) {
                int y = (int) (Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE + i*Block.SIZE);
                Block ground = new Block(
                        new Vector2(x, y),
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                gameObjects.addGameObject(ground, groundLayer);
                ground.setTag("ground");

            }

        }






    }
}

