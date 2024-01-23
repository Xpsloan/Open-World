package core;


import tileengine.TETile;
import tileengine.Tileset;


import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;


public class Hallway {
    Room start;
    Random random;
    List<Coordinate> hallwayCoordinates;
    List<Coordinate> roomCoordinates;
    List<Coordinate> openSpaces;
    public Hallway(Room r, List<Coordinate> oSpace, Random ran) {
        random = ran;
        start = r;
        roomCoordinates = r.getOpenSpaces();
        openSpaces = oSpace;
        openSpaces.removeAll(roomCoordinates);
        hallwayCoordinates = new ArrayList<>();
    }
    public Coordinate getRandomCoordinate() {
        int index = random.nextInt(openSpaces.size());
        return openSpaces.get(index);
    }
    public Coordinate getMyRandomCoordinate() {
        int index = random.nextInt(roomCoordinates.size());
        return roomCoordinates.get(index);
    }
    public void getHallwayCoordinates() {
        Coordinate star = getMyRandomCoordinate();
        int[] s = {star.getX(), star.getY()};
        Coordinate end = getRandomCoordinate();
        int[] e = {end.getX(), end.getY()};
        int vertOrHor = random.nextInt(0, 2);




        //Going to start with vertical shift
        while (!Arrays.equals(e, s)) {
            hallwayCoordinates.add(new Coordinate(s[0], s[1]));
            if (vertOrHor == 0) {
                if (s[0] > e[0]) {
                    s[0] -= 1;
                } else if (s[0] < e[0]) {
                    s[0] += 1;
                } else if (s[1] > e[1]) {
                    s[1] -= 1;
                } else if (s[1] < e[1]) {
                    s[1] += 1;
                }
            } else {
                if (s[1] > e[1]) {
                    s[1] -= 1;
                } else if (s[1] < e[1]) {
                    s[1] += 1;
                } else if (s[0] > e[0]) {
                    s[0] -= 1;
                } else if (s[0] < e[0]) {
                    s[0] += 1;
                }
            }
        }
    }
    public void renderHallway(TETile[][] world) {
        for (Coordinate c: hallwayCoordinates) {
            world[c.getX()][c.getY()] = Tileset.FLOOR;
        }
    }
    public void makeHallway(TETile[][] world) {
        getHallwayCoordinates();
        renderHallway(world);
    }
    public void connectRooms(Room b, TETile[][] world) {
        //getting coordinates from me
        Coordinate star = getMyRandomCoordinate();
        int[] s = {star.getX(), star.getY()};


        //getting coordinates from room b
        int index = random.nextInt(b.getOpenSpaces().size());
        Coordinate end = b.getOpenSpaces().get(index);
        int[] e = {end.getX(), end.getY()};
        int vertOrHor = random.nextInt(0, 2);


        //Going to start with vertical shift
        while (!Arrays.equals(e, s)) {
            hallwayCoordinates.add(new Coordinate(s[0], s[1]));
            if (vertOrHor == 0) {
                if (s[0] > e[0]) {
                    s[0] -= 1;
                } else if (s[0] < e[0]) {
                    s[0] += 1;
                } else if (s[1] > e[1]) {
                    s[1] -= 1;
                } else if (s[1] < e[1]) {
                    s[1] += 1;
                }
            } else {
                if (s[1] > e[1]) {
                    s[1] -= 1;
                } else if (s[1] < e[1]) {
                    s[1] += 1;
                } else if (s[0] > e[0]) {
                    s[0] -= 1;
                } else if (s[0] < e[0]) {
                    s[0] += 1;
                }
            }
        }
        renderHallway(world);
    }
}