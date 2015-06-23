package ymd.Common;

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

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;

import ymd.Common.Message;
import ymd.Common.MsgRing;
import ymd.Common.RenderServer;

/**
 * 
 * @author zlh 
 * 接收连接，集中处理消息
 */
public class MainServer implements Runnable{
	public static boolean falt=true;
	//开启线程监听渲染服务器的连接
	class Acceptor extends Thread {
		Acceptor(){
			try {
				mServerSocket = new ServerSocket(mPort);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void run() {
			mLogger.info("acceptor startup");
			while (falt) {
				acceptConnection();
			}
		}

		private void acceptConnection() {
			try {
				Socket socket = mServerSocket.accept();
				RenderServer renderServer = new RenderServer(socket);
				renderServer.start();
				mMsgServers.add(renderServer);
				mLogger.info("Message Server Count: "+mMsgServers.size());
				onConnected(renderServer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		protected ServerSocket mServerSocket;
	}
	
	public MainServer(int _port) {
		PropertyConfigurator.configure("log4j.properties");
		mLogger=Logger.getLogger(this.getClass());
		
		mPort=_port;
		mAcceptor=new Acceptor();
		mMsgServers = new Vector<MsgServer>();
	}
	
	public void run(){
		startup();
		
		while(true){
			for(int i=0;i<mMsgServers.size();++i){
				MsgServer rs=mMsgServers.get(i);
				//断开连接
				mLogger.info("MessageServer State "+rs.getServerState());
				if(rs.getServerState()==RenderServer.MSS_Close){
					mLogger.info("close MsgServer begin");
					rs.mLoop=false;
					try {
						rs.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mMsgServers.remove(i);
					mLogger.info("close MsgServer finish");
					break;
				}
				//处理消息
				if(rs.isReadable()){
					mLogger.info("handle message begin");
					Message msg=mMsgServers.get(i).readBegin();
					postMessage(msg);
					mMsgServers.get(i).readEnd();
					mLogger.info("handle message finish");
				}
			}
		}
		
	}
	
	protected void postMessage(Message _msg){
		
	}
	
	protected void startup(){
		mLogger.info("startup");
		mAcceptor.start();
	}
	
	protected void shutdown(){
		mLogger.info("shutdown");
	}
	
	/**
	 * 新服务器加入时回调
	 * @param _server
	 */
	protected void onConnected(MsgServer _server){
		mLogger.info("new MsgServer join");
	}
	
	protected void onUpdateReserServerState(Message _msg){
		mLogger.info("MsgServer update state");
		RenderServer rs=(RenderServer)_msg._Source;
		rs.updateState(_msg);
	}

	protected int mPort;
	protected String mName;
	protected Logger mLogger;
	protected Acceptor mAcceptor;
	protected Vector<MsgServer> mMsgServers;
}
