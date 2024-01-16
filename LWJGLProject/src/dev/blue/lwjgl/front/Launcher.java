package dev.blue.lwjgl.front;

import dev.blue.lwjgl.EngineManager;
import dev.blue.lwjgl.WindowManager;
import dev.blue.lwjgl.utils.Constants;

public class Launcher {
	
	private static WindowManager window;
	private static TestGame game;
	
	public static void main(String[] args) {
		window = new WindowManager(Constants.TITLE, 900, 900, false);
		game = new TestGame();
		EngineManager engine = new EngineManager();
		try {
			engine.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static WindowManager getWindow() {
		return window;
	}
	
	public static TestGame getGame() {
		return game;
	}

}