package de.melon.hexsweeper.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.melon.hexsweeper.GUI.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "HexSweeper";
		config.height = 700;
		config.width = 1000;

		new LwjglApplication(new Game(), config);
	}
}
