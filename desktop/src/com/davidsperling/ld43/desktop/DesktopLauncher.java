package com.davidsperling.ld43.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.davidsperling.ld43.LD43;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Blast Ant";
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new LD43(), config);
	}
}
