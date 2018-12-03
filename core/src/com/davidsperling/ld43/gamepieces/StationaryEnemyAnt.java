package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.davidsperling.ld43.screens.LevelScreen;

public abstract class StationaryEnemyAnt extends Ant implements EnemyBug {
    private static boolean loaded = false;

    public StationaryEnemyAnt(LevelScreen levelScreen, int gridX, int gridY, Ant.Direction spawnFeetDirection, Ant.Direction spawnFacingDirection, Texture texture0, Texture texture1) {
        super(levelScreen, gridX, gridY, texture0, texture1);
        this.feet = spawnFeetDirection;
        this.forward = spawnFacingDirection;
        this.setRotation(spawnFeetDirection);
    }

    @Override
    public float getMoveSpeed() {
        return 0;
    }

    @Override
    public float getRotationSpeed() {
        return 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawAnt(batch);
    }

    @Override
    public void blast() {
        levelScreen.destroyAtPosition(getGridX(), getGridY());
    }
}
