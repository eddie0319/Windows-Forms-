<%@ page contentType="text/html" %>
<%@ page pageEncoding = "utf-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="javax.naming.Context" %>
<%@ page import="javax.naming.InitialContext" %>
<HEAD><%@ include file="head.txt" %></HEAD>
<title>商品</title>
<style>
   #ok{
      font-family:宋体;font-size:26;color:black; 
   }
</style>
<HTML><body id=ok background =D:\eclipse\shoe\image\back.jpg>
<div align="center">
选择某类鞋子,分页显示这类鞋子。
<% Connection con=null; 
   Statement sql;
   ResultSet rs;
   Context context = new InitialContext();
   Context contextNeeded=(Context)context.lookup("java:comp/env");
   DataSource ds=(DataSource)contextNeeded.lookup("shoeConn");
   try {
      con= ds.getConnection();
      sql=con.createStatement();  
      rs=sql.executeQuery("SELECT * FROM shoeClassify");
      out.print("<form action='queryServlet' id =ok method ='post'>") ;
      out.print("<select id =ok name='fenleiNumber'>") ;
      while(rs.next()){
         int id = rs.getInt(1);
         String shoeCategory = rs.getString(2);
         out.print("<option value ="+id+">"+shoeCategory+"</option>");
      }  
      out.print("</select>");
      out.print("<input type ='submit' id =ok value ='提交'>");  
      out.print("</form>");
      rs.close();
      con.close();
   }
   catch(SQLException e){ 
      out.print(e);
   }
   finally{
     try{
        con.close();
     }
     catch(Exception ee){}
   } 
%>
</div></body></HTML>
