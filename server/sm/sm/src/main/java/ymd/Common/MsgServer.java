package ymd.Common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;

import ymd.ImageServer.ImageServer;

public class MsgServer extends Thread{
	
	public final static int MSS_Ready=1;
	public final static int MSS_Runing=2;
	public final static int MSS_Close=3;
	
	public MsgServer(Socket _socket){
		try {
			mLogger=Logger.getLogger(this.getClass());
			
			mLoop=true;
			mNewMsg=null;
			mNewMsgLength=0;
			mSocket=_socket;
			mRecvStream=new DataInputStream(mSocket.getInputStream());
			mSendStream=new DataOutputStream(mSocket.getOutputStream());
			
			mRecvRing=new MsgRing(3);
			mSendRing=new MsgRing(3);
			mServerState=MSS_Ready;
			mServerName=mSocket.getInetAddress().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		mLogger.info("startup");
		while(mLoop){
			try {
				if(mRecvStream.available()>0){
					recvMessage(mRecvStream);
				}
				if(mSendRing.isReadable()){
					sendMessage(mSendStream);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mLogger.info("shutdown");
	}
	
	public void updateState(Message _msg){
		_msg.seekBody();
		mServerState=_msg.readInt16();
		mServerName=_msg.readUTF();
	}
	
	public Message writeBegin(){
		Message msg=mSendRing.writeBegin();
		msg._Source=this;
		return msg;
	}
	
	public void writeEnd(){
		mSendRing.writeEnd();
	}
	
	public Message readBegin(){
		return mRecvRing.readBegin();
	}
	
	public void readEnd(){
		mRecvRing.readEnd();
	}
	
	public boolean isReadable(){
		return mRecvRing.isReadable();
	}
	
	public boolean isWritable(){
		return mSendRing.isWritable();
	}
	
	public int getServerState(){
		return mServerState;
	}
	
	public String getServerName(){
		return mServerName;
	}
	
	private void recvMessage(DataInputStream _src){
		if(mNewMsg==null){
			mLogger.info("recv begin");
			mNewMsg=mRecvRing.writeBegin();
			mNewMsg._Source=this;
			mNewMsg.seekRead(0,0);
			mNewMsg.seekWrite(0,0);
			mNewMsg.recvBytes(_src,4);
			mNewMsgLength=mNewMsg.getLength();
			mLogger.info("message length "+mNewMsgLength);
		}
		
		int remain=mNewMsgLength-mNewMsg.getMost();
		if(remain>0){
			mNewMsg.recvBytes(_src,remain);
			mLogger.info("recv since " + mNewMsg.getMost());
		}
		
		if(mNewMsg.getMost()==mNewMsgLength){
			mRecvRing.writeEnd();
			mLogger.info("recv finish " + mNewMsg.getMost());
			mNewMsg=null;
		}
	}
	
	private void sendMessage(DataOutputStream _dest){
		mLogger.info("send message begin ");
		Message msg=mSendRing.readBegin();
		msg.sendBytes(_dest);
		mSendRing.readEnd();
		System.out.println("Send message finish "+msg.getLength());
	}
	
	
	public boolean mLoop;
	
	protected Socket mSocket;
	protected DataInputStream mRecvStream;
	protected DataOutputStream mSendStream;
	
	protected String mServerName;
	protected int mServerState;
	protected int mNewMsgLength;
	protected Message mNewMsg;
	protected MsgRing mRecvRing;
	protected MsgRing mSendRing;
	
	protected Logger mLogger;
}
