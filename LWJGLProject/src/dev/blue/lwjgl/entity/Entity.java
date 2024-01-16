package dev.blue.lwjgl.entity;

import org.joml.Vector3f;

public class Entity {
	
	private Model model;
	private Vector3f pos, rot; 
	private float scale;
	
	public Entity(Model model, Vector3f pos, Vector3f rot, float scale) {
		this.model = model;
		this.pos = pos;
		this.rot = rot;
		this.scale = scale;
	}
	
	public void incPos(float x, float y, float z) {
		this.pos.x += x;
		this.pos.y += y;
		this.pos.z += z;
	}
	
	public void setPos(float x, float y, float z) {
		this.pos.x = x;
		this.pos.y = y;
		this.pos.z = z;
	}
	
	public void incRot(float x, float y, float z) {
		this.rot.x += x;
		this.rot.y += y;
		this.rot.z += z;
	}
	
	public void setRot(float x, float y, float z) {
		this.rot.x = x;
		this.rot.y = y;
		this.rot.z = z;
	}

	public Model getModel() {
		return model;
	}

	public Vector3f getPos() {
		return pos;
	}

	public Vector3f getRot() {
		return rot;
	}

	public float getScale() {
		return scale;
	}
	
}
