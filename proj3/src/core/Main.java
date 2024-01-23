package core;


public class Main {
    private static final int WIDTH = 75;
    private static final int HEIGHT = 40;


    public static void main(String[] args) {
        Menu menu = new Menu();
        World world;
        if (menu.loadGame) {
            world = menu.ld;
        } else {
            world = new World(WIDTH, HEIGHT, menu.getSeed());
        }
        while (true) {
            world.initializeRender();
            world.gameLoop();
            world = new World(WIDTH, HEIGHT, world.getRandom().nextInt());
        }
    }


}
