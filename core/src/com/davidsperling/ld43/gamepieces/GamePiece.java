package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.davidsperling.ld43.Constants;
import com.davidsperling.ld43.screens.LevelScreen;

public abstract class GamePiece extends Actor {
    protected LevelScreen levelScreen;
    protected int gridX;
    protected int gridY;

    public GamePiece(LevelScreen levelScreen, int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.levelScreen = levelScreen;
        this.setX(gridX * Constants.GRID_UNIT);
        this.setY(gridY * Constants.GRID_UNIT);
    }

    public GamePiece(LevelScreen levelScreen) {
        this(levelScreen, 0, 0);
    }

    public abstract void update(float delta);

    public LevelScreen getLevelScreen() {
        return levelScreen;
    }

    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public boolean isSolid() {
        return false;
    }

    public void blast() {}
}
