package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.davidsperling.ld43.Constants;
import com.davidsperling.ld43.screens.LevelScreen;

import java.util.ArrayList;

public class MapExtra extends EffectPiece {
    public static final float CHANCE_OF_APPEARING = .25f;

    public static final String EXTRAS_PREFIX = "images/extras/";
    public static final String GRASS_1_TEXTURE_PATH = "grass1.png";
    public static final String GRASS_2_TEXTURE_PATH = "grass2.png";
    public static final String LEAF_1_TEXTURE_PATH = "leaf1.png";
    public static final String LEAF_2_TEXTURE_PATH = "leaf2.png";
    public static final String MUSHROOM_1_TEXTURE_PATH = "mushroom1.png";
    public static final String MUSHROOM_2_TEXTURE_PATH = "mushroom2.png";
    public static final String ROOTS_1_TEXTURE_PATH = "roots1.png";
    public static final String ROOTS_2_TEXTURE_PATH = "roots2.png";
    private static ArrayList<Texture> textures = new ArrayList<Texture>();

    private static boolean isLoaded = false;

    public MapExtra(LevelScreen levelScreen, float x, float y) {
        super(levelScreen, x * Constants.GRID_UNIT, y * Constants.GRID_UNIT);

        setFps(0);

        setFrontLayer(MathUtils.randomBoolean());

        setFrame(MathUtils.random(8));

        setTextureList(textures);
    }

    public static void load() {
        if (!isLoaded) {
            textures.add(new Texture(Gdx.files.internal(EXTRAS_PREFIX + GRASS_1_TEXTURE_PATH)));
            textures.add(new Texture(Gdx.files.internal(EXTRAS_PREFIX + GRASS_2_TEXTURE_PATH)));
            textures.add(new Texture(Gdx.files.internal(EXTRAS_PREFIX + LEAF_1_TEXTURE_PATH)));
            textures.add(new Texture(Gdx.files.internal(EXTRAS_PREFIX + LEAF_2_TEXTURE_PATH)));
            textures.add(new Texture(Gdx.files.internal(EXTRAS_PREFIX + MUSHROOM_1_TEXTURE_PATH)));
            textures.add(new Texture(Gdx.files.internal(EXTRAS_PREFIX + MUSHROOM_2_TEXTURE_PATH)));
            textures.add(new Texture(Gdx.files.internal(EXTRAS_PREFIX + ROOTS_1_TEXTURE_PATH)));
            textures.add(new Texture(Gdx.files.internal(EXTRAS_PREFIX + ROOTS_2_TEXTURE_PATH)));

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
