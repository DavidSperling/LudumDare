package com.davidsperling.ld43.gamepieces;

import com.davidsperling.ld43.screens.LevelScreen;

public class EmptySpace extends GamePiece {
    private static EmptySpace instance;

    private EmptySpace(LevelScreen levelScreen) {
        super(levelScreen);
    }

    @Override
    public void update(float delta) {

    }

    public static EmptySpace getInstance(LevelScreen levelScreen) {
        if (instance == null) {
            instance = new EmptySpace(levelScreen);
        }
        return instance;
    }
}
