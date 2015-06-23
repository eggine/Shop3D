package jfinal.controller.front;


import com.alibaba.fastjson.JSONObject;

import jfinal.controller.BaseController;
import jfinal.model.Item;
import jfinal.modelsupport.ItemSupport;
import jfinal.util.Const;
import jfinal.util.Result;

public class ItemController extends BaseController<Item> {
	public ItemController(){
		setModelsupport(new ItemSupport());
	}
//	/**
//	 * 商品列表
//	 */
//	public void list(){
//		render("produceList.html");
//	}
//	/**
//	 * 商品详情
//	 */
//	public void detail(){
//		setAttr("itemid", getPara("itemis"));
//		render("produceDetail.html");
//	}
//	
	/**
	 * 定制页面
	 */
	public void custom(){
		redirect("/Custom/custom");
	}
	
}
