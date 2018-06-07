<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet"
href="${pageContext.request.contextPath}/resources/style
/overallstyle.css" media="screen"/>
<link type="text/css" rel="stylesheet" 
href="${pageContext.request.contextPath}/resources/style
/getapprovalstyle.css" media="screen"/>
<title>결재받기</title>
</head>
<body>
	<%@include file="/WEB-INF/importviews/header.jsp"%>
	<%@include file="/WEB-INF/importviews/aside.jsp"%>
	<%@include file="/WEB-INF/importviews/nav.jsp"%>
	<section>
		<div id="maincontent">
			<div id="approvallist">
				<input type="hidden" id="userid" value="<%=session.getAttribute("id")%>"/>
			<table class="aprovtlist">
    			<thead>
    			<tr>
     			<th>결재명</th>
        		<th>생성일자</th>
        		<th>결재여부</th>
        		</thead>
        		<tbody id="resultlist">
        		<!-- 검색결과가 뿌려진다 -->
        		</tbody>
        	</table>
			</div>
		</div>
	</section>
</body>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
	//0.OnReady
	$(document).ready(function() {
		var userid = $('#userid').val();
		var aprovs = [];
		
		if(userid=="null"){
			alert("로그인된 유저만 사용할 수 있습니다!");
			window.location.href = "login";
		}else{
			$.ajax({
				url: "/getaprovlist.ajax?userid="+userid,
				type: "get",
				async:false,
				success:function(data){
					if(data==""){
						$("#approvallist").empty();
						var appendElement = $("#approvallist");
						var appendStr = "<h2 style='text-align:center;width:600px;'>"+
						"귀하 앞으로 요청된 결재작업이 없습니다!</h2>";
						appendElement.append(appendStr);
					}else{
						aprovs = data;
						$.each(aprovs,function(index){
						var appendElement = $("#resultlist");
						var appendStr = "";
						
						appendStr = "<tr id=result"+index+"></tr>"
						appendElement.append(appendStr);
						
						appendElement = $("#result"+index);
							
						$.each(aprovs[index],function(key,value){
							if(key=="aprov_reg"||key=="aprov_title"||key=="aprov_status"){
							appendElement = $("#result"+index);
							if(key=="aprov_title"){
								appendStr = 
									"<td><a id='"+aprovs[index].aprov_ai+
									"' onclick=approve('"+aprovs[index].aprov_ai+"')>"
									+value
									+"</a></td>";	
							}else if(key=="aprov_status"){
								switch(value){
								case 0:
									appendStr = "<td>X</td>";
									break;
								case 1:
									appendStr = "<td>O</td>";
									break;
								default: 
									appendStr = "<td>null</td>";
									break;
								}
								}else{
									appendStr = "<td>"+value+"</td>";
								}
							appendElement.append(appendStr);
							}
						});
						});
					}
				},error:function(request,status,error){
			        alert("code:"+request.status+"\n"+"message:"+status+"\n"+"error:"+error+"데이터 결과값이 없습니다!");
				}	
			});
		}
	});
	
	function approve(aprov_pk){
		var  pk = aprov_pk;
		if(!confirm("정말로 승인하겠습니까?")){
			alert("결재승인이 취소되었습니다");
		}else{
			alert("결재승인이 완료되었습니다");
			$.ajax({
				url: "/completeaprov.ajax?pk="+pk,
				type: "get",
				contentType : "application/x-www-form-urlencoded; charset=UTF-8",
				async: false,
				success:function(data){
					
				}
			});
		}
	}
</script>
</html>