import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import util.ColorSupplier;
import world.Sky;
import world.Terrain;
import world.daynight.Night;
import world.daynight.Sun;
import world.daynight.SunHalo;
import world.trees.Tree;

import java.awt.*;
import java.util.Random;

public class PepseGameManager extends GameManager {
    private static final int GROUND_EXTERIOR_LAYER = Layer.STATIC_OBJECTS;

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Random random = new Random();

        Vector2 windowDimensions = windowController.getWindowDimensions();
        Sky.create(gameObjects(), windowDimensions, Layer.BACKGROUND);
        Terrain terrain = new Terrain(gameObjects(), Layer.STATIC_OBJECTS, windowDimensions);
        terrain.createInRange(0,1300);
        Night.create(gameObjects(),Layer.FOREGROUND, windowDimensions, 30);
        GameObject sun = Sun.create(windowDimensions, 30, gameObjects(),  Layer.BACKGROUND+1);
        SunHalo.create(gameObjects(), Layer.BACKGROUND+2, sun, new Color(255, 255, 0, 20));
        Tree tree = new Tree(gameObjects(), terrain::groundHeightAt, Layer.BACKGROUND+3, Layer.BACKGROUND+4,Layer.BACKGROUND+5,0 ,
                0, random.nextInt());
        tree.createInRange(0,1300);


        gameObjects().addGameObject(new GameObject(Vector2.ZERO, windowDimensions, null),Layer.BACKGROUND+5);
        gameObjects().layers().shouldLayersCollide(Layer.BACKGROUND+5, GROUND_EXTERIOR_LAYER, true);
    }

    public static void main(String[] args) { new PepseGameManager().run();}

}
