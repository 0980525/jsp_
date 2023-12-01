<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2> Form 태그로 전달한 파라미터 받기</h2>

<!-- 스클립틀릿 방식은 parameter값이 문자열로 인식. 연산 불가능 -->
<%=request.getParameter("name") %><br>
<%=request.getParameter("age") %><br>

<!-- EL방식은 내부적으로 형변환을 하여 연산이 가능함 -->
이름 : ${param.name }<br>
나이 : ${param.age }<br>
<br>
<!-- c:if로 나이가 18세 미만이면 미성년자입니다. 라고 출력 -->
<c:if test="${param.age<18 }">
미성년자입니다.
</c:if>

<!-- choose 를 사용하여 다중 조건 처리 -->
<c:choose>
	<c:when test="${param.age>=19 }">
		<h3>${param.name }님은 ${param.age }세 성인입니다.</h3>
	</c:when>
	<c:when test="${param.age>=15 }">
	<h3>${param.name }님은 ${param.age }세 청소년입니다.</h3>
	</c:when>
	<c:when test="${param.age>=5 }">
	<h3>${param.name }님은 ${param.age }세 어린이입니다.</h3>
	</c:when>
	<c:when test="${param.age<5 }">
		<h3>${param.name }님은 ${param.age }세 유아입니다.</h3>
	</c:when>
</c:choose>

<a href="step-2.jsp">step-2.jsp로 이동</a>
<form action="step-4.jsp">
<input type="checkbox" name="food" value="바나나"> 바나나<br>
<input type="checkbox" name="food" value="딸기"> 딸기<br>
<input type="checkbox" name="food" value="귤"> 귤<br>
<input type="checkbox" name="food" value="포도"> 포도<br>
<input type="checkbox" name="food" value="복숭아"> 복숭아<br>
<button type="submit">전송</button><br>
</form>
</body>
</html>