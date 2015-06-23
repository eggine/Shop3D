package ymd.UserServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import ymd.Common.MainServer;
import ymd.Common.Message;
import ymd.Common.MsgRing;
import ymd.Common.MsgServer;
import ymd.Common.RenderServer;

/**
 * 
 * @author zlh
 * 管理远程渲染服务器，渲染服务器和图片服务器的消息都会写到这集中处理消息
 */
public class UserServer extends MainServer{
	
	private static UserServer instance=null;
	public static UserServer getinstance(){
		if(instance==null){
			instance=new UserServer();
		}
		return instance;
	}
	
	private UserServer() {
		super(56789);
	}
	
	
	public void closeThread(){
		falt=false;
	}
	
	protected void postMessage(Message _msg){
		int msgType=_msg.getType();
		switch(msgType){
		case Message.MSG_RenderServerState:
			onUpdateReserServerState(_msg);
			break;
		}
	}
	
	protected void onConnected(MsgServer _server){
		super.onConnected(_server);
	}
	
	/**
	 * 向渲染服务器请求图片，渲染服务器将图片返回给图片服务器
	 * @param _item 商品id
	 * @param _custom 商品方案
	 */
	public void requestImages(String _item, String _custom,List<String> _imagesNo) {
		mLogger.info("request images begin ");
		
		//构造消息体json
		JSONObject root = new JSONObject();
		root.put("itemid", _item);
		root.put("custom", _custom);
		JSONArray images = new JSONArray();
		for (String imageNo :_imagesNo) {
			images.put(imageNo);
		}
		root.put("images", images);
		String msgBody = root.toString();
		System.out.println(msgBody);
		Message msg=null;
		try {
			RenderServer rs=selectRenderServer();
			msg=rs.writeBegin();
			msg.setType(Message.MSG_RequireImages);//设置类型
			msg.writeUTF(msgBody);
			rs.writeEnd();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mLogger.info("request images finish " + msg.getMost());
	}
	
	/**
	 * 单独上传商品3D数据，包含商品描述json
	 */
	public void uploadItemData3D(String _itemID,byte[] _zip){
		try {
			mLogger.info("upload item data3d begin");
			RenderServer rs=selectRenderServer();
			Message msg=rs.writeBegin();
			msg.writeInt32(0);
			msg.writeInt16(Message.MSG_UploadItem3D);
			msg.writeUTF(_itemID);
			msg.writeInt32(_zip.length);//写入商品模型数据
			msg.writeBytes(_zip);
			rs.writeEnd();
			mLogger.info("upload item data3d finish");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected RenderServer selectRenderServer(){
		RenderServer rs=null;
		for(int i=0;i<mMsgServers.size();i++){
			if(mMsgServers.get(i) instanceof RenderServer){
				rs=(RenderServer)mMsgServers.get(i);
				break;
			}
		}
		return rs;
	}

}
