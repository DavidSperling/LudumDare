package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.davidsperling.ld43.screens.LevelScreen;

public class BlastAntSpawn extends GamePiece {
    private static final String TEXTURE_FILE_PATH = "images/doors/blastAntSpawn.png";
    private static Texture texture;
    private static TextureRegion textureRegion;
    private int remainingAnts = -1;
    private static boolean loaded = false;

    private Ant.Direction spawnFeetDirection;
    private Ant.Direction spawnMoveDirection;

    public BlastAntSpawn(LevelScreen levelScreen, int gridX, int gridY, Ant.Direction spawnFeetDirection, Ant.Direction spawnMoveDirection) {
        super(levelScreen, gridX, gridY);
        this.spawnFeetDirection = spawnFeetDirection;
        this.spawnMoveDirection = spawnMoveDirection;
        this.setRotation(GamePiece.antDirectionToDegrees(spawnFeetDirection));
    }

    @Override
    public void update(float delta) {

        if(levelScreen.getBlastAnt() == null && remainingAnts != 0) {
            BlastAnt blastAnt = new BlastAnt(levelScreen, gridX, gridY, spawnFeetDirection, spawnMoveDirection);
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
        drawTextureRegionWithRotation(batch, textureRegion);
    }

    public static void load() {
        if (!loaded) {
            texture = new Texture(Gdx.files.internal(TEXTURE_FILE_PATH));
            textureRegion = new TextureRegion(texture);
            loaded = true;
        }
    }

    public static void unload() {
        if (loaded) {
            texture.dispose();
            textureRegion = null;
            loaded = false;
        }
    }
}
