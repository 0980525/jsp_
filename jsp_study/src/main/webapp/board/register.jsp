<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기 페이지</title>
</head>
<body>
<h1>Board Register Page</h1>
<form action="/brd/insert" method="post">
제목 : <input type="text" name="title"><br>
<%-- value="${ses.id}" readonly="readonly" => 작성자 이름을 ses객체에서 id를 가져옴 + 읽기전용(수정 안됨)
로그인 하지 않은 상태로 글을 쓰면 작성자 이름이 들어가지 않음 / 
 --%>
작성자 : <input type="text" name="writer" value="${ses.id}" readonly="readonly"><br>
내용 : <br>
<textarea rows="10" cols="30" name="content"></textarea><br>

<button type="submit">전송</button><br>
</form>
<button type="button">리스트로</button>

</body>
</html>