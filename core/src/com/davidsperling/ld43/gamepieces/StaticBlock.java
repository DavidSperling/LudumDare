package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.davidsperling.ld43.screens.LevelScreen;

public class StaticBlock extends Block {
    private static final String TEXTURE_FILE_PATH = "images/blocks/static.png";
    private static final String GRASS_TEXTURE_FILE_PATH = "images/blocks/grassStatic.png";
    private static Texture texture;
    private static Texture grassTexture;
    private static boolean loaded = false;

    public StaticBlock(LevelScreen levelScreen, int gridX, int gridY) {
        super(levelScreen, gridX, gridY);
    }

    public static void load() {
        if (!loaded) {
            texture = new Texture(Gdx.files.internal(TEXTURE_FILE_PATH));
            grassTexture = new Texture(Gdx.files.internal(GRASS_TEXTURE_FILE_PATH));
            loaded = true;
        }
    }

    public static void unload() {
        if (loaded) {
            texture.dispose();
            grassTexture.dispose();
        }
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (this.gridY == levelScreen.getLevelList().size()) {
            batch.draw(grassTexture, getX(), getY());
        } else {
            batch.draw(texture, getX(), getY());
        }
    }
}
