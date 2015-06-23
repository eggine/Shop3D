package ymd.Common;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 包含读写消息的全部接口，包括从socket流读写
 * @author zlh
 */
public class Message extends ByteBuffer{
	
	public final static int MSG_RenderServerState=1;
	public final static int MSG_RequireImages=2;
	public final static int MSG_TransferImages=3;
	public final static int MSG_UploadItem3D=4;
	
	public Message(){
		super(10240);
	}
	
	public void setType(int _type){
		try {
			writeInt32(0);
			writeInt16(_type);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 从_socketIStream接收消息并写到自身的buf中
	 * @param _socketIStream
	 */
	public void recvBytes(DataInputStream _socketIStream,int _length){
		try {
			reserve(mRight+_length);
			mRight+=_socketIStream.read(mBuffer,mRight,_length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 将自身buf中的数据写到_socketOStream并发送出去
	 * @param _socketOStream
	 */
	public void sendBytes(DataOutputStream _socketOStream){
		try {
			//写入消息长度到首部
			writeInt32(0,this.getAvailable());
			//写入socket发送
			_socketOStream.write(mBuffer,0,mRight);
			_socketOStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] readImage(){
		int length=readInt32();
		System.out.println("Image Size " + length);
		byte[] img=new byte[length];
		readBytes(img,0,length);
		return img;
	}
	
	public int getType(){
		int type=readInt16(4);
		return type;
	}
	
	/**
	 * 消息帧中记录的长度，mBuffer的长度比这个大
	 * @return
	 */
	public int getLength(){
		int length=readInt32(0);
		return length;
	}
	
	/**
	 * 将读指针定位到消息体的开始
	 */
	public void seekBody(){
		seekRead(0,6);//消息头长度为6字节
	}
	
	/**
	 * 将读指针定位到消息头的开始
	 */
	public void seekHead(){
		seekRead(0,0);
	}
	
	public MsgServer _Source;
	
}
