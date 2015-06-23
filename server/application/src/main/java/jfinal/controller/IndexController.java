package jfinal.controller;


import java.util.List;

import org.apache.log4j.Logger;

import jfinal.model.Item;
import jfinal.model.User;
import jfinal.util.Const;
import jfinal.util.Message;
import jfinal.util.Result;
import jfinal.validator.LoginValidtor;
import jfinal.validator.RegisterValidtor;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;


/**
 * 普通用户的登录跳转为用户中心
 * @author jiangshao
 *
 */
public class IndexController extends BaseController<User>{

	
	private static Logger log=Logger.getLogger(IndexController.class);
	/**
	 * 主页
	 */
	/**
	 * 定制页面
	 */
	public void custom(){
		String itemname=getPara("itemname");
		if(null!=getPara("device")){
		redirect("/Custom/custom?device=mobile&itemname="+itemname);
		}else{
		redirect("/Custom/custom?itemname="+itemname);
		}
	}
	
	public void index(){
		List<Item> data = Item.dao.find("select * from Item");
		setAttr("items", data);
		if(null!=getPara("device")){
			render("m_index.html");
		}else{
			render("index.html");
		}
	}
	public void list(){
		if(null!=getPara("device")){
			render("m_produceList.html");	
		}else{
		setAttr("items", Item.dao.find("select * from Item"));
		render("produceList.html");
		}
	}
	
	public void detail(){
		String name = getPara("itemId"); 
		Item item=Item.dao.findFirst("select * from Item where name=?",name);
		setAttr("item", item);
		if(null!=getPara("device")){
			render("m_produceDetail.html");	
		}else{
		    render("produceDetail.html");
		}
	}
	
	/**
	 * 登录
	 */
	@Before(LoginValidtor.class)
	public void login() {
		JSONObject json =new JSONObject();
		String username = getPara("User.username");
		String password = getPara("User.password");
		log.info(username+"登录....");
		User data = User.dao.findFirst(
				"select * from User where username=? and password=?", username,
				password);
		if (data == null) {
			log.info(username+"登录失败");
			renderJson(Result.returnresult(Const.RENDER_RESULT_ERROR));
		} else {
			/* 记录缓存信息 */
			log.info(username+"登录成功，正保存session数据");
			getSession().setAttribute("user", data);
			json=Result.returnresult(Const.RENDER_RESULT_SUCCESS);
			json.put(Const.DATA, data);
			renderJson(json);
		}
	}
	
	/**
	 * 注册
	 * @throws Exception
	 */
	@Before(RegisterValidtor.class)
	public void register() throws Exception{
		super.save();
	}

}
