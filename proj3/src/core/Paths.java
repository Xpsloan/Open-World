package core;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.*;

public class Paths {

    private static boolean validPosition(TETile[][] world, Coordinate position) {
        if (!world[position.getX()][position.getY()].equals(Tileset.WALL)
                && !world[position.getX()][position.getY()].equals(Tileset.NOTHING)) {
            return true;
        }
        return false;
    }

    private static ArrayList<Coordinate> getNeighbors(TETile[][] world, Coordinate position) {
        ArrayList<Coordinate> ret = new ArrayList<>();
        int[][] d = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        for (int i = 0; i < d.length; i += 1) {
            Coordinate coor = new Coordinate(position.getX() + d[i][0], position.getY() + d[i][1]);
            if (validPosition(world, coor)) {
                ret.add(coor);
            }
        }
        return ret;
    }


    public static void clearPaths(TETile[][] world) {
        for (int x = 0; x < world.length; x += 1) {
            for (int y = 0; y < world[0].length; y += 1) {
                if (world[x][y].equals(Tileset.GRASS)) {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
    }

    public static List<Coordinate> getPath(TETile[][] world, Coordinate start, Coordinate end) {
        Queue<Coordinate> q = new LinkedList<>();
        HashMap<Coordinate, Coordinate> check = new HashMap<>();

        q.add(start);
        check.put(start, null);
        while (q.peek() != null) {
            Coordinate c = q.poll();
            if (c.equals(end)) {
                return pathTo(check, end, start);
            }
            for (Coordinate neighbors : getNeighbors(world, c)) {
                if (!check.containsKey(neighbors)) {
                    q.add(neighbors);
                    check.put(neighbors, c);
                }
            }
        }
        return null;
    }


    public static void showPath(TETile[][] world, Coordinate start, Coordinate end) {
        List<Coordinate> path = getPath(world, start, end);
        if (path.isEmpty()) {
            return;
        }
        path.remove(path.size() - 1);
        for (Coordinate p: path) {
            if (world[p.getX()][p.getY()].equals(Tileset.FLOOR)) {
                world[p.getX()][p.getY()] = Tileset.GRASS;
            }
        }
    }

    public static ArrayList<Coordinate> pathTo(HashMap<Coordinate, Coordinate> path,
                                               Coordinate end, Coordinate start) {
        ArrayList<Coordinate> ret = new ArrayList<>();
        Coordinate curr = end;
        while (!curr.equals(start)) {
            ret.add(0, curr);
            curr = path.get(curr);
        }
        return ret;
    }

}
