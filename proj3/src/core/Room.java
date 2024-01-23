

package core;


import tileengine.TETile;
import tileengine.Tileset;


import java.util.*;


public class Room {
    private int width;
    private int height;
    private HashSet<Coordinate> roomPixels;
    private ArrayList<Coordinate> openSpace;
    private int startingX;
    private int startingY;


    public static final int MAX_SIZE = 10;


    public Room(int worldWidth, int worldHeight, Random r) {
        this.width = r.nextInt(5, MAX_SIZE);  // Random value between 3 and 7
        this.height = r.nextInt(5, MAX_SIZE); // Random value between 3 and 7
        this.startingX = r.nextInt(worldWidth);
        this.startingY = r.nextInt(worldHeight);
        if (startingX + width - 1 >= worldWidth) {
            startingX = startingX - ((startingX + width - 1) - (worldWidth - 2));
        }


        if (startingY + height - 1 >= worldHeight - 4) {
            startingY = startingY - ((startingY + height - 1) - (worldHeight - 2));
        }
        this.roomPixels = new HashSet<>();
        this.openSpace = new ArrayList<>();
        initializeRoom();
    }


    private void initializeRoom() {
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h += 1) {
                roomPixels.add(new Coordinate(startingX + w, startingY + h));
            }
        }
    }
    public void generateRoom(TETile[][] world) {
        for (Coordinate c: roomPixels) {
            if (c.getX() == startingX || c.getX() == (startingX + width) - 1) {
                world[c.getX()][c.getY()] = Tileset.WALL;
            } else if (c.getY() == startingY || c.getY() == (startingY + height) - 1) {
                world[c.getX()][c.getY()] = Tileset.WALL;
            } else {
                openSpace.add(new Coordinate(c.getX(), c.getY()));
                world[c.getX()][c.getY()] = Tileset.FLOOR;
            }
        }
    }


    public int getWidth() {
        return width;
    }


    public int getHeight() {
        return height;
    }


    public HashSet<Coordinate> getRoomPixels() {
        return roomPixels;
    }


    public ArrayList<Coordinate> getOpenSpaces() {
        return openSpace;
    }


    public boolean overlaps(Room otherRoom) {
        for (Coordinate c: otherRoom.getRoomPixels()) {
            if (roomPixels.contains(c)) {
                return true;
            }
        }
        return false;
    }


    public boolean isInsideBounds(int startX, int startY, int worldWidth, int worldHeight) {
        return startX >= 0 && startY >= 0 && startX + width <= worldWidth && startY + height <= worldHeight;
    }


}