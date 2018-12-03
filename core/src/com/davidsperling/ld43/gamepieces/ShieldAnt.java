package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.davidsperling.ld43.screens.LevelScreen;

public class ShieldAnt extends StationaryEnemyAnt {
    private static final String TEXTURE_FILE_PATH_0 = "images/ants/shield/shield0.png";
    private static final String TEXTURE_FILE_PATH_1 = "images/ants/shield/shield1.png";
    private static Texture shieldAntTexture0;
    private static Texture shieldAntTexture1;
    private static boolean loaded = false;

    public ShieldAnt(LevelScreen levelScreen, int gridX, int gridY, Ant.Direction spawnFeetDirection, Ant.Direction spawnFacingDirection) {
        super(levelScreen, gridX, gridY, spawnFeetDirection, spawnFacingDirection, shieldAntTexture0, shieldAntTexture1);
    }

    public static void load() {
        if (!loaded) {
            shieldAntTexture0 = new Texture(Gdx.files.internal(TEXTURE_FILE_PATH_0));
            shieldAntTexture1 = new Texture(Gdx.files.internal(TEXTURE_FILE_PATH_1));
            loaded = true;
        }
    }

    public static void unload() {
        if (loaded) {
            shieldAntTexture0.dispose();
            shieldAntTexture1.dispose();
            loaded = false;
        }
    }
}
