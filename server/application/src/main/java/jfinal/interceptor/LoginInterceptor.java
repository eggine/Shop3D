package jfinal.interceptor;

import org.apache.log4j.Logger;

import jfinal.model.User;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * 登录拦截器
 * @author jiangshao
 *
 */
public class LoginInterceptor implements Interceptor {
	private static Logger log =Logger.getLogger(LoginInterceptor.class);
	
	public void intercept(ActionInvocation ai) {
		log.info("LoginInterceptor------登录拦截。。。。。");
		Controller controller = ai.getController();
		User loginUser = controller.getSessionAttr("user");
		if (loginUser != null && ai.getActionKey() != null) {
			log.info(loginUser.get("name")+"----合法操作，");
			ai.invoke();
		} else {
			log.info(loginUser.get("name")+"----非法操作，需登录后才能继续操作，");
			controller.redirect("/admin/login");
		}

	}

}
