package dev.blue.lwjgl.front;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import dev.blue.lwjgl.Camera;
import dev.blue.lwjgl.ILogic;
import dev.blue.lwjgl.MouseInput;
import dev.blue.lwjgl.WindowManager;
import dev.blue.lwjgl.entity.Entity;
import dev.blue.lwjgl.entity.Material;
import dev.blue.lwjgl.entity.Model;
import dev.blue.lwjgl.entity.ObjectLoader;
import dev.blue.lwjgl.entity.Texture;
import dev.blue.lwjgl.entity.terrain.Terrain;
import dev.blue.lwjgl.lighting.DirectionalLight;
import dev.blue.lwjgl.lighting.PointLight;
import dev.blue.lwjgl.lighting.SpotLight;
import dev.blue.lwjgl.rendering.RenderManager;
import dev.blue.lwjgl.utils.Constants;

public class TestGame implements ILogic {
	
	private final RenderManager renderer;
	private final ObjectLoader loader;
	private final WindowManager window;
	
	private List<Entity> entities;
	private List<Terrain> terrains;/////////
	
	private Camera camera;
	
	Vector3f cameraInc;
	
	private float lightAngle;
	private DirectionalLight directionalLight;
	private PointLight[] pointLights;
	private SpotLight[] spotLights;

	public TestGame() {
		renderer = new RenderManager();
		window = Launcher.getWindow();
		loader = new ObjectLoader();
		camera = new Camera();
		cameraInc = new Vector3f(0, 0, 0);
		lightAngle = -45;
	}

	@Override
	public void init() throws Exception {
		renderer.init();
		
		Model model = loader.loadOBJModel("/models/cube.obj");
		model.setTexture(new Texture(loader.loadTexture("/textures/crate.png")), 1f);
		
		terrains = new ArrayList<Terrain>();
		Terrain terrain = new Terrain(new Vector3f(0, -1, -800), loader, new Material(new Texture(loader.loadTexture("/textures/crate.png")), 0.1f));
		Terrain terrain2 = new Terrain(new Vector3f(-800, -1, -800), loader, new Material(new Texture(loader.loadTexture("/textures/leaves.png")), 0.1f));
		terrains.add(terrain);
		terrains.add(terrain2);
		
		entities = new ArrayList<Entity>();
		Random rand = new Random();
		for(int i = 0; i < 200; i++) {
			float x = rand.nextFloat() * 100 - 50;
			float y = rand.nextFloat() * 100 - 50;
			float z = rand.nextFloat() * -300;
			entities.add(new Entity(model, new Vector3f(x, y, z), 
					new Vector3f(rand.nextFloat() * 180, rand.nextFloat() * 180, 0), 1));
		}
		entities.add(new Entity(model, new Vector3f(0, 0, -2), new Vector3f(0, 0, 0), 1));
		
		//point
		float lightIntensity = 2f;
		Vector3f lightPosition = new Vector3f(0, 0, -3.8f);
		Vector3f lightColor = new Vector3f(0.1f, 0.1f, 1);
		PointLight pointLight = new PointLight(lightColor, lightPosition, lightIntensity, 0, 0, 1);
		lightPosition = new Vector3f(-0.3f, 0, -3.8f);
		lightColor = new Vector3f(1, 1, 1);
		PointLight pointLight1 = new PointLight(lightColor.add(-0.5f, 0, -0.5f), lightPosition, lightIntensity, 0, 0, 1);
		
		//spot
		Vector3f coneDir = new Vector3f(0, 0, 1);
		float cutoff = (float) Math.cos(Math.toRadians(180));
		SpotLight spotLight = new SpotLight(
				new PointLight(lightColor, new Vector3f(0, 0, 1), lightIntensity, 0, 0, 1), 
				coneDir, cutoff);
		
		//directional
		lightPosition = new Vector3f(-10, -10, 0);
		lightColor = new Vector3f(1f, 1f, 1f);
		lightIntensity = 0.5f;
		directionalLight = new DirectionalLight(lightColor, lightPosition, lightIntensity);
		directionalLight.setDirection(new Vector3f(0, 1, 0));
		
		pointLights = new PointLight[] {pointLight, pointLight1};
		spotLights = new SpotLight[] {/*spotLight*/};
	}

	@Override
	public void input() {
		cameraInc.set(0, 0, 0);
		//advance/retreat for camera
		if(window.isKeyPressed(GLFW.GLFW_KEY_W)) {
			cameraInc.z = -1;
		}
		if(window.isKeyPressed(GLFW.GLFW_KEY_S)) {
			cameraInc.z = 1;
		}
		//strafe for camera
		if(window.isKeyPressed(GLFW.GLFW_KEY_A)) {
			cameraInc.x = -1;
		}
		if(window.isKeyPressed(GLFW.GLFW_KEY_D)) {
			cameraInc.x = 1;
		}
		//up/down for camera
		if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			cameraInc.y = -1;
		}
		if(window.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
			cameraInc.y = 1;
		}
		//Change point light position
		if(window.isKeyPressed(GLFW.GLFW_KEY_0)) {
			pointLights[0].getPosition().x += 0.001f;
		}
		if(window.isKeyPressed(GLFW.GLFW_KEY_9)) {
			pointLights[0].getPosition().x -= 0.001f;
		}
		//Change spotlight color
		if(window.isKeyPressed(GLFW.GLFW_KEY_7)) {
			pointLights[1].getColor().x += 0.003f;
		}
		if(window.isKeyPressed(GLFW.GLFW_KEY_8)) {
			pointLights[1].getColor().x -= 0.003f;
		}
	}
	@Override
	public void update(float interval, MouseInput mouseInput) {
		camera.movePosition(cameraInc.x * Constants.CAMERA_MOVE_SPEED, cameraInc.y * Constants.CAMERA_MOVE_SPEED, cameraInc.z * Constants.CAMERA_MOVE_SPEED);
		
		if(mouseInput.isRightButtonPress()) {
			Vector2f rotVec = mouseInput.getDisplayVec();
			camera.moveRotation(rotVec.x * Constants.MOUSE_SENSITIVITY, rotVec.y * Constants.MOUSE_SENSITIVITY, 0);
		}
		
		//entity.incRot(0.0f, 0.005f, 0.0f);
		
		//lightAngle += 0.2f;
		//if(lightAngle > 90) {
		//	if(lightAngle >= 360) {
		//		lightAngle = 0;
		//	}
		//}
		//double angleRad = Math.toRadians(lightAngle);
		//directionalLight.getDirection().x = (float)Math.sin(angleRad);
		//directionalLight.getDirection().y = (float)Math.cos(angleRad);
		for(Entity entity : entities) {
			renderer.processEntity(entity);
		}
		
		for(Terrain terrain : terrains) {
			renderer.processTerrain(terrain);
		}
	}

	@Override
	public void render() {
		renderer.render(camera, directionalLight, pointLights, spotLights);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		loader.cleanup();
	}

}
