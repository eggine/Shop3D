package fileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

import com.jfinal.kit.PathKit;

import ymd.Common.HttpClientUtil;
import ymd.Common.HttpConst;

public class UNZipFiles {

	private static Logger log = Logger.getLogger(UNZipFiles.class);

	/**
	 * 解压文件到指定目录
	 * 
	 * @param zipFile
	 * @param descDir
	 * @author isea533
	 */
	@SuppressWarnings("rawtypes")
	public void unZipFiles(File zipFile, String itemname, int file_type)
			throws IOException {
		String targetDirPath =File.separator + "picture"
				+ File.separator + itemname;
		String thumbnail_path="";
		switch (file_type) {
		case 1:
			// 预览图
			targetDirPath += File.separator + "preview" + File.separator;
			break;
		case 2:
			// 材质缩略图
			targetDirPath += File.separator + "material" + File.separator;
			break;
		case 3:
			// 模型缩略图
			targetDirPath += File.separator + "thumbnail" + File.separator;
			break;
		default:
			targetDirPath += File.separator + "preview" + File.separator;
			break;
		}

		File pathFile = new File(targetDirPath);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		ZipFile zip = new ZipFile(zipFile);
		log.info("******************解压开始********************");
		for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream(entry);
			String outPath = (targetDirPath + zipEntryName).replaceAll("\\*",
					File.separator);
			String save_outPath=PathKit.getWebRootPath()+outPath;
//			String outPath = (targetDirPath + zipEntryName).replaceAll("\\*",
//					File.separator);///远程的
			// 判断路径是否存在,不存在则创建文件路径
			File file = new File(save_outPath.substring(0,
					save_outPath.lastIndexOf(File.separator)));
			if (!file.exists()) {
				file.mkdirs();
			}
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(save_outPath).isDirectory()) {
				continue;
			}
			// 输出文件路径信息
			log.info(save_outPath);
			thumbnail_path+=HttpConst.BASE_208_PATH+outPath+",";

			OutputStream out = new FileOutputStream(save_outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
		}
		log.info("******************解压完毕********************");
		//发送http 请求到应用服务器
		Map<String,String>params=new HashMap<String,String>();
		params.put("itemname", itemname);
		params.put("type", file_type+"");
		params.put("address", thumbnail_path.replaceAll("/picture", ""));
		log.info("send thumbnail address to application server");
		new HttpClientUtil().postImagesAddress(HttpConst.SAVE_ITEM_POST_THUMBNAIL_ADDRESS, params);
	}
}
