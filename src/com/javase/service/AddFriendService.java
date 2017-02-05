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
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.sf.json.JSONObject;

public class AddFriendService {

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
		File yourFile = new File( vcode + ".jpg");
		yourFile.createNewFile(); // if file already exists will do nothing 
		FileOutputStream fos = new FileOutputStream(yourFile, false); 
		
		
		//FileOutputStream fos = new FileOutputStream("pic/" + vcode + ".jpg/");
		fos.write(response);
		fos.close();
		return "WebContent/pic/" + vcode + ".jpg";
	}

	public void AddFriend(String username, String filePath) {

		// ①
		// http://pan.baidu.com/api/user/search?need_relation=1&user_list=%5B%222%22%5D&t=1486098843073&bdstoken=6beeb43bf470ead35b55036b05c55f6b&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjA5ODg0MzA3NDAuODQzNjM4NzAyODQ5NzA2Mw==&clienttype=0
		// ②
		// http://pan.baidu.com//genimage?333242386563323463616633346566373232376336363736376432396666643366623230313835373133363330303030303030303030303030303134383630393931393101602706497ADB48BDAA36D2E078FA4E
		// ③
		// http://pan.baidu.com/api/analytics?_lsid=1486098843236&_lsix=1&version=v5&page=1&clienttype=0&type=verifyCode_img_success
		// ④
		// http://pan.baidu.com/api/user/search?need_relation=1&user_list=%5B%222%22%5D&t=1486098889192&input=zt8q&vcode=333242386563323463616633346566373232376336363736376432396666643366623230313835373133363330303030303030303030303030303134383630393931393101602706497ADB48BDAA36D2E078FA4E&bdstoken=6beeb43bf470ead35b55036b05c55f6b&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjA5ODg4OTE5MzAuODQwNDI5ODU4NjMzMjQ3NQ==&clienttype=0
		// ⑤
		// http://pan.baidu.com/mbox/relation/addfollow?uk=2&type=normal&t=1486099854286&bdstoken=6beeb43bf470ead35b55036b05c55f6b&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjA5OTg1NDI4NzAuNTI5ODE3MDE5MTg1OTMxNw==&clienttype=0
		// http://pan.baidu.com/mbox/relation/addfollow?uk=4233222792&type=normal&input=2qbf&vcode=3332423865633234636166333465663732323763363637363764323966666433666231353638303738303438303030303030303030303030303031343836313331343137F04AC77127F04DF9BD60FB38F2476CB7&t=1486131376931&bdstoken=6beeb43bf470ead35b55036b05c55f6b&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjEzMTM3NjkzMTAuMzQ5NDE4NjA4MzMxOTkyMQ==&clienttype=0
		String url = "http://pan.baidu.com/api/user/search?need_relation=1&user_list=[\"2\"]&t=1486098843073&bdstoken=6beeb43bf470ead35b55036b05c55f6b&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjA5ODg0MzA3NDAuODQzNjM4NzAyODQ5NzA2Mw==&clienttype=0";
		// Document doc;
		Response execute;
		try {
			// doc =
			// Jsoup.connect(url).ignoreContentType(true).headers(header()).cookies(ReadFileGetCookie(filePath)).get();
			execute = Jsoup.connect(url).ignoreContentType(true).headers(header()).cookies(ReadFileGetCookie(filePath))
					.method(Method.GET).execute();

			String body = execute.body();
			System.out.println(body);
			JSONObject json = JSONObject.fromObject(body);
			System.out.println(json.get("vcode"));
			String vcode=(String) json.get("vcode");
			captcha(vcode);

		} catch (IOException e) {
			e.printStackTrace();

		}

	}
}