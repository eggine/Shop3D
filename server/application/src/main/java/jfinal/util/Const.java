package jfinal.util;

public class Const {
	
	/*每一个json具备的基础信息*/
	public static final String STATE="result";//状态
	public static final String MESSAGE="message";//提示信息
	public static final String DATA="data";//数据
	public static final String URL="url";//跳转地址
	public static final String PAGE_NO="pageNo";//页码
	public static final String PAGE_SIZE="pageSize";//页量
	
	
	
	public static final String SEARCH_CONTENT="searchContent";//搜索条件
	
	/*状态*/
	public static final String  SUCCESS="success";//成功状态
	public static final String  ERROR="error";//失败状态
	
	////////////缩略图类型/////////////////
	public static final int IMAGE_PREVIEW=1; ///预览图
	public static final int IMAGE_MATERIAL_TH=2;//材质缩略图
	public static final int IMAGE_THUMBNAIL=3;//模型缩略图
	public static final int RENDER_RESULT_SUCCESS=1;
	public static final int RENDER_RESULT_EXCEPTION=2;
	public static final int RENDER_RESULT_ERROR=0;
}
