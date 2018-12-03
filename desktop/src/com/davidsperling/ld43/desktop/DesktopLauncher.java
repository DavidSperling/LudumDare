package com.davidsperling.ld43.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.davidsperling.ld43.LD43;

public class DesktopLauncher {
	private static Graphics.DisplayMode displayMode = null;
	private static boolean fullScreen = false;

	public static void main (String[] arg) {
		ResolutionSelector.createAndShowGUI();
	}

	public static void startGame() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Blast Ant";
		if (displayMode != null) {
			config.setFromDisplayMode(displayMode);
		} else {
			config.width = 1280;
			config.height = 720;
		}
		config.fullscreen = fullScreen;
		new LwjglApplication(new LD43(), config);
	}

	public static void setDisplayMode(Graphics.DisplayMode displayMode) {
		DesktopLauncher.displayMode = displayMode;
	}

	public static void setFullScreen(boolean fullScreen) {
		DesktopLauncher.fullScreen = fullScreen;
	}
}
