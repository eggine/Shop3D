package controller;

import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

import fileUtil.DeleteUploadFiles;
import fileUtil.UNZipFiles;

import java.io.File;
import java.io.IOException;

public class UploadFileController extends Controller {
	/**
	 * 缩略图上传（多文件）
	 * @throws Exception 
	 */
	public void thumbnailupload() throws Exception {
		UploadFile uploadfile = getFile("file");
		String itemname = getPara("itemname");
		String file_type = getPara("filetype");
		new UNZipFiles().unZipFiles(uploadfile.getFile(), itemname,
				Integer.parseInt(file_type));
		new DeleteUploadFiles().deletefile(uploadfile.getSaveDirectory());
		renderText("success");
	}

}