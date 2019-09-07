package de.melon.hexsweeper.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.melon.hexsweeper.GUI.GameRenderer;

import java.awt.*;
import java.net.URL;

public class DesktopLauncher {
	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "HexSweeper";
		config.addIcon("icon128.png", Files.FileType.Internal);
		config.addIcon("icon64.png", Files.FileType.Internal);
		config.addIcon("icon32.png", Files.FileType.Internal);
		config.height = 700;
		config.width = 1000;

		new LwjglApplication(new GameRenderer(2), config);
	}
}
