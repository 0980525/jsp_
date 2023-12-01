<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<!-- 메뉴판 먹고싶은 메뉴 5가지 체크박스 형태로 만들어 step-6.jsp로 전송 
	step-6.jsp에서 주문자 명, 어떤 메뉴를 주문했는지 출력
-->
<form action="step-6.jsp" method="post">
주문자 명 : <input type="text" name=ordername><br>
메뉴 1 : <input type="checkbox" name="menu" value="김치볶음밥"> 김치볶음밥 <br>
메뉴 2 : <input type="checkbox" name="menu" value="참치김치찌개"> 참치김치찌개 <br>
메뉴 3 : <input type="checkbox" name="menu" value="명란고사리파스타"> 명란고사리파스타 <br>
메뉴 4 : <input type="checkbox" name="menu" value="치킨마요덮밥"> 치킨마요덮밥 <br>
메뉴 5 : <input type="checkbox" name="menu" value="사이다"> 사이다 <br>
<br>
<button type="submit">주문하기</button>

</form>
<!-- <a>=get -->
<!-- <form action="step-6.jsp" method="post">
주문자 명 : <input type="text" name=ordername><br>
메뉴 1 : <input type="checkbox" name="menu" value="김치볶음밥"> 김치볶음밥 <br>
메뉴 2 : <input type="checkbox" name="menu" value="참치김치찌개"> 참치김치찌개 <br>
메뉴 3 : <input type="checkbox" name="menu" value="명란고사리파스타"> 명란고사리파스타 <br>
메뉴 4 : <input type="checkbox" name="menu" value="치킨마요덮밥"> 치킨마요덮밥 <br>
메뉴 5 : <input type="checkbox" name="menu" value="사이다"> 사이다 <br>
<br>
<button type="submit">주문하기</button> -->

</form>
</body>
</html>