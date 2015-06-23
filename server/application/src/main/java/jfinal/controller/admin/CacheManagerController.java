package jfinal.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jfinal.model.Custom;

import com.jfinal.core.Controller;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * 缓存管理
 * @author jiangshao
 *
 */
public class CacheManagerController extends Controller {
		public void query(){
			List list=new ArrayList<Custom>();
//			CacheKit.put("CustomList", "test", new Custom());
			List cache = CacheKit.getKeys("CustomList");
			for (Object object : cache) {
				list.add(CacheKit.get("CustomList", object));
			}
			renderJson(list);
		}
		
		
		public void remove(){
			
		}
}
