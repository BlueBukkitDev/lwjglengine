package dev.blue.lwjgl;

public interface ILogic {
	
	void init() throws Exception;
	
	void input();
	
	void update(float interval, MouseInput input);
	
	void render(); 
	
	void cleanup();
}
