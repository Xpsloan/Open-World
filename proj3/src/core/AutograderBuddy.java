package core;
import tileengine.TETile;
import tileengine.Tileset;

public class AutograderBuddy {

    /**
     * Simulates a game, but doesn't render anything or call any StdDraw
     * methods. Instead, returns the world that would result if the input string
     * had been typed on the keyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quit and
     * save. To "quit" in this method, save the game to a file, then just return
     * the TETile[][]. Do not call System.exit(0) in this method.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */

    public static final int PRODUCT = 10;
    public static final int WIDTH = 75;
    public static final int HEIGHT = 40;


    public static TETile[][] getWorldFromInput(String input) {
        String[] values = input.split("");
        int seed = 0;

        if (values[0].equals("L") || values[0].equals("l")) {
            World world = SaveLoad.loadGame();
            int i = 1;
            String prev = "";
            while (i < values.length) {
                if (prev.equals(":") && (values[i].equals("Q") || values[i].equals("q"))) {
                    SaveLoad.saveGame(world);
                    return world.getWorld();
                }
                world.getPlayer().move(values[i].charAt(0));
                prev = values[i];
                i += 1;
            }
            return world.getWorld();
        } else {
            // get seed
            int i = 1;
            while (!values[i].equals("S") && !values[i].equals("s")) {
                seed = seed * PRODUCT + Integer.parseInt(values[i]);
                i += 1;
            }
            World world = new World(WIDTH, HEIGHT, seed);
            i += 1;
            String prev = "";
            while (i < values.length) {
                if (prev.equals(":") && (values[i].equals("Q") || values[i].equals("q"))) {
                    SaveLoad.saveGame(world);
                    return world.getWorld();
                }
                world.getPlayer().move(values[i].charAt(0));
                prev = values[i];
                i += 1;
            }
            return world.getWorld();
        }
    }



    /**2o
     * Used to tell the autograder which tiles are the floor/ground (including
     * any lights/items resting on the ground). Change this
     * method if you add additional tiles.
     */
    public static boolean isGroundTile(TETile t) {
        return t.character() == Tileset.FLOOR.character()
                || t.character() == Tileset.AVATAR.character()
                || t.character() == Tileset.FLOWER.character();
    }

    /**
     * Used to tell the autograder while tiles are the walls/boundaries. Change
     * this method if you add additional tiles.
     */
    public static boolean isBoundaryTile(TETile t) {
        return t.character() == Tileset.WALL.character()
                || t.character() == Tileset.LOCKED_DOOR.character()
                || t.character() == Tileset.UNLOCKED_DOOR.character();
    }

}
