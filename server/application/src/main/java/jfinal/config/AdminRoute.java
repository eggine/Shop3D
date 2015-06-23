package jfinal.config;

import jfinal.controller.admin.AdminIndexController;
import jfinal.controller.admin.AdminController;
import jfinal.controller.admin.CacheManagerController;

import com.jfinal.config.Routes;

public class AdminRoute extends Routes{

	@Override
	public void config() {
			add("/admin",AdminIndexController.class,"views/admin");
			add("/admin/manager", AdminController.class,"views/admin");
			add("/admin/cache",CacheManagerController.class,"views/admin");
	}
	
}
