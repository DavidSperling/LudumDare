package com.davidsperling.ld43.utility;

import com.badlogic.gdx.Gdx;
import com.davidsperling.ld43.Constants;
import com.davidsperling.ld43.gamepieces.Ant;
import com.davidsperling.ld43.gamepieces.BlastAntSpawn;
import com.davidsperling.ld43.gamepieces.BreakBlock;
import com.davidsperling.ld43.gamepieces.CrumbleBlock;
import com.davidsperling.ld43.gamepieces.EmptySpace;
import com.davidsperling.ld43.gamepieces.ExitDoor;
import com.davidsperling.ld43.gamepieces.GamePiece;
import com.davidsperling.ld43.gamepieces.LemmingAntSpawn;
import com.davidsperling.ld43.gamepieces.MovingAttackAnt;
import com.davidsperling.ld43.gamepieces.PangolinBody;
import com.davidsperling.ld43.gamepieces.PangolinTarget;
import com.davidsperling.ld43.gamepieces.ShieldAnt;
import com.davidsperling.ld43.gamepieces.StaticBlock;
import com.davidsperling.ld43.gamepieces.StationaryAttackAnt;
import com.davidsperling.ld43.gamepieces.WaterBlock;
import com.davidsperling.ld43.screens.LevelScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.davidsperling.ld43.utility.LevelLoader.MapChars.ATTACK_ANT_CHARS;
import static com.davidsperling.ld43.utility.LevelLoader.MapChars.BLAST_ANT_CHARS;
import static com.davidsperling.ld43.utility.LevelLoader.MapChars.BLAST_ANT_LIMIT_PARAMETER;
import static com.davidsperling.ld43.utility.LevelLoader.MapChars.LEMMING_CHARS;
import static com.davidsperling.ld43.utility.LevelLoader.MapChars.LEMMING_COUNT_PARAMETER;
import static com.davidsperling.ld43.utility.LevelLoader.MapChars.LEMMING_REQUIREMENT_PARAMETER;
import static com.davidsperling.ld43.utility.LevelLoader.MapChars.SHIELD_CHARS;

public class LevelLoader {
    public static class MapChars {
        public static final char STATIC_BLOCK = '#';
        public static final char BREAK_BLOCK = '*';
        public static final char CRUMBLE_BLOCK = '%';
        public static final char WATER_BLOCK = '~';
        public static final char EXIT = '$';
        public static final char FORCE_EMPTY = '.';

        public static final char STATIONARY_ATTACK_ANT = '+';

        public static final String BLAST_ANT_CHARS = "wxyz";
        public static final char BLAST_ANT_DOWN = 'w';
        public static final char BLAST_ANT_RIGHT = 'x';
        public static final char BLAST_ANT_UP = 'y';
        public static final char BLAST_ANT_LEFT = 'z';

        public static final String ATTACK_ANT_CHARS = "abcdefgh";
        public static final char ATTACK_ANT_DOWN_RIGHT = 'a';
        public static final char ATTACK_ANT_RIGHT_UP = 'b';
        public static final char ATTACK_ANT_UP_LEFT = 'c';
        public static final char ATTACK_ANT_LEFT_DOWN = 'd';
        public static final char ATTACK_ANT_DOWN_LEFT = 'e';
        public static final char ATTACK_ANT_RIGHT_DOWN = 'f';
        public static final char ATTACK_ANT_UP_RIGHT = 'g';
        public static final char ATTACK_ANT_LEFT_UP = 'h';

        public static final String LEMMING_CHARS = "12345678";
        public static final char LEMMING_DOWN_RIGHT = '1';
        public static final char LEMMING_RIGHT_UP = '2';
        public static final char LEMMING_UP_LEFT = '3';
        public static final char LEMMING_LEFT_DOWN = '4';
        public static final char LEMMING_DOWN_LEFT = '5';
        public static final char LEMMING_RIGHT_DOWN = '6';
        public static final char LEMMING_UP_RIGHT = '7';
        public static final char LEMMING_LEFT_UP = '8';

        public static final String SHIELD_CHARS = "]_-[";
        public static final char SHIELD_DOWN = '_';
        public static final char SHIELD_UP = '-';
        public static final char SHIELD_LEFT = '[';
        public static final char SHIELD_RIGHT = ']';

        public static final char PANGOLIN_BODY = 'P';
        public static final char PANGOLIN_TARGET = 'p';

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
                } else if (currentChar == MapChars.WATER_BLOCK) {
                    levelScreen.raiseWaterLevel(gridY);
                    rowList.add(new WaterBlock(levelScreen, gridX, gridY));
                } else if (currentChar == MapChars.EXIT) {
                    rowList.add(new ExitDoor(levelScreen, gridX, gridY));
                } else if (currentChar == MapChars.STATIONARY_ATTACK_ANT) {
                    rowList.add(new StationaryAttackAnt(levelScreen, gridX, gridY));
                } else if (currentChar == MapChars.PANGOLIN_BODY) {
                    rowList.add(new PangolinBody(levelScreen, gridX, gridY));
                } else if (currentChar == MapChars.PANGOLIN_TARGET) {
                    rowList.add(new PangolinTarget(levelScreen, gridX, gridY));
                } else if (BLAST_ANT_CHARS.indexOf(currentChar) != -1) {
                    rowList.add(getBlastAntSpawn(levelScreen, currentChar, gridX, gridY));
                } else if (ATTACK_ANT_CHARS.indexOf(currentChar) != -1) {
                    rowList.add(getMovingAttackAnt(levelScreen, currentChar, gridX, gridY));
                } else if (LEMMING_CHARS.indexOf(currentChar) != -1) {
                    rowList.add(getLemmingAntSpawn(levelScreen, currentChar, gridX, gridY));
                } else if (SHIELD_CHARS.indexOf(currentChar) != -1) {
                    List<GamePiece> previousRow = getPreviousRow(levelList, rowNumber, colNumber);
                    rowList.add(getShieldAnt(levelScreen, currentChar, gridX, gridY, previousRow, rowList));
                } else if (currentChar == MapChars.FORCE_EMPTY) {
                    rowList.add(EmptySpace.getInstance(levelScreen));
                } else {
                    rowList.add(null);
                }
            }
            levelList.add(rowList);
        }
        levelScreen.setLevelList(levelList);
    }

    private static List<GamePiece> getPreviousRow(List<List<GamePiece>> levelList, int currentRowNumber, int currentColNumber) {
        List<GamePiece> previousRow;
        if (currentRowNumber == 0) {
            previousRow = new ArrayList<GamePiece>();
            for (int i = 0; i <= currentColNumber; i++) {
                previousRow.add(null);
            }
        } else {
            previousRow = levelList.get(currentRowNumber - 1);
        }
        return previousRow;
    }

    private static BlastAntSpawn getBlastAntSpawn(LevelScreen levelScreen, char mapChar, int gridX, int gridY) {
        if (mapChar == MapChars.BLAST_ANT_UP) {
            return new BlastAntSpawn(levelScreen, gridX, gridY, Ant.Direction.UP, Ant.Direction.RIGHT);
        } else if (mapChar == MapChars.BLAST_ANT_LEFT) {
            return new BlastAntSpawn(levelScreen, gridX, gridY, Ant.Direction.LEFT, Ant.Direction.RIGHT);
        } else if (mapChar == MapChars.BLAST_ANT_RIGHT) {
            return new BlastAntSpawn(levelScreen, gridX, gridY, Ant.Direction.RIGHT, Ant.Direction.RIGHT);
        } else {
            return new BlastAntSpawn(levelScreen, gridX, gridY, Ant.Direction.DOWN, Ant.Direction.RIGHT);
        }
    }

    private static MovingAttackAnt getMovingAttackAnt(LevelScreen levelScreen, char mapChar, int gridX, int gridY) {
        if (mapChar == MapChars.ATTACK_ANT_DOWN_RIGHT) {
            return new MovingAttackAnt(levelScreen, gridX, gridY, Ant.Direction.DOWN, Ant.Direction.RIGHT);
        } else if (mapChar == MapChars.ATTACK_ANT_RIGHT_UP) {
            return new MovingAttackAnt(levelScreen, gridX, gridY, Ant.Direction.RIGHT, Ant.Direction.RIGHT);
        } else if (mapChar == MapChars.ATTACK_ANT_UP_LEFT) {
            return new MovingAttackAnt(levelScreen, gridX, gridY, Ant.Direction.UP, Ant.Direction.RIGHT);
        } else if (mapChar == MapChars.ATTACK_ANT_LEFT_DOWN) {
            return new MovingAttackAnt(levelScreen, gridX, gridY, Ant.Direction.LEFT, Ant.Direction.RIGHT);
        } else if (mapChar == MapChars.ATTACK_ANT_DOWN_LEFT) {
            return new MovingAttackAnt(levelScreen, gridX, gridY, Ant.Direction.DOWN, Ant.Direction.LEFT);
        } else if (mapChar == MapChars.ATTACK_ANT_RIGHT_DOWN) {
            return new MovingAttackAnt(levelScreen, gridX, gridY, Ant.Direction.RIGHT, Ant.Direction.LEFT);
        } else if (mapChar == MapChars.ATTACK_ANT_UP_RIGHT) {
            return new MovingAttackAnt(levelScreen, gridX, gridY, Ant.Direction.UP, Ant.Direction.LEFT);
        } else if (mapChar == MapChars.ATTACK_ANT_LEFT_UP) {
            return new MovingAttackAnt(levelScreen, gridX, gridY, Ant.Direction.LEFT, Ant.Direction.LEFT);
        } else {
            return new MovingAttackAnt(levelScreen, gridX, gridY, Ant.Direction.DOWN, Ant.Direction.RIGHT);
        }
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

    private static ShieldAnt getShieldAnt(LevelScreen levelScreen, char mapChar, int gridX, int gridY, List<GamePiece> previousRow, List<GamePiece> thisRow) {
        Ant.Direction[] directionCouplet;
        Ant.Direction absoluteFacing;
        if (mapChar == MapChars.SHIELD_UP) {
            absoluteFacing = Ant.Direction.UP;
        } else if (mapChar == MapChars.SHIELD_LEFT) {
            absoluteFacing = Ant.Direction.LEFT;
        } else if (mapChar == MapChars.SHIELD_RIGHT) {
            absoluteFacing = Ant.Direction.RIGHT;
        } else {
            absoluteFacing = Ant.Direction.DOWN;
        }
        directionCouplet = getValidAntDirections(levelScreen, absoluteFacing, gridX, gridY, previousRow, thisRow);
        return new ShieldAnt(levelScreen, gridX, gridY, directionCouplet[0], directionCouplet[1]);
    }

    private static Ant.Direction[] getValidAntDirections(LevelScreen levelScreen, Ant.Direction absoluteFacing, int gridX, int gridY, List<GamePiece> previousRow, List<GamePiece> thisRow) {
        if (absoluteFacing == Ant.Direction.DOWN) {
            if (thisRow.get(gridX - 1) == null) {
                return getDirectionCouplet(Ant.Direction.RIGHT, Ant.Direction.LEFT);
            } else {
                return getDirectionCouplet(Ant.Direction.LEFT, Ant.Direction.RIGHT);
            }
        } else if (absoluteFacing == Ant.Direction.UP) {
            if (thisRow.get(gridX - 1) == null) {
                return getDirectionCouplet(Ant.Direction.RIGHT, Ant.Direction.RIGHT);
            } else {
                return getDirectionCouplet(Ant.Direction.LEFT, Ant.Direction.LEFT);
            }
        } else if (absoluteFacing == Ant.Direction.LEFT) {
            if (previousRow.get(gridX) == null) {
                return getDirectionCouplet(Ant.Direction.DOWN, Ant.Direction.LEFT);
            } else {
                return getDirectionCouplet(Ant.Direction.UP, Ant.Direction.RIGHT);
            }
        } else if (absoluteFacing == Ant.Direction.RIGHT) {
            if (previousRow.get(gridX) == null) {
                return getDirectionCouplet(Ant.Direction.DOWN, Ant.Direction.RIGHT);
            } else {
                return getDirectionCouplet(Ant.Direction.UP, Ant.Direction.LEFT);
            }
        } else {
            return getDirectionCouplet(Ant.Direction.DOWN, Ant.Direction.LEFT);
        }
    }

    private static Ant.Direction[] getDirectionCouplet(Ant.Direction feet, Ant.Direction facing) {
        Ant.Direction couplet[] = {feet, facing};
        return couplet;
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
