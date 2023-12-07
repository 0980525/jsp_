<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Detail Page</title>
</head>
<body>
	<div>
	<img alt="" src="/_fileUpload/${bvo.imageFile }">
	</div>
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
<a href="/brd/modify?bno=${bvo.bno }"><button>수정</button></a><!-- bno따라감 -->
<a href="/brd/remove?bno=${bvo.bno }"><button>삭제</button></a>
</c:if>
<a href="/brd/list"><button>list로 가기</button></a><!-- list로 가기 -->
<!-- 댓글 comment line 12.04 -->
<hr>
<div>
comment line<br>
<!-- bno 히든으로 가져올수 있고 / 스크립트로 넘길수 있음 -->
<input type="text" id="cmtWriter" value="${ses.id }" readonly="readonly"><br>
<input type="text" id="cmtText" placeholder="Add Comment..."><br>
<button type="button" id="cmtAddButtom"> 댓글 등록 </button>
</div>
<hr>
<!-- 댓글 표시 라인 -->
<div id="commentline">
	<div>
		<div> cno, bno, writer,regdate </div>
		<div>
			<button>수정</button> <button>삭제</button><br>
			<input value="content">
		</div>
	</div>
</div>
<script type="text/javascript">
	const bnoVal=`<c:out value="${bvo.bno}"/>`
	console.log(bnoVal);
	
</script>
<script src="/resource/board_detail.js"></script>
<script type="text/javascript">
	printCommentList(bnoVal);
</script>
</body>
</html>