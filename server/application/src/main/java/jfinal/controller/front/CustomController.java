package jfinal.controller.front;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.plugin.ehcache.CacheKit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jfinal.controller.BaseController;
import jfinal.model.Custom;
import jfinal.model.Item;
import jfinal.service.CustomService;
import jfinal.util.Const;
import jfinal.util.Result;
import jfinal.validator.CustomValidator;
import org.apache.log4j.Logger;
import org.json.JSONException;

import ymd.Common.HttpConst;

public class CustomController extends BaseController<Custom> {
	private static Logger log = Logger.getLogger(CustomController.class);

	public void custom() {
		String itemname=getPara("itemname");
		setAttr("itemname", itemname);
		if(null!=getPara("device")){
			render("m_CustomDemo.html");
		}else{
			render("CustomDemo.html");
		}
	}

	/*
	 * 获取商品的默认方案码和视觉坐标
	 */
	public void getDefaultCustom() {
		String itemname = getPara("itemname");
		Item data = (Item) Item.dao.findFirst("select * from Item where name=?",itemname);
		JSONObject json = new JSONObject();
		if (data == null) {
			renderJson(Result.returnresult(Const.RENDER_RESULT_ERROR));
		}
		json.put("data", data);
		json.put("result", "success");
		json.put("message", "操作成功！");
		if (getPara("callback") != null) {
			String result = "mobile(" + json.toString() + ")";
			renderJson(result);
		} else {
			renderJson(json);
		}

	}

	/**
	 * 定制方案
	 * 
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	public void customizing() throws JSONException, InterruptedException {
		log.info("应用服务器接收到定制请求时间:"+System.currentTimeMillis());
		String itemname = getPara("itemname");
		String custom = getPara("custom");
		String _images = getPara("imagesno");
		log.info("Accept User Customizing Require");
		CustomService customService = new CustomService();
		List<Custom> data = customService.findCustomAllImages(itemname, custom,
				_images);
		if (data == null) {
			renderJson(Result.returnresult(Const.RENDER_RESULT_EXCEPTION));
		} else {
			JSONObject json = Result.returnresult(Const.RENDER_RESULT_SUCCESS);
			json.put("data", data);
			if (getPara("callback") != null) {
				String result = "mobile(" + json.toString() + ")";
				renderJson(result);
			} else {
				renderJson(json);
			}
		}
	}
	
	
	public void initDefaultCustom(){
		String _item = getPara("itemid");
		String custom = getPara("scheme");
		String _images = getPara("imgs");
		String vision=getPara("vision");
		CustomService customService = new CustomService();
		List<Custom> data;
		try {
			data = customService.findCustomAllImages(_item, custom,
					_images);
		if(data!=null){
			Item it=Item.dao.findFirst("select * from Item where name=?",_item);
			StringBuffer default_vision=new StringBuffer();
			for (Custom c : data) {
				default_vision.append(c.get("address")).append(",");
			}
				
			boolean result = it.set("default_custom", custom).set("mesoptic_vision", HttpConst.BASE_208_PATH+"/picture/"+_item+"/"+custom+"_"+vision+".jpg").set("default_vision", default_vision.substring(0, default_vision.toString().length()-1)).update();
			if(result){
				JSONObject json = Result.returnresult(Const.RENDER_RESULT_SUCCESS);
				renderJson(json);
			}else{
				JSONObject json = Result.returnresult(Const.RENDER_RESULT_EXCEPTION);
				renderJson(json);	
			}
		}	} catch (Exception e) {
			JSONObject json = Result.returnresult(Const.RENDER_RESULT_EXCEPTION);
			renderJson(json);
		}	
	}

	/**
	 * 保存方案
	 */
	@Before({ CustomValidator.class })
	public void save() {
		log.info("Accept User save Custom Require");
		int count = 0;
		Custom o = (Custom) getModel(getModelClass());
		Map paraMap = getParaMap();
		Set<String> nameSet = paraMap.keySet();
		try {
			for (String name : nameSet) {
				String[] props = name.split("\\.");
				if ((props.length == 2)
						&& (props[0].equals(getModelClass().getSimpleName()))) {
					o.set(props[1], ((String[]) paraMap.get(name))[0]);
					count++;
				}
			}

			if (count != 0) {
				log.info("take custom data to cache");
				CacheKit.put(
						"CustomList",
						o.getStr("itemId") + "-" + o.getStr("custom") + "-"
								+ o.getStr("imageNumber"), o);
				boolean result = o.save();
				log.info("take custom data to database " + result);

			}
		} catch (Exception e) {
			renderJson(Result.returnresult(Const.RENDER_RESULT_EXCEPTION));
			e.printStackTrace();
			return;
		}
		renderJson(Result.returnresult(Const.RENDER_RESULT_SUCCESS));
	}

}