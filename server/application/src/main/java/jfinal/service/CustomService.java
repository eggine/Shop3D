package jfinal.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;

import ymd.UserServer.UserServer;

import com.jfinal.plugin.ehcache.CacheKit;
import jfinal.model.Custom;

public class CustomService {
	private static Logger log=Logger.getLogger(CustomService.class);

	/**
	 * 获取方案的数据
	 * 
	 * @param itemId
	 *            商品id
	 * @param custom
	 *            方案码
	 * @param images
	 *            图片下标，用 ','分隔
	 * @return custom对象list
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	public List<Custom> findCustomAllImages(String itemname, String custom,
			String images) throws JSONException, InterruptedException {
		log.info("应用服务器开始搜索定制图片的时间:"+System.currentTimeMillis());
		List<Custom> list = new ArrayList<Custom>();
		List<String> imageNumbers = new ArrayList<String>();
		String[] _imageArray = images.split(",");
		for (String string : _imageArray) {
			imageNumbers.add(string);
		}
		// 先去缓存查数据
		findDataFromCache(itemname, custom, _imageArray, list);
		if (list.size() == _imageArray.length) {
			log.info("Get all the required package request data");
			return list;
		} else {
			// 去掉查出来的在的图片下标
			for (Custom ct : list) {
				String imageNumber = ct.get("imageNumber");
				// 去掉已存在的图片信息
				imageNumbers.remove(imageNumber);
			}
			// 查询数据库是否有数据
			log.info("Find the database schema data ,start....");
			for (String imageNumber : imageNumbers) {
				Custom c = Custom.dao
						.findFirst(
								"select * from Custom where itemId=? and custom=? and imageNumber=?",
								itemname, custom, imageNumber);
				if (c != null)
					list.add(c);
			}
			log.info("Find the database schema data , end");
			// 检查是否已经拿到所有需要的数据
			if (list.size() == _imageArray.length) {
				log.info("Get all the required package request data");
				return list;
			} else {
				// 去掉查出来的在的图片下标
				for (Custom ct : list) {
					String imageNumber = ct.get("imageNumber");
					// 去掉已存在的图片信息
					imageNumbers.remove(imageNumber);
				}
				//TODO 请求渲染服务器生成图片
				log.info("Send the project request to render the server for rendering");
				log.info("itemid"+itemname+"-------custom"+custom);
				UserServer.getinstance().requestImages(itemname, custom,
						imageNumbers);
				// 减产缓冲区是否已经把图片生成
				//
				int count=0;
				log.info("Scanning rendering server is already generated images");
				while (list.size() < _imageArray.length&&count<200) {
					Thread.sleep(100);
					findDataFromCache(itemname, custom,
							imageNumbers.toArray(new String[] {}), list);
					count++;
				}
				//如果超过扫描的情况下依然没有查找到所有需要的数据就返回null ，做超时提示
				if(list.size() < _imageArray.length)return null;

			}
		}
		log.info("Get all the required package request data");
		log.info("应用服务器接收完所有的定制图片地址时间:"+System.currentTimeMillis());
		return list;
	}

	
	/**
	 * 从缓存的地方取数据
	 * @param item
	 * @param custom
	 * @param _imagesArray
	 * @param list
	 */
	public void findDataFromCache(String item, String custom,
			String[] _imagesArray, List<Custom> list) {
		for (String imageno : _imagesArray) {
			Custom c = (Custom) CacheKit.get("CustomList", item + "-" + custom
					+ "-" + imageno);
			if (c != null){
				if(!list.contains(c))
				list.add(c);
			}
				
		}
//		log.info("Find the cached data");
	}

}
