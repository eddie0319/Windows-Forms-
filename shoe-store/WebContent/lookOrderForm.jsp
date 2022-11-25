<%@ page contentType="text/html" %>
<%@ page pageEncoding = "utf-8" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="javax.naming.Context" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="java.sql.*" %>
<jsp:useBean id="loginBean" class="save.data.Login" scope="session"/>
<HEAD><%@ include file="head.txt" %></HEAD>
<title>查看订单</title>
<style>
   #tom{
      font-family:宋体;font-size:26;color:black 
   }
</style>
<HTML><body bgcolor=cyan id=tom>
<div align="center">
<%  if(loginBean==null){
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
    Context  context = new InitialContext();
    Context  contextNeeded = (Context)context.lookup("java:comp/env");
    DataSource  ds = (DataSource)contextNeeded.lookup("shoeConn");
    Connection con =null;
    Statement sql; 
    ResultSet rs;
    out.print("<table border=1>");
    out.print("<tr>");
    out.print("<th id=tom width=110>"+"订单序号");
    out.print("<th id=tom width=100>"+"用户名称");
    out.print("<th id=tom width=200>"+"订单信息");
    out.print("</tr>"); 
   try{
       con = ds.getConnection();
       sql=con.createStatement(); 
       String SQL = 
      "SELECT * FROM orderForm where logname='"+loginBean.getLogname()+"'";
       rs=sql.executeQuery(SQL);
       while(rs.next()) {
           out.print("<tr>");
           out.print("<td id=tom>"+rs.getString(1)+"</td>"); 
           out.print("<td id=tom>"+rs.getString(2)+"</td>");
           out.print("<td id=tom>"+rs.getString(3)+"</td>");
           out.print("</tr>") ; 
       }
       out.print("</table>");
       con.close() ;
    }
    catch(SQLException e) { 
       out.print("<h1>"+e+"</h1>");
    }
    finally{
        try{
             con.close();
        }
        catch(Exception ee){}
    } 
%>
</div></body></HTML>
