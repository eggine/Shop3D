package ymd.UserServer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Test {
		public void write(byte[]data,String filePath) throws IOException{
		        FileOutputStream fos = null;
		        FileChannel fc_out = null;
		        try {
		            fos = new FileOutputStream(filePath, true);
		            fc_out = fos.getChannel();
		            ByteBuffer buf = ByteBuffer.wrap(data);
		            buf.put(data);
		            buf.flip();
		            fc_out.write(buf);
		        } catch (IOException e) {
		            e.printStackTrace();
		        } finally {
		            if (null != fc_out) {
		                fc_out.close();
		            }
		            if (null != fos) {
		                fos.close();
		            }
		        }
		}
}
