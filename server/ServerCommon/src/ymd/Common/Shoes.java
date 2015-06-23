package ymd.Common;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;


public class Shoes {
	
	public class SubMesh{
		public SubMesh(String _name,JSONArray _parent){
			mName=_name;
			mMaterials=new Vector<String>();
			mJsonSubMesh=new JSONObject();
			mJsonSubMesh.put("Name", mName);
			mJsonSubMesh.put("Material", new JSONArray());
			_parent.put(mJsonSubMesh);
		}
		
		public void addMaterial(String _mtl){
			mMaterials.add(_mtl);
			mJsonSubMesh.getJSONArray("Material").put(_mtl);
		}
		
		private String mName;
		private Vector<String> mMaterials;
		private JSONObject mJsonSubMesh;
	}
	
	public class Mesh{
		public Mesh(String _name,JSONArray _parent){
			mName=_name;
			mSubMeshs=new Vector<SubMesh>();
			mJsonMesh=new JSONObject();
			mJsonMesh.put("Name", mName);
			mJsonMesh.put("SubMesh", new JSONArray());
			_parent.put(mJsonMesh);
		}
		
		public SubMesh createSubMesh(String _name){
			SubMesh submesh=new SubMesh(_name,mJsonMesh.getJSONArray("SubMesh"));
			mSubMeshs.add(submesh);
			return submesh;
		}
		
		private String mName;
		private Vector<SubMesh> mSubMeshs;
		private JSONObject mJsonMesh;
	}
	
	public class Component{
		public Component(String _name,JSONArray _parent){
			mName=_name;
			mMeshs=new Vector<Mesh>();
			mJsonComponent=new JSONObject();
			mJsonComponent.put("Name", mName);
			mJsonComponent.put("Mesh", new JSONArray());
			_parent.put(mJsonComponent);
		}
		
		public Mesh createMesh(String _name){
			Mesh mesh=new Mesh(_name,mJsonComponent.getJSONArray("Mesh"));
			mMeshs.add(mesh);
			return mesh;
		}
		
		private String mName;
		private Vector<Mesh> mMeshs;
		private JSONObject mJsonComponent;
	}
	
	public Shoes(String _id,String _name){
		mID=_id;
		mName=_name;
		mComponents=new Vector<Component>();
		mJsonShoes=new JSONObject();
		mJsonShoes.put("ID", mID);
		mJsonShoes.put("Name", mName);
		mJsonShoes.put("Component", new JSONArray());
	}
	
	public Component createComponent(String _name){
		Component component=new Component(_name,mJsonShoes.getJSONArray("Component"));
		mComponents.add(component);
		return component;
	}
	
	public String toString(){
		return mJsonShoes.toString();
	}
	
	private String mID;
	private String mName;
	private Vector<Component> mComponents;
	private JSONObject mJsonShoes;

}
