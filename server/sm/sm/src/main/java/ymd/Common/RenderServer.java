package ymd.Common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Iterator;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;

import ymd.Common.Message;
import ymd.Common.MsgRing;
import ymd.Common.MsgServer;

/**
 * 
 * @author zlh
 * 与远程渲染服务器通信
 */
public class RenderServer extends MsgServer{
	
	public RenderServer(Socket _socket){
		super(_socket);

	}
	
	
}
