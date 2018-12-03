package com.davidsperling.ld43;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.davidsperling.ld43.screens.AntFact;
import com.davidsperling.ld43.screens.LevelScreen;
import com.davidsperling.ld43.screens.TitleScreen;
import com.davidsperling.ld43.screens.Transition;

import java.util.ArrayList;
import java.util.List;

public class LD43 extends Game {
    public static final String LEVEL_LIST_FILE_PATH = "levels/levelList.txt";

    public static final String LEVEL_PREFIX = "Level";
    public static final String TITLE_SCREEN_PREFIX = "TitleScreen";
    public static final String TRANSITION_PREFIX = "Transition";
    public static final String ANT_FACT_PREFIX = "AntFact";

	public SpriteBatch batch;
	public BitmapFont font;
	public List<String> levels = new ArrayList<String>();
	public int currentLevelNumber = 0;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		loadLevelList();
		if (levels.size() > 0) {
            goToLevel(0);
        } else {
		    System.exit(0);
        }

	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}

	public void loadLevelList() {
        String[] levelFilePaths = Gdx.files.internal(LEVEL_LIST_FILE_PATH).readString().split("[\\r\\n]+");
        for (int i = 0; i < levelFilePaths.length; i++) {
            if (levelFilePaths[i].length() != 0) {
                levels.add(levelFilePaths[i]);
            }
        }
    }

    public void loadNextLevel() {
	    currentLevelNumber++;
        if (currentLevelNumber >= levels.size()) {
            currentLevelNumber = 0;
        }
        goToLevel(currentLevelNumber);
    }

    public void goToLevel(int levelNumber) {
	    String[] splitLevelLine = levels.get(levelNumber).split(":");
        if (TITLE_SCREEN_PREFIX.equals(splitLevelLine[0])) {
            this.setScreen(new TitleScreen(this));
        } else if (TRANSITION_PREFIX.equals(splitLevelLine[0])) {
            this.setScreen(new Transition(this));
        } else if (ANT_FACT_PREFIX.equals(splitLevelLine[0])) {
            this.setScreen(new AntFact(this, splitLevelLine[1]));
        } else if (LEVEL_PREFIX.equals(splitLevelLine[0])) {
            this.setScreen(new LevelScreen(this, splitLevelLine[1]));
        }
    }

    public void escapeToTitle() {
        currentLevelNumber = 0;
	    goToLevel(0);
    }
}
