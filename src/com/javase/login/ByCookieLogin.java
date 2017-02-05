package com.javase.login;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;

public class ByCookieLogin {

	// 设置其Cookies机制为浏览器模式
	public static Map<String, String> header() {

		// 将浏览器头信息保存在map里面
		// passport
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Host", "pan.baidu.com");
		headers.put("Referer", "http://pan.baidu.com");
		headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		headers.put("Accept-Language", "zh-CN,zh;q=0.8");
		headers.put("Accept-Encoding", "gzip, deflate, sdch");
		headers.put("X-Forwarded-For", "112.224.21.186");
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
		return headers;
	}

	public static Map<String, String> ReadFileGetCookie() {
		// 如果你文件里没有中文，那个GBK编码就无所谓，否则要按实际编码来定
		BufferedReader br;
		Map<String, String> map = new HashMap<String, String>();
		try {
			br = new BufferedReader(new FileReader("Cookie2"));
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

	public static void main(String[] args) {
		// 发送请求
		// ReadFileGetCookie();
		// http://pan.baidu.com/mbox/user/promotion?t=1486049420961&bdstoken=6beeb43bf470ead35b55036b05c55f6b&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjA0OTQyMDk2MTAuMjc0NzIwMTk1MDM3NDYxOA==&clienttype=0
		//String url="http://pan.baidu.com/mbox/user/promotion?t=1486049420961&bdstoken=6beeb43bf470ead35b55036b05c55f6b&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjA0OTQyMDk2MTAuMjc0NzIwMTk1MDM3NDYxOA==&clienttype=0";
		//"http://pan.baidu.com"
		//http://pan.baidu.com/api/user/search?need_relation=1&user_list=%5B%22%E4%BD%A0%E5%A5%BD%22%5D&t=1486050049863&bdstoken=6beeb43bf470ead35b55036b05c55f6b&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjA1MDA0OTg2NDAuMzk1NjA3NzQ4OTMwMzc5MQ==&clienttype=0
		//String url="http://pan.baidu.com/api/user/search?need_relation=1&user_list=%5B%22first%22%5D&t=1486050155103&bdstoken=6beeb43bf470ead35b55036b05c55f6b&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjA1MDE1NTEwMzAuNTgwMTgwMDkyNzk1NzkzOQ==&clienttype=0";
		String url="http://pan.baidu.com/api/user/search?need_relation=1&user_list=%5B%221%22%5D&t=1486096294071&bdstoken=6beeb43bf470ead35b55036b05c55f6b&channel=chunlei&web=1&app_id=250528&logid=MTQ4NjA5NjI5NDA3MjAuNjQ3MTIzNjg4NDYzNTA1&clienttype=0";
		Document doc;
		try {
			//doc = Jsoup.connect("http://pan.baidu.com").headers(header()).cookies(ReadFileGetCookie()).post();
			doc = Jsoup.connect(url).ignoreContentType(true).headers(header()).cookies(ReadFileGetCookie()).post();
			System.out.println(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
