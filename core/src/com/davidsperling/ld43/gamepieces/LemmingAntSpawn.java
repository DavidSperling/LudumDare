package com.davidsperling.ld43.gamepieces;

import com.davidsperling.ld43.Constants;
import com.davidsperling.ld43.screens.LevelScreen;

public class LemmingAntSpawn extends GamePiece {
    private float clock = 0f;

    private Ant.Direction spawnFeetDirection;
    private Ant.Direction spawnMoveDirection;

    private int remainingAnts = -1;

    public LemmingAntSpawn(LevelScreen levelScreen, int gridX, int gridY, Ant.Direction spawnFeetDirection, Ant.Direction spawnMoveDirection) {
        super(levelScreen, gridX, gridY);
        this.spawnFeetDirection = spawnFeetDirection;
        this.spawnMoveDirection = spawnMoveDirection;
    }

    @Override
    public void update(float delta) {
        clock += delta * levelScreen.getClockModifier();
        if (clock > Constants.LEMMING_ANT_SPAWN_TICK && remainingAnts != 0) {
            LemmingAnt newAnt = new LemmingAnt(levelScreen, gridX, gridY, spawnFeetDirection, spawnMoveDirection);
            levelScreen.getLemmingAnts().add(newAnt);
            remainingAnts--;
            clock -= Constants.LEMMING_ANT_SPAWN_TICK;
        }
    }

    public void setRemainingAnts(int remainingAnts) {
        this.remainingAnts = remainingAnts;
    }

    public int getRemainingAnts() {
        return remainingAnts;
    }
}
