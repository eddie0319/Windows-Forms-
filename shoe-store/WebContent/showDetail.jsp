<%@ page contentType="text/html" %>
<%@ page pageEncoding = "utf-8" %>
<%@ page import="save.data.Login" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="javax.naming.Context" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="java.sql.*" %>
<jsp:useBean id="loginBean" class="save.data.Login" scope="session"/>
<HEAD><%@ include file="head.txt" %></HEAD>
<title>商品详情</title>
<style>
   #tom{
      font-family:宋体;font-size:26;color:black 
   }
</style>
<HTML><body background =D:\eclipse\shoe\image\back.jpg id=tom><center>
<%  try{ 
         loginBean = (Login)session.getAttribute("loginBean");
         if(loginBean==null){
           response.sendRedirect("login.jsp");
           return;
         }
         else {
           boolean b =loginBean.getLogname()==null||
                   loginBean.getLogname().length()==0;
           if(b){
              response.sendRedirect("login.jsp");
              return;
           }
         }
      }
      catch(Exception exp){
           response.sendRedirect("login.jsp");
           return;
      }
   String shoeID = request.getParameter("shoeID"); 
   if(shoeID==null) {
       out.print("没有产品号，无法查看细节");
       return;
   } 
   Context context = new InitialContext();
   Context contextNeeded = (Context)context.lookup("java:comp/env");
   DataSource ds = (DataSource)contextNeeded.lookup("shoeConn");
   Connection con = null;
   Statement sql; 
   ResultSet rs;
   try{ 
     con= ds.getConnection();
     sql=con.createStatement();
     String query="SELECT * FROM shoeForm where shoe_version = '"+shoeID+"'";
     rs=sql.executeQuery(query);
     out.print("<table id=tom border=2>");
     out.print("<tr>");
     out.print("<th>产品号");
     out.print("<th>名称");
     out.print("<th>制造商");
     out.print("<th>价格");
     out.print("<th>加入购物车<th>");
     out.print("</tr>");
     String picture="D:/eclipse/shoe/image/background.jpg";
     String detailMess="";
     while(rs.next()){
       shoeID=rs.getString(1);
       String name=rs.getString(2);
       String maker=rs.getString(3);
       String price=rs.getString(4);
       detailMess=rs.getString(5);
       picture=rs.getString(6); 
       out.print("<tr>");
       out.print("<td>"+shoeID+"</td>");
       out.print("<td>"+name+"</td>");
       out.print("<td>"+maker+"</td>");
       out.print("<td>"+price+"</td>");
       String shopping =
       "<a href ='putGoodsServlet?shoeID="+shoeID+"'>添加到购物车</a>";
       out.print("<td>"+shopping+"</td>"); 
       out.print("</tr>");
     } 
     out.print("</table>");
     out.print("产品详情:<br>");
     out.println("<div align=center>"+detailMess+"<div>");
     String pic ="<img src='D:/eclipse/shoe/image/"+picture+"' width=260 height=200 ></img>";
     out.print(pic);
     con.close();
  }
  catch(SQLException exp){}
  finally{
     try{
          con.close();
     }
     catch(Exception ee){}
  } 
%>
</center></body></HTML>
