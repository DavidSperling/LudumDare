package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.davidsperling.ld43.Constants;

public class GridMover {
    private enum State {
        STOPPED,
        MOVING
    }

    private GamePiece gamePiece;
    private int targetGridX;
    private int targetGridY;
    private float targetRotation;
    private float moveSpeed;
    private float rotationSpeed;
    private State state = State.STOPPED;

    public GridMover(GamePiece gamePiece, float moveSpeed, float rotationSpeed) {
        this.gamePiece = gamePiece;
        this.targetGridX = this.gamePiece.getGridX();
        this.targetGridY = this.gamePiece.getGridY();
        this.rotationSpeed = rotationSpeed;
        this.moveSpeed = moveSpeed;
    }

    public void update(float delta) {
        if (state == State.MOVING) {
            Vector3 targetVector = getTargetVector(delta);
            if (targetVector.x == 0 && targetVector.y == 0 && targetVector.z == 0) {
                state = State.STOPPED;
            } else {
                gamePiece.setX(gamePiece.getX() + targetVector.x);
                gamePiece.setY(gamePiece.getY() + targetVector.y);
                gamePiece.setRotation(gamePiece.getRotation() + targetVector.z);
            }
        }
    }

    public void setTarget(int targetX, int targetY, float rotation) {
        setTargetGridX(targetX);
        setTargetGridY(targetY);
        setTargetRotation(rotation);
    }

    public void setTargetGridX(int targetGridX) {
        if (this.targetGridX != targetGridX) {
            state = State.MOVING;
            this.targetGridX = targetGridX;
        }
    }

    public void setTargetGridY(int targetGridY) {
        if (this.targetGridY != targetGridY) {
            state = State.MOVING;
            this.targetGridY = targetGridY;
        }
    }

    public void setTargetRotation(float targetRotation) {
        if (this.targetRotation != targetRotation) {
            state = State.MOVING;
            this.targetRotation = targetRotation;
        }
    }

    public Vector3 getTargetVector(float delta) {
        float targetX = targetGridX * Constants.GRID_UNIT;
        float targetY = targetGridY * Constants.GRID_UNIT;
        float currentX = gamePiece.getX();
        float currentY = gamePiece.getY();
        float currentRotation = gamePiece.getRotation();
        float frameMove = moveSpeed * delta;
        float rotationMove = rotationSpeed * delta;
        float moveX = MathUtils.clamp(targetX - currentX, -frameMove, frameMove);
        float moveY = MathUtils.clamp(targetY - currentY, -frameMove, frameMove);
        float moveRotation = MathUtils.clamp(targetRotation - currentRotation, -rotationMove, rotationMove);
        return new Vector3(moveX, moveY, moveRotation);
    }

    public void moveUp() {
        state = State.MOVING;
        this.targetGridY += 1;
    }

    public void moveDown() {
        state = State.MOVING;
        this.targetGridY -= 1;
    }

    public void moveLeft() {
        state = State.MOVING;
        this.targetGridX -= 1;
    }

    public void moveRight() {
        state = State.MOVING;
        this.targetGridX += 1;
    }

    public void rotateClockwise() {
        state = State.MOVING;
        this.targetRotation -= 90;
    }

    public void rotateCounterClockwise() {
        state = State.MOVING;
        this.targetRotation += 90;
    }

    public boolean isMoving() {
        return (state == State.MOVING);
    }
}
