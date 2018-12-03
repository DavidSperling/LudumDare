package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    public Ant.Direction findGround() {
        if (levelScreen.pieceAtPosition(gridX, gridY -1).isSolid()) {
            return Ant.Direction.DOWN;
        } else if (levelScreen.pieceAtPosition(gridX+1, gridY).isSolid()) {
            return Ant.Direction.RIGHT;
        } else if (levelScreen.pieceAtPosition(gridX - 1, gridY).isSolid()) {
            return Ant.Direction.LEFT;
        } else if (levelScreen.pieceAtPosition(gridX, gridY + 1).isSolid()) {
            return Ant.Direction.UP;
        } else {
            return Ant.Direction.DOWN;
        }
    }

    public static float antDirectionToDegrees(Ant.Direction direction) {
        if (direction == Ant.Direction.DOWN) {
            return 0;
        } else if (direction == Ant.Direction.RIGHT) {
            return 90;
        } else if (direction == Ant.Direction.UP) {
            return 180;
        } else if (direction == Ant.Direction.LEFT) {
            return 270;
        } else {
            return 45;
        }
    }

    public void drawTextureRegionWithRotation(Batch batch, TextureRegion textureRegion) {
        float x = getX();
        float y = getY();
        float originX = textureRegion.getTexture().getWidth() / 2;
        float originY = textureRegion.getTexture().getHeight() / 2;
        float width = textureRegion.getTexture().getWidth();
        float height = textureRegion.getTexture().getHeight();
        float scaleX = 1;
        float scaleY = 1;
        float rotation = getRotation();
        batch.draw(textureRegion, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
    }
}
