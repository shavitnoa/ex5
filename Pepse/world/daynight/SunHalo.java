package world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {
    private static final float SUN_HALO_RADIUS = Sun.getSunRadius() + 120;
    private static final String SUN_HALO = "sunHalo";

    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color){
        OvalRenderable SunHaloRenderable = new OvalRenderable(color);
        GameObject sunHalo = new GameObject(Vector2.ZERO, new Vector2(SUN_HALO_RADIUS, SUN_HALO_RADIUS), SunHaloRenderable);
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        sunHalo.setTag(SUN_HALO);
        gameObjects.addGameObject(sunHalo, layer);
        return sunHalo;
    }

}
