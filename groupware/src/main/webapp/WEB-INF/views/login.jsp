<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>로그인</title>
</head>
<body>
<h1>로그인</h1><br>
<h2>아이디</h2><br>
<input type="text" id="user_id" name="user_id"><br>
<h2>비밀번호</h2><br>
<input type="password" id="user_pw" name="user_pw"><br><br>
<input type="button" value="로그인" id="loginbtn" name="loginbtn" onclick="login()">
<input type="button" value="회원가입" id="joinbtn" onclick="gojoin()">
</body>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
var alertMsg = "";

//1.로그인
function login() {
	var flag = true;
	user_id = document.getElementById("user_id").value;
	user_pw = document.getElementById("user_pw").value;
	
	flag = loginexception();
	if(flag){
		loginproc();
	}else{
		alert("로그인실패!");
	}
}

//2.예외처리
function loginexception(){
	
	var elementArray = settingElementArray();
	var flag = true;
	var kor = "";
	
	$.each(elementArray, function(index,item){
		flag = checkElementEmpty(item);
		if(flag == false){
			alertMsg = "비어있는 항목이 있습니다!";
			alert(alertMsg);
			return false;
		}
	});
	
	return flag;
}

//3.로그인처리
function loginproc(){
	var logininfo = new Object();
	logininfo.user_id = user_id;
	logininfo.user_pw = user_pw;

	$.ajax({
		url : '/userlogincheck.ajax',
		data : logininfo,
		type : 'post',
		success:function(data){	
			if(data=="T"){
				alertMsg = "로그인에 성공하였습니다!";
				alert(alertMsg);
				loginsuclink();
			}else{
				alertMsg = "잘못된 아이디 또는 비밀번호입니다!";
				alert(alertMsg);
			}
		}
	});
}

//4.로그인 성공시 index 링크
function loginsuclink(){
	window.location.href = "index";
}

//5. element array create
function settingElementArray(){

	var elementArray = new Array();

	elementArray.push($('#user_id'));
	elementArray.push($('#user_pw'));

	return elementArray;
}

function checkElementEmpty(element){
	var val = element.val();
	var flag = true;

	if(val ==""){
		flag = false;
	}
	return flag;
}

function gojoin(){
	location.href = "join";
}
</script>
</html>