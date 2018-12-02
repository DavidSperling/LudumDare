package com.davidsperling.ld43.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.davidsperling.ld43.Constants;
import com.davidsperling.ld43.LD43;
import com.davidsperling.ld43.gamepieces.BlastAnt;
import com.davidsperling.ld43.gamepieces.BlastAntSpawn;
import com.davidsperling.ld43.gamepieces.BreakBlock;
import com.davidsperling.ld43.gamepieces.CrumbleBlock;
import com.davidsperling.ld43.gamepieces.EmptySpace;
import com.davidsperling.ld43.gamepieces.ExitDoor;
import com.davidsperling.ld43.gamepieces.GamePiece;
import com.davidsperling.ld43.gamepieces.LemmingAnt;
import com.davidsperling.ld43.gamepieces.LemmingAntSpawn;
import com.davidsperling.ld43.gamepieces.StaticBlock;
import com.davidsperling.ld43.utility.LevelLoader;

import java.util.ArrayList;
import java.util.List;

public class LevelScreen implements Screen {
    private final LD43 game;
    private OrthographicCamera camera;
    private Viewport viewport;

    private List<List<GamePiece>> levelList;

    private static final String BACKGROUND_TEXTURE_FILE_PATH = "images/background/greenBrownBack.png";
    private Texture backgroundTexture;

    private BlastAnt blastAnt = null;
    private BlastAntSpawn blastAntSpawn = null;
    private LemmingAntSpawn lemmingAntSpawn = null;
    private List<LemmingAnt> lemmingAnts = new ArrayList<LemmingAnt>();
    private int lemmingAntsCleared = 0;
    private ExitDoor exit = null;

    private int startingLemmingCount = -1;
    private int blastAntLimit = -1;

    private float clockModifier = 2.0f;

    public LevelScreen(final LD43 game, String levelName) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
        viewport = new FitViewport(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, camera);
        camera.translate(0, Constants.GRID_UNIT);

        LevelLoader.loadLevel(levelName, this);
        extractSpawns();
        handleParameters();

        BlastAnt.load();
        LemmingAnt.load();
        StaticBlock.load();
        BreakBlock.load();
        CrumbleBlock.load();
        ExitDoor.load();
        BlastAntSpawn.load();
        backgroundTexture = new Texture(Gdx.files.internal(BACKGROUND_TEXTURE_FILE_PATH));
    }

    @Override
    public void show() {

    }

    public void updateGame(float delta) {
        if (blastAntSpawn != null) {
            blastAntSpawn.update(delta);
        }
        if (lemmingAntSpawn != null){
            lemmingAntSpawn.update(delta);
        }

        LemmingAnt.updateClock(this, delta);

        for (int index = lemmingAnts.size() - 1; index >= 0; index--) {
            LemmingAnt lemmingAnt = lemmingAnts.get(index);
            lemmingAnt.update(delta);
            if (lemmingAnt.isDead()) {
                lemmingAnts.remove(index);
            }
        }

        if (blastAnt != null) {
            blastAnt.update(delta);
        }

        for (List<GamePiece> rowList : levelList) {
            for (GamePiece gamePiece : rowList) {
                updateFromList(gamePiece, delta);
            }
        }
    }

    @Override
    public void render(float delta) {
        updateGame(delta);

        Gdx.gl.glClearColor(.529f, .808f, .922f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, -Constants.GRID_UNIT, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);

        if (exit != null) {
            exit.draw(game.batch, 1.0f);
        }

        if (blastAntSpawn != null) {
            blastAntSpawn.draw(game.batch, 1.0f );
        }

        for (List<GamePiece> row : levelList) {
            for (Actor actor : row) {
                if (actor != null) {
                    actor.draw(game.batch, 1.0f);
                }
            }
        }
        if (blastAnt != null) {
            blastAnt.draw(game.batch, 1.0f);
        }

        for (LemmingAnt lemmingAnt : lemmingAnts) {
            lemmingAnt.draw(game.batch, 1.0f);
        }

        drawScore();

        game.batch.end();
    }

    public void drawScore() {
        System.out.println(game.font);
        game.font.draw(game.batch, "Remaining blast ants:" + blastAntLimit, 0, 0);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        StaticBlock.unload();
        BreakBlock.unload();
        BlastAnt.unload();
        LemmingAnt.unload();
        CrumbleBlock.unload();
        ExitDoor.unload();
        blastAntSpawn.unload();
        backgroundTexture.dispose();
    }

    public BlastAnt getBlastAnt() {
        return blastAnt;
    }

    public void setBlastAnt(BlastAnt blastAnt) {
        this.blastAnt = blastAnt;
    }

    private void extractSpawns() {
        for (List<GamePiece> row : levelList) {
            for (Actor actor : row) {
                if (actor != null) {
                    if (actor.getClass() == BlastAntSpawn.class) {
                        this.blastAntSpawn = (BlastAntSpawn) actor;
                    } else if (actor.getClass() == LemmingAntSpawn.class){
                        this.lemmingAntSpawn = (LemmingAntSpawn) actor;
                    } else if (actor.getClass() == ExitDoor.class) {
                        this.exit = (ExitDoor) actor;
                    }
                }
            }
        }
    }

    public GamePiece pieceAtPosition(int gridX, int gridY) {
        GamePiece pieceAtPosition = levelList.get(levelList.size() - gridY).get(gridX);
        if (pieceAtPosition != null) {
            return pieceAtPosition;
        } else {
            return EmptySpace.getInstance(this);
        }
    }

    public void destroyAtPosition(int gridX, int gridY) {
        setAtPosition(gridX, gridY, null);
    }

    public void setAtPosition(int gridX, int gridY, GamePiece gamePiece) {
        levelList.get(levelList.size() - gridY).set(gridX, gamePiece);
    }

    public List<LemmingAnt> getLemmingAnts() {
        return lemmingAnts;
    }

    private void updateFromList(GamePiece gamePiece, float delta) {
        if (gamePiece instanceof BreakBlock || gamePiece instanceof  CrumbleBlock) {
            gamePiece.update(delta);
        }
    }

    public float getClockModifier() {
        return clockModifier;
    }

    public List<List<GamePiece>> getLevelList() {
        return levelList;
    }

    public void setLevelList(List<List<GamePiece>>levelList) {
        this.levelList = levelList;
    }

    public void setStartingLemmingCount(int startingLemmingCount) {
        this.startingLemmingCount = startingLemmingCount;
    }

    public int getStartingLemmingCount() {
        return startingLemmingCount;
    }

    private void handleParameters() {
        if (lemmingAntSpawn != null) {
            lemmingAntSpawn.setRemainingAnts(startingLemmingCount);
        }
        if (blastAntSpawn != null) {
            blastAntSpawn.setRemainingAnts(blastAntLimit);
        }
    }

    public ExitDoor getExit() {
        return exit;
    }

    public void setExit(ExitDoor exit) {
        this.exit = exit;
    }

    public void lemmingAntCleared() {
        this.lemmingAntsCleared++;
    }

    public void setBlastAntLimit(int blastAntLimit) {
        this.blastAntLimit = blastAntLimit;
    }
}
