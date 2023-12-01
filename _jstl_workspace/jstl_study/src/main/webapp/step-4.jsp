<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

과일 1 : ${paramValues.food[0] }<br>
과일 2 : ${paramValues.food[1] }<br>
과일 3 : ${paramValues.food[2] }<br>
과일 4 : ${paramValues.food[3] }<br>
과일 5 : ${paramValues.food[4] }<br>

<!-- 위 values를 foreach로 나타내기 foods에 담긴 만큼 배열실행 -->
<h3>c:forEach를 사용한 출력</h3>
<c:forEach items="${paramValues.food}" var="foods">
	과일 : ${foods }<br>
</c:forEach>
<hr>
<hr>
<!-- 번호넣기 varStatus =>index(0번지부터 시작), count(1부터 시작) -->
<h3>c:forEach를 사용한 출력</h3>
<c:forEach items="${paramValues.food}" var="foods" varStatus="st">
	과일${st.index}/${st.count} : ${foods }<br>
</c:forEach>

<%

String friends[]={"삼겹살","소주","족발","맥주"};
//java controller에서 jsp로 데이터를 보낼 때 사용
request.setAttribute("f", friends);

%>
<br>
<hr>
<!-- requestScope는 생략가능 -->
<c:forEach items="${requestScope.f }" var="fname" varStatus="st">
	count : ${st.count } /
	index : ${st.index } /

	${fname }<br>
</c:forEach>

<br>
<hr>
<!-- 원하는 데이터만 반복 가능
begin=>시작숫자
end=>끝 숫자
var=>요소저장변수
 -->
<c:forEach begin="1" end="10" var="i">
	${i }/

</c:forEach>

<a href="step-5.jsp">step-5으로 이동</a>
<a href="step-3.jsp">step-3으로 이동</a>
</body>
</html>