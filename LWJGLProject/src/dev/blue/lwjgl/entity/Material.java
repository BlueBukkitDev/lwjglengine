package dev.blue.lwjgl.entity;

import org.joml.Vector4f;

import dev.blue.lwjgl.utils.Constants;

public class Material {
	
	private Vector4f ambientColor, diffuseColor, specularColor;
	private float reflectance; 
	private Texture texture;
	private boolean disableCulling;
	
	public Material() {
		this.ambientColor = Constants.DEFAULT_COLOR;
		this.diffuseColor = Constants.DEFAULT_COLOR;
		this.specularColor = Constants.DEFAULT_COLOR;
		this.texture = null;
		this.disableCulling = false;
		this.reflectance = 0;
	}
	
	public Material(Vector4f color, float reflectance) {
		this(color, color, color, reflectance, null);
	}
	
	public Material(Vector4f color, float reflectance, Texture texture) {
		this(color, color, color, reflectance, texture);
	}
	
	public Material(Texture texture) {
		this(Constants.DEFAULT_COLOR, Constants.DEFAULT_COLOR, Constants.DEFAULT_COLOR, 0, texture);
	}
	
	public Material(Texture texture, float reflectance) {
		this(Constants.DEFAULT_COLOR, Constants.DEFAULT_COLOR, Constants.DEFAULT_COLOR, reflectance, texture);
	}
	
	public Material(Vector4f ambientColor, Vector4f diffuseColor, Vector4f specularColor, float reflectance, Texture texture) {
		this.ambientColor = ambientColor;
		this.diffuseColor = diffuseColor;
		this.specularColor = specularColor;
		this.reflectance = reflectance;
		this.texture = texture;
	}

	public Vector4f getAmbientColor() {
		return ambientColor;
	}

	public void setAmbientColor(Vector4f ambientColor) {
		this.ambientColor = ambientColor;
	}

	public Vector4f getDiffuseColor() {
		return diffuseColor;
	}

	public void setDiffuseColor(Vector4f diffuseColor) {
		this.diffuseColor = diffuseColor;
	}

	public Vector4f getSpecularColor() {
		return specularColor;
	}

	public void setSpecularColor(Vector4f specularColor) {
		this.specularColor = specularColor;
	}

	public float getReflectance() {
		return reflectance;
	}

	public void setReflectance(float reflectance) {
		this.reflectance = reflectance;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public boolean hasTexture() {
		return texture != null;
	}
	
	public boolean isDisableCulling() {
		return disableCulling;
	}
	
	public void setDisableCulling(boolean disable) {
		this.disableCulling = disable;
	}
}
