package world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import util.ColorSupplier;
import world.Block;

import java.awt.*;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class Tree {
    // -------------------------------------- PRIVATE -------------------------------------
    // CONSTANTS
    private static final int BOUND_VALUE_FOR_TREES = 10;
    private static final int RANGE_VALUE_FOR_PLACING_LEAF = 10;
    private static final int BOUND_VALUE_FOR_PLACING_LEAF = 7;
    private static final int BOUND_VALUE_FOR_TREE_HIGH = 8;
    private static final int MIN_TREE_HIGH = 7;
    private static final float FACTOR_FOR_TOP_SIZE = 4 / 5f;

    private final int treesLayer;
    private final int leavesLayer;
    private final int layerLeavesShouldCollide;
    private final int fallingLeavesLayer;
    private final int layerTreeShouldCollide;
    private final int seed;
    private static final Color BASE_TREE_COLOR = new Color(100, 50, 20);
    private final GameObjectCollection gameObjects;
    private final Function<Integer, Float> groundHeightAt;
    private final Random random = new Random();
    // mapping xCoordinate to set of objects which related to this location
//    private static final HashMap<Integer, HashSet<RemovableObject>> locationToObjsMap = new HashMap<>();

    // --------------------------------- STRINGS FOR GAME ---------------------------------
    private static final String STEM = "stem";
    // -------------------------------------- METHODS --------------------------------------

    /**
     * Constructor. Construct a new tree instance.
     *
     * @param gameObjects              The collection of all participating game objects.
     * @param groundHeightAt           groundHeightAt function.
     * @param treesLayer               layer of the trees
     * @param leavesLayer              layer of the leaves
     * @param fallingLeavesLayer   layer of the falling leaves
     * @param layerLeavesShouldCollide the layer of the object which should collide with the leaves - terrain
     *                                 layer.
     * @param layerTreeShouldCollide   the layer of the object which should collide with the tree - avatar
     *                                 layer
     * @param seed                     seed
     */
    public Tree(GameObjectCollection gameObjects, Function<Integer, Float> groundHeightAt,
                int treesLayer, int leavesLayer, int fallingLeavesLayer, int layerLeavesShouldCollide,
                int layerTreeShouldCollide, int seed) {
        this.groundHeightAt = groundHeightAt;
        this.gameObjects = gameObjects;
        this.treesLayer = treesLayer;
        this.leavesLayer = leavesLayer;
        this.fallingLeavesLayer = fallingLeavesLayer;
        this.layerLeavesShouldCollide = layerLeavesShouldCollide; // terrain layer
        this.layerTreeShouldCollide = layerTreeShouldCollide; // avatar layer
        this.seed = seed;
    }
    public void createInRange(int minX, int maxX) {
        minX -= minX % Block.SIZE;
        maxX += maxX % Block.SIZE;
        for (int coordinateX = minX; coordinateX < maxX; coordinateX+=Block.SIZE) {
            this.random.setSeed(Objects.hash(coordinateX, seed));
            if(random.nextInt(BOUND_VALUE_FOR_TREES)==0){
                int treeHigh = random.nextInt(BOUND_VALUE_FOR_TREE_HIGH) +MIN_TREE_HIGH;
                createStem(new Vector2(coordinateX, groundHeightAt.apply(coordinateX)-Block.SIZE),treeHigh);
                createLeaves(new Vector2(coordinateX, groundHeightAt.apply(coordinateX)-Block.SIZE),treeHigh);
            }

        }
    }


    /*
    Create stem for a current tree.
    */
    private void createStem(Vector2 topLeftCorner, int treeHigh) {
        for (int high = 0; high < treeHigh; high++) {
            Block stem = new Block(
                    new Vector2(topLeftCorner.x(), topLeftCorner.y() - high * Block.SIZE),
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_TREE_COLOR)));
            gameObjects.addGameObject(stem, this.treesLayer);
            stem.setTag(STEM);
        }
    }

    private void createLeaves(Vector2 treeCoordinates, int treeHigh) {
        int topOfTheTreeSize = (int) (treeHigh * FACTOR_FOR_TOP_SIZE);

        Vector2 leavesTopLeftCorner = new Vector2((float) (treeCoordinates.x() - (topOfTheTreeSize / 2) *
                Block.SIZE), (float) (treeCoordinates.y() - (topOfTheTreeSize / 2 + treeHigh) * Block.SIZE));

        for (int row = 0; row < topOfTheTreeSize; row++) {
            for (int column = 1; column <= topOfTheTreeSize; column++) {
                if (this.random.nextInt(RANGE_VALUE_FOR_PLACING_LEAF) <= BOUND_VALUE_FOR_PLACING_LEAF) {
                    Vector2 placeForLeaf = new Vector2(leavesTopLeftCorner.x() + row * Block.SIZE,
                            leavesTopLeftCorner.y() + column * Block.SIZE);

                    // create leaf
                    Leaf leaf = new Leaf(placeForLeaf, this.leavesLayer, this.fallingLeavesLayer, this.gameObjects);
                    this.gameObjects.addGameObject(leaf, this.leavesLayer);
                }
            }
        }
    }
}
