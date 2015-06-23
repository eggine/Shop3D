package jfinal.controller;
import java.util.Date;
import java.util.Map;
import java.util.Set;
/**
 * 多条件搜索辅助类
 * @author jiangshao
 *
 */
public class BaseControllerSupport {
	public static String[] makePara(Object obj, Map<String,String[]> paraMap) {
        String[] returnStr = new String[2];
        StringBuffer sqlStr = new StringBuffer(128);
        StringBuffer paraStr = new StringBuffer(128);
        Set<String> nameSet = paraMap.keySet();
        try {
            for(String name:nameSet){
            	String[] props = name.split("\\.");
                if(props[0].equals(obj.getClass().getSimpleName())){
                    Class<?> type = obj.getClass().getDeclaredField(props[1]).getType();
                    if (paraMap.get(name) != null && !paraMap.get(name)[0].equals("")) {
                        if(type == String.class){
                            sqlStr.append(" and ").append(props[1]).append(" like '%").append(paraMap.get(name)[0]).append("%'");
                        }else if(type == Integer.class){
                            sqlStr.append(" and ").append(props[1]).append("=").append(paraMap.get(name)[0]);
                        }else if(type == Double.class){
                            sqlStr.append(" and ").append(props[1]).append("=").append(paraMap.get(name)[0]);
                        }else if(type == Boolean.class){
                            sqlStr.append(" and ").append(props[1]).append("=").append(paraMap.get(name)[0]);
                        }else if(type == Date.class){
                            sqlStr.append(" and ").append(props[1]).append(" ='").append(paraMap.get(name)[0]).append("'");
                        }
                        paraStr.append("&").append(name).append("=").append(paraMap.get(name)[0]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        returnStr[0] = sqlStr.toString();//查询sql
        returnStr[1] = paraStr.toString();//参数
        return returnStr;
    }

}
