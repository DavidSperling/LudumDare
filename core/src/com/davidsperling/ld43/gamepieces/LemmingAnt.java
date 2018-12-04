package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.davidsperling.ld43.Constants;
import com.davidsperling.ld43.screens.LevelScreen;

import java.util.List;

public class LemmingAnt extends Ant {
    private static final String TEXTURE_FILE_PATH_0 = "images/ants/lemming/lemming0.png";
    private static final String TEXTURE_FILE_PATH_1 = "images/ants/lemming/lemming1.png";
    private static final String DEATH_SOUND_FILE_PATH = "audio/sfx/antDie.wav";
    private static Sound deathSound;
    private static Texture lemmingAntTexture0;
    private static Texture lemmingAntTexture1;

    private static float lemmingAntClock = 0;

    private static final float MOVE_SPEED = 128;
    private static final float ROTATION_SPEED = 180;

    private Direction moveDirection;

    private static boolean loaded = false;

    public LemmingAnt(LevelScreen levelScreen, int gridX, int gridY, Ant.Direction spawnFeetdirection, Ant.Direction spawnMoveDirection) {
        super(levelScreen, gridX, gridY, lemmingAntTexture0, lemmingAntTexture1);
        this.feet = spawnFeetdirection;
        this.forward = spawnMoveDirection;
        this.moveDirection = spawnMoveDirection;
        this.setRotation(spawnFeetdirection);
        gridMover.setTargetRotation(this.getRotation());
    }

    @Override
    public float getMoveSpeed() {
        return MOVE_SPEED * levelScreen.getClockModifier();
    }

    @Override
    public float getRotationSpeed() {
        return ROTATION_SPEED * levelScreen.getClockModifier();
    }

    @Override
    public void update(float delta) {
        boolean wasFalling = false;
        if (isFalling) {
            wasFalling = true;
            if (!gridMover.isMoving()) {
                isFalling = false;
            }
        }
        super.update(delta);
        if (wasFalling && !isFalling) {
            die();
        }
        if (!gridMover.isMoving()) {
            if (levelScreen.getExit().getGridX() == this.getGridX() && levelScreen.getExit().getGridY() == this.getGridY()) {
                levelScreen.lemmingAntCleared();
                this.die();
            } else if (levelScreen.pieceAtPosition(gridX, gridY).getClass() == StationaryAttackAnt.class){
                this.die();
            } else if (getObjectAtNextPosition() instanceof ShieldAnt) {
                if (moveDirection == Direction.RIGHT) {
                    forward = Direction.LEFT;
                    moveDirection = Direction.LEFT;
                } else {
                    forward = Direction.RIGHT;
                    moveDirection = Direction.RIGHT;
                }
            }
        }

        for (MovingAttackAnt movingAttackAnt : levelScreen.getMovingAttackAnts()) {
            if (movingAttackAnt.getCollisionCenter().dst(this.getCollisionCenter()) < Constants.GRID_UNIT / 3.0f) {
                die();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawAnt(batch);
    }

    public static void load() {
        resetClock();
        if (!loaded) {
            lemmingAntTexture0 = new Texture(Gdx.files.internal(TEXTURE_FILE_PATH_0));
            lemmingAntTexture1 = new Texture(Gdx.files.internal(TEXTURE_FILE_PATH_1));
            deathSound = Gdx.audio.newSound(Gdx.files.internal(DEATH_SOUND_FILE_PATH));
            loaded = true;
        }
    }

    public static void unload() {
        if (loaded) {
            lemmingAntTexture0.dispose();
            lemmingAntTexture1.dispose();
            deathSound.dispose();
            loaded = false;
        }
    }

    public static void resetClock() {
        lemmingAntClock = 0;
    }

    public static void updateClock(LevelScreen levelScreen, float delta) {
        lemmingAntClock += delta * levelScreen.getClockModifier();
        if (lemmingAntClock > Constants.LEMMING_ANT_TICK) {
            lemmingAntClock -= Constants.LEMMING_ANT_TICK;
            List<LemmingAnt> lemmingAnts = levelScreen.getLemmingAnts();
            for (LemmingAnt lemmingAnt : lemmingAnts) {
                if (!lemmingAnt.gridMover.isMoving()) {
                    if (lemmingAnt.moveDirection == Direction.RIGHT) {
                        lemmingAnt.moveRight();
                    } else if (lemmingAnt.moveDirection == Direction.LEFT) {
                        lemmingAnt.moveLeft();
                    }
                }
            }
        }
    }

    @Override
    public void fall() {
        isFalling = true;
        if (!gridMover.isMoving() && gridY > 0) {
            gridMover.setTargetRotation(0);
            gridY -= 1;
            gridMover.moveDown();
            feet = Direction.DOWN;
        }
    }

    @Override
    public void die() {
        levelScreen.getLemmingAnts().remove(this);
        deathSound.play();
    }
}
