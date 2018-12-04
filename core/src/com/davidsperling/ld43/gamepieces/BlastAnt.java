package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.davidsperling.ld43.Constants;
import com.davidsperling.ld43.screens.LevelScreen;

public class BlastAnt extends Ant {
    private static final String TEXTURE_FILE_PATH_0 = "images/ants/blast/blast0.png";
    private static final String TEXTURE_FILE_PATH_1 = "images/ants/blast/blast1.png";
    private static final String BLAST_SOUND_FILE_PATH = "audio/sfx/antExplode.wav";
    private static Sound blastSound;
    private static final float MOVE_SPEED = 512;
    private static final float ROTATION_SPEED = 720;
    private static Texture blastAntTexture0;
    private static Texture blastAntTexture1;
    private int frame = 0;
    private static boolean loaded = false;

    private float downAngle = 270;

    public BlastAnt(LevelScreen levelScreen, int gridX, int gridY, Direction spawnFeetDirection, Direction spawnFacingDirection) {
        super(levelScreen, gridX, gridY, blastAntTexture0, blastAntTexture1);
        feet = spawnFeetDirection;
        forward = spawnFacingDirection;
        setRotation(feet);
        gridMover.setTargetRotation(this.getRotation());
    }

    @Override
    public float getMoveSpeed() {
        return MOVE_SPEED;
    }

    @Override
    public float getRotationSpeed() {
        return ROTATION_SPEED;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (!gridMover.isMoving()) {
            GamePiece overlappedPiece = levelScreen.pieceAtPosition(gridX, gridY);
            if (overlappedPiece instanceof  EnemyBug) {
                overlappedPiece.blast();
                blast();
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                moveRight();
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                moveLeft();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                blast();
            }

            for (MovingAttackAnt movingAttackAnt : levelScreen.getMovingAttackAnts()) {
                if (movingAttackAnt.getCollisionCenter().dst(this.getCollisionCenter()) < Constants.GRID_UNIT / 3.0f) {
                    movingAttackAnt.blast();
                    blast();
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawAnt(batch);
    }

    public static void load() {
        if (!loaded) {
            blastAntTexture0 = new Texture(Gdx.files.internal(TEXTURE_FILE_PATH_0));
            blastAntTexture1 = new Texture(Gdx.files.internal(TEXTURE_FILE_PATH_1));
            blastSound = Gdx.audio.newSound(Gdx.files.internal(BLAST_SOUND_FILE_PATH));
            loaded = true;
        }
    }

    public static void unload() {
        if (loaded) {
            blastAntTexture0.dispose();
            blastAntTexture1.dispose();
            blastSound.dispose();
            loaded = false;
        }
    }

    public void blast() {
        Vector2 collisionCenter = getCollisionCenter();
        levelScreen.getEffectPieces().add(new Explosion(levelScreen, collisionCenter.x, collisionCenter.y));
        getStoodOnBlock().blast();
        levelScreen.setBlastAnt(null);
        blastSound.play();
    }

    public void die() {
        levelScreen.setBlastAnt(null);
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
}
