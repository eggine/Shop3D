package controller;

import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.JSchException;
import com.jfinal.core.Controller;
import dataEntity.Directory;
import directory.SSHCommand;
import java.util.ArrayList;

public class LookDirectory extends Controller {
	private String rootURL = "../picture";

	public void index() {
		ArrayList list = new ArrayList();
		try {
			SSHCommand.getInstance().execCmd("ls -a  cd ", this.rootURL, list);
		} catch (JSchException e) {
			e.printStackTrace();
		}
		Directory d = new Directory();
		d.setRootUrl(this.rootURL);
		String data = JSON.toJSONString(list);
		renderJson(data);
	}

	public void query() {
		String url = getPara("URL");
		ArrayList list = new ArrayList();
		try {
			SSHCommand.getInstance().execCmd("ls -a  cd ", url, list);
		} catch (JSchException e) {
			e.printStackTrace();
		}
		renderJson(list);
	}
}