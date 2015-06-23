package jfinal.modelsupport;


public class Mesh{ 
	public String name;
	public SubMesh[] submesh;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SubMesh[] getSubmesh() {
		return submesh;
	}
	public void setSubmesh(SubMesh[] submesh) {
		this.submesh = submesh;
	}


}