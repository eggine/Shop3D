package dataEntity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.events.StartDocument;

import sun.rmi.runtime.NewThreadAction;

public class ByteToArray {
	
	public static void main(String[] args) {
		ByteToArray instance = new ByteToArray();
		int msgtype = 1;
		byte[] msgtypes = instance.intToBytes(msgtype);
		String itemid = "AAAAAAAAA";
		byte[] itemids = itemid.getBytes();
		byte[] itemidls = instance.intToBytes(itemids.length);
		
		String customid = "BBBBBB";
		byte[] customids = customid.getBytes();
		byte[] customidls = instance.intToBytes(customids.length);
		String image1 = "IMAGE1";
		byte[] image1s = image1.getBytes();
		byte[] image1ls = instance.intToBytes(image1s.length);
		String image2 = "IMAGE2";
		byte[] image2s = image2.getBytes();
		byte[] image2ls = instance.intToBytes(image2s.length);
		int x = 4 + 4 + itemids.length + customids.length + image1s.length
				+ image2s.length+itemidls.length + customidls.length + image1ls.length
				+ image2ls.length;
		int msglength = x;
		System.out.println("数组总长度"+x);
		byte[] msglengths = instance.intToBytes(msglength);

		byte[] mobiao = new byte[x];
		
		
//		for (int i = 0; i < msglengths.length; i++) {
//			System.out.print(msglengths[i]+",");
//		}
//		for (int i = 0; i < msgtypes.length; i++) {
//			System.out.print(msgtypes[i]+",");
//		}
//		//////
//		for (int i = 0; i < itemidls.length; i++) {
//			System.out.print(itemidls[i]+",");
//		}
//		for (int i = 0; i < itemids.length; i++) {
//			System.out.print(itemids[i]+",");
//		}
//		/////////
//		for (int i = 0; i < customidls.length; i++) {
//			System.out.print(customidls[i]+",");
//		}
//		for (int i = 0; i < customids.length; i++) {
//			System.out.print(customids[i]+",");
//		}
//		//////////
//		for (int i = 0; i < image1ls.length; i++) {
//			System.out.print(image1ls[i]+",");
//		}
//		for (int i = 0; i < image1s.length; i++) {
//			System.out.print(image1s[i]+",");
//		}
//		/////////
//		for (int i = 0; i < image2ls.length; i++) {
//			System.out.print(image2ls[i]+",");
//		}
//		///////
//		for (int i = 0; i < image2s.length; i++) {
//			System.out.print(image2s[i]+",");
//		}
	byte[]mubiao={0,0,0,51,0,0,0,1,0,0,0,9,65,65,65,65,65,65,65,65,65,0,0,0,6,66,66,66,66,66,66,0,0,0,6,73,77,65,71,69,49,0,0,0,6,73,77,65,71,69,50,};
		Map<String, Object> map=new HashMap<String, Object>();
		instance.Tofile(mubiao, map);

	}

	public void Tofile(final byte[] b, final Map<String, Object> map) {
		new Thread(new Runnable() {
			public void run() {
				boolean falt=true;
				while (falt) {
					int index = 0;// 解释数组结束所在下标
					byte[] model = new byte[4];
					if (b.length > 4) {
						for (int i = index; i < 4; i++) {
							model[i] = b[i];
						}
						index = index + 4;
						// /解释字节数组的总长度
						int msgLength = ByteToInt(model);
						map.put("msglenth", msgLength);
						if (b.length == msgLength) {
							// /解释图片类型
							for (int i = index; i < index + 4; i++) {
								model[i - 4] = b[i];
							}
							index = index + 4;
							// 解释数据类型
							int msgtype = ByteToInt(model);
							map.put("msgtype", msgtype);
							for (int i = index; i < index + 4; i++) {
								model[ i- 8] = b[i];
							}
							int itemIDlength = ByteToInt(model);
							// int itemIDlength=ByteToInt(getbyteArray(b, index,
							// index+4));
							index += itemIDlength+4;
							String itemID = new String(getbyteArray(b, index
									- itemIDlength, index));
							map.put("itemID", itemID);
							for (int i = index; i < index + 4; i++) {
								model[i -index] = b[i];
							}
							int customIDlength = ByteToInt(model);
							// int customIDlength=ByteToInt(getbyteArray(b,
							// index, index+4));
							index += customIDlength+4;
							String customID = new String(getbyteArray(b, index
									- customIDlength, index));
							map.put("customID", customID);
							try {
								transformImage(b, index);
							} catch (Exception e) {
								falt=false;
							}
						} else {
							falt=false;

						}
					}
				}

			}
		}).start();
	}

	/**
	 * z转换图片
	 * 
	 * @param b
	 *            批量图片字节数组
	 * @param m
	 *            开始下标
	 * @return
	 * @throws Exception 
	 */
	public int transformImage(byte[] b, int m) throws Exception {
		int index = m;
		int mscLength = 0;
		byte[] model = new byte[4];
		for (int i = index; i < index + 4; i++) {
			model[i-index]=b[i];
		}
		mscLength=ByteToInt(model);
		index += mscLength+4;// /图片结束的下标
		System.out.println("图片数据-------------"+getFile(getbyteArray(b, index - mscLength, index)));
		if(index==(b.length-1)){
			throw new Exception("妈蛋的，不要递归了");
			}
		return transformImage(b,index) ;
	}

	/**
	 * 获取待转换 的数组
	 * 
	 * @param m
	 * @param start 开始坐标
	 * @param end 结束坐标
	 * @return
	 */
	public byte[] getbyteArray(byte[] m, int start, int end) {
		byte[] mubiao = new byte[end - start];
		for (int i = start; i < end; i++) {
			mubiao[i - start] = m[i];
		}
		return mubiao;
	}

	/**
	 * 字节数组转整形
	 * 
	 * @param b
	 * @return
	 */
	public int ByteToInt(byte[] bytes) {
	      int result = 0;  
	        if(bytes.length == 4){  
	            int a = (bytes[0] & 0xff) << 24;
	            int b = (bytes[1] & 0xff) << 16;  
	            int c = (bytes[2] & 0xff) << 8;  
	            int d = (bytes[3] & 0xff);  
	            result = a | b | c | d;  
	        }  
	        return result;
	}

	public String getFile(byte[] b) {
		for (int i = 0; i < b.length; i++) {
			System.out.println(b[i]+",");
		}
		System.out.println("------------------------------");
			return new String(b);
	}

	public byte[] intToBytes(int num) {
	     byte[] result = new byte[4];  
	        result[0] = (byte)((num >>> 24) & 0xff); 
	        result[1] = (byte)((num >>> 16)& 0xff );  
	        result[2] = (byte)((num >>> 8) & 0xff );  
	        result[3] = (byte)((num >>> 0) & 0xff );  
	        return result;  
	}

}
