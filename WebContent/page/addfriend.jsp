<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/WEB-INF/include/base.jsp" %>
<title>Insert title here</title>
</head>
<body>
<h3>请输入正确的用户名</h3>
<form action="AddFriend?method=CreateQun" method="post">
<input type="text" name="username">
<input type="submit" value="提交">
</form>


<form action="AddFriend?method=getCaptchaByUserText" method="post">
<c:if test="${!empty vcode}">
<img src="http://${pageContext.request.serverName }:${pageContext.request.serverPort}${pageContext.request.contextPath}/pic/${vcode}">
<input type="text" name="captcha">
<input type="hidden" name="vcode" value="${vcode2}">
<input type="submit" value="提交">
</c:if>
</form>
</body>
</html>