package com.javase.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.junit.Test;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import net.sf.json.JSONObject;

public class CreateQunService {
	// 设置其Cookies机制为浏览器模式
	public Map<String, String> header() {

		// 将浏览器头信息保存在map里面
		// passport
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Host", "pan.baidu.com");
		headers.put("Referer", "http://pan.baidu.com/mbox/homepage");
		headers.put("Accept", "*/*");
		headers.put("Accept-Language", "zh-CN,zh;q=0.8");
		headers.put("Accept-Encoding", "gzip, deflate, sdch");
		// headers.put("X-Forwarded-For", "112.224.21.186");
		// Pragma:no-cache
		// Accept: */*
		headers.put("Upgrade-Insecure-Requests", "1");
		headers.put("Pragma", "no-cache");
		headers.put("X-Requested-With", "XMLHttpRequest");
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
		return headers;
	}

	public Map<String, String> ReadFileGetCookie(String filePath) {

		// 如果你文件里没有中文，那个GBK编码就无所谓，否则要按实际编码来定
		BufferedReader br;
		Map<String, String> map = new HashMap<String, String>();

		try {
			br = new BufferedReader(new FileReader(filePath));
			String str;
			while ((str = br.readLine()) != null) {
				// 如果分隔符不是空格，改成对应的分隔符，比如tab即"\t"
				String[] strs = str.split("; ");
				for (int i = 0; i < strs.length; i++) {
					String[] strss = strs[i].split("==");
					try {
						map.put(strss[0], strss[1]);
						System.out.println("Map<" + strss[0] + "," + strss[1] + ">");
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	// ①打开创建群页面创建群（可以查看到好友列表）
	// http://pan.baidu.com/mbox/relation/getfollowlist?start=0&limit=20&t=1486128895170&bdstoken=6beeb43bf470ead35b55036b05c55f6b&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjEyODg5NTE3MTAuOTQxNzY5NjY3NzgxNDU5Mw==&clienttype=0
	// ②创建群
	// http://pan.baidu.com/mbox/group/specialcreate?user_list=%5B%5D&t=1486130318224&bdstoken=6beeb43bf470ead35b55036b05c55f6b&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjEzMDMxODIyNTAuODUyOTA2NzEyMTU2NjUxMQ==&clienttype=0
	// ③如果出现验证码，用户输入然后再提交到
	// http://pan.baidu.com/mbox/group/specialcreate?user_list=%5B%5D&t=1486303662865&input=d7dr&vcode=3332423865633234636166333465663732323763363637363764323966666433666232343938353530373930303030303030303030303030303134383633303337313300081EBBA7C5141DABC6F3DD7F163CA6&bdstoken=a415b145c1f106b29e83d20bc76ce0b7&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjMwMzY2Mjg2NTAuMDE3NDI1MjY3OTA1ODc0Mzk4&clienttype=0
	// ⑤拉好友入群
	// http://pan.baidu.com/mbox/group/adduser?user_list=%5B4233222792%5D&gid=343209548133175143&t=1486130750495&bdstoken=6beeb43bf470ead35b55036b05c55f6b&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjEzMDc1MDQ5NjAuMzc4NjQ1Mzc5MDc3MDg1NA==&clienttype=0

	public Map<String, String> CreateQunByCaptcha(String captcha, String vcode, String filePath) {
		String url = "http://pan.baidu.com/mbox/group/specialcreate?user_list=[]&t=1486303662865&input=%s&vcode=%s&bdstoken=a415b145c1f106b29e83d20bc76ce0b7&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjMwMzY2Mjg2NTAuMDE3NDI1MjY3OTA1ODc0Mzk4&clienttype=0";
		String requestUrl = String.format(url, captcha, vcode);
		System.out.println(requestUrl);
		Response execute;
		try {
			execute = Jsoup.connect(requestUrl).ignoreContentType(true).headers(header())
					.cookies(ReadFileGetCookie(filePath)).method(Method.GET).execute();

			String body = execute.body();
			System.out.println(body);
			
			Map<String , String> createQun = new HashMap<String,String>();
			
			JSONObject json = JSONObject.fromObject(body);
			
			String gid = (String) json.get("gid");
			System.out.println(gid);
		
			String vcode2 = (String) json.get("vcode");
			System.out.println(vcode2);
			
			String errno =  Integer.toString((Integer) json.get("errno"));
			System.out.println(errno);
			createQun.put("gid", gid);
			createQun.put("vcode", vcode2);
			createQun.put("error", errno);
			if (vcode2!=null) {
				captcha(vcode2);
			}
			return createQun;
			
			
		} catch (IOException e) {
			e.printStackTrace();

		}
		return null;

	}

	public void ByCreateQunGetFriendList(String filePath) {
		String url = "http://pan.baidu.com/mbox/relation/getfollowlist?start=0&limit=20&t=1486128895170&bdstoken=6beeb43bf470ead35b55036b05c55f6b&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjEyODg5NTE3MTAuOTQxNzY5NjY3NzgxNDU5Mw==&clienttype=0";
		Response execute;
		try {
			execute = Jsoup.connect(url).ignoreContentType(true).headers(header()).cookies(ReadFileGetCookie(filePath))
					.method(Method.GET).execute();

			String body = execute.body();
			System.out.println(body);
			JSONObject json = JSONObject.fromObject(body);
			System.out.println(json.get("vcode"));
			String vcode = (String) json.get("vcode");

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public void PushFriendInQun(String filePath) {
		String url = "http://pan.baidu.com/mbox/group/adduser?user_list=[\"4233222792\"]&gid=343209548133175143&t=1486130750495&bdstoken=6beeb43bf470ead35b55036b05c55f6b&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjEzMDc1MDQ5NjAuMzc4NjQ1Mzc5MDc3MDg1NA==&clienttype=0";
		Response execute;
		try {
			execute = Jsoup.connect(url).ignoreContentType(true).headers(header()).cookies(ReadFileGetCookie(filePath))
					.method(Method.GET).execute();

			String body = execute.body();
			System.out.println(body);
			JSONObject json = JSONObject.fromObject(body);
			System.out.println(json.get("vcode"));
			String vcode = (String) json.get("vcode");

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public String CreateQun(String filePath) {
		// bdstoken可以从这里拿到 http://pan.baidu.com/mbox/homepage
		// yunData.MYBDSTOKEN = "a415b145c1f106b29e83d20bc76ce0b7";
		// String url="http://www.baidu.com";
		String url = "http://pan.baidu.com/mbox/group/specialcreate?user_list=[]&t=1486215828942&bdstoken=a415b145c1f106b29e83d20bc76ce0b7&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjIxNTgyODk0MjAuNTQ5MjMyMjAxOTI5NzkxNw==&clienttype=0";
		// String
		// url="http://pan.baidu.com/mbox/group/specialcreate?user_list=[]&t=1486130318224&bdstoken=6beeb43bf470ead35b55036b05c55f6b&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjEzMDMxODIyNTAuODUyOTA2NzEyMTU2NjUxMQ==&clienttype=0";
		Response execute;
		try {
			execute = Jsoup.connect(url).ignoreContentType(true).headers(header()).cookies(ReadFileGetCookie(filePath))
					.method(Method.GET).execute();

			String body = execute.body();
			System.out.println(body);
			JSONObject json = JSONObject.fromObject(body);
			System.out.println(json.get("vcode"));
			String vcode = (String) json.get("vcode");
			System.out.println(json.get("gid"));
			if (vcode != null) {
				return captcha(vcode);

			}
			return "1";

		} catch (IOException e) {
			e.printStackTrace();

		}
		return null;

	}

	/**
	 * 如果有验证码的话，把验证写入文件再读取
	 * 
	 * @param vcode
	 * @return
	 * @throws IOException
	 */
	public String captcha(String vcode) throws IOException {
		// https://passport.baidu.com/cgi-bin/genimage?codeString
		String baseUrl = "http://pan.baidu.com/genimage?%s";
		String requestUrl = String.format(baseUrl, vcode);
		URL url = new URL(requestUrl);
		InputStream in = new BufferedInputStream(url.openStream());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while (-1 != (n = in.read(buf))) {
			out.write(buf, 0, n);
		}
		out.close();
		in.close();
		byte[] response = out.toByteArray();
		// "D:/javacode/eclipse-jee-mars-2-win32-x86_64/pic/"+vcode + ".jpg"
		// D:\javacode\workspace\BaiduYun2\WebContent\pic\33324238
		// (WebContent/pic/"+vcode + ".jpg")
		// 从servlet跳转过来的路径 D:\javacode\eclipse-jee-mars-2-win32-x86_64\eclipse
		// 从当前页面直接访问的路径 D:\javacode\workspace\BaiduYun2
		// 获取绝对路径
		File f = new File("");
		String absolutePath = f.getAbsolutePath();
		System.out.println(absolutePath);

		// CreateQunService.class.getClassLoader().getResource().getPath();
		// String path =
		// CreateQunService.class.getClassLoader().getResource("").getPath();
		// File yourFile = new File(path+"/WebContent/pic/"+vcode + ".jpg");

		// D:\javacode\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\BaiduYun2\WEB-INF\classes

		File yourFile = new File(CreateQunService.class.getClassLoader().getResource(".").getPath() + File.separator
				+ "../../pic/" + vcode + ".jpg");
		System.out.println(yourFile.getCanonicalPath());
		yourFile.createNewFile(); // if file already exists will do nothing
		FileOutputStream fos = new FileOutputStream(yourFile, false);

		// FileOutputStream fos = new FileOutputStream("pic/" + vcode +
		// ".jpg/");
		fos.write(response);
		fos.close();
		return vcode + ".jpg";
	}

	public static void main(String[] args) {
		CreateQunService c = new CreateQunService();
		// System.out.println(c.CreateQun("Cookie2"));
		try {
			System.out.println(c.captcha(
					"3332423865633234636166333465663732323763363637363764323966666433666233313534343339363430303030303030303030303030303134383632323031333963B500A2C5805418785ABB4D940F55FE"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
