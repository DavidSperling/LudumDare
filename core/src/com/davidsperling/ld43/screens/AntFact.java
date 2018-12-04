package com.davidsperling.ld43.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.davidsperling.ld43.Constants;
import com.davidsperling.ld43.LD43;

public class AntFact implements Screen {
    private static final String FACT_FOLDER = "images/antFacts/";
    private static final String MUSIC_FILE_PATH = "audio/music/ElevatorAnts.mp3";
    private String customMusicFilePath = null;
    private static Music music = null;
    private Texture backgroundTexture;
    private final LD43 game;
    private OrthographicCamera camera;
    private Viewport viewport;

    private boolean playsMusic = true;

    public AntFact(LD43 game, String factFileName) {
        this.game = game;
        backgroundTexture = new Texture(Gdx.files.internal(FACT_FOLDER + factFileName));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
        viewport = new FitViewport(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, camera);
    }

    @Override
    public void show() {
        if (playsMusic) {
            if (customMusicFilePath == null) {
                music = Gdx.audio.newMusic(Gdx.files.internal(MUSIC_FILE_PATH));
            } else {
                music = Gdx.audio.newMusic(Gdx.files.internal(customMusicFilePath));
            }
            music.setVolume(0.45f);
            music.play();
        }
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.escapeToTitle();
        }

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.loadNextLevel();
        }
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
        backgroundTexture.dispose();
        if (music != null) {
            music.dispose();
        }
    }

    public void setPlaysMusic(boolean playsMusic) {
        this.playsMusic = playsMusic;
    }

    public void setCustomMusic(String filePath) {
        customMusicFilePath = filePath;
    }
}
