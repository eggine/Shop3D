package ymd.Common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

public class Main {
	
    public static byte[] writeInt(int v){
    	byte[] buf=new byte[4];
    	buf[0]=(byte)((v >>> 24) & 0xFF);
    	buf[1]=(byte)((v >>> 16) & 0xFF);
    	buf[2]=(byte)((v >>>  8) & 0xFF);
    	buf[3]=(byte)((v >>>  0) & 0xFF);
    	return buf;
    }
    
    public static int readInt(byte[] buf) {
        int ch1 = buf[0];
        int ch2 = buf[1];
        int ch3 = buf[2];
        int ch4 = buf[3];
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ByteBuffer bab=new ByteBuffer(1024);
		try {
			bab.writeInt32(1234);
			System.out.println(bab.readInt32());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
