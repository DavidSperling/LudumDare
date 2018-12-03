package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.davidsperling.ld43.screens.LevelScreen;

public class StationaryAttackAnt extends StationaryEnemyAnt {
    private static final String TEXTURE_FILE_PATH_0 = "images/ants/attack/attack0.png";
    private static final String TEXTURE_FILE_PATH_1 = "images/ants/attack/attack1.png";
    private static Texture attackAntTexture0;
    private static Texture attackAntTexture1;
    private static boolean loaded = false;

    public StationaryAttackAnt(LevelScreen levelScreen, int gridX, int gridY) {
        super(levelScreen, gridX, gridY, Direction.DOWN, Direction.LEFT, attackAntTexture0, attackAntTexture1);
    }

    public static void load() {
        if (!loaded) {
            attackAntTexture0 = new Texture(Gdx.files.internal(TEXTURE_FILE_PATH_0));
            attackAntTexture1 = new Texture(Gdx.files.internal(TEXTURE_FILE_PATH_1));
            loaded = true;
        }
    }

    public static void unload() {
        if (loaded) {
            attackAntTexture0.dispose();
            attackAntTexture1.dispose();
            loaded = false;
        }
    }
}
