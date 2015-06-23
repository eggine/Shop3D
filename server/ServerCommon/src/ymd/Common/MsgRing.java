package ymd.Common;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ymd.ImageServer.ImageServer;


public class MsgRing{
	
	protected class Node{
		public Node(){
			mBuffer=new Message();
		}
		public Node mNext;
		public Message mBuffer;
	}
	
	public MsgRing(int _size){
		mSize=_size;
		Node[] nodes=new Node[mSize];
		for(int i=0;i<mSize;i++){
			nodes[i]=new Node();
		}
		for(int i=0;i<mSize-1;i++){
			nodes[i].mNext=nodes[i+1];
		}
		nodes[mSize-1].mNext=nodes[0];
		
		mReadable=nodes[0];
		mWritable=nodes[0];
	}
	
	public synchronized Message readBegin(){
		Message buf=mReadable.mBuffer;
		buf.seekRead(0, 0);
		return buf;
	}
	
	public synchronized void readEnd(){
		mReadable=mReadable.mNext;
	}
	
	public synchronized Message writeBegin(){
		Message buf=mWritable.mBuffer;
		buf.seekWrite(0, 0);
		return buf;
	}
	
	public synchronized void writeEnd(){
		mWritable=mWritable.mNext;
	}
	
	public synchronized boolean isReadable(){
		return (mReadable!=mWritable);
	}
	
	public synchronized boolean isWritable(){
		return (mReadable!=mWritable.mNext);
	}
	
	private int mSize;
	private volatile Node mReadable;//使用volatile避免线程阻塞
	private volatile Node mWritable;//使用volatile避免线程阻塞
}
