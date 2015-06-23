package jfinal.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;

import ymd.Common.HttpConst;
import ymd.UserServer.UserServer;

import com.alibaba.fastjson.JSON;

import jfinal.model.Custom;
import jfinal.modelsupport.Component;
import jfinal.modelsupport.Description;
import jfinal.modelsupport.Mesh;
import jfinal.modelsupport.SubMesh;
import jfinal.util.UploadFileUtil;

public class AdminService {
	private static Logger log = Logger.getLogger(AdminService.class);
	private static final int DEFAULT_BUFFER_SIZE = 1024;

	/**
	 * 处理上传的json文件
	 * 
	 * @param item
	 * @param co
	 * @return
	 */
	public void operateUploadJson(File file, String encoding,
			Map<String, String> map,String itemname) {
		Description co = fileTostring(file, encoding);
//		String item = itemname;
		Component[] compontents = co.getComponent();
		for (Component component : compontents) {
			Mesh[] meshs = component.getMesh();
			for (Mesh mesh : meshs) {
				// TODO 模型缩略图
//				mesh.setName(HttpConst.BASE_208_PATH + "/" + item
//						+ "/thumbnail/" + mesh.getName() + ".jpg");
				mesh.setName(HttpConst.BASE_208_PATH + "/picture/" + itemname
						+ "/thumbnail/" + mesh.getName() + ".jpg");///本地
				SubMesh[] submeshs = mesh.getSubmesh();
				for (SubMesh subMesh : submeshs) {
					String[] material = subMesh.getMaterial();
					String[] new_material = new String[material.length];
					for (int i = 0; i < material.length; i++) {
						//TODO 材质缩略图
//						new_material[i] = HttpConst.BASE_208_PATH + "/" + item
//								+ "/material/" + material[i] + ".jpg";
						new_material[i] = HttpConst.BASE_208_PATH + "/picture/" + itemname
								+ "/material/" + material[i] + ".jpg";//本地
						
					}
					subMesh.setMaterial(new_material);
				}
			}
		}
		map.put("description", JSON.toJSONString(co).trim());
	}

	/**
	 * 文本文件转换为指定编码的字符串
	 * 
	 * @param file
	 *            文本文件
	 * @param encoding
	 *            编码类型
	 * @return 转换后的字符串
	 * @throws IOException
	 */
	private Description fileTostring(File file, String encoding) {
		InputStreamReader reader = null;
		StringWriter writer = new StringWriter();
		try {
			if (encoding == null || "".equals(encoding.trim())) {
				reader = new InputStreamReader(new FileInputStream(file),
						encoding);
			} else {
				reader = new InputStreamReader(new FileInputStream(file));
			}
			// 将输入流写入输出流
			char[] buffer = new char[DEFAULT_BUFFER_SIZE];
			int n = 0;
			while (-1 != (n = reader.read(buffer))) {
				writer.write(buffer, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		// 返回转换结果
		if (writer != null)
			return JSON.parseObject(writer.toString(), Description.class);
		else
			return null;
	}

	/**
	 * 发送商品的zip到渲染服务器
	 * 
	 * @param itemId
	 * @param item_zip
	 * @return
	 */
	public boolean renderItemZip(String itemId, File item_zip) {
		log.info("render item zip begin...");
		try {
			UserServer.getinstance().uploadItemData3D(itemId,
					new UploadFileUtil().getBytes(item_zip));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		log.info("render item zip end...");
		return true;
	}

	
	
	public List<Custom> renderDefaultCustom(String itemId, String custom,
			String imageNo) throws JSONException, InterruptedException {
		return new CustomService().findCustomAllImages(itemId, custom, imageNo);
	}
}
