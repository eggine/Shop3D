package jfinal.validator;

import jfinal.util.Message;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class CustomValidator extends Validator {

	@Override
	protected void handleError(Controller arg0) {
		// TODO Auto-generated method stub
		arg0.renderText(Message.SERVER_EXCEPTION);
		
	}

	@Override
	protected void validate(Controller arg0) {
		validateRequiredString("Custom.itemId", "Msg", "请输入商品id");
		validateRequiredString("Custom.custom", "Msg", "请输入方案码");
		validateRequiredString("Custom.imageNumber", "Msg", "请输入图片编码");
		validateRequiredString("Custom.address", "Msg", "请输入图片地址");
	}

}
