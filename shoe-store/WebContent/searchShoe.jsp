<%@ page contentType="text/html" %>
<%@ page pageEncoding = "utf-8" %>
<HEAD><%@ include file="head.txt" %></HEAD>
<title>查询页面</title>
<style>
   #tom{
      font-family:宋体;font-size:26;color:black; 
   }
</style>
<HTML><body bgcolor =pink id=tom>
<div align="center">
<p id=tom>查询时可以输入鞋子的编号或鞋子名称及价格。<br>
鞋子名称支持模糊查询。
<br>输入价格是在2个值之间的价格，格式是：价格1-价格2<br>
例如：897.98-10000。
</p>
<form action="searchByConditionServlet" id =tom method="post" >
   <br>输入查询信息:<input type=text id=tom name="searchMess"><br>
   <input type =radio name="radio" id =tom value="shoe_version"/ >
    鞋子编号
   <input type =radio name="radio" id =tom value="shoe_name" >
    鞋子名称
   <input type =radio name="radio" value="shoe_price" checked="ok">
    鞋子价格
   <br><input type=submit id =tom value="提交">
</form>
</div></body></HTML>
