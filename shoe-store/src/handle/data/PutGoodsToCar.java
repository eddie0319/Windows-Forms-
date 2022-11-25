package handle.data;
import save.data.Login;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class PutGoodsToCar extends HttpServlet {
   public void init(ServletConfig config) throws ServletException { 
      super.init(config);
   }
   public  void  service(HttpServletRequest request,
                         HttpServletResponse response) 
                         throws ServletException,IOException {
      request.setCharacterEncoding("utf-8");
      Connection con=null;
      PreparedStatement pre=null;  
      ResultSet rs;
      String shoeID = request.getParameter("shoeID");
      Login loginBean=null;
      HttpSession session=request.getSession(true);
      try{ 
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
      try {
        Context  context = new InitialContext();
        Context  contextNeeded=(Context)context.lookup("java:comp/env");
        DataSource ds=
        (DataSource)contextNeeded.lookup("shoeConn");
        con= ds.getConnection();
        String queryShoeForm =  
        "select * from shoeForm where shoe_version =?";
        String queryShoppingForm = 
        "select goodsAmount from shoppingForm where goodsId =?";
         String updateSQL = 
        "update shoppingForm set goodsAmount =? where goodsId=?";
         String insertSQL =
        "insert into shoppingForm values(?,?,?,?,?)";
         pre = con.prepareStatement(queryShoppingForm);
         pre.setString(1,shoeID);
         rs = pre.executeQuery();
         if(rs.next()){
             int amount = rs.getInt(1);
             amount++;
             pre = con.prepareStatement(updateSQL);
             pre.setInt(1,amount);
             pre.setString(2,shoeID);
             pre.executeUpdate();
         }
         else { 
             pre = con.prepareStatement(queryShoeForm); 
             pre.setString(1,shoeID);
             rs = pre.executeQuery();
             if(rs.next()){
                pre = con.prepareStatement(insertSQL); 
                pre.setString(1,rs.getString("shoe_version"));
                pre.setString(2,loginBean.getLogname());
                pre.setString(3,rs.getString("shoe_name"));
                pre.setFloat(4,rs.getFloat("shoe_price"));
                pre.setInt(5,1);
                pre.executeUpdate(); 
             }
         }
         con.close();
         response.sendRedirect("lookShoppingCar.jsp");
      }
      catch(SQLException exp){ 
         response.getWriter().print(""+exp);
      }
      catch(NamingException exp){}
      finally{
        try{
             con.close();
        }
        catch(Exception ee){}
      }  
   }
}
