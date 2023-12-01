<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<!-- query String 방식으로 name=이가네사탕 address=제주 Step-2.jsp로 이동 -->
	<!-- step-2.jsp 페이지를 생성 후 파라미터를 출력 -->
	
	상품명 : ${param.name }<br>
	<br>
	원산지 : ${param.address }<br>
	<hr>
	1.스크립틀릿 방식으로 파라미터 전달받기<br>
	<!-- 컨트롤러에서 받는 방식 -->
	<%= request.getParameter("name") %><br>
	<%= request.getParameter("address") %><br>
	<hr>
	2.EL방식으로 파라미터 전달받기<br>
	${param.name }<br>
	${param.address }<br>
	
	
	<form action="step-3.jsp">
	이름 : <input type="text" name="name"><br>
	나이 : <input type="text" name="age"><br>
	<button type="submit">전송</button>
	</form>
</body>
</html>