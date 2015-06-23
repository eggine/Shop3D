package ymd.Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;


public class HttpClientUtil {
	private static Logger log = Logger.getLogger(HttpClientUtil.class);

	public void postImagesAddress(String host, Map<String, String> params) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(host);

		List formparams = new ArrayList();
		for (Map.Entry entry : params.entrySet()) {
			formparams.add(new BasicNameValuePair((String) entry.getKey(),
					(String) entry.getValue()));
		}

		try {
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(
					formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			this.log.info("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					this.log.info("--------------------------------------");
					this.log.info("Response content: "
							+ EntityUtils.toString(entity, "UTF-8"));
					this.log.info("--------------------------------------");
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		log.info("图片服务器开始发送http请求时间:"+System.currentTimeMillis());
	}

	/**
	 * 上传文件
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	public boolean postFile(File file, String item, String file_type)
			throws ParseException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(HttpConst.SAVE_ITEM_TH_POST_REQUIRE);
		InputStream inputStream = new FileInputStream(file);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addBinaryBody("file", inputStream,
				ContentType.create("application/zip"), "file");
		builder.addTextBody("itemname", item, ContentType.TEXT_PLAIN);
		builder.addTextBody("filetype", file_type, ContentType.TEXT_PLAIN);
		HttpEntity entity = builder.build();
		post.setEntity(entity);
		CloseableHttpResponse response = httpclient.execute(post);
		try {
			try {
				HttpEntity entity1 = response.getEntity();
				if (entity1 != null) {
					this.log.info("--------------------------------------");
					this.log.info("Response content: "
							+ EntityUtils.toString(entity1, "UTF-8"));
					this.log.info("--------------------------------------");
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
		}

	public static void main(String[] args) throws ParseException, IOException {
		File file = new File("H:\\A6DH899.zip");
		new HttpClientUtil().postFile(file, "A6DH899", "2");
	}

}