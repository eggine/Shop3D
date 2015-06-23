package jfinal.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class LoginValidtor extends Validator {

	@Override
	protected void handleError(Controller c) {
		c.keepPara("User.username");
		c.keepPara("User.password");
		c.render("login.html");
	}

	@Override
	protected void validate(Controller c) {
		validateRequiredString("User.username", "nameMsg", "请输入用户名");
		validateRequiredString("User.password", "passMsg", "请输入密码");
//		validateRegex("User.age","[ |　]*([0-9]{1,9})[ |　]*","passMsg2","只能输入数字");
	}

}
