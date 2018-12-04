package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.davidsperling.ld43.screens.LevelScreen;

public class BreakBlock extends Block {
    private static final String TEXTURE_FILE_PATH = "images/blocks/break.png";
    private static final String SOUND_FILE_PATH = "audio/sfx/blockFall.wav";
    private static Sound sound;
    private static final float MOVE_SPEED = 512;
    private static final float ROTATION_SPEED = 720;

    private static Texture texture;
    private static boolean loaded = false;
    private boolean isFalling = false;

    private GridMover gridMover;

    public BreakBlock(LevelScreen levelScreen, int gridX, int gridY) {
        super(levelScreen, gridX, gridY);
        gridMover = new GridMover(this, MOVE_SPEED, ROTATION_SPEED);
    }

    public static void load() {
        if (!loaded) {
            texture = new Texture(Gdx.files.internal(TEXTURE_FILE_PATH));
            sound = Gdx.audio.newSound(Gdx.files.internal(SOUND_FILE_PATH));
            loaded = true;
        }
    }

    public static void unload() {
        if (loaded) {
            texture.dispose();
            sound.dispose();
            loaded = false;
        }
    }

    @Override
    public void update(float delta) {
        boolean wasFalling = isFalling;

        if (!gridMover.isMoving()) {
            if (!levelScreen.pieceAtPosition(getGridX(), getGridY() - 1).isSolid() && (getGridY() - 1) > 0 &&
                    (isFalling ||
                    !levelScreen.pieceAtPosition(getGridX() + 1, getGridY()).isSolid() &&
                    !levelScreen.pieceAtPosition(getGridX(), getGridY() + 1).isSolid() &&
                    !levelScreen.pieceAtPosition(getGridX() - 1, getGridY()).isSolid())) {
                isFalling = true;
                levelScreen.pieceAtPosition(getGridX(), getGridY() - 1).blast();
                levelScreen.setAtPosition(getGridX(), getGridY() - 1, this);
                levelScreen.destroyAtPosition(getGridX(), getGridY());
                this.gridY -= 1;
                gridMover.moveDown();
            } else {
                isFalling = false;
                if (wasFalling) {
                    sound.play();
                }
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
