package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.graphics.Texture;
import com.davidsperling.ld43.screens.LevelScreen;

public class AntSense extends Ant {
    public AntSense(LevelScreen levelScreen, int gridX, int gridY, Ant.Direction spawnFeetDirection, Ant.Direction spawnFacingDirection) {
        super(levelScreen, gridX, gridY, null, null);
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

    public GamePiece getBlockAtNextPosition() {
        if (forward == Direction.RIGHT) {
            moveRight();
        } else {
            moveLeft();
        }
        int targetGridX = gridMover.getTargetGridX();
        int targetGridY = gridMover.getTargetGridY();
        return levelScreen.pieceAtPosition(targetGridX, targetGridY);
    }
}
