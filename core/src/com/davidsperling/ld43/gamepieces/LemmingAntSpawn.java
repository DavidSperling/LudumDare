package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.davidsperling.ld43.Constants;
import com.davidsperling.ld43.screens.LevelScreen;

public class LemmingAntSpawn extends GamePiece {
    private static final String TEXTURE_FILE_PATH = "images/doors/lemmingAntSpawn.png";
    private static Texture texture;
    private static TextureRegion textureRegion;
    private static boolean loaded = false;

    private float clock = 0f;

    private Ant.Direction spawnFeetDirection;
    private Ant.Direction spawnMoveDirection;

    private int remainingAnts = -1;

    public LemmingAntSpawn(LevelScreen levelScreen, int gridX, int gridY, Ant.Direction spawnFeetDirection, Ant.Direction spawnMoveDirection) {
        super(levelScreen, gridX, gridY);
        this.spawnFeetDirection = spawnFeetDirection;
        this.spawnMoveDirection = spawnMoveDirection;
        setRotation(GamePiece.antDirectionToDegrees(spawnFeetDirection));
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

    public void setRemainingAnts(int remainingAnts) {
        this.remainingAnts = remainingAnts;
    }

    public int getRemainingAnts() {
        return remainingAnts;
    }
}
