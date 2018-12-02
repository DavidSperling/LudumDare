package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.davidsperling.ld43.screens.LevelScreen;

public class BreakBlock extends Block {
    private static final String TEXTURE_FILE_PATH = "images/blocks/break.png";
    private static final float MOVE_SPEED = 512;
    private static final float ROTATION_SPEED = 720;

    private static Texture texture;
    private static boolean loaded = false;

    private GridMover gridMover;

    public BreakBlock(LevelScreen levelScreen, int gridX, int gridY) {
        super(levelScreen, gridX, gridY);
        gridMover = new GridMover(this, MOVE_SPEED, ROTATION_SPEED);
    }

    public static void load() {
        if (!loaded) {
            texture = new Texture(Gdx.files.internal(TEXTURE_FILE_PATH));
            loaded = true;
        }
    }

    public static void unload() {
        if (loaded) {
            texture.dispose();
        }
    }

    @Override
    public void update(float delta) {
        if (!gridMover.isMoving()) {
            if (!levelScreen.pieceAtPosition(getGridX(), getGridY() - 1).isSolid() &&
                    !levelScreen.pieceAtPosition(getGridX() + 1, getGridY()).isSolid() &&
                    !levelScreen.pieceAtPosition(getGridX(), getGridY() + 1).isSolid() &&
                    !levelScreen.pieceAtPosition(getGridX() - 1, getGridY()).isSolid()) {

                levelScreen.pieceAtPosition(getGridX(), getGridY() - 1).blast();
                levelScreen.setAtPosition(getGridX(), getGridY() - 1, this);
                levelScreen.destroyAtPosition(getGridX(), getGridY());
                this.gridY -= 1;
                gridMover.moveDown();
            }
        }
        gridMover.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
    }

    @Override
    public void blast() {
        levelScreen.destroyAtPosition(getGridX(), getGridY());
    }
}
