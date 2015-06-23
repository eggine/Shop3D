package ymd.Common;

import java.io.EOFException;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.Arrays;

/**
 * 字节缓冲区，自动扩容
 * 
 * @author zlh
 * 
 */
public class ByteBuffer extends ByteArray {
	
	protected int mLeft = 0;
	protected int mRight = 0;

	public ByteBuffer(int _capacity) {
		super(new byte[_capacity]);
	}
	
	public void seekRead(int _basis,int _offset){
		mLeft=_basis;
		mLeft+=_offset;
	}
	
	public void seekWrite(int _basis,int _offset){
		mRight=_basis;
		mRight+=_offset;
	}

	public void reserve(int _capacity) {
		if (mBuffer.length < _capacity) {
			mBuffer = Arrays.copyOf(mBuffer,
					Math.max(mBuffer.length << 1, _capacity));
		}
	}

	public synchronized void writeByte(int _b) {
		reserve(mRight+1);
		writeByte(mRight,_b);
		mRight += 1;
	}
	
	public synchronized void writeBytes(byte[] _src) {
		reserve(mRight+_src.length);
		writeBytes(mRight,_src,0,_src.length);
		mRight += _src.length;
	}
	
	public synchronized void writeBytes(byte[] _src, int off, int len) {
		reserve(mRight+len);
		writeBytes(mRight,_src,off,len);
		mRight += len;
	}

	public final void writeBoolean(boolean v) {
		reserve(mRight+1);
		writeByte(mRight,v ? 1 : 0);
		mRight += 1;
	}

	public final void writeChar(int v) {
		reserve(mRight+2);
		writeChar(mRight,v);
		mRight += 2;
	}
	
	public final void writeInt16(int v) throws IOException {
		reserve(mRight+4);
		writeInt16(mRight,v);
		mRight+=2;
	}

	public final void writeInt32(int v) throws IOException {
		reserve(mRight+4);
		writeInt32(mRight,v);
		mRight+=4;
	}
	

	public final void writeFloat(float v) throws IOException {
		reserve(mRight+4);
		writeInt32(mRight,Float.floatToIntBits(v));
		mRight+=4;
	}

	public final void writeDouble(double v) throws IOException {
		reserve(mRight+8);
		writeLong(mRight,Double.doubleToLongBits(v));
		mRight+=8;
	}

	public final void writeChars(String s) throws IOException {
		reserve(mRight+s.length()*2);
		writeChars(mRight,s);
		mRight+=s.length()*2;
	}
	
	public final int writeUTF(String str) throws IOException {
		byte[] buf=toUTF(str);
		reserve(mRight+buf.length);
		writeBytes(mRight,buf,0,buf.length);
		mRight+=buf.length;
		return buf.length;
	}
	
	public final int readByte(){
		int b=mBuffer[mLeft]&0xff;
		mLeft+=1;
		return b;
	}
	
	public final void readBytes(byte[] _dest,int _destPos,int _length){
		System.arraycopy(mBuffer, mLeft, _dest, _destPos, _length);
		mLeft+=_length;
	}
	
    public final int readInt32() {
    	int ret=readInt32(mLeft);
    	mLeft+=4;
        return ret;
    }
    
    public final int readInt16() {
    	int ret=readInt16(mLeft);
    	mLeft+=2;
        return ret;
    }
    
    public String readUTF(){
    	String utf=readUTF(mLeft);
    	mLeft+=2;
    	mLeft+=utf.getBytes().length;
    	return utf;
    }
    
	public synchronized int getMost() {
		return mRight;
	}

	public synchronized int getAvailable() {
		return mRight - mLeft;
	}
	
	

}
