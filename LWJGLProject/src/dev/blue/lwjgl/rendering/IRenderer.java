package dev.blue.lwjgl.rendering;

import dev.blue.lwjgl.Camera;
import dev.blue.lwjgl.entity.Model;
import dev.blue.lwjgl.lighting.DirectionalLight;
import dev.blue.lwjgl.lighting.PointLight;
import dev.blue.lwjgl.lighting.SpotLight;

public interface IRenderer<T> {
	
	public void init() throws Exception;
	
	public void render(Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight dirLight);
	
	abstract void bind(Model model);
	
	public void unbind();
	
	public void prepare(T t, Camera camera);
	
	public void cleanup();
}
