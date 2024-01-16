package dev.blue.lwjgl.entity;

public class Model {
	
	private int id;
	private int vertexCount;
	private Material material;
	
	public Model(int id, int vertexCount) {
		this.id = id;
		this.vertexCount = vertexCount;
		this.material = new Material();
	}
	
	public Model(int id, int vertexCount, Texture texture) {
		this.id = id;
		this.vertexCount = vertexCount;
		this.material = new Material(texture);
	}
	
	public Model(Model model, Texture texture) {
		this.id = model.getID();
		this.vertexCount = model.getVertexCount();
		this.material = model.getMaterial();
		this.material.setTexture(texture);
	}
	
	public int getID() {
		return id;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public void setTexture(Texture texture) {
		this.material.setTexture(texture);
	}
	
	public Texture getTexture() {
		return material.getTexture();
	}
	
	public void setTexture(Texture texture, float reflectance) {
		this.material.setTexture(texture);
		this.material.setReflectance(reflectance);
	}
	
}
