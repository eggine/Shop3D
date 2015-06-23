package ymd.ImageServer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ymd.Common.HttpClientUtil;
import ymd.Common.HttpConst;
import ymd.Common.Message;

public class ResolutionImages {
	private static Logger log = Logger.getLogger(ResolutionImages.class);

	public void resolutionMsgBuffer(Message _msg, Map<String, String> map)
			throws IOException, JSONException {
		_msg.seekRead(0, 0);

		int msgLength = _msg.readInt32();

		int msgtype = _msg.readInt16();

		log.info(Integer.valueOf(msgtype));

		String request = _msg.readUTF();
		log.info(request);

		JSONObject json = new JSONObject(request);
		String ItemID = (String) json.get("itemid");
		String Custom = (String) json.get("custom");
		JSONArray data = json.getJSONArray("images");
		map.put("itemid", ItemID);
		map.put("custom", Custom);

		log.info("data.length" + data.length());
		for (int i = 0; i < data.length(); i++) {
			sendRequestFromHTTP(_msg, map, data.getString(i));
		}
	}

	public void sendRequestFromHTTP(Message _msg, Map<String, String> map,
			String imageNum) throws ClientProtocolException, IOException {
		log.info("sendRequestFromHTTP----------------" + imageNum);
		String itemID = (String) map.get("itemid");
		String custom = (String) map.get("custom");
		/*    测试               */
		String path = "D:/Pro/Shoes3D/server/Image_Server/imageserver/src/main/webapp/picture/" + itemID+"/";// 本地
//		 String path = File.separator +"picture"+File.separator+ itemID;
		String name = custom + "_" + imageNum + ".jpg";

		File file = new File(path);

		if (!file.exists())
			file.mkdir();
		map.put("Address", path + File.separator + name);
		byte[] data = _msg.readImage();
		if (saveImage(data, path+File.separator+name)) {
			Map parms = new HashMap();
			parms.put("Custom.itemId", itemID);
			parms.put("Custom.custom", custom);
			parms.put("Custom.imageNumber", imageNum);
//			parms.put("Custom.address", HttpConst.BASE_208_PATH
//					+ File.separator + itemID + File.separator + name);
			/*    测试               */
			parms.put("Custom.address", HttpConst.BASE_208_PATH+"/picture"
					+ "/" + itemID + "/" + name);///本地
			new HttpClientUtil().postImagesAddress(
					HttpConst.SAVE_CUSTOM_POST_IMAGE_ADDRESS, parms);
		}
	}

	public boolean saveImage(byte[] imageBody, String path) throws IOException {
		if ((imageBody.length < 3) || (path.equals("")))
			return false;
		long startTime=System.currentTimeMillis();
		log.info("start save image time:"+startTime);
		 FileOutputStream fos = null;
	        FileChannel fc_out = null;
	        try {
	            fos = new FileOutputStream(path, true);
	            fc_out = fos.getChannel();
	            ByteBuffer buf = ByteBuffer.wrap(imageBody);
	            buf.put(imageBody);
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
	   log.info("save image end time:"+(System.currentTimeMillis()-startTime));
		return true;
	}
}