package com.javase.servlet;


import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javase.service.AddFriendService;
import com.javase.service.CreateQunService;

public class AddFriend extends BaseServlet{
	private static final long serialVersionUID = 1L;
	private AddFriendService addFriendService=new AddFriendService();
	private CreateQunService createQunService=new CreateQunService();

	
	protected void CreateQun(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String filePath = this.getServletContext().getRealPath("Cookie3");
			//addFriendService.AddFriend(username,filePath);
		 String createQun = createQunService.CreateQun(filePath);
		 if (createQun=="1"){
			System.out.println("群创建成功");
		}else if (createQun==null) {
			System.out.println("群没有创建成功，未知异常");
		}else{
			 System.out.println("需要输入验证码"+createQun);
			 request.setAttribute("vcode", createQun);
			 request.getRequestDispatcher("/page/addfriend.jsp").forward(request, response);
		}
		
	}
	
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doGet(request, response);
//	}
	
	
	protected void getCaptchaByUserText(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String captcha= request.getParameter("captcha");
		String vcode= request.getParameter("vcode");
		System.out.println("返回的"+captcha);
		System.out.println("返回的"+vcode);
		String filePath = this.getServletContext().getRealPath("Cookie3");
		Map<String, String> createQun = createQunService.CreateQunByCaptcha(captcha, vcode, filePath);
		if (createQun==null) {
			System.out.println("未知异常");
			 request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}else{
			if (createQun.get("gid")!=null) {
				System.out.println("群创建成功");
			}else if (createQun.get("vcode")!=null) {
				System.out.println("验证码输入有误,请重新输入");
				request.setAttribute("vcode", createQun.get("vcode")+".jpg");
				request.setAttribute("vcode2",createQun.get("vcode"));
				System.out.println(createQun.get("vcode"));
				request.getRequestDispatcher("/page/addfriend.jsp").forward(request, response);
			}
		}
		
	}

}
