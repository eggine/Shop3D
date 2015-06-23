package directory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
public class SSHTEST {
	public static SSHTEST instance=null;
	private static JSch jsch;
	private static Session session;
	
	private SSHTEST(){
	}
	public static SSHTEST getInstance(){
		if(instance==null){
			return new SSHTEST();
		}
		return instance;
	}

	
	/**
	 * 连接到指定的IP
	 * 
	 * @throws JSchException
	 */
	public void connect() throws JSchException {
		jsch = new JSch();
		session = jsch.getSession("root", "120.25.208.98", 22);
		session.setPassword("Jsw123456");
		
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		
		session.connect();
	}

	/**
	 * 执行相关的命令
	 * @throws JSchException 
	 */
	public void execCmd(String commandType,String url,ArrayList<String> list) throws JSchException {
		String command=commandType+url;
		connect();
		BufferedReader reader = null;
		Channel channel = null;
		try {
//			while (command != null) {
				channel = session.openChannel("exec");
				((ChannelExec) channel).setCommand(command);
				
				channel.setInputStream(null);
				((ChannelExec) channel).setErrStream(System.err);

				channel.connect();
				InputStream in = channel.getInputStream();
				reader = new BufferedReader(new InputStreamReader(in));
				String buf = null;
				while ((buf = reader.readLine()) != null) {
//					System.out.println(buf);
					list.add(buf);
				}
//			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSchException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			channel.disconnect();
			session.disconnect();
		}
	}
	
	public static void main (String[]args){
		try {
			ArrayList<String>list=new ArrayList<String>();
			new SSHTEST().execCmd(ComandType.LS_A,"../alidata/server",list);
			for (String s:list) {
				System.out.println(s);
			}
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
   
}
