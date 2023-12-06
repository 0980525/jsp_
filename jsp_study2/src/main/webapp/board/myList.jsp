<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>myList</title>
</head>
<body>
<h1>내가 쓴 게시글</h1>

<table>
	<tr>
		<th>번호</th>
		<th>제목</th>
		<th>내용</th>
		<th>작성일</th>	
		<th>조회수</th>
	</tr>
<c:forEach items="${mylist }" var="bvo">
	<tr>
		<td><a href="/brd/detail?bno=${bvo.bno }">${bvo.bno }</a></td>
		<td>${bvo.title }</td>
		<td>${bvo.content }</td>
		<td>${bvo.regdate }</td>
		<td>${bvo.readcount }</td>
	</tr>
	
	</c:forEach>
 
 
</table>

</body>
</html>