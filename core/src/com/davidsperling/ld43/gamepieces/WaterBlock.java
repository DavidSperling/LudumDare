package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.davidsperling.ld43.screens.LevelScreen;

public class WaterBlock extends Block {
    private static final String WATER_TEXTURE_PATH = "images/blocks/water.png";
    private static final String SURFACE_0_TEXTURE_PATH = "images/blocks/waterSurface0.png";
    private static final String SURFACE_1_TEXTURE_PATH = "images/blocks/waterSurface1.png";
    private static final float WATER_ALPHA = 0.3f;
    private static Texture waterTexture;
    private static Texture serface0Texture;
    private static Texture surface1Texture;
    private static boolean loaded = false;

    public WaterBlock(LevelScreen levelScreen, int gridX, int gridY) {
        super(levelScreen, gridX, gridY);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(1.0f, 1.0f, 1.0f, WATER_ALPHA);
        if (gridY < levelScreen.getWaterLevel()) {
            batch.draw(waterTexture, getX(), getY());
        } else {
            batch.draw(serface0Texture, getX(), getY());
        }
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void load() {
        if (!loaded) {
            waterTexture = new Texture(Gdx.files.internal(WATER_TEXTURE_PATH));
            serface0Texture = new Texture(Gdx.files.internal(SURFACE_0_TEXTURE_PATH));
            surface1Texture = new Texture(Gdx.files.internal(SURFACE_1_TEXTURE_PATH));
            loaded = true;
        }
    }

    public static void unload() {
        if (loaded) {
            waterTexture.dispose();
            serface0Texture.dispose();
            serface0Texture.dispose();
            loaded = false;
        }
    }

    @Override
    public boolean isSolid() {
        if (gridY < levelScreen.getWaterLevel()) {
            return true;
        } else {
            return false;
        }
    }
}
