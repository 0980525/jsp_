<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>modify Page</title>
</head>
<body>
<form action="/brd/edit">
<table>
	<tr>
		<th>bno</th>
		<td><input type="text" name="bno" value="${bvo.bno }" readonly="readonly"></td>
	</tr>
	<tr>
		<th>title</th>
		<td><input type="text" name="title" value="${bvo.title }"></td>
	</tr>
	<tr>
		<th>writer</th>
		<td>${bvo.writer }"</td>
	</tr>
	<tr>
		<th>내용</th>
		<td><textarea rows="10" cols="30" name="content">${bvo.content }</textarea>
		</td>
	</tr>
	<tr>
		<th>readcount</th>
		<td>${bvo.readcount }</td>
	</tr>
	<tr>
		<th>regdate</th>
		<td>${bvo.regdate }</td>
	</tr>
	<tr>
		<th>moddate</th>
		<td>${bvo.moddate }</td>
	</tr>
</table>
<button type="submit">수정</button>

</form>
<a href="/brd/remove?bno=${bvo.bno} "><button type="button">modify</button></a>
<a href="/brd/list"><button type="button">list</button></a>
</body>
</html>