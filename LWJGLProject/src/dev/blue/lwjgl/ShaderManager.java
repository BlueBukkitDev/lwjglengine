package dev.blue.lwjgl;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import dev.blue.lwjgl.entity.Material;
import dev.blue.lwjgl.lighting.DirectionalLight;
import dev.blue.lwjgl.lighting.PointLight;
import dev.blue.lwjgl.lighting.SpotLight;

public class ShaderManager {
	
	private final int programID;
	private int vertexShaderID, fragmentShaderID;
	
	private final Map<String, Integer> uniforms;
	
	public ShaderManager() throws Exception {
		programID = GL20.glCreateProgram();
		if(programID == 0) {
			throw new Exception("Could not create shader");
		}
		uniforms = new HashMap<>();
	}
	
	public void createUniform(String uniformName) throws Exception {
		int uniformLocation = GL20.glGetUniformLocation(programID, uniformName);
		if(uniformLocation < 0) {
			throw new Exception("Could not find uniform "+uniformName);
		}
		uniforms.put(uniformName, uniformLocation);
	}
	
	public void createDirectionalLightUniform(String uniformName) throws Exception {
		createUniform(uniformName+".color");
		createUniform(uniformName+".direction");
		createUniform(uniformName+".intensity");
	}
	
	public void createMaterialUniform(String uniformName) throws Exception {
		createUniform(uniformName+".ambient");
		createUniform(uniformName+".diffuse");
		createUniform(uniformName+".specular");
		createUniform(uniformName+".hasTexture");
		createUniform(uniformName+".reflectance");
	}
	
	public void createPointLightUniform(String uniformName) throws Exception {
		createUniform(uniformName+".color");
		createUniform(uniformName+".position");
		createUniform(uniformName+".intensity");
		createUniform(uniformName+".constant");
		createUniform(uniformName+".linear");
		createUniform(uniformName+".exponent");
	}
	
	public void createPointLightListUniform(String uniformName, int size) throws Exception {
		for(int i = 0; i < size; i++) {
			createPointLightUniform(uniformName + "[" + i + "]");
		}
	}
	
	public void createSpotLightUniform(String uniformName) throws Exception {
		createPointLightUniform(uniformName+".pl");
		createUniform(uniformName+".conedir");
		createUniform(uniformName+".cutoff");
	}
	
	public void createSpotLightListUniform(String uniformName, int size) throws Exception {
		for(int i = 0; i < size; i++) {
			createSpotLightUniform(uniformName + "[" + i + "]");
		}
	}
	
	public void setUniform(String uniformName, Matrix4f value) {
		try(MemoryStack stack = MemoryStack.stackPush()){
			GL20.glUniformMatrix4fv(uniforms.get(uniformName), false, value.get(stack.mallocFloat(16)));
		}
	}
	
	public void setUniform(String uniformName, Vector3f value) {
		GL20.glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
	}
	
	public void setUniform(String uniformName, Vector4f value) {
		GL20.glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
	}
	
	public void setUniform(String uniformName, int value) {
		GL20.glUniform1i(uniforms.get(uniformName), value);
	}
	public void setUniform(String uniformName, float value) {
		GL20.glUniform1f(uniforms.get(uniformName), value);
	}
	
	public void setUniform(String uniformName, boolean value) {
		GL20.glUniform1f(uniforms.get(uniformName), value?1:0);
	}
	
	public void setUniform(String uniformName, Material material) {
		setUniform(uniformName+".ambient", material.getAmbientColor());
		setUniform(uniformName+".diffuse", material.getDiffuseColor());
		setUniform(uniformName+".specular", material.getSpecularColor());
		setUniform(uniformName+".hasTexture", material.hasTexture() ? 1:0);
		setUniform(uniformName+".reflectance", material.getReflectance());
	}
	
	public void setUniform(String uniformName, DirectionalLight light) {
		setUniform(uniformName + ".color", light.getColor());
		setUniform(uniformName + ".direction", light.getDirection());
		setUniform(uniformName + ".intensity", light.getIntensity());
	}
	
	public void setUniform(String uniformName, PointLight pointlight) {
		setUniform(uniformName+".color", pointlight.getColor());
		setUniform(uniformName+".position", pointlight.getPosition());
		setUniform(uniformName+".intensity", pointlight.getIntensity());
		setUniform(uniformName+".constant", pointlight.getConstant());
		setUniform(uniformName+".linear", pointlight.getLinear());
		setUniform(uniformName+".exponent", pointlight.getExponent());
	}
	
	public void setUniform(String uniformName, SpotLight spotLight) {
		setUniform(uniformName+".pl", spotLight.getPointLight());
		setUniform(uniformName+".conedir", spotLight.getConeDirection());
		setUniform(uniformName+".cutoff", spotLight.getCutoff());
	}
	
	public void setUniform(String uniformName, PointLight[] pointLights) {
		int numLights = pointLights != null ? pointLights.length : 0;
		for(int i = 0; i < numLights; i++) {
			setUniform(uniformName, pointLights[i], i);
		}
	}
	
	public void setUniform(String uniformName, PointLight pointLight, int pos) {
		setUniform(uniformName + "["+pos+"]", pointLight);
	}
	
	public void setUniform(String uniformName, SpotLight[] spotLights) {
		int numLights = spotLights != null ? spotLights.length : 0;
		for(int i = 0; i < numLights; i++) {
			setUniform(uniformName, spotLights[i], i);
		}
	}
	
	public void setUniform(String uniformName, SpotLight spotLight, int pos) {
		setUniform(uniformName + "[" +pos+"]", spotLight);
	}
	
	public void createVertexShader(String shaderCode) throws Exception {
		vertexShaderID = createShader(shaderCode, GL20.GL_VERTEX_SHADER);
	}
	
	public void createFragmentShader(String shaderCode) throws Exception {
		fragmentShaderID = createShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
	}
	
	public int createShader(String shaderCode, int shaderType) throws Exception {
		int shaderID = GL20.glCreateShader(shaderType);
		if(shaderID == 0) {
			throw new Exception("Error creating shader of type "+shaderType);
		}
		
		GL20.glShaderSource(shaderID, shaderCode);
		GL20.glCompileShader(shaderID);
		
		if(GL20.glGetShaderi(shaderID,  GL20.GL_COMPILE_STATUS) == 0) {
			throw new Exception("Error compiling shader code of type "+shaderType+". Info: "+GL20.glGetShaderInfoLog(shaderID, 1024));
		}
		
		GL20.glAttachShader(programID, shaderID);
		
		return shaderID;
	}
	
	public void link() throws Exception {
		GL20.glLinkProgram(programID);
		if(GL20.glGetProgrami(programID,  GL20.GL_LINK_STATUS) == 0) {
			throw new Exception("Error compiling shader code. Info: "+GL20.glGetProgramInfoLog(programID, 1024));
		}
		if(vertexShaderID != 0) {
			GL20.glDetachShader(programID, vertexShaderID);
		}
		if(fragmentShaderID != 0) {
			GL20.glDetachShader(programID, fragmentShaderID);
		}
		GL20.glValidateProgram(programID);
		if(GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == 0) {
			throw new Exception("Unable to validate shader code: "+GL20.glGetProgramInfoLog(programID, 1024));
		}
	}
	
	public void bind() {
		GL20.glUseProgram(programID);
	}
	
	public void unbind() {
		GL20.glUseProgram(0);
	}
	
	public void cleanup() {
		unbind();
		if(programID != 0) {
			GL20.glDeleteProgram(programID);
		}
	}
	
}
