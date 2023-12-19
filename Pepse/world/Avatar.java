package world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Avatar extends GameObject {
    // --------------------------------- STRINGS FOR GAME ---------------------------------
    private static final String AVATAR = "avatar";
    private static final float AVATAR_SIZE = 40;
    private static UserInputListener inputListener;


    // -------------------------------------- METHODS --------------------------------------

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    private Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader){
        Avatar avatar = new Avatar(topLeftCorner, Vector2.ONES.mult(AVATAR_SIZE), null);
        Avatar.inputListener = inputListener;
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        avatar.setTag(AVATAR);
        return avatar;
    }
}
