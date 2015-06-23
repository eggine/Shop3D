/*    */ package directory;
/*    */ 
/*    */ import com.jcraft.jsch.Channel;
/*    */ import com.jcraft.jsch.ChannelExec;
/*    */ import com.jcraft.jsch.JSch;
/*    */ import com.jcraft.jsch.JSchException;
/*    */ import com.jcraft.jsch.Session;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class SSHCommand
/*    */ {
/* 15 */   public static SSHCommand instance = null;
/*    */   private static JSch jsch;
/*    */   private static Session session;
/*    */ 
/*    */   public static SSHCommand getInstance()
/*    */   {
/* 22 */     if (instance == null) {
/* 23 */       return new SSHCommand();
/*    */     }
/* 25 */     return instance;
/*    */   }
/*    */ 
/*    */   public void connect()
/*    */     throws JSchException
/*    */   {
/* 35 */     jsch = new JSch();
/* 36 */     session = jsch.getSession("root", "120.25.208.98", 22);
/* 37 */     session.setPassword("Jsw123456");
/*    */ 
/* 39 */     Properties config = new Properties();
/* 40 */     config.put("StrictHostKeyChecking", "no");
/* 41 */     session.setConfig(config);
/*    */ 
/* 43 */     session.connect();
/*    */   }
/*    */ 
/*    */   public void execCmd(String commandType, String url, ArrayList<String> list)
/*    */     throws JSchException
/*    */   {
/* 51 */     String command = commandType + url;
/* 52 */     connect();
/* 53 */     BufferedReader reader = null;
/* 54 */     Channel channel = null;
/*    */     try
/*    */     {
/* 57 */       channel = session.openChannel("exec");
/* 58 */       ((ChannelExec)channel).setCommand(command);
/*    */ 
/* 60 */       channel.setInputStream(null);
/* 61 */       ((ChannelExec)channel).setErrStream(System.err);
/*    */ 
/* 63 */       channel.connect();
/* 64 */       InputStream in = channel.getInputStream();
/* 65 */       reader = new BufferedReader(new InputStreamReader(in));
/* 66 */       String buf = null;
/* 67 */       while ((buf = reader.readLine()) != null)
/*    */       {
/* 69 */         list.add(buf);
/*    */       }
/*    */     }
/*    */     catch (IOException e) {
/* 73 */       e.printStackTrace();
/*    */     } catch (JSchException e) {
/* 75 */       e.printStackTrace();
/*    */     } finally {
/*    */       try {
/* 78 */         reader.close();
/*    */       } catch (IOException e) {
/* 80 */         e.printStackTrace();
/*    */       }
/* 82 */       channel.disconnect();
/* 83 */       session.disconnect();
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void main(String[] args) {
/*    */     try {
/* 89 */       ArrayList <String>list = new ArrayList();
/* 90 */       new SSHCommand().execCmd("ls -a  cd ", "../picture", list);
/* 91 */       for (String s :list )
/* 92 */         System.out.println(s);
/*    */     }
/*    */     catch (JSchException e)
/*    */     {
/* 96 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           H:\imageserver\WEB-INF\classes\
 * Qualified Name:     directory.SSHCommand
 * JD-Core Version:    0.6.1
 */