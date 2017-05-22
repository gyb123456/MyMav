<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'login.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	  
	<script type="text/javascript" src="js/jquery.min.js"></script>
  </head>
  
  <script type="text/javascript">
   $(function(){
	/*   alert("fff");
		  var x = $("input[name='userName']").val();
		  alert(x); 
		  window.location.href="user/login";*/
	  });
	  
   function JumpHome(){
    	  var x = $("input[name='userName']").val();
		  alert(x); 
		  window.location.href="user/home";
  	}
	 
  </script>
  
  <body>
    	这是登录界面
    	<form action="user/login" method="post">
	    	用户名：<input name="userName" value="1">
	    	密码：<input name="password">
    	<input type="submit" value="登录" />
    	</form>
    	<button onclick="JumpHome()">跳到home界面</button>
  </body>

</html>
