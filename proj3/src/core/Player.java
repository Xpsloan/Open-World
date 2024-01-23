package core;
import tileengine.TETile;
import tileengine.Tileset;

public class Player {
    private Coordinate position;
    private TETile[][] world;


    public Player(TETile[][] world, Coordinate initialPosition) {
        this.world = world;
        this.position = initialPosition;
        world[position.getX()][position.getY()] = Tileset.AVATAR;
    }

    public void move(char direction) {
        int newX = position.getX();
        int newY = position.getY();

        switch (direction) {
            case 'W':
                newY += 1;
                break;
            case 'A':
                newX -= 1;
                break;
            case 'S':
                newY -= 1;
                break;
            case 'D':
                newX += 1;
                break;
            case 'w':
                newY += 1;
                break;
            case 'a':
                newX -= 1;
                break;
            case 's':
                newY -= 1;
                break;
            case 'd':
                newX += 1;
                break;
            default:
        }

        if (isValidMove(newX, newY)) {
            world[position.getX()][position.getY()] = Tileset.FLOOR;
            position = new Coordinate(newX, newY);
            world[position.getX()][position.getY()] = Tileset.AVATAR;
        }
    }

    public boolean goodStep(int x, int y) {
        if (!world[x][y].equals(Tileset.NOTHING) && !world[x][y].equals(Tileset.WALL)) {
            return true;
        }
        return false;
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < world.length && y >= 0 && y < world[0].length
                && (goodStep(x, y) || (world[x][y].equals(Tileset.UNLOCKED_DOOR)));
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate newPosition) {
        this.position = newPosition;
    }


}