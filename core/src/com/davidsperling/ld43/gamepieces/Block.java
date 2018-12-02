package com.davidsperling.ld43.gamepieces;

import com.davidsperling.ld43.screens.LevelScreen;

public abstract class Block extends GamePiece {

    public Block(LevelScreen levelScreen, int gridX, int gridY) {
        super(levelScreen, gridX, gridY);
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}
