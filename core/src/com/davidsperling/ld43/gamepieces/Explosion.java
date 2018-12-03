package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.davidsperling.ld43.Constants;
import com.davidsperling.ld43.screens.LevelScreen;

import java.util.ArrayList;

public class Explosion extends EffectPiece {
    private static final String TEXTURE_FILE_PATH_0 = "images/effects/explosion/explosion0.png";
    private static final String TEXTURE_FILE_PATH_1 = "images/effects/explosion/explosion1.png";
    private static final String TEXTURE_FILE_PATH_2 = "images/effects/explosion/explosion2.png";
    private static ArrayList<Texture> textures = new ArrayList<Texture>();

    private static boolean isLoaded = false;

    public Explosion(LevelScreen levelScreen, float x, float y) {
        super(levelScreen, x - Constants.GRID_UNIT / 2.0f, y - Constants.GRID_UNIT / 2.0f);
        setOneShot(true);
        setFps(6.0f);
        setTextureList(textures);
    }

    public static void load() {
        if (!isLoaded) {
            textures.add(new Texture(Gdx.files.internal(TEXTURE_FILE_PATH_0)));
            textures.add(new Texture(Gdx.files.internal(TEXTURE_FILE_PATH_1)));
            textures.add(new Texture(Gdx.files.internal(TEXTURE_FILE_PATH_2)));
            isLoaded = true;
        }
    }

    public static void unload() {
        if (isLoaded) {
            while(!textures.isEmpty()) {
                textures.remove(0).dispose();
            }
            isLoaded = false;
        }
    }
}
