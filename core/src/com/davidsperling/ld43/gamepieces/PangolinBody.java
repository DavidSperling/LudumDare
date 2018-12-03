package com.davidsperling.ld43.gamepieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.davidsperling.ld43.Constants;
import com.davidsperling.ld43.screens.LevelScreen;

public class PangolinBody extends StaticBlock {
    private static final String TEXTURE_FILE_PATH = "images/pangolin/pangolinBody.png";
    private static Texture texture;
    private static boolean loaded = false;

    private static final Vector2 MOUTH_OFFSET = new Vector2(0.7f * Constants.GRID_UNIT, 0.5f * Constants.GRID_UNIT);

    private PangolinTarget pangolinTarget = null;

    private Vector2 tongueTipLocation = null;
    private Vector2 tongueTipUnitVector = null;
    private float tongueLength = -1;
    private static float tongueClock = 0;

    private static ShapeRenderer shapeRenderer;

    public PangolinBody(LevelScreen levelScreen, int gridX, int gridY) {
        super(levelScreen, gridX, gridY);
    }

    public static void load() {
        resetClock();
        if (!loaded) {
            texture = new Texture(Gdx.files.internal(TEXTURE_FILE_PATH));
            shapeRenderer = new ShapeRenderer();
            loaded = true;
        }
    }

    public static void unload() {
        if (loaded) {
            texture.dispose();
            shapeRenderer.dispose();
            loaded = false;
        }
    }

    @Override
    public void update(float delta) {
        tongueClock += delta * levelScreen.getClockModifier() / Constants.PANGOLIN_TICK;
        while (tongueClock >= 2.0f) {
            tongueClock -= 2.0f;
        }
        setTongueTipLocation();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shapeRenderer.setProjectionMatrix(levelScreen.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.6f, 0.2f, 0.0f, 1.0f);
        Gdx.gl.glLineWidth(4);

        shapeRenderer.line(getMouthLocation().x, getMouthLocation().y, tongueTipLocation.x, tongueTipLocation.y);
        shapeRenderer.end();
        batch.begin();
        batch.draw(texture, getX() - 2 * Constants.GRID_UNIT, getY() - (.2f * Constants.GRID_UNIT));
    }

    private Vector2 getMouthLocation() {
        return new Vector2(getX(), getY()).add(MOUTH_OFFSET);
    }

    private Vector2 findPangolinTarget() {
        if (pangolinTarget == null) {
            pangolinTarget = levelScreen.getPangolinTarget();
        }
        return new Vector2(pangolinTarget.getX() + 0.5f * Constants.GRID_UNIT, pangolinTarget.getY());
    }

    private Vector2 getTongueAngle() {
        if (tongueTipUnitVector == null) {
            tongueTipUnitVector = (findPangolinTarget().sub(getMouthLocation())).nor();
        }
        return tongueTipUnitVector.cpy();
    }

    private float getTongueLength() {
        if (tongueLength == -1) {
            tongueLength = Math.abs(findPangolinTarget().dst(getMouthLocation()));
        }
        return Math.abs(findPangolinTarget().dst(getMouthLocation()));
    }

    private void setTongueTipLocation() {
        float t;

        if (tongueClock <= 1) {
            t = tongueClock;
        } else {
            t = 2.0f - tongueClock;
        }
        Vector2 tongueTipOffset = getTongueAngle().scl(t * getTongueLength());
        tongueTipLocation = getMouthLocation().add(tongueTipOffset);
    }

    public Vector2 getTongueTipLocation() {
        return tongueTipLocation;
    }

    private static void resetClock() {
        tongueClock = 0;
    }

}
