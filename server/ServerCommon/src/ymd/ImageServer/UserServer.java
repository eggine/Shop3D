package ymd.ImageServer;

import java.net.Socket;

import ymd.Common.MsgRing;
import ymd.Common.MsgServer;

/**
 * 
 * @author zlh
 * 与远程用户服务器通信
 */
public class UserServer extends MsgServer{

	public UserServer(Socket _socket){
		super(_socket);
	}
	
}
