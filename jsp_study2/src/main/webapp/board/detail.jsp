<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Detail Page</title>
</head>
<body>
<h1>Detail Page</h1>
<table>
	<tr>
		<th>번호</th>
		<td>${bvo.bno }</td>
	</tr>
	<tr>
		<th>제목</th>
		<td>${bvo.title }</td>
	</tr>
	<tr>
		<th>writer</th>
		<td>${bvo.writer }</td>
	</tr>
	<tr>
		<th>내용</th>
		<td>${bvo.content }</td>
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
<c:if test="${bvo.writer eq ses.id }">
<a href="/brd/modify?bno=${bvo.bno }"><button>수정</button></a>
<a href="/brd/remove?bno=${bvo.bno }"><button>삭제</button></a>
</c:if>
<a href="/brd/list"><button>list로 가기</button></a>
<hr>
<!-- 댓글 표시 라인 -->
<div id="commentline">
	<div>
		<div> cno,bno,writer,regdate</div>
		<div>
			<button>수정</button><button>삭제</button><br>
			<input value="content">
		</div>
	</div>
</div>
<script type="text/javascript">
	const bnoVal = `<c:out value="${bvo.bno}"/>`
	console.log(bnoVal);
</script>
<script src="/resource/board_detail.js"></script>
<script type="text/javascript">
	printCommentList(bnoVal);
</script>
</body>
</html>