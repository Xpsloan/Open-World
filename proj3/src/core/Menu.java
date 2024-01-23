package core;

import edu.princeton.cs.algs4.StdDraw;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 *  Provides the logic for Tetris.
 *
 *  @author Erik Nelson, Omar Yu, Noah Adhikari, Jasmine Lin
 */

public class Menu {
    private boolean done;
    private boolean inSeedMenu;
    private int seed;
    boolean loadGame;
    World ld;



    public Menu() {
        seed = 0;
        done = false;
        inSeedMenu = false;
        loadGame = false;
        draw();
    }

    private void draw() {
        startMenu();
        menuLoop();
    }

    public void startMenu() {
        StdDraw.clear(StdDraw.BLACK);
        title();
        slogan();
        menuOptions();
    }


    public static final double MIDDLE = 0.5;
    public static final int A = 60;
    public static final double B = 0.8;
    public static final double C = 0.7;

    public void title() {
        Font font = new Font("Arial", Font.BOLD, A);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(MIDDLE, B, "WALMART");
        StdDraw.text(MIDDLE, C, "PAC-MAN");
    }

    public static final int D = 20;
    public static final double E = 0.15;
    public static final double F = 0.1;

    public void slogan() {
        Font font = new Font("Arial", Font.BOLD, D);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(MIDDLE, E, "You: Mom, can I get pac-man?");
        StdDraw.text(MIDDLE, F, "Mom: We have pac-man at home...");
    }


    public static final int G = 30;
    public static final double H = 0.57;
    public static final double I = 0.43;
    public void menuOptions() {
        Font font = new Font("Arial", Font.BOLD, G);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(MIDDLE, H, "New Game (N)");
        StdDraw.text(MIDDLE, MIDDLE, "Load Game (L)");
        StdDraw.text(MIDDLE, I, "Quit (Q)");
    }



    public static final int J = 50;
    public static final double K = 0.6;
    public static final double L = 0.3;
    public static final double M = 0.23;


    public void seedMenu() {
        StdDraw.clear(StdDraw.BLACK);
        Font font = new Font("Arial", Font.BOLD, J);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(MIDDLE, K, "Input Seed:");

        font = new Font("Arial", Font.BOLD, G);
        StdDraw.setFont(font);
        StdDraw.text(MIDDLE, MIDDLE, String.valueOf(seed));
        StdDraw.text(MIDDLE, L, "Start (S)");
        StdDraw.text(MIDDLE, M, "Quit (Q)");
    }







    public static final int PRODUCT = 10;
    private void updateInput() {
        if (StdDraw.hasNextKeyTyped()) {
            char pressedKey = StdDraw.nextKeyTyped();
            if (!inSeedMenu) {
                if (pressedKey == 'n' || pressedKey == 'N') {
                    seedMenu();
                    inSeedMenu = true;
                }
                if (pressedKey == 'q' || pressedKey == 'Q') {
                    System.exit(0);
                }
                if (pressedKey == 'l' || pressedKey == 'L') {
                    done = true;
                    loadGame = true;
                    if (canLoad()) {
                        ld = SaveLoad.loadGame();
                    } else {

                        System.exit(0);
                    }
                }
            } else {
                if (pressedKey == '\b') {
                    seed = Math.floorDiv(seed, PRODUCT);
                    seedMenu();
                }
                if (Character.isDigit(pressedKey)) {
                    int num = Character.getNumericValue(pressedKey);
                    seed = seed * PRODUCT + num;
                    seedMenu();
                }
                if (pressedKey == '\b') {
                    seed = Math.floorDiv(seed, PRODUCT);
                    seedMenu();
                }
                if (pressedKey == 'q' || pressedKey == 'Q') {
                    System.exit(0);
                }
                if (pressedKey == 's' || pressedKey == 'S') {
                    done = true;
                }
            }
        }
    }
    public boolean canLoad() {
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
    public void menuLoop() {
        while (!done) {
            updateInput();
        }
    }

    public int getSeed() {
        return seed;
    }



}
