package core;
import java.io.*;
import java.util.ArrayList;

public class SaveLoad {
    public static void saveGame(World w) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("save-file.txt"));
            writer.write("\n" + w.getWidth());
            writer.write("\n" + w.getHeight());
            writer.write("\n" + w.getPlayer().getPosition().getX() + "," + w.getPlayer().getPosition().getY());
            String coins = "\n";
            for (Coordinate c: w.getCoins()) {
                coins += c.getX() + ",";
                coins += c.getY() + " ";
            }
            writer.write(coins);
            String ghosts = "\n";
            for (Ghost g: w.getGhosts()) {
                ghosts += g.getPosition().getX() + ",";
                ghosts += g.getPosition().getY() + " ";
            }
            writer.write(ghosts);
            writer.write("\n" + w.getSeed());

            if (w.getShowPaths()) {
                writer.write("\n" + "1");
            } else {
                writer.write("\n" + "0");
            }

            if (w.getShowWholeWorld()) {
                writer.write("\n" + "1");
            } else {
                writer.write("\n" + "0");
            }


            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean canLoadFull() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("save-file.txt"));
            br.readLine();
            if (br.readLine() == null) {
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static World loadGame() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("save-file.txt"));
            reader.readLine();

            // get Dimensions
            int width = Integer.parseInt(reader.readLine());
            int height = Integer.parseInt(reader.readLine());


            // get the players x,y coordinates
            String playCoordinates = reader.readLine();
            String[] coordinates = playCoordinates.split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            Coordinate playerCoordinate = new Coordinate(x, y);

            // get Coin Coordinates
            ArrayList<Coordinate> coins = new ArrayList<>();
            String coinCoordinates = reader.readLine();
            coordinates = coinCoordinates.split(" ");
            for (int i = 0; i < coordinates.length; i += 1) {
                String[] oneCoin = coordinates[i].split(",");
                x = Integer.parseInt(oneCoin[0]);
                y = Integer.parseInt(oneCoin[1]);
                coins.add(new Coordinate(x, y));
            }

            // get Ghost Coordinates
            ArrayList<Coordinate> ghosts = new ArrayList<>();
            String ghostCoordinates = reader.readLine();
            coordinates = ghostCoordinates.split(" ");
            for (int i = 0; i < coordinates.length; i += 1) {
                String[] oneGhost = coordinates[i].split(",");
                x = Integer.parseInt(oneGhost[0]);
                y = Integer.parseInt(oneGhost[1]);
                ghosts.add(new Coordinate(x, y));
            }

            // get the seed
            int seed = Integer.parseInt(reader.readLine());

            // get booleans
            boolean showPaths;
            if (Integer.parseInt(reader.readLine()) == 1) {
                showPaths = true;
            } else {
                showPaths = false;
            }

            boolean showWorld;
            if (Integer.parseInt(reader.readLine()) == 1) {
                showWorld = true;
            } else {
                showWorld = false;
            }

            reader.close();
            return new World(width, height, seed, playerCoordinate, ghosts, coins, showPaths, showWorld);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
