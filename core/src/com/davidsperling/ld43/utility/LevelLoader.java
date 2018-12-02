package com.davidsperling.ld43.utility;

import com.badlogic.gdx.Gdx;
import com.davidsperling.ld43.Constants;
import com.davidsperling.ld43.gamepieces.Ant;
import com.davidsperling.ld43.gamepieces.BlastAntSpawn;
import com.davidsperling.ld43.gamepieces.BreakBlock;
import com.davidsperling.ld43.gamepieces.CrumbleBlock;
import com.davidsperling.ld43.gamepieces.ExitDoor;
import com.davidsperling.ld43.gamepieces.GamePiece;
import com.davidsperling.ld43.gamepieces.LemmingAntSpawn;
import com.davidsperling.ld43.gamepieces.StaticBlock;
import com.davidsperling.ld43.screens.LevelScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.davidsperling.ld43.utility.LevelLoader.MapChars.BLAST_ANT_LIMIT_PARAMETER;
import static com.davidsperling.ld43.utility.LevelLoader.MapChars.LEMMING_COUNT_PARAMETER;
import static com.davidsperling.ld43.utility.LevelLoader.MapChars.LEMMING_REQUIREMENT_PARAMETER;

public class LevelLoader {
    public static class MapChars {
        public static char STATIC_BLOCK = '#';
        public static char BREAK_BLOCK = '*';
        public static char CRUMBLE_BLOCK = '%';
        public static char BLAST_ANT_SPAWN = '!';
        public static char EXIT = '$';

        public static char LEMMING_DOWN_RIGHT = '1';
        public static char LEMMING_RIGHT_UP = '2';
        public static char LEMMING_UP_LEFT = '3';
        public static char LEMMING_LEFT_DOWN = '4';
        public static char LEMMING_DOWN_LEFT = '5';
        public static char LEMMING_RIGHT_DOWN = '6';
        public static char LEMMING_UP_RIGHT = '7';
        public static char LEMMING_LEFT_UP = '8';

        public static String LEMMING_COUNT_PARAMETER = "lemmingCount";
        public static String BLAST_ANT_LIMIT_PARAMETER = "blastAntLimit";
        public static String LEMMING_REQUIREMENT_PARAMETER = "lemmingRequirement";
    }

    public static void loadLevel(String levelName, LevelScreen levelScreen) {
        List<List<GamePiece>> levelList = new ArrayList<List<GamePiece>>();
        String filePath = Constants.LEVELS_FOLDER_PATH + levelName;
        String levelString = Gdx.files.internal(filePath).readString();
        ArrayList<String> splitLevelString = new ArrayList(Arrays.asList(levelString.split("[\\r\\n]+")));
        extractParameters(levelScreen, splitLevelString);
        for (int rowNumber = 0; rowNumber < splitLevelString.size(); rowNumber++) {
            String rowString = splitLevelString.get(rowNumber);

            List<GamePiece> rowList = new ArrayList();

            for (int colNumber = 0; colNumber < rowString.length(); colNumber++) {
                char currentChar = rowString.charAt(colNumber);
                int gridX = colNumber;
                int gridY = splitLevelString.size() - rowNumber;
                if (currentChar == MapChars.STATIC_BLOCK) {
                    rowList.add(new StaticBlock(levelScreen, gridX, gridY));
                } else if (currentChar == MapChars.BREAK_BLOCK) {
                    rowList.add(new BreakBlock(levelScreen, gridX, gridY));
                } else if (currentChar == MapChars.CRUMBLE_BLOCK) {
                    rowList.add(new CrumbleBlock(levelScreen, gridX, gridY));
                } else if (currentChar == MapChars.BLAST_ANT_SPAWN) {
                    rowList.add(new BlastAntSpawn(levelScreen, gridX, gridY));
                } else if (currentChar == MapChars.EXIT) {
                    rowList.add(new ExitDoor(levelScreen, gridX, gridY));
                } else if ("12345678".indexOf(currentChar) != -1) {
                    rowList.add(getLemmingAntSpawn(levelScreen, currentChar, gridX, gridY));
                } else {
                    rowList.add(null);
                }
            }
            levelList.add(rowList);
        }
        levelScreen.setLevelList(levelList);
    }

    private static LemmingAntSpawn getLemmingAntSpawn(LevelScreen levelScreen, char mapChar, int gridX, int gridY) {
        if (mapChar == MapChars.LEMMING_DOWN_RIGHT) {
            return new LemmingAntSpawn(levelScreen, gridX, gridY, Ant.Direction.DOWN, Ant.Direction.RIGHT);
        } else if (mapChar == MapChars.LEMMING_RIGHT_UP) {
            return new LemmingAntSpawn(levelScreen, gridX, gridY, Ant.Direction.RIGHT, Ant.Direction.RIGHT);
        } else if (mapChar == MapChars.LEMMING_UP_LEFT) {
            return new LemmingAntSpawn(levelScreen, gridX, gridY, Ant.Direction.UP, Ant.Direction.RIGHT);
        } else if (mapChar == MapChars.LEMMING_LEFT_DOWN) {
            return new LemmingAntSpawn(levelScreen, gridX, gridY, Ant.Direction.LEFT, Ant.Direction.RIGHT);
        } else if (mapChar == MapChars.LEMMING_DOWN_LEFT) {
            return new LemmingAntSpawn(levelScreen, gridX, gridY, Ant.Direction.DOWN, Ant.Direction.LEFT);
        } else if (mapChar == MapChars.LEMMING_RIGHT_DOWN) {
            return new LemmingAntSpawn(levelScreen, gridX, gridY, Ant.Direction.RIGHT, Ant.Direction.LEFT);
        } else if (mapChar == MapChars.LEMMING_UP_RIGHT) {
            return new LemmingAntSpawn(levelScreen, gridX, gridY, Ant.Direction.UP, Ant.Direction.LEFT);
        } else if (mapChar == MapChars.LEMMING_LEFT_UP) {
            return new LemmingAntSpawn(levelScreen, gridX, gridY, Ant.Direction.LEFT, Ant.Direction.LEFT);
        } else {
            return new LemmingAntSpawn(levelScreen, gridX, gridY, Ant.Direction.DOWN, Ant.Direction.RIGHT);
        }
    }

    private static void extractParameters(LevelScreen levelScreen, ArrayList<String> rows) {
        for (int index = rows.size() - 1; index >= 0; index--) {
            String rowString = rows.get(index);
            if (rowString.charAt(0) == '?') {
                loadParameter(levelScreen, rowString.substring(1).split("="));
                rows.remove(index);
            }
        }
    }

    private static void loadParameter(LevelScreen levelScreen, String[] parameter) {
        if (LEMMING_COUNT_PARAMETER.equals(parameter[0])) {
            levelScreen.setStartingLemmingCount(Integer.parseInt(parameter[1]));
        } else if (BLAST_ANT_LIMIT_PARAMETER.equals(parameter[0])) {
            levelScreen.setBlastAntLimit(Integer.parseInt(parameter[1]));
        } else if (LEMMING_REQUIREMENT_PARAMETER.equals(parameter[0])) {
            levelScreen.setLemmingRequirement(Integer.parseInt(parameter[1]));
        }
    }
}
