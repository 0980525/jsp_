<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP_study</title>
</head>
<body>
<h1> Hello my First JSP World~!!</h1>
<!-- get : 이동 =>주소표시줄에 데이터를 ?쿼리스트링으로 달고 이동  
/ post : 데이터를 등록(많은 데이터를 이동 ) => 별도의 저장공간에 담아서 이동 /form태그 안에서만 줄수있음(아이디,비밀번호)/  -->
<form action="/memb/login" method="post">
	ID : <input type="text" name="id" placeholder="ID">
	Password : <input type="password" name="pwd" placeholder="Password">
	<button type="submit">login</button>
</form>

<div>
	<c:if test="${ses.id ne null}">
	${ses.id} 님이 login 하였습니다.<br>
	계정 생성일 : ${ses.regdate} <br>
	마지막 접속 : ${ses.lastlogin}
	<a href="/memb/detailMove"><button>회원정보수정</button></a>
	<a href="/memb/list"><button>회원리스트</button></a>
	<a href="/memb/logout"><button>로그아웃</button></a>
	<a href="/brd/register"><button type="button">글쓰기 페이지로 이동</button></a>
	</c:if>
	
</div>
<hr>
<a href="/memb/join"><button>회원가입</button></a>

<a href="/brd/register"><button>글쓰기 페이지로 이동</button></a>
<a href="/brd/list"><button>게시판 리스트로 이동</button></a>
<script type="text/javascript">

const msg_login = `<c:out value="${msg_login}"/>`;
console.log(msg_login);
	if(msg_login == '-1'){
		alert('로그인 정보가 일치하지 않습니다.'); 
	}
	
	const msg_modify =`<c:out value="${msg_modify}"/>`;
	console.log(msg_modify);
	if(msg_modify =='-1'){
		alert('회원정보가 수정되었습니다.');
	}
	</script>
</body>
</html>