package jfinal.model;

import java.io.Serializable;

import com.jfinal.plugin.activerecord.Model;

/**
 * 上传下载文件的记录表，包括时间，名字，位置大小，等等
 * @author jiangshao
 *
 */
public class Custom extends Model<Custom> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Custom dao=new Custom();

}
