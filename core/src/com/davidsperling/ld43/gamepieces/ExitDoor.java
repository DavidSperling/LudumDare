package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.davidsperling.ld43.screens.LevelScreen;

public class ExitDoor extends GamePiece {
    private static final String TEXTURE_FILE_PATH = "images/doors/exit.png";
    private static Texture texture;
    private static TextureRegion textureRegion;

    private static boolean loaded = false;

    public ExitDoor(LevelScreen levelScreen, int gridX, int gridY) {
        super(levelScreen, gridX, gridY);
    }

    @Override
    public void update(float delta) {

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
