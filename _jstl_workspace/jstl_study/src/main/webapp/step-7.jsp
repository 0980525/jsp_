<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="jstl_study.CarVO" %>
    <%@page import="java.util.ArrayList" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	CarVO car1 = new CarVO("1234","소나타",2000);
	request.setAttribute("car1", car1);
	%>
	
	<!-- car1 객체 출력 -->
	
	${car1.num }<br><!-- java->getnum /  -->
	${requestScope.car1.name }<br>
	${car1.price }
	
	<%
	ArrayList<CarVO>list = new ArrayList<>();
	list.add(new CarVO("1111","그랜져",4500));
	list.add(new CarVO("2222","아반떼",3500));
	list.add(car1);
	
	request.setAttribute("list", list);
	session.setAttribute("list", list);
	%>
	<c:forEach items="${list }" var="cvo">
	num : ${cvo.num }<br>
	name : ${cvo.name }<br>
	price : ${cvo.price }<br>
	<hr>
	</c:forEach>
	
	
</body>
</html>