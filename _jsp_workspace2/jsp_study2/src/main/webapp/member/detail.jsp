<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>detail page</title>
</head>
<body>
<h1>Member detail page</h1>
<form action="/memb/modify" method="post">
	ID : <input type="text" name="id" value="${ses.id }" readonly="readonly">
	PW : <input type="password" name="pwd" value="${ses.pwd }" >
	E-mail : <input type="text" name="email" value="${ses.email }" >
	Age : <input type="text" name="age" value="${ses.age }">
<button type="submit">수정</button>
<a href="/memb/remove?id=${ses.id }"><button type="button">회원 탈퇴</button></a>
</form>
</body>
</html>