package jfinal.validator;

import jfinal.util.Const;
import jfinal.util.Message;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class ItemValidator extends Validator {

	@Override
	protected void handleError(Controller arg0) {
		JSONObject json=new JSONObject();
		json.put(Const.STATE, Const.ERROR);
		json.put(Const.MESSAGE, Message.ERROR);
		arg0.renderJson(json);
	}

	@Override
	protected void validate(Controller arg0) {
		validateRequiredString("Item.name", "Msg", "请输入商品名称");
	}

}
