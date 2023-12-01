<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <%
    //post방식의 한글 처리
    request.setCharacterEncoding("UTF-8");
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3>주문자명 : ${param.ordername }</h3>
<c:forEach items="${paramValues.menu }" var="food" varStatus="st">
 ${st.count } . ${food} <br>
</c:forEach>
주문완료 <br>


<a href="step-7.jsp">step-7로 이동</a>
</body>
</html>