package handle.data;
import save.data.Login;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class HandleBuyGoods extends HttpServlet {
   public void init(ServletConfig config) throws ServletException { 
      super.init(config);
   }
   public  void  service(HttpServletRequest request,
                         HttpServletResponse response) 
                         throws ServletException,IOException {
      request.setCharacterEncoding("utf-8");
      String logname = request.getParameter("logname");
      Connection con=null;
      PreparedStatement pre=null; 
      ResultSet rs;
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
        con = ds.getConnection();
        String querySQL = 
        "select * from shoppingForm where logname = ?";
         String insertSQL ="insert into orderForm values(?,?,?)";
         String deleteSQL ="delete from shoppingForm where logname =?";
         pre = con.prepareStatement(querySQL);
         pre.setString(1,logname);
         rs = pre.executeQuery();
         StringBuffer buffer = new StringBuffer();
         float sum = 0;
         boolean canCreateForm = false;
         while(rs.next()){
            canCreateForm = true;
            String  goodsId = rs.getString(1);
            logname = rs.getString(2);
            String  goodsName = rs.getString(3);
            float price = rs.getFloat(4); 
            int amount = rs.getInt(5);
            sum +=  price*amount;
            buffer.append("<br>商品id:"+goodsId+",名称:"+goodsName+
            "单价"+price+"数量"+amount);
         }
         if(canCreateForm == false){
             response.setContentType("text/html;charset=utf-8");
             PrintWriter out=response.getWriter();
             out.println("<html><body>");
             out.println("<h2>"+logname+"请先选择商品添加到购物车") ;
             out.println("<br><a href =index.jsp>主页</a></h2>");
             out.println("</body></html>");
             return;
         }
         String strSum = String.format("%10.2f",sum);
         buffer.append("<br>"+logname+"<br>购物车的商品总价:"+strSum);
         pre = con.prepareStatement(insertSQL); 
         pre.setInt(1,0); 
         pre.setString(2,logname);
         pre.setString(3,new String(buffer));
         pre.executeUpdate();
         pre = con.prepareStatement(deleteSQL);
         pre.setString(1,logname);
         pre.executeUpdate(); 
         con.close();
         response.sendRedirect("lookOrderForm.jsp");
      }
      catch(Exception e) { 
         response.getWriter().print(""+e);
      }
      finally{
        try{
             con.close();
        }
        catch(Exception ee){}
      }  
   }
}

