package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.davidsperling.ld43.screens.LevelScreen;

public class BlastAntSpawn extends GamePiece {
    private static final String TEXTURE_FILE_PATH = "images/doors/blastAntSpawn.png";
    private static Texture texture;
    private int remainingAnts = -1;
    private static boolean loaded = false;

    public BlastAntSpawn(LevelScreen levelScreen, int gridX, int gridY) {
        super(levelScreen, gridX, gridY);
    }

    @Override
    public void update(float delta) {

        if(levelScreen.getBlastAnt() == null && remainingAnts != 0) {
            BlastAnt blastAnt = new BlastAnt(levelScreen, gridX, gridY);
            remainingAnts--;
            levelScreen.setBlastAnt(blastAnt);
        }
    }

    public int getRemainingAnts() {
        return remainingAnts;
    }

    public void setRemainingAnts(int remainingAnts) {
        this.remainingAnts = remainingAnts;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
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
}
