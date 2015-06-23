package ymd.ImageServer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import ymd.Common.MainServer;
import ymd.Common.Message;
import ymd.Common.MsgRing;
import ymd.Common.RenderServer;

/**
 * 
 * @author zlh 
 * 管理远程渲染服务器，集中处理消息
 */
public class ImageServer extends MainServer{
	
	
	private static ImageServer instance=null;
	public static ImageServer getInstance(){
		if (instance==null){
			instance=new ImageServer();
		}
		return instance;
	}
	public void cloaseImageServer(){
		falt=false;
	}

	private ImageServer() {
		super(56790);
	}
	
	protected void postMessage(Message _msg){
		_msg.seekRead(0, 0);
		int msgLength=_msg.readInt32();
		int msgType=_msg.readInt16();
		switch(msgType){
		case Message.MSG_RenderServerState:
			onUpdateReserServerState(_msg);
			break;
		case Message.MSG_TransferImages:
			onRecvImages(_msg);
			break;
		}
	}

	/**
	 * 接收渲染服务器传来的图片序列
	 * @param _msg 完整的图片消息
	 */
	protected void onRecvImages(Message _msg) {
		try {
			System.out.println("Images recv begin");
			Map<String,String> map=new HashMap<String,String>();
			new ResolutionImages().resolutionMsgBuffer(_msg,map);
			System.out.println("ImageServer : Images recv finish "+map.size());
		} catch (Exception _e) {
			_e.printStackTrace();
		}

	}
}
