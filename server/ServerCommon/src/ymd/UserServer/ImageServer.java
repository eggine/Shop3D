package ymd.UserServer;

import java.net.Socket;

import ymd.Common.MsgRing;
import ymd.Common.MsgServer;

/**
 * 
 * @author zlh
 * 与远程图片服务器通信
 */
public class ImageServer extends MsgServer{
	
	public ImageServer(Socket _socket){
		super(_socket);
	}
	
}
