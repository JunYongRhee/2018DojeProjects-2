<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" 
href="${pageContext.request.contextPath}/resources/style
/headerstyle.css" media="screen"/>
<title>import header</title>
</head>
<body>
<header>
<h1><a href="index">TestGroupware</a></h1>
<ul class="topmenu" id="logout">
<li>안녕하세요!</li>
<li>알림</li>
<li><a href="join">회원가입</a></li>
<li><a href="login">로그인</a></li>
</ul>
<ul class="topmenu" id="logined">
<li><%=session.getAttribute("id")%>님</li>
<li>알림</li>
<li><a href="userupdate">회원정보수정</a></li>
<li><a href="logout">로그아웃</a></li>
</ul>
</header>
</body>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
var loginedid = "<%=(String)session.getAttribute("id")%>";
//1.로그인&로그아웃 페이지 보여주기
$(document).ready(function() {
	if(loginedid!="null"&&loginedid!=""){
		$('#logout').css('display','none');
	}else{
		$('#logined').css('display','none');
	}
});
</script>
</html>