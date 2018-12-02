package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.davidsperling.ld43.screens.LevelScreen;

public abstract class Ant extends GamePiece {
    private static final float ANIMATION_SPEED = .01f;

    private final TextureRegion textureRegion0;
    private final TextureRegion textureRegion1;

    private float animationOffset = 0;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT,
    }

    protected Direction feet = Direction.DOWN;
    protected Direction forward = Direction.RIGHT;

    private boolean isDead = false;

    protected GridMover gridMover;
    public Ant(LevelScreen levelScreen, int gridX, int gridY, Texture texture0, Texture texture1) {
        super(levelScreen, gridX, gridY);
        this.setRotation(0);
        this.textureRegion0 = new TextureRegion(texture0);
        this.textureRegion1 = new TextureRegion(texture1);
        gridMover = new GridMover(this, getMoveSpeed(), getRotationSpeed());
    }
    public abstract float getMoveSpeed();
    public abstract float getRotationSpeed();
    public void moveRight() {
        forward = Direction.RIGHT;
        if (feet == Direction.DOWN) {
            moveRightAbsolute();
        } else if (feet == Direction.LEFT) {
            moveDownAbsolute();
        } else if (feet == Direction.UP) {
            moveLeftAbsolute();
        } else if (feet == Direction.RIGHT) {
            moveUpAbsolute();
        }
    }
    public void moveLeft() {
        forward = Direction.LEFT;
        if (feet == Direction.DOWN) {
            moveLeftAbsolute();
        } else if (feet == Direction.LEFT) {
            moveUpAbsolute();
        } else if (feet == Direction.UP) {
            moveRightAbsolute();
        } else if (feet == Direction.RIGHT) {
            moveDownAbsolute();
        }
    }

    public void moveRightAbsolute() {
        if (!levelScreen.pieceAtPosition(gridX + 1, gridY).isSolid()) {
            if (feet == Direction.DOWN) {
                gridX += 1;
                gridMover.moveRight();
                if (!levelScreen.pieceAtPosition(gridX, gridY - 1).isSolid()) {
                    gridMover.rotateClockwise();
                    gridY -= 1;
                    gridMover.moveDown();
                    feet = Direction.LEFT;
                }
            } else if (feet == Direction.UP) {
                gridX += 1;
                gridMover.moveRight();
                if (!levelScreen.pieceAtPosition(gridX, gridY + 1).isSolid()) {
                    gridMover.rotateCounterClockwise();
                    gridY += 1;
                    gridMover.moveUp();
                    feet = Direction.LEFT;
                }
            }
        } else {
            if (feet == Direction.DOWN) {
                gridMover.rotateCounterClockwise();
                feet = Direction.RIGHT;
            } else if (feet == Direction.UP) {
                gridMover.rotateClockwise();
                feet = Direction.RIGHT;
            }
        }
    }

    public void moveLeftAbsolute() {
        if (!levelScreen.pieceAtPosition(gridX - 1, gridY).isSolid()) {
            if (feet == Direction.DOWN) {
                gridX -= 1;
                gridMover.moveLeft();
                if (!levelScreen.pieceAtPosition(gridX, gridY - 1).isSolid()) {
                    gridMover.rotateCounterClockwise();
                    gridY -= 1;
                    gridMover.moveDown();
                    feet = Direction.RIGHT;
                }
            } else if (feet == Direction.UP) {
                gridX -= 1;
                gridMover.moveLeft();
                if (!levelScreen.pieceAtPosition(gridX, gridY + 1).isSolid()) {
                    gridMover.rotateClockwise();
                    gridY += 1;
                    gridMover.moveUp();
                    feet = Direction.RIGHT;
                }
            }
        } else {
            if (feet == Direction.DOWN) {
                gridMover.rotateClockwise();
                feet = Direction.LEFT;
            } else if (feet == Direction.UP) {
                gridMover.rotateCounterClockwise();
                feet = Direction.LEFT;
            }
        }
    }

    public void moveUpAbsolute() {
        if (!levelScreen.pieceAtPosition(gridX, gridY + 1).isSolid()) {
            if (feet == Direction.RIGHT) {
                gridY += 1;
                gridMover.moveUp();
                if (!levelScreen.pieceAtPosition(gridX + 1, gridY).isSolid()) {
                    gridMover.rotateClockwise();
                    gridX += 1;
                    gridMover.moveRight();
                    feet = Direction.DOWN;
                }
            } else if (feet == Direction.LEFT) {
                gridY += 1;
                gridMover.moveUp();
                if (!levelScreen.pieceAtPosition(gridX - 1, gridY).isSolid()) {
                    gridMover.rotateCounterClockwise();
                    gridX -= 1;
                    gridMover.moveLeft();
                    feet = Direction.DOWN;
                }
            }
        } else {
            if (feet == Direction.RIGHT) {
                gridMover.rotateCounterClockwise();
                feet = Direction.UP;
            } else if (feet == Direction.LEFT) {
                gridMover.rotateClockwise();
                feet = Direction.UP;
            }
        }
    }

    public void moveDownAbsolute() {
        if (!levelScreen.pieceAtPosition(gridX, gridY - 1).isSolid()) {
            if (feet == Direction.RIGHT) {
                gridY -= 1;
                gridMover.moveDown();
                if (!levelScreen.pieceAtPosition(gridX + 1, gridY).isSolid()) {
                    gridMover.rotateCounterClockwise();
                    gridX += 1;
                    gridMover.moveRight();
                    feet = Direction.UP;
                }
            } else if (feet == Direction.LEFT) {
                gridY -= 1;
                gridMover.moveDown();
                if (!levelScreen.pieceAtPosition(gridX - 1, gridY).isSolid()) {
                    gridMover.rotateClockwise();
                    gridX -= 1;
                    gridMover.moveLeft();
                    feet = Direction.UP;
                }
            }
        } else {
            if (feet == Direction.RIGHT) {
                gridMover.rotateClockwise();
                feet = Direction.DOWN;
            } else if (feet == Direction.LEFT) {
                gridMover.rotateCounterClockwise();
                feet = Direction.DOWN;
            }
        }
    }

    public void drawAnt(Batch batch) {

        TextureRegion textureRegion = getAnimationFrame();
        float x = getX();
        float y = getY();
        float originX = textureRegion.getTexture().getWidth() / 2;
        float originY = textureRegion.getTexture().getHeight() / 2;
        float width = textureRegion.getTexture().getWidth();
        float height = textureRegion.getTexture().getHeight();
        float scaleX = 1;
        float scaleY = 1;
        float rotation = this.getRotation();

        if (forward == Direction.LEFT) {
            textureRegion.flip(true, false);
        }
        batch.draw(textureRegion, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
        if (forward == Direction.LEFT) {
            textureRegion.flip(true, false);
        }
    }

    public void update(float delta) {
        gridMover.update(delta);
        if (gridMover.isMoving()) {
            animationOffset += delta;
        } else {
            GamePiece stoodOnBlock = getStoodOnBlock();
            if (stoodOnBlock == null || stoodOnBlock instanceof EmptySpace) {
                fall();
            } else if (stoodOnBlock instanceof CrumbleBlock) {
                CrumbleBlock crumbleBlock = (CrumbleBlock) stoodOnBlock;
                crumbleBlock.stepOn();
            }
        }
    }

    private TextureRegion getAnimationFrame() {
        if (animationOffset > 2 * ANIMATION_SPEED) {
            animationOffset -= 2 * ANIMATION_SPEED;
        }
        if (animationOffset < ANIMATION_SPEED) {
            return textureRegion0;
        } else {
            return textureRegion1;
        }
    }

    protected GamePiece getStoodOnBlock() {
        if (feet == Direction.DOWN) {
            return levelScreen.pieceAtPosition(gridX, gridY - 1);
        } else if (feet == Direction.RIGHT) {
            return levelScreen.pieceAtPosition(gridX + 1, gridY);
        } else if (feet == Direction.UP) {
            return levelScreen.pieceAtPosition(gridX, gridY + 1);
        } else if (feet == Direction.LEFT) {
            return levelScreen.pieceAtPosition(gridX - 1, gridY);
        } else {
            return null;
        }
    }

    public void setRotation(Direction direction) {
        if (direction == Direction.DOWN) {
            this.setRotation(0);
        } else if (direction == Direction.RIGHT) {
            this.setRotation(90);
        } else if (direction == Direction.UP) {
            this.setRotation(180);
        } else if (direction == Direction.LEFT) {
            this.setRotation(270);
        }
    }

    public void fall() {

    }

    public void die() {

    }

    public boolean isDead() {
        return isDead;
    }
}
