package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.davidsperling.ld43.screens.LevelScreen;

public class CrumbleBlock extends Block {
    private static final String TEXTURE_FILE_PATH = "images/blocks/crumble.png";
    private static final float MOVE_SPEED = 512;
    private static final float ROTATION_SPEED = 720;
    private static final float STARTING_CRUMBLE_TIMER = .25f;

    private static Texture texture;
    private static boolean loaded = false;

    private boolean isCrumbling = false;
    private float crumbleTimer;

    public CrumbleBlock(LevelScreen levelScreen, int gridX, int gridY) {
        super(levelScreen, gridX, gridY);
        crumbleTimer = STARTING_CRUMBLE_TIMER;
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
            loaded = false;
        }
    }

    @Override
    public void update(float delta) {
        if (isCrumbling) {
            crumbleTimer -= delta * levelScreen.getClockModifier();
        }
        if (crumbleTimer < 0) {
            this.blast();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(1.0f, 1.0f, 1.0f, Math.max(crumbleTimer/STARTING_CRUMBLE_TIMER, 0.0f));
        batch.draw(texture, getX(), getY());
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void blast() {
        levelScreen.destroyAtPosition(getGridX(), getGridY());
    }

    public void stepOn() {
        isCrumbling = true;
    }
}
