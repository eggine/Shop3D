package jfinal.util;

import java.util.ArrayList;
import java.util.List;

public class Test {
		public static void main(String[]arg){
			List<String> list=new ArrayList<String>();
			list.add("1");
			list.add("2");
			list.add("3");
			list.add("4");
			
			if(list.contains("3")){
				list.remove("3");
			}
			
			
		}
}
