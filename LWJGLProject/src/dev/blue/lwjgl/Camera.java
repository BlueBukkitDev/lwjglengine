package dev.blue.lwjgl;

import org.joml.Vector3f;

public class Camera {

	private Vector3f pos, rot;
	
	public Camera() {
		pos = new Vector3f(0, 0, 0);
		rot = new Vector3f(0, 0, 0);
	}
	
	public Camera(Vector3f position, Vector3f rotation) {
		pos = position;
		rot = rotation;
	}
	
	public void movePosition(float x, float y, float z) {
		if(z != 0) {
			pos.x += (float) Math.sin(Math.toRadians(rot.y)) * -1.0 * z;
			pos.z += (float) Math.cos(Math.toRadians(rot.y)) * z;
		}
		
		if(x != 0) {
			pos.x += (float) Math.sin(Math.toRadians(rot.y - 90)) * -1.0 * x;
			pos.z += (float) Math.cos(Math.toRadians(rot.y - 90)) * x;
		}
		pos.y += y;
	}
	
	public void setPosition(float x, float y, float z) {
		this.pos.x = x;
		this.pos.y = y;
		this.pos.z = z;
	}
	
	public void setRotation(float x, float y, float z) {
		this.rot.x = x;
		this.rot.y = y;
		this.rot.z = z;
	}
	
	public void moveRotation(float x, float y, float z) {
		this.rot.x += x;
		this.rot.y += y;
		this.rot.z += z;
	}

	public Vector3f getPos() {
		return pos;
	}

	public Vector3f getRot() {
		return rot;
	}
	
}
