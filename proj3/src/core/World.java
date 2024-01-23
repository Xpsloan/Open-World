package core;
import java.awt.*;
import java.util.List;
import edu.princeton.cs.algs4.*;
import tileengine.TETile;
import tileengine.Tileset;
import tileengine.TERenderer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.*;


public class World {
    private TETile[][] world;
    private int worldWidth;
    private int worldHeight;
    private Random random;
    private List<Room> allRooms;
    private long seed;
    private List<Coordinate> oSpace;
    private Player player;
    private TERenderer ter;
    private List<Coordinate> coins;
    private boolean firstRender;
    private List<Ghost> ghosts;
    private boolean lineOfSightEnabled = true;
    private boolean showWholeWorld = false;
    private int totalCoins;

    public World(int width, int height, long s) {
        this.seed = s;
        this.worldWidth = width;
        this.worldHeight = height;
        this.random = new Random(seed);
        this.world = new TETile[width][height];
        this.allRooms = new ArrayList<>();
        oSpace = new ArrayList<>();
        coins = new ArrayList<>();
        firstRender = true;
        this.ghosts = new ArrayList<>();
        this.ter = new TERenderer();

        initializeWorld();
        generateWorld();
        generateCoins();
        totalCoins = coins.size();
        initializeGhosts();

        Coordinate initialPlayerPosition = getBottomRight();
        this.player = new Player(world, initialPlayerPosition);
        firstRender = false;
    }

    public int getSeed() {
        return (int) seed;
    }
    public List<Coordinate> getCoins() {
        return coins;
    }
    public List<Ghost> getGhosts() {
        return ghosts;
    }

    public World(int width, int height, long s, Coordinate playerCoordinate, List<Coordinate> gh,
                 List<Coordinate> co, boolean sP, boolean sW) {
        this.seed = s;
        this.worldWidth = width;
        this.worldHeight = height;
        this.random = new Random(seed);
        this.world = new TETile[width][height];
        this.allRooms = new ArrayList<>();
        oSpace = new ArrayList<>();
        coins = new ArrayList<>();
        firstRender = true;
        this.ghosts = new ArrayList<>();
        this.ter = new TERenderer();

        initializeWorld();
        generateWorld();
        generateCoins();
        totalCoins = coins.size();
        initializeGhosts();

        // getRidOfCoins
        List<Coordinate> getRidCoins = coins.subList(0, coins.size());
        getRidCoins.removeAll(co);
        for (Coordinate c: getRidCoins) {
            world[c.getX()][c.getY()] = Tileset.FLOOR;
        }
        coins = co;
        // done


        // moving player position to before save
        this.player = new Player(world, playerCoordinate);
        //done

        // moving ghost positions
        for (int i = 0; i < 2; i += 1) {
            ghosts.get(i).movePosition(gh.get(i));
        }
        // done

        // maintaining game environment
        showGhostPath = sP;
        showWholeWorld = sW;


        firstRender = false;
    }


    public boolean getShowWholeWorld() {
        return showWholeWorld;
    }
    public boolean getShowPaths() {
        return showGhostPath;
    }


    public void generateCoin() {
        for (int i = 0; i < TRIES; i += 1) {
            int x = random.nextInt(worldWidth);
            int y = random.nextInt(worldHeight);
            if (world[x][y].equals(Tileset.FLOOR)) {
                coins.add(new Coordinate(x, y));
                world[x][y] = Tileset.UNLOCKED_DOOR;
                return;
            }
        }
    }

    private final int COIN = 20;
    public void generateCoins() {
        for (int i = 0; i < COIN; i += 1) {
            generateCoin();
        }
    }

    private Coordinate getTopLeft() {
        for (int y = world[0].length - 1; y >= 0; y -= 1) {
            for (int x = 0; x < world.length; x += 1) {
                if (world[x][y].equals(Tileset.FLOOR)) {
                    return new Coordinate(x, y);
                }
            }
        }
        return null;
    }


    private Coordinate getBottomLeft() {
        for (int x = 0; x < world.length; x += 1) {
            for (int y = 0; y < world[0].length; y += 1) {
                if (world[x][y].equals(Tileset.FLOOR)) {
                    return new Coordinate(x, y);
                }
            }
        }
        return null;
    }


    private Coordinate getBottomRight() {
        for (int x = world.length - 1; x >= 0; x -= 1) {
            for (int y = 0; y < world[0].length; y += 1) {
                if (world[x][y].equals(Tileset.FLOOR)) {
                    return new Coordinate(x, y);
                }
            }
        }
        return null;
    }


    private void initializeGhosts() {
        Ghost ghost = new Ghost(world, getBottomLeft());
        ghosts.add(ghost);
        ghost = new Ghost(world, getTopLeft());
        ghosts.add(ghost);
    }

    private int ghostMoveCounter = 0;
    private final int delay = 100;

    private void moveGhosts() {
        ghostMoveCounter++;
        if (ghostMoveCounter >= delay) {
            for (Ghost ghost : ghosts) {
                ghost.move(player.getPosition(), world);
            }
            ghostMoveCounter = 0;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void initializeRender() {
        this.ter.initialize(worldWidth, worldHeight);
    }

    private void initializeWorld() {
        for (int x = 0; x < worldWidth; x++) {
            for (int y = 0; y < worldHeight; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }
    private void generateWorld() {
        generateRooms();
        for (Room room : allRooms) {
            room.generateRoom(world);
        }
        findOpenSpaces();
        connectAllRooms();
        makeWalls();
    }



    // Below are the methods for making the room and hallways
    public void makeWallForCoordinate(int x, int y) {
        if (x + 1 < worldWidth && world[x + 1][y].equals(Tileset.NOTHING)) {
            world[x + 1][y] = Tileset.WALL;
        }
        if (x - 1 >= 0 && world[x - 1][y].equals(Tileset.NOTHING)) {
            world[x - 1][y] = Tileset.WALL;
        }
        if (y + 1 < worldHeight && world[x][y + 1].equals(Tileset.NOTHING)) {
            world[x][y + 1] = Tileset.WALL;
        }
        if (y - 1 >= 0 && world[x][y - 1].equals(Tileset.NOTHING)) {
            world[x][y - 1] = Tileset.WALL;
        }
        if (x - 1 >= 0 && y - 1 >= 0 && world[x - 1][y - 1].equals(Tileset.NOTHING)) {
            world[x - 1][y - 1] = Tileset.WALL;
        }
        if (x + 1 < worldWidth && y + 1 < worldHeight && world[x + 1][y + 1].equals(Tileset.NOTHING)) {
            world[x + 1][y + 1] = Tileset.WALL;
        }
        if (x + 1 < worldWidth && y - 1 >= 0 && world[x + 1][y - 1].equals(Tileset.NOTHING)) {
            world[x + 1][y - 1] = Tileset.WALL;
        }
        if (x - 1 >= 0 && y + 1 < worldHeight && world[x - 1][y + 1].equals(Tileset.NOTHING)) {
            world[x - 1][y + 1] = Tileset.WALL;
        }
    }
    public void makeWalls() {
        for (int x = 0; x < worldWidth; x += 1) {
            for (int y = 0; y < worldHeight; y += 1) {
                if (world[x][y].equals(Tileset.FLOOR)) {
                    makeWallForCoordinate(x, y);
                }
            }
        }
    }
    public void connectAllRooms() {
        Room first = allRooms.get(0);
        for (int i = 1; i < allRooms.size(); i += 1) {
            Hallway hallway = new Hallway(first, oSpace, random);
            hallway.connectRooms(allRooms.get(i), world);
            first = allRooms.get(i);
        }
    }
    private void findOpenSpaces() {
        for (Room r: allRooms) {
            oSpace.addAll(r.getOpenSpaces());
        }
    }
    private boolean overlapsRooms(Room room) {
        for (Room r: allRooms) {
            if (room.overlaps(r)) {
                return true;
            }
        }
        return false;
    }
    public static final int TRIES = 100;
    private Room noOverlapRoom() {
        if (allRooms.isEmpty()) {
            return new Room(worldWidth, worldHeight, random);
        } else {
            int tries = TRIES;
            while (tries > 0) {
                Room room = new Room(worldWidth, worldHeight, random);
                if (!overlapsRooms(room)) {
                    return room;
                }
                tries -= 1;
            }
            return null;
        }
    }
    public static final int NUM_ROOMS = 31;
    private void generateRooms() {
        int numRooms = random.nextInt(7, NUM_ROOMS);
        for (int i = 0; i < numRooms; i += 1) {
            Room r = noOverlapRoom();
            if (r != null) {
                allRooms.add(r);
            }
        }
    }
    // Above are the methods for making the room and hallways


    private void renderWorld() {
        Paths.clearPaths(world);
        if (showGhostPath) {
            for (Ghost ghost: ghosts) {
                Paths.showPath(world, ghost.getPosition(), player.getPosition());
            }
        }

        TETile[][] visibleTiles = getVisibleTiles(player.getPosition());
        for (Ghost ghost : ghosts) {
            Coordinate ghostPosition = ghost.getPosition();
            if ((isWithinVisibilityRadius(player.getPosition(), ghostPosition, 5))
                    && (lineOfSightEnabled || hasLineOfSight(player.getPosition(), ghostPosition))) {
                visibleTiles[ghostPosition.getX()][ghostPosition.getY()] = Tileset.LOCKED_DOOR;
            }
        }
        Coordinate playerPosition = player.getPosition();
        visibleTiles[playerPosition.getX()][playerPosition.getY()] = Tileset.AVATAR;

        iAmTired(visibleTiles);
        updateHUD();
    }

    private TETile[][] getVisibleTiles(Coordinate playerPosition) {
        if (showWholeWorld) {
            return world.clone();
        }
        int visibilityRadius = 5;

        TETile[][] visibleTiles = new TETile[worldWidth][worldHeight];

        for (int x = 0; x < worldWidth; x++) {
            for (int y = 0; y < worldHeight; y++) {
                Coordinate tilePos = new Coordinate(x, y);
                if (isWithinVisibilityRadius(playerPosition, tilePos, visibilityRadius)
                        && (lineOfSightEnabled || hasLineOfSight(playerPosition, tilePos))) {
                    visibleTiles[x][y] = world[x][y];
                } else {
                    visibleTiles[x][y] = Tileset.NOTHING;
                }
            }
        }
        return visibleTiles;
    }

    private boolean hasLineOfSight(Coordinate start, Coordinate end) {
        int startX = start.getX();
        int startY = start.getY();
        int endX = end.getX();
        int endY = end.getY();

        int dx = endX - startX;
        int dy = endY - startY;

        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        double xIncrement = (double) dx / steps;
        double yIncrement = (double) dy / steps;

        double x = startX;
        double y = startY;

        for (int i = 0; i < steps; i++) {
            int xRound = (int) Math.round(x);
            int yRound = (int) Math.round(y);

            if (world[xRound][yRound] != Tileset.NOTHING) {
                return false;
            }
            x += xIncrement;
            y += yIncrement;
        }
        return true;
    }

    private boolean isWithinVisibilityRadius(Coordinate playerPos, Coordinate tilePos, int radius) {
        int distance = calculateDistance(playerPos, tilePos);
        return distance <= radius;
    }

    private int calculateDistance(Coordinate a, Coordinate b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    private void iAmTired(TETile[][] frame) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        StdDraw.clear(new Color(0, 0, 0));
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (frame[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                frame[x][y].draw(x, y);
            }
        }
    }

    public void updateHUD() {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        // Drawing the new text at a specific position
        if (mouseX >= 0 && mouseX < worldWidth && mouseY >= 0 && mouseY < worldHeight) {
            TETile tileUnderMouse = world[mouseX][mouseY];
            StdDraw.setPenColor(StdDraw.WHITE);
            if (tileUnderMouse.equals(Tileset.NOTHING)) {
                StdDraw.textLeft(1, worldHeight - 0.5, "Tile: nothing");
            } else {
                StdDraw.textLeft(1, worldHeight - 0.5, "Tile: " + tileUnderMouse.description());
            }
            // Display date and time
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            StdDraw.textRight(worldWidth - 1, worldHeight - 0.5, formattedDateTime);
            StdDraw.text(worldWidth / 2, worldHeight - 0.5, "TO QUIT: hold down Shift and press : (colon) key");
            StdDraw.text(worldWidth / 2, 0.5, "Coins Collected: " + (totalCoins - coins.size()) + "/" + totalCoins);
        } else {
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.textLeft(1, worldHeight - 0.5, "Tile: nothing");

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            StdDraw.textRight(worldWidth - 1, worldHeight - 0.5, formattedDateTime);
            StdDraw.text(worldWidth / 2, worldHeight - 0.5, "TO QUIT: hold down Shift and press : (colon) key");
            StdDraw.text(worldWidth / 2, 0.5, "Coins Collected: " + (totalCoins - coins.size()) + "/" + totalCoins);
        }
    }


    public TETile[][] getWorld() {
        return world;
    }

    public void movePlayer(char direction) {
        player.move(direction);
    }

    private boolean showGhostPath = false;

    private void gotCoin() {
        if (coins.contains(player.getPosition())) {
            coins.remove(player.getPosition());
        }
    }


    public void updateWorld() {
        gotCoin();
        if (StdDraw.hasNextKeyTyped()) {
            char key = StdDraw.nextKeyTyped();
            if (key == ' ') {
                showWholeWorld = !showWholeWorld;
                lineOfSightEnabled = !showWholeWorld;
            }
            if (key == 'l' || key == 'L') {
                Paths.clearPaths(world);
                showGhostPath = !showGhostPath;
            }
            movePlayer(key);
            moveGhosts();
            if (inExitMenu && (key == 'q' || key == 'Q')) {
                SaveLoad.saveGame(this);
                System.exit(0);
            }
            inExitMenu = key == ':';
            renderWorld();
        } else {
            moveGhosts();
            renderWorld();
        }
    }

    public int getWidth() {
        return worldWidth;
    }

    public int getHeight() {
        return worldHeight;
    }

    private boolean inExitMenu;

    public void exitMenu() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledSquare(worldWidth / 2, worldHeight / 2, 5);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(worldWidth / 2, worldHeight / 2 + 2, "Quit Menu");
        StdDraw.text(worldWidth / 2, worldHeight / 2, "Save & Quit (Q)");
        StdDraw.show();
    }


    private boolean done = false;
    public void gameLoop() {
        inExitMenu = false;
        while (!done) {
            updateWorld();
            if (wonGame()) {
                displayWinScreen();
                handleGameOverInput();
            }
            if (isGameOver()) {
                displayGameOverScreen();
                handleGameOverInput();
            }
            if (inExitMenu) {
                exitMenu();
            }
            StdDraw.show();
        }
    }

    private void displayWinScreen() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(worldWidth / 2, worldHeight / 2, "YOU WIN!!!");
        StdDraw.text(worldWidth / 2, worldHeight / 2 - 2, "Press Enter to play again.");
        StdDraw.text(worldWidth / 2, worldHeight / 2 - 4, "Quit (Q).");
        StdDraw.show();

    }

    private void displayGameOverScreen() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(worldWidth / 2, worldHeight / 2, "Game Over!");
        StdDraw.text(worldWidth / 2, worldHeight / 2 - 2, "Press Enter to restart.");
        StdDraw.text(worldWidth / 2, worldHeight / 2 - 4, "Quit (Q).");
        StdDraw.show();
    }

    public Random getRandom() {
        return random;
    }

    private void handleGameOverInput() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == '\n') {
                    done = true;
                    return;
                }
                if (key == 'q' || key == 'Q') {
                    System.exit(0);
                }
            }
        }
    }

    private boolean wonGame() {
        return coins.size() == 0;
    }

    private boolean isGameOver() {
        Coordinate playerPosition = player.getPosition();
        for (Ghost ghost : ghosts) {
            if (playerPosition.equals(ghost.getPosition())) {
                return true;
            }
        }
        return false;
    }


}