package com.davidsperling.ld43.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.davidsperling.ld43.Constants;
import com.davidsperling.ld43.LD43;

public class Transition implements Screen {
    private static final String VERTEX_SHADER_PATH = "shaders/basic.vertex.glsl";
    private static final String FRAGMENT_SHADER_PATH = "shaders/tvStatic.fragment.glsl";
    private static final String SOUND_FILE_PATH = "audio/sfx/whiteNoise.wav";
    private static final float LINGER_TIME = .3f;
    private static Sound whiteNoise;

    private static final String BACKGROUND_TEXTURE_FILE_PATH = "images/titleScreen/titleBackground.png";
    private Texture backgroundTexture;
    private final LD43 game;
    private OrthographicCamera camera;
    private Viewport viewport;

    private String vertexShader;
    private String fragmentShader;
    private ShaderProgram shaderProgram;

    private int seedCounter = 0;
    private float clock = 0;

    public Transition(LD43 game) {
        this.game = game;
        backgroundTexture = new Texture(Gdx.files.internal(BACKGROUND_TEXTURE_FILE_PATH));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
        viewport = new FitViewport(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, camera);

        whiteNoise = Gdx.audio.newSound(Gdx.files.internal(SOUND_FILE_PATH));

        vertexShader = Gdx.files.internal(VERTEX_SHADER_PATH).readString();
        fragmentShader = Gdx.files.internal(FRAGMENT_SHADER_PATH).readString();
        ShaderProgram.pedantic = false;
        shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
        if (!shaderProgram.isCompiled()) {
            System.out.println("Shader not compiled : " + shaderProgram.getLog());
        }
    }

    @Override
    public void show() {
        whiteNoise.play();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.escapeToTitle();
        }

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        shaderProgram.begin();
        shaderProgram.setUniformf("seedCounter", seedCounter);
        seedCounter++;
        shaderProgram.end();

        game.batch.begin();
        game.batch.disableBlending();
        game.batch.setShader(shaderProgram);
        game.batch.draw(backgroundTexture, 0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
        game.batch.setShader(null);
        game.batch.enableBlending();
        game.batch.end();

        clock += delta;
        if (clock > LINGER_TIME) {
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
        whiteNoise.stop();
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        shaderProgram.dispose();
    }
}
