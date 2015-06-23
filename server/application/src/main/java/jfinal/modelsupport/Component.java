package jfinal.modelsupport;


public class Component {
	public String name;
	public Mesh[] mesh;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Mesh[] getMesh() {
		return mesh;
	}
	public void setMesh(Mesh[] mesh) {
		this.mesh = mesh;
	}
}