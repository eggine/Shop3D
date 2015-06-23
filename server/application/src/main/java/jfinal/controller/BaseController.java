package jfinal.controller;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import jfinal.util.Const;
import jfinal.util.Message;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 继承此类的子类具备基本的CRUD
 * 
 * @param <M>Model
 */
public class BaseController<M extends Model<M>> extends Controller {
	private static Logger log = Logger.getLogger(BaseController.class);

	public BaseController() {
		// 把class的变量保存起来，不用每次去取
		this.setModelClass(getClazz());
	}

	/**
	 * 获取M的class
	 * 
	 * @return M
	 */
	@SuppressWarnings("unchecked")
	public Class<M> getClazz() {
		Type t = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) t).getActualTypeArguments();
		return (Class<M>) params[0];
	}

	protected Class<M> modelClass;
	protected Object modelsupport;

	public Class<M> getModelClass() {
		return modelClass;
	}

	public void setModelClass(Class<M> modelClass) {
		this.modelClass = modelClass;
	}

	public Object getModelsupport() {
		return modelsupport;
	}

	public void setModelsupport(Object modelsupport) {
		this.modelsupport = modelsupport;
	}

	/**
	 * 模糊查询,用于异步请求 pageNo,pagesize需要附带
	 */
/*	public void query() {
		try {
			int pageNo = getParaToInt("pageNumber") == null ? 1
					: getParaToInt("pageNumber");
			int pageSize = getParaToInt("pageSize") == null ? 15
					: getParaToInt("pageSize");
			String sqlCondition = " 1=1";
			Map<String, String[]> paraMap = getParaMap();
			String[] paraStr = BaseControllerSupport.makePara(modelsupport,
					paraMap);
			sqlCondition += paraStr[0];
			Page<Record> list = Db.paginate(pageNo, pageSize, "select *",
					"from " + getModelClass().getSimpleName() + " where"
							+ sqlCondition + " order by id desc");
			JSONObject json = new JSONObject();
			json.put(Const.DATA, list);// 结果
			json.put(Const.SEARCH_CONTENT, paraStr[1]);// /搜索条件
			json.put(Const.STATE, Const.SUCCESS);
			json.put(Const.MESSAGE, Message.SUCCESS);
			if (getPara("callback") != null) {
				String result = "mobile(" + json.toString() + ")";
				renderJson(result);
			} else {
				renderJson(json);
			}
		} catch (Exception e) {
			JSONObject json = new JSONObject();
			json.put(Const.STATE, Const.ERROR);
			json.put(Const.MESSAGE, Message.ERROR);
			renderJson(json);
			e.printStackTrace();
		}
	}
*/
	
	
	
	public void query() {
		try {
			int pageNo = getParaToInt("pageNumber") == null ? 1
					: getParaToInt("pageNumber");
			int pageSize = getParaToInt("pageSize") == null ? 15
					: getParaToInt("pageSize");
			String sqlCondition = " 1=1";
			Map<String, String[]> paraMap = getParaMap();
			String[] paraStr = BaseControllerSupport.makePara(modelsupport,
					paraMap);
			sqlCondition += paraStr[0];
			Page<Record> list = Db.paginate(pageNo, pageSize, "select *",
					"from " + getModelClass().getSimpleName() + " where"
							+ sqlCondition + " order by id desc");
			JSONObject json = new JSONObject();
			json.put(Const.DATA, list);// 结果
			json.put(Const.SEARCH_CONTENT, paraStr[1]);// /搜索条件
			json.put(Const.STATE, Const.SUCCESS);
			json.put(Const.MESSAGE, Message.SUCCESS);
			renderJson(json);
		} catch (Exception e) {
			JSONObject json = new JSONObject();
			json.put(Const.STATE, Const.ERROR);
			json.put(Const.MESSAGE, Message.ERROR);
			renderJson(json);
			e.printStackTrace();
		}
	}

	
	
	
	/**
	 * 通用分页查找
	 */
	public void getByPage() {
		Page<Record> list = Db
				.paginate(getParaToInt("pageNo"), getParaToInt("pageSize"),
						"select *", "from " + getModelClass().getSimpleName()
								+ " order by id desc");
		JSONObject json = new JSONObject();
		json.put(Const.STATE, Const.SUCCESS);
		json.put(Const.MESSAGE, Message.SUCCESS);
		json.put(Const.DATA, list);
		renderJson(json);
	}

	/**
	 * 通用根据id查找
	 */
	public void getById() {
		int id = getParaToInt("id");
		JSONObject json = new JSONObject();
		json.put(Const.DATA, Db.findById(getModelClass().getSimpleName(), id));
		json.put(Const.STATE, Const.SUCCESS);
		json.put(Const.MESSAGE, Message.SUCCESS);
		if (getPara("callback") != null) {
			String result = "mobile(" + json.toString() + ")";
			renderJson(result);
		} else {
			renderJson(json);
		}
	}

	/**
	 * 通用新增
	 * 
	 * @throws Exception
	 */
	public void save() throws Exception {
		log.info(getModelClass().getSimpleName() + "-----调用save()方法");
		int count = 0;// /计数器，计算保存的属性的列数
		M o = getModel(getModelClass());
		Map<String, String[]> paraMap = getParaMap();
		Set<String> nameSet = paraMap.keySet();
		JSONObject json = new JSONObject();
		try {
			for (String name : nameSet) {
				String[] props = name.split("\\.");
				if (props.length == 2) {
					if (props[0].equals(getModelClass().getSimpleName())) {
						o.set(props[1], paraMap.get(name)[0]);
						count++;
					}
				}
			}
			if (count != 0) {
				o.save();// 保存
			}
		} catch (Exception e) {
			json.put(Const.STATE, Const.ERROR);
			json.put(Const.MESSAGE, Message.ERROR);
			renderJson(json);
		}
		json.put(Const.STATE, Const.SUCCESS);
		json.put(Const.MESSAGE, Message.SUCCESS);
		renderJson(json);

	}

	/**
	 * 通用修改(根据id)
	 * 
	 * @throws Exception
	 */
	public void update() throws Exception {
		M o = getModel(getModelClass()).findById(getParaToInt("id"));
		Map<String, String[]> paraMap = getParaMap();
		Set<String> nameSet = paraMap.keySet();
		JSONObject json = new JSONObject();
		try {
			for (String name : nameSet) {
				o.set(name, paraMap.get(name)[0]);
			}
		} catch (Exception e) {
			json.put(Const.STATE, Const.ERROR);
			json.put(Const.MESSAGE, Message.ERROR);
			renderJson(json);
		}
		json.put(Const.STATE, Const.SUCCESS);
		json.put(Const.MESSAGE, Message.SUCCESS);
		renderText(o.update() + "");
	}

	/**
	 * 通用删除
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {
		renderText(Db.deleteById(getModelClass().getSimpleName(),
				getParaToInt("id")) + "");
	}
}