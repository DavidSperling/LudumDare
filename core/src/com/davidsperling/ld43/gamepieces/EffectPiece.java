package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.davidsperling.ld43.screens.LevelScreen;

import java.util.ArrayList;
import java.util.List;

public class EffectPiece extends GamePiece {
    private float lifeSpan = -1;
    private float fps = -1;
    private float frame = 0;
    private boolean frontLayer = true;
    private boolean oneShot = false;
    private boolean isDead = false;

    private List<Texture> textureList = new ArrayList<Texture>();

    public EffectPiece(LevelScreen levelScreen, float x, float y) {
        super(levelScreen);
        setX(x);
        setY(y);
    }


    @Override
    public void update(float delta) {
        if (fps == -1) {
            frame++;
        } else {
            frame += delta * fps;
        }
        if (oneShot && frame > textureList.size()) {
            die();
        }

        frame %= textureList.size();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (textureList != null && textureList.size() > 0 && !isDead) {
            int frameNumber = MathUtils.floor(frame);
            TextureRegion frameTextureRegion = new TextureRegion(textureList.get(frameNumber));
            drawTextureRegionWithRotation(batch, frameTextureRegion);
        }
    }

    public void die() {
        isDead = true;
    }

    public float getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(float lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public float getFps() {
        return fps;
    }

    public void setFps(float fps) {
        this.fps = fps;
    }

    public float getFrame() {
        return frame;
    }

    public void setFrame(float frame) {
        this.frame = frame;
    }

    public boolean isFrontLayer() {
        return frontLayer;
    }

    public void setFrontLayer(boolean frontLayer) {
        this.frontLayer = frontLayer;
    }

    public boolean isOneShot() {
        return oneShot;
    }

    public void setOneShot(boolean oneShot) {
        this.oneShot = oneShot;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public List<Texture> getTextureList() {
        return textureList;
    }

    public void setTextureList(List<Texture> textureList) {
        this.textureList = textureList;
    }
}
