package jfinal.validator;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class ThumbnailValodator extends Validator
{
  protected void handleError(Controller arg0)
  {
    JSONObject json = new JSONObject();
    json.put("result", "error");
    json.put("message", "操作失败！");
    arg0.renderJson(json);
  }

  protected void validate(Controller arg0)
  {
    validateRequiredString("itemID", "Msg", "请输入商品名称");
    validateRequiredString("filetype", "Msg", "请输入商品名称");
    validateRequiredString("thumbnail_urls", "Msg", "请输入商品名称");
  }
}