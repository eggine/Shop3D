package jfinal.validator;

import org.apache.log4j.Logger;

import jfinal.util.Const;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class RegisterValidtor  extends Validator {
	private static Logger log=Logger.getLogger(RegisterValidtor.class);
	@Override
	protected void handleError(Controller c) {
		c.keepPara("User.username");
		c.keepPara("User.password");
		JSONObject json=new JSONObject();
		json.put(Const.URL, "register.html");
		
	}

	@Override
	protected void validate(Controller arg0) {
		log.info("登录校验");
		validateRequiredString("User.username", "nameMsg", "请输入用户名");
		validateRequiredString("User.password", "passMsg", "请输入密码");
	}

}
