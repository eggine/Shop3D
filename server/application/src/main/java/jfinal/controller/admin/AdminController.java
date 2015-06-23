package jfinal.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.ParseException;
import org.apache.log4j.Logger;
import org.json.JSONException;

import ymd.Common.HttpClientUtil;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.upload.UploadFile;
import jfinal.controller.BaseController;
import jfinal.model.Item;
import jfinal.modelsupport.UserSupport;
import jfinal.service.AdminService;
import jfinal.util.Const;
import jfinal.util.Result;
import jfinal.util.UploadFileUtil;

//@Before({LoginInterceptor.class})
public class AdminController extends BaseController<Item> {
	private static Logger log = Logger.getLogger(AdminController.class);

	public AdminController() {
		setModelsupport(new UserSupport());
	}

	public void index() {
		render("main.html");
	}

	public void table() {
		render("table.html");
	}

	public void upload() {
		render("upload.html");
	}

	public void cache() {
		render("cache.html");
	}

	public void downloadM() {
		renderFile(new File("H:\\tingting.zip"));
	}

	/**
	 * 上传商品zip
	 * 
	 * @throws Exception
	 */
	public void uploadItem() throws Exception {
		UploadFile u_item_zip = getFile("file");
		String item = getPara("itemname");
		AdminService service = new AdminService();
			// 发送文件到渲染服务器
		if (service.renderItemZip(item, u_item_zip.getFile())) {
			// 新增一个记录m
			Item o;
			o=Item.dao.findFirst("select *　from Item where name=?",item);
			if(o==null)new Item().set("name", item).set("brand", "一麻袋").save();
				// 删除临时文件
				new UploadFileUtil().deletefile(u_item_zip.getSaveDirectory());
				log.info(" save an new Item data to database");
				renderJson(Result.returnresult(Const.RENDER_RESULT_SUCCESS));
		} else {
			renderJson(Result.returnresult(Const.RENDER_RESULT_EXCEPTION));
		}

	}

	/**
	 * 上传商品描述json
	 * 
	 * @throws Exception
	 * 
	 */
	public void uploadItemDes() throws Exception {
		UploadFile u_description_json = getFile("file");
		String item = getPara("itemname");
		String desc=getPara("desc");
		

		log.info(" save item description json to database");
		Item o = Item.dao.findFirst("select * from Item where name=?", item);
		if (o == null)new Item().dao.set("name", item).set("brand", desc).set("id",new Long(System.currentTimeMillis()).intValue()).save();
			o = Item.dao.findFirst("select * from Item where name=?", item);
			AdminService service = new AdminService();
			Map<String, String> map = new HashMap<String, String>();
			service.operateUploadJson(u_description_json.getFile(), "utf8", map,item);
			// TODO 商品描述json字符串保存数据库,和默认方案码，
			o.set("brand", desc).set("description", map.get("description").trim()).update();
			new UploadFileUtil().deletefile(u_description_json
					.getSaveDirectory());
			redirect("/admin/manager/upload");
//			 render("upload.html");
	}

	/**
	 * 上传缩略图
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	public void uploadthumbnail() throws ParseException, IOException {
		UploadFile u_thumbnail = getFile("file");
		String item = getPara("itemname");
		String file_type = getPara("filetype");
		log.info("send thumbnail to imageserver");
		new HttpClientUtil().postFile(u_thumbnail.getFile(), item, file_type);
		setAttr(Const.DATA, Result.returnresult(Const.RENDER_RESULT_SUCCESS));
//		renderJson(Result.returnresult(Const.RENDER_RESULT_SUCCESS));
		redirect("/admin/manager/upload");
	}

	/**
	 * 来自图片服务器请求。，更新商品表的缩略图地址（商品预览图，模型缩略图，材质缩略图）
	 */
	@ClearInterceptor(ClearLayer.ALL)
	public void updatethumbnail() {
		String itemname = getPara("itemname");
		String type = getPara("type");
		String update_value = getPara("address");
		log.info("accept thumbnail address form imageserver and type= " + type
				+ "and update value =" + update_value);
		Item o = Item.dao.findFirst("select * from Item where name=?", itemname);
		boolean result = false;
		if (o != null) {
			switch (Integer.parseInt(type)) {
			case Const.IMAGE_PREVIEW:
				result = o.set("preview", update_value.substring(0, update_value.length()-1)).update();
				break;
			case Const.IMAGE_MATERIAL_TH:
				result = o.set("material_th", update_value).update();
				break;
			case Const.IMAGE_THUMBNAIL:
				result = o.set("thumbnail", update_value).update();
				break;
			default:
				result = o.set("preview", update_value.substring(0, update_value.length()-1)).update();
				break;
			}
			if (result) {
				renderJson(Result.returnresult(Const.RENDER_RESULT_SUCCESS));
//				redirect("admin/manager/upload");
			} else {
				renderJson(Result.returnresult(Const.RENDER_RESULT_ERROR));
			}
		} else {
			renderJson(Result.returnresult(Const.RENDER_RESULT_EXCEPTION));
		}
	}

	/**
	 * 商品上传完成后，需要确定商品的默认方案和视觉;
	 * 
	 * @throws InterruptedException
	 * @throws JSONException
	 */
	public void updateItemDefInfo() throws JSONException, InterruptedException {
		String item_name = getPara("item");
		String default_custom_ = getPara("def_custom");
		String default_vision = getPara("def_view");
		String mesoptic_vision = getPara("mesoptic");
		Item o = Item.dao.findFirst("select *　from Item where name=?",
				item_name);
		log.info("save default custom to table Item");
		o.set("default_custom", default_custom_)
				.set("default_vision", default_vision)
				.set("mesoptic_vision", mesoptic_vision).update();
		JSONObject json = Result.returnresult(Const.RENDER_RESULT_SUCCESS);
		json.put("data", new AdminService().renderDefaultCustom(item_name,
				default_custom_, default_vision));
		renderJson(json);
	}

}