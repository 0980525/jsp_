<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Index Page</title>
</head>
<body>
<h1>hello</h1>
<!-- 로그인 -->
<form action="/memb/login" method="post"> 
ID : <input type="text" name="id" placeholder="ID">
PW : <input type="password" name="pwd" placeholder="Password">
<button type="submit">login</button>
</form>
<!-- 로그인 후 생성 -->

<div>
	<c:if test="${ses.id ne null }">
		${ses.id }님이 login하였습니다.<br>
		계정 생성일 : ${ ses.regdate}<br>
		마지막 접속일 : ${ ses.lastlogin}<br>
		<a href="/memb/detail"><button>회원 정보</button></a>
		<a href="/memb/list"><button>회원 리스트</button></a>
		<a href="/memb/logout"><button>로그아웃</button></a>
		<a href="/brd/register"><button>글쓰기 페이지로 이동</button></a>
		<a href="/brd/mylist"><button>내가 쓴 게시글</button></a>
	</c:if>

</div>

<!-- 페이지 이동 -->
<a href="/memb/join"><button>회원가입</button></a>
<a href="/brd/register"><button>Register 글쓰기 페이지</button></a>
<a href="/brd/list"><button>게시판 리스트</button></a>
<script type="text/javascript">
const msg_login = `<c:out value="${msg_login}"/>`;
console.log(msg_login);
	if(msg_login == -1){
		alert('로그인 정보가 일치하지 않습니다.');
	}
const msg_modify = `<c:out value="${msg_modify}"/>`;
console.log(msg_modify);
	if(msg_modify == -1){
		alert('회원정보가 수정되었습니다.');
	}


</script>
</body>
</html>