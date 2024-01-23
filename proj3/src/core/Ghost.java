package core;

import tileengine.TETile;
import tileengine.Tileset;

public class Ghost {
    private Coordinate position;
    private TETile originalTile;

    private TETile[][] world;

    public Ghost(TETile[][] world, Coordinate coordinate) {
        this.world = world;
        this.position = coordinate;
        this.originalTile = world[position.getX()][position.getY()];
        world[position.getX()][position.getY()] = Tileset.LOCKED_DOOR;
    }

    public void move(Coordinate playerPosition, TETile[][] w) {
        Coordinate nextMove = Paths.getPath(w, position, playerPosition).get(0);
        if (nextMove != null && !w[nextMove.getX()][nextMove.getY()].equals(Tileset.LOCKED_DOOR)) {
            w[position.getX()][position.getY()] = originalTile;
            position = nextMove;

            originalTile = w[position.getX()][position.getY()];
            w[position.getX()][position.getY()] = Tileset.LOCKED_DOOR;
        }
    }

    public Coordinate getPosition() {
        return position;
    }

    public void movePosition(Coordinate newPosition) {
        world[position.getX()][position.getY()] = originalTile;
        position = newPosition;
        world[position.getX()][position.getY()] = Tileset.LOCKED_DOOR;
    }

}