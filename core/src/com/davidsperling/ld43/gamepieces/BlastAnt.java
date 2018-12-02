package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.davidsperling.ld43.screens.LevelScreen;

public class BlastAnt extends Ant {
    private static final String TEXTURE_FILE_PATH_0 = "images/ants/blast/blast0.png";
    private static final String TEXTURE_FILE_PATH_1 = "images/ants/blast/blast1.png";
    private static final float MOVE_SPEED = 512;
    private static final float ROTATION_SPEED = 720;
    private static Texture blastAntTexture0;
    private static Texture blastAntTexture1;
    private int frame = 0;
    private static boolean loaded = false;

    private float downAngle = 270;

    public BlastAnt(LevelScreen levelScreen, int gridX, int gridY) {
        super(levelScreen, gridX, gridY, blastAntTexture0, blastAntTexture1);
    }

    @Override
    public float getMoveSpeed() {
        return MOVE_SPEED;
    }

    @Override
    public float getRotationSpeed() {
        return ROTATION_SPEED;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (!gridMover.isMoving()) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                moveRight();
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                moveLeft();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                blast();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawAnt(batch);
    }

    public static void load() {
        if (!loaded) {
            blastAntTexture0 = new Texture(Gdx.files.internal(TEXTURE_FILE_PATH_0));
            blastAntTexture1 = new Texture(Gdx.files.internal(TEXTURE_FILE_PATH_1));
            loaded = true;
        }
    }

    public static void unload() {
        if (loaded) {
            blastAntTexture0.dispose();
            blastAntTexture1.dispose();
        }
    }

    public void blast() {
        getStoodOnBlock().blast();
        levelScreen.setBlastAnt(null);
    }
}
