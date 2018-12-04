package com.davidsperling.ld43.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.davidsperling.ld43.Constants;
import com.davidsperling.ld43.LD43;
import com.davidsperling.ld43.gamepieces.BlastAnt;
import com.davidsperling.ld43.gamepieces.BlastAntSpawn;
import com.davidsperling.ld43.gamepieces.BreakBlock;
import com.davidsperling.ld43.gamepieces.CrumbleBlock;
import com.davidsperling.ld43.gamepieces.EffectPiece;
import com.davidsperling.ld43.gamepieces.EmptySpace;
import com.davidsperling.ld43.gamepieces.ExitDoor;
import com.davidsperling.ld43.gamepieces.Explosion;
import com.davidsperling.ld43.gamepieces.GamePiece;
import com.davidsperling.ld43.gamepieces.LemmingAnt;
import com.davidsperling.ld43.gamepieces.LemmingAntSpawn;
import com.davidsperling.ld43.gamepieces.MapExtra;
import com.davidsperling.ld43.gamepieces.MovingAttackAnt;
import com.davidsperling.ld43.gamepieces.PangolinBody;
import com.davidsperling.ld43.gamepieces.PangolinTarget;
import com.davidsperling.ld43.gamepieces.ShieldAnt;
import com.davidsperling.ld43.gamepieces.StaticBlock;
import com.davidsperling.ld43.gamepieces.StationaryAttackAnt;
import com.davidsperling.ld43.gamepieces.WaterBlock;
import com.davidsperling.ld43.utility.LevelLoader;

import java.util.ArrayList;
import java.util.List;

public class LevelScreen implements Screen {
    private final LD43 game;
    private OrthographicCamera camera;
    private Viewport viewport;

    private final String levelName;

    private List<List<GamePiece>> levelList;

    private static final String BACKGROUND_TEXTURE_FILE_PATH = "images/background/greenBrownBack.png";
    private static final String MUSIC_FILE_PATH = "audio/music/LevelTheme.mp3";
    private Music music;
    private Texture backgroundTexture;

    private BlastAnt blastAnt = null;
    private BlastAntSpawn blastAntSpawn = null;
    private LemmingAntSpawn lemmingAntSpawn = null;
    private List<LemmingAnt> lemmingAnts = new ArrayList<LemmingAnt>();
    private List<MovingAttackAnt> movingAttackAnts = new ArrayList<MovingAttackAnt>();
    private List<EffectPiece> effectPieces = new ArrayList<EffectPiece>();
    private PangolinBody pangolinBody = null;
    private PangolinTarget pangolinTarget = null;
    private int lemmingAntsCleared = 0;
    private int lemmingRequirement = 0;
    private ExitDoor exit = null;

    private int startingLemmingCount = -1;
    private int blastAntLimit = -1;
    private int waterLevel = 0;

    private float clockModifier = 2.0f;

    public LevelScreen(final LD43 game, String levelName) {
        this.game = game;
        this.levelName = levelName;
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
        LemmingAntSpawn.load();
        ShieldAnt.load();
        StationaryAttackAnt.load();
        MovingAttackAnt.load();
        WaterBlock.load();
        PangolinBody.load();
        Explosion.load();
        MapExtra.load();

        music = Gdx.audio.newMusic(Gdx.files.internal(MUSIC_FILE_PATH));
        backgroundTexture = new Texture(Gdx.files.internal(BACKGROUND_TEXTURE_FILE_PATH));
    }

    @Override
    public void show() {
        music.setLooping(true);
        music.setVolume(0.4f);
        music.play();
    }

    public void updateGame(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            game.setScreen(new LevelScreen(game, levelName));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (lemmingAntsCleared >= lemmingRequirement || Gdx.input.isKeyPressed(Input.Keys.GRAVE)) {
                game.loadNextLevel();
            }
        }

        for (EffectPiece effectPiece : effectPieces) {
            effectPiece.update(delta);
        }

        if (blastAntSpawn != null) {
            blastAntSpawn.update(delta);
        }
        if (lemmingAntSpawn != null) {
            lemmingAntSpawn.update(delta);
        }
        if (pangolinBody != null) {
            pangolinBody.update(delta);
        }

        LemmingAnt.updateClock(this, delta);
        MovingAttackAnt.updateClock(this, delta);

        for (int index = lemmingAnts.size() - 1; index >= 0; index--) {
            LemmingAnt lemmingAnt = lemmingAnts.get(index);
            lemmingAnt.update(delta);
            if (lemmingAnt.isDead()) {
                lemmingAnts.remove(index);
            }
        }

        for (int index = movingAttackAnts.size() - 1; index >= 0; index--) {
            MovingAttackAnt movingAttackAnt = movingAttackAnts.get(index);
            movingAttackAnt.update(delta);
            if (movingAttackAnt.isDead()) {
                movingAttackAnts.remove(index);
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.escapeToTitle();
        }

        updateGame(delta);

        Gdx.gl.glClearColor(.529f, .808f, .922f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, -Constants.GRID_UNIT, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);

        for (EffectPiece effectPiece : effectPieces) {
            if (!effectPiece.isFrontLayer()) {
                effectPiece.draw(game.batch, 1.0f);
            }
        }

        if (exit != null) {
            exit.draw(game.batch, 1.0f);
        }

        if (blastAntSpawn != null) {
            blastAntSpawn.draw(game.batch, 1.0f);
        }

        if (lemmingAntSpawn != null) {
            lemmingAntSpawn.draw(game.batch, 1.0f);
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

        for (MovingAttackAnt movingAttackAnt : movingAttackAnts) {
            movingAttackAnt.draw(game.batch, 1.0f);
        }

        if (pangolinBody != null) {
            pangolinBody.draw(game.batch, 1.0f);
        }

        for (int i = effectPieces.size() - 1; i >= 0; i--) {
            EffectPiece effectPiece = effectPieces.get(i);
            if (effectPiece.isFrontLayer()) {
                effectPiece.draw(game.batch, 1.0f);
            }
            if (effectPiece.isDead()) {
                effectPieces.remove(i);
            }
        }

        game.batch.end();

        drawScore();
    }

    public void drawScore() {
        game.batch.begin();
        game.font.getData().setScale(2.0f);
        game.font.draw(game.batch, "Remaining blast ants:" + blastAntSpawn.getRemainingAnts(), Constants.GRID_UNIT / 2.0f, camera.position.y + camera.viewportHeight / 2.0f - Constants.GRID_UNIT / 2.0f);

        String completionString = "Completion: " + lemmingAntsCleared + "/" + lemmingRequirement;
        if (lemmingAntsCleared >= lemmingRequirement) {
            completionString += " (Press 'Enter' to continue...)";
        }

        game.font.draw(game.batch, completionString, camera.viewportWidth / 2.0f, camera.position.y + camera.viewportHeight / 2.0f - Constants.GRID_UNIT / 2.0f);
        game.font.draw(game.batch, "Press 'r' to reset level", Constants.GRID_UNIT / 2.0f, camera.position.y - camera.viewportHeight / 2.0f + Constants.GRID_UNIT / 2.0f);
        game.batch.end();
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
        music.stop();
    }

    @Override
    public void dispose() {
        StaticBlock.unload();
        BreakBlock.unload();
        BlastAnt.unload();
        LemmingAnt.unload();
        CrumbleBlock.unload();
        ExitDoor.unload();
        ShieldAnt.unload();
        BlastAntSpawn.unload();
        StationaryAttackAnt.unload();
        MovingAttackAnt.unload();
        WaterBlock.unload();
        PangolinBody.unload();
        Explosion.unload();
        music.dispose();
        backgroundTexture.dispose();
    }

    public BlastAnt getBlastAnt() {
        return blastAnt;
    }

    public void setBlastAnt(BlastAnt blastAnt) {
        this.blastAnt = blastAnt;
    }

    private void extractSpawns() {
        for (int rowIndex = 0; rowIndex < levelList.size(); rowIndex++) {
            List<GamePiece> row = levelList.get(rowIndex);
            for (int colIndex = 0; colIndex < row.size(); colIndex++) {
                Actor actor = row.get(colIndex);
                if (actor != null) {
                    if (actor.getClass() == BlastAntSpawn.class) {
                        this.blastAntSpawn = (BlastAntSpawn) actor;
                    } else if (actor.getClass() == LemmingAntSpawn.class) {
                        this.lemmingAntSpawn = (LemmingAntSpawn) actor;
                    } else if (actor.getClass() == ExitDoor.class) {
                        this.exit = (ExitDoor) actor;
                        this.exit.setRotation(GamePiece.antDirectionToDegrees(this.exit.findGround()));
                    } else if (actor.getClass() == StationaryAttackAnt.class) {
                        StationaryAttackAnt stationaryAttackAnt = (StationaryAttackAnt) actor;
                        stationaryAttackAnt.findFooting();
                    } else if (actor.getClass() == MovingAttackAnt.class) {
                        movingAttackAnts.add((MovingAttackAnt) actor);
                        levelList.get(rowIndex).set(colIndex, null);
                    } else if (actor.getClass() == PangolinBody.class) {
                        pangolinBody = (PangolinBody) actor;
                    } else if (actor.getClass() == PangolinTarget.class) {
                        pangolinTarget = (PangolinTarget) actor;
                    }
                } else {
                    if (rowIndex < levelList.size() - 1 && colIndex < levelList.get(rowIndex + 1).size()) {
                        Actor below = levelList.get(rowIndex + 1).get(colIndex);
                        if (below != null && below instanceof StaticBlock && MathUtils.randomBoolean(MapExtra.CHANCE_OF_APPEARING)) {
                            int gridX = colIndex;
                            int gridY = levelList.size() - rowIndex;
                            this.effectPieces.add(new MapExtra(this, gridX, gridY));
                        }
                    }
                }
            }
        }
    }

    public GamePiece pieceAtPosition(int gridX, int gridY) {
        if (levelList.size() - gridY < 0 || levelList.size() - gridY >= levelList.size() || gridX < 0 || gridX >= levelList.get(levelList.size() - gridY).size()) {
            return EmptySpace.getInstance(this);
        }
        if (levelList.size() - gridY >= levelList.size()) {
            return EmptySpace.getInstance(this);
        }

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
        if (levelList.size() - gridY < 0 || levelList.size() - gridY >= levelList.size() || gridX < 0 || gridX >= levelList.get(levelList.size() - gridY).size()) {
            return;
        }
        if (levelList.size() - gridY >= levelList.size()) {
            return;
        }
        levelList.get(levelList.size() - gridY).set(gridX, gamePiece);
    }

    public List<LemmingAnt> getLemmingAnts() {
        return lemmingAnts;
    }

    public List<MovingAttackAnt> getMovingAttackAnts() {
        return movingAttackAnts;
    }

    private void updateFromList(GamePiece gamePiece, float delta) {
        if (gamePiece instanceof BreakBlock || gamePiece instanceof CrumbleBlock) {
            gamePiece.update(delta);
        }
    }

    public float getClockModifier() {
        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            return clockModifier * 3;
        }
        return clockModifier;
    }

    public List<List<GamePiece>> getLevelList() {
        return levelList;
    }

    public void setLevelList(List<List<GamePiece>> levelList) {
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

    public int getLemmingRequirement() {
        return lemmingRequirement;
    }

    public void setLemmingRequirement(int lemmingRequirement) {
        this.lemmingRequirement = lemmingRequirement;
    }

    public void raiseWaterLevel(int newWaterLevel) {
        if (newWaterLevel > waterLevel) {
            waterLevel = newWaterLevel;
        }
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    public PangolinTarget getPangolinTarget() {
        return pangolinTarget;
    }

    public PangolinBody getPangolin() {
        return pangolinBody;
    }

    public List<EffectPiece> getEffectPieces() {
        return effectPieces;
    }

    public Camera getCamera() {
        return camera;
    }
}
