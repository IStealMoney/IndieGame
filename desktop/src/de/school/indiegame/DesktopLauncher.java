package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("IndieGame");
		Graphics.Monitor primary = Lwjgl3ApplicationConfiguration.getPrimaryMonitor();
		Graphics.DisplayMode[] modes = Lwjgl3ApplicationConfiguration.getDisplayModes(primary);
		Graphics.DisplayMode usedMode = null;
		for (Graphics.DisplayMode mode : modes) {
			if (mode.height == 1080 && mode.width == 1920) {
				usedMode = mode;
			}
			System.out.println(mode.height);
		}
		config.setFullscreenMode(usedMode);
		//config.setWindowedMode(Main.SCREEN_SIZE[0], Main.SCREEN_SIZE[1]);
		new Lwjgl3Application(new Main(), config);
	}
}
