package world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import util.ColorSupplier;

import java.awt.*;

public class Night {
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){

        GameObject night = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(ColorSupplier.approximateColor(Color.BLACK)));
        gameObjects.addGameObject(night, layer);
        night.setTag("night");
        new Transition<Float>(
                night, //the game object being changed
                night.renderer()::setOpaqueness,  //the method to call
                0f,    //initial transition value
                MIDNIGHT_OPACITY,   //final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT,  //use a cubic interpolator
                cycleLength/2,   //transtion fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);  //nothing further to execute upon reaching final value
        return night;
    }
}
