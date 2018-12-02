package com.davidsperling.ld43.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.davidsperling.ld43.Constants;
import com.davidsperling.ld43.LD43;

import java.security.Key;

public class TitleScreen implements Screen {
    private static final String BACKGROUND_TEXTURE_FILE_PATH = "images/titleScreen/titleBackground.png";
    private Texture backgroundTexture;
    private final LD43 game;

    public TitleScreen(LD43 game) {
        this.game = game;
        backgroundTexture = new Texture(Gdx.files.internal(BACKGROUND_TEXTURE_FILE_PATH));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.loadNextLevel();
        }
    }

    @Override
    public void resize(int width, int height) {

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
        backgroundTexture.dispose();
    }
}
