package jfinal.config;

import jfinal.controller.IndexController;
import jfinal.controller.front.CustomController;
import jfinal.controller.front.ItemController;

import com.jfinal.config.Routes;

public class FrontRoute extends Routes {

	@Override
	public void config() {
		add("/",IndexController.class,"web");
		add("/Item", ItemController.class,"web");
		add("/Custom", CustomController.class,"web");
		
	}
}
