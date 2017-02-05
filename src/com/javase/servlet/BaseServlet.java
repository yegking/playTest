package com.javase.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 专门用来被其他的Servlet继承
 * @author Administrator
 *
 */

public class BaseServlet extends HttpServlet{
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//处理post请求乱码，只需要在getParamter方法第一次调用前，设置request的编码
		request.setCharacterEncoding("utf-8");
		
		//获取用户请求的参数
		String methodName = request.getParameter("method");
		
		//通过方法名获取到方法的对象
		
		//获取当前类的Class对象
		Class cla = this.getClass();
		try {
			//getDeclaredMethod*()获取的是类自身声明的所有方法
			Method method =cla.getDeclaredMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
			//setAccessible(true)的方式关闭安全检查就可以达到提升反射速度的目的 ,设置为true,则表示可以忽略访问权限的限制，直接调用
			method.setAccessible(true);
			method.invoke(this, request,response);
		} catch (Exception e) {
			throw  new RuntimeException(e);
		}
	}

}
