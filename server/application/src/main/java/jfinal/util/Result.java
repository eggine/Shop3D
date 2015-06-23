package jfinal.util;

import com.alibaba.fastjson.JSONObject;

public class Result {
	public static JSONObject returnresult(int type) {
		JSONObject json = new JSONObject();
		switch (type) {
		case Const.RENDER_RESULT_SUCCESS:
			json.put(Const.STATE, Const.SUCCESS);
			json.put(Const.MESSAGE, Message.SUCCESS);
			break;
		case Const.RENDER_RESULT_EXCEPTION:
			json.put(Const.STATE, Const.ERROR);
			json.put(Const.MESSAGE, Message.SERVER_EXCEPTION);
			break;
		default:
			json.put(Const.STATE, Const.ERROR);
			json.put(Const.MESSAGE, Message.ERROR);
			break;
		}
		return json;
	}
}
