package ymd.ImageServer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.json.JSONArray;
import org.json.JSONObject;

import ymd.Common.Message;

/**
 * 转换图片
 * 
 * @author jiangshao
 * 
 */
public class Transform {
	private volatile static Transform instance = null;

	private Transform() {
	}

	public static Transform getInstance() {
		if (instance == null) {
			synchronized (Transform.class) {
				if (instance == null) {
					instance = new Transform();
				}
			}
		}
		return instance;
	}

	/**
	 * 转换字节数组为 商品id，方案id和批量图片
	 * 
	 * @param b
	 * @param map
	 *            出参 备用
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public void transformByteArrayInfo(Message _msg,
			final Map<String, Object> map) throws IOException {
		_msg.seekHead();//关键
		//消息长度
		int msgLength = _msg.readInt32();
		map.put("msglenth", msgLength);
		// 解释消息类型
		int msgtype = _msg.readInt16();
		map.put("msgtype", msgtype);
		System.out.println(msgtype);
		// json正文
		String request = _msg.readUTF();
		map.put("request", request);
		JSONObject json = new JSONObject(request);
		String ItemID = (String) json.get("ItemID");
		String Custom = (String) json.get("Custom");
		JSONArray images = json.getJSONArray(("Images"));
		for (int i = 0; i < images.length(); i++) {
			transformImage(_msg, ItemID, Custom, images.getString(i));
		}
	}

	/**
	 * 转换图片
	 * 
	 * @param b
	 *            批量图片字节数组
	 * @param m
	 *            开始下标
	 * @return 结束下标
	 * @throws Exception
	 */
	public void transformImage(Message _msg, String itemID, String customID,
			String imageNum) {
		String path = "C:\\picture\\" + itemID;
		String name = customID + "_" + imageNum + ".jpg";
		// 写到磁盘
		File file = new File(path);
		// 如果文件夹不存在则创建
		file.mkdir();
		byte[] data = _msg.readImage();
		if (writeImage(data, path, name)) {
			// TODO httpclient
		}
	}

	/**
	 * 字节数组转文件（写图片）
	 * 
	 * @param b
	 * @return
	 */
	public boolean writeImage(byte[] imageBody, String path, String name) {
		if (imageBody.length < 3 || path.equals(""))
			return false;
		ByteArrayInputStream bais = new ByteArrayInputStream(imageBody);
		Iterator<ImageReader> readers = ImageIO
				.getImageReadersByFormatName("jpg");
		ImageReader reader = readers.next();
		ImageInputStream imageStream;
		try {
			imageStream = ImageIO.createImageInputStream(bais);
			reader.setInput(imageStream, true);
			BufferedImage bi = reader.read(0);
			FileOutputStream out = new FileOutputStream(name + ".jpg");
			ImageIO.write(bi, "jpg", out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}

}
