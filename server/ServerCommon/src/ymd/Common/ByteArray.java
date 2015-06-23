package ymd.Common;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.Arrays;

/**
 * 字节数组的包裹类，不管理缓冲区
 * 
 * @author zlh
 * 
 */
public class ByteArray {

	protected byte[] mBuffer;

	public ByteArray(byte[] _byte) {
		mBuffer = _byte;
	}

	public synchronized void writeByte(int _start, int _b) {
		mBuffer[_start] = (byte) _b;
	}

	public synchronized void writeBytes(int _destPos, byte[] _src, int _srcPos, int _len) {
		System.arraycopy(_src, _srcPos, mBuffer, _destPos, _len);
	}

	public final void writeBoolean(int _start, boolean _v) {
		writeByte(_start, _v ? 1 : 0);
	}
	
	public final void writeChar(int _start, int _v) {
		writeByte(_start, (_v >>> 8) & 0xFF);
		writeByte(_start + 1, (_v >>> 0) & 0xFF);
	}

	public final void writeInt16(int _start, int _v) {
		writeByte(_start, (_v >>> 8) & 0xFF);
		writeByte(_start + 1, (_v >>> 0) & 0xFF);
	}

	public final void writeInt32(int _start, int v) {
		writeByte(_start, (v >>> 24) & 0xFF);
		writeByte(_start + 1, (v >>> 16) & 0xFF);
		writeByte(_start + 2, (v >>> 8) & 0xFF);
		writeByte(_start + 3, (v >>> 0) & 0xFF);
	}

	public final void writeLong(int _start, long v) {
		mBuffer[_start] = (byte) (v >>> 56);
		mBuffer[_start + 1] = (byte) (v >>> 48);
		mBuffer[_start + 2] = (byte) (v >>> 40);
		mBuffer[_start + 3] = (byte) (v >>> 32);
		mBuffer[_start + 4] = (byte) (v >>> 24);
		mBuffer[_start + 5] = (byte) (v >>> 16);
		mBuffer[_start + 6] = (byte) (v >>> 8);
		mBuffer[_start + 7] = (byte) (v >>> 0);
	}

	public final void writeFloat(int _start, float v) {
		writeInt32(_start, Float.floatToIntBits(v));
	}

	public final void writeDouble(int _start, double v) throws IOException {
		writeLong(_start, Double.doubleToLongBits(v));
	}

	public final void writeChars(int _start, String s) throws IOException {
		int len = s.length();
		for (int i = 0; i < len; i++) {
			int v = s.charAt(i);
			writeByte(_start, (v >>> 8) & 0xFF);
			writeByte(_start, (v >>> 0) & 0xFF);
		}
	}

	public final int writeUTF(int _start, String str) throws IOException {
		byte[] buf = toUTF(str);
		writeBytes(_start, buf, 0, buf.length);
		return buf.length;
	}

	public static byte[] toUTF(String str) {
		int strlen = str.length();
		int utflen = 0;
		int c, count = 0;

		/* use charAt instead of copying String to char array */
		for (int i = 0; i < strlen; i++) {
			c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				utflen++;
			} else if (c > 0x07FF) {
				utflen += 3;
			} else {
				utflen += 2;
			}
		}

		// 首部用int记录字符串长度
		byte[] bytearr = new byte[utflen + 2];
		bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);
		bytearr[count++] = (byte) ((utflen >>> 0) & 0xFF);

		int i = 0;
		for (i = 0; i < strlen; i++) {
			c = str.charAt(i);
			if (!((c >= 0x0001) && (c <= 0x007F)))
				break;
			bytearr[count++] = (byte) c;
		}

		for (; i < strlen; i++) {
			c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				bytearr[count++] = (byte) c;

			} else if (c > 0x07FF) {
				bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
				bytearr[count++] = (byte) (0x80 | ((c >> 6) & 0x3F));
				bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
			} else {
				bytearr[count++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
				bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
			}
		}

		return bytearr;
	}

	public String readUTF(int _srcPos) {
		int utflen = readInt16(_srcPos);
		_srcPos+=2;
		byte[] bytearr = null;
		char[] chararr = null;
		bytearr = new byte[utflen];
		chararr = new char[utflen];

		int c, char2, char3;
		int count = 0;
		int chararr_count = 0;

		System.arraycopy(mBuffer,_srcPos, bytearr,0,utflen);

		while (count < utflen) {
			c = (int) bytearr[count] & 0xff;
			if (c > 127)
				break;
			count++;
			chararr[chararr_count++] = (char) c;
		}

		while (count < utflen) {
			c = (int) bytearr[count] & 0xff;
			switch (c >> 4) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				/* 0xxxxxxx */
				count++;
				chararr[chararr_count++] = (char) c;
				break;
			case 12:
			case 13:
				/* 110x xxxx 10xx xxxx */
				count += 2;
				char2 = (int) bytearr[count - 1];
				chararr[chararr_count++] = (char) (((c & 0x1F) << 6) | (char2 & 0x3F));
				break;
			case 14:
				/* 1110 xxxx 10xx xxxx 10xx xxxx */
				count += 3;
				char2 = (int) bytearr[count - 2];
				char3 = (int) bytearr[count - 1];
				chararr[chararr_count++] = (char) (((c & 0x0F) << 12)
						| ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0));
				break;
			}
		}
		// The number of chars produced may be less than utflen
		return new String(chararr, 0, chararr_count);
	}

	public final int readByte(int _start) {
		int b = mBuffer[_start] & 0xff;
		return b;
	}

	public final int readInt32(int _start) {
		int ch1 = readByte(_start);
		int ch2 = readByte(_start + 1);
		int ch3 = readByte(_start + 2);
		int ch4 = readByte(_start + 3);
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	public final int readInt16(int _start) {
		int ch1 = readByte(_start);
		int ch2 = readByte(_start + 1);
		return (ch1 << 8) + (ch2 << 0);
	}

	public byte[] getBuffer() {
		return mBuffer;
	}

}
