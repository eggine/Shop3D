package jfinal.controller.admin;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import jfinal.controller.BaseController;
import jfinal.model.User;
import jfinal.util.Const;
import jfinal.util.Message;
import jfinal.util.Result;

/**
 * 管理员的跳转为后台管理中心
 * @author jiangshao
 *
 */
public class AdminIndexController extends BaseController<User>{
	private static Logger log=Logger.getLogger(AdminIndexController.class);
	
		
		/**
		 * 管理员登录
		 */
		public void login(){
			JSONObject json =new JSONObject();
			String username = getPara("User.username");
			String password = getPara("User.password");
			log.info(username+"登录....");
//			User data = User.dao.findFirst(
//					"select * from User where username=? and password=?", username,
//					password);
			if ("admin".equals(username)&&"123".equals(password)) {
				log.info(username+"登录失败");
				renderJson(Result.returnresult(Const.RENDER_RESULT_ERROR));
				
			} else {
				/* 记录缓存信息 */
				log.info(username+"登录成功，正保存session数据");
				json=Result.returnresult(Const.RENDER_RESULT_SUCCESS);
				json.put(Const.DATA, "admin");
				getSession().setAttribute("user", "admin");
				
			}
			renderJson(json);
		}
		
		
}
