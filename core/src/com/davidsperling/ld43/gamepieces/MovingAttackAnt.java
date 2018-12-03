package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.davidsperling.ld43.Constants;
import com.davidsperling.ld43.screens.LevelScreen;

import java.util.List;

public class MovingAttackAnt extends Ant implements EnemyBug {
    private static final String TEXTURE_FILE_PATH_0 = "images/ants/attack/attack0.png";
    private static final String TEXTURE_FILE_PATH_1 = "images/ants/attack/attack1.png";
    private static Texture attackAntTexture0;
    private static Texture attackAntTexture1;
    private static boolean loaded = false;

    private static boolean falling = false;

    private static final float MOVE_SPEED = 256;
    private static final float ROTATION_SPEED = 360;

    private static float movingAttackAntClock = 0.0f;

    private Direction moveDirection;

    public MovingAttackAnt(LevelScreen levelScreen, int gridX, int gridY, Ant.Direction spawnFeetDirection, Ant.Direction spawnMoveDirection) {
        super(levelScreen, gridX, gridY, attackAntTexture0, attackAntTexture1);
        this.feet = spawnFeetDirection;
        this.forward = spawnMoveDirection;
        this.moveDirection = spawnMoveDirection;
        this.setRotation(spawnFeetDirection);
        gridMover.setTargetRotation(this.getRotation());
    }

    @Override
    public float getMoveSpeed() {
        return MOVE_SPEED * levelScreen.getClockModifier();
    }

    @Override
    public float getRotationSpeed() {
        return MOVE_SPEED * levelScreen.getClockModifier();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawAnt(batch);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (getObjectAtNextPosition() instanceof ShieldAnt) {
            if (forward == Direction.RIGHT) {
                forward = Direction.LEFT;
            } else {
                forward = Direction.RIGHT;
            }
        }
    }

    public static void updateClock(LevelScreen levelScreen, float delta) {
        movingAttackAntClock += delta * levelScreen.getClockModifier();
        if (movingAttackAntClock > Constants.MOVING_ATTACK_ANT_TICK) {
            movingAttackAntClock -= Constants.MOVING_ATTACK_ANT_TICK;
            List<MovingAttackAnt> movingAttackAnts = levelScreen.getMovingAttackAnts();
            for (MovingAttackAnt movingAttackAnt : movingAttackAnts) {
                if (!movingAttackAnt.gridMover.isMoving()) {
                    if (movingAttackAnt.forward == Direction.RIGHT) {
                        movingAttackAnt.moveRight();
                    } else if (movingAttackAnt.forward == Direction.LEFT) {
                        movingAttackAnt.moveLeft();
                    }
                }
            }
        }
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

    @Override
    public void fall() {
        if (!gridMover.isMoving() && gridY > 0) {
            gridMover.setTargetRotation(0);
            gridY -= 1;
            gridMover.moveDown();
            feet = Direction.DOWN;
            isFalling = true;
        }
    }

    @Override
    public void die() {
        isDead = true;
    }

    @Override
    public void blast() {
        die();
    }

}
