package handle.data;
import save.data.Record_Bean;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
public class SearchByCondition extends HttpServlet{
   public void init(ServletConfig config) throws ServletException{
      super.init(config);
   }
   public void service(HttpServletRequest request,
               HttpServletResponse response) throws ServletException,IOException{
      request.setCharacterEncoding("utf-8");
      HttpSession session=request.getSession(true); 
      String searchMess= request.getParameter("searchMess");
      String radioMess= request.getParameter("radio");
      if(searchMess==null||searchMess.length()==0) {
         response.getWriter().print("没有查询信息，无法查询");
         return;
      }
      Connection con=null; 
      String queryCondition="";
      float max=0,min=0;
      if(radioMess.contains("shoe_version")){
        queryCondition =
        "SELECT shoe_version,shoe_name,shoe_made,shoe_price "+
        "FROM shoeForm where shoe_version='"+searchMess+"'";  
      }
      else if(radioMess.contains("shoe_name")) {
         queryCondition =
        "SELECT shoe_version,shoe_name,shoe_made,shoe_price "+
        "FROM shoeForm where shoe_name like '%"+searchMess+"%'";
      }
      else if(radioMess.contains("shoe_price")) {
         String priceMess[] = searchMess.split("[-]+");
         try{
           min = Float.parseFloat(priceMess[0]);
           max = Float.parseFloat(priceMess[1]);
         }
         catch(NumberFormatException exp){
            min = 0;
            max = 0;
         }
         queryCondition =
        "SELECT shoe_version,shoe_name,shoe_made,shoe_price "+
        "FROM shoeForm where shoe_price<="+max+" and shoe_price>="+min;  
      }
      Record_Bean dataBean=null;
      try{ 
         dataBean=(Record_Bean)session.getAttribute("dataBean");
         if(dataBean==null){
            dataBean=new Record_Bean();
            session.setAttribute("dataBean",dataBean);
         }
      }
      catch(Exception exp){} 
      try{ 
          Context  context = new InitialContext();
          Context  contextNeeded=(Context)context.lookup("java:comp/env");
          DataSource ds=
          (DataSource)contextNeeded.lookup("shoeConn");
           con= ds.getConnection();
          Statement sql=con.createStatement();
          ResultSet rs=sql.executeQuery(queryCondition);
          ResultSetMetaData metaData = rs.getMetaData();
          int columnCount = metaData.getColumnCount(); 
          rs.last();
          int rows=rs.getRow();
          String [][] tableRecord=dataBean.getTableRecord();
          tableRecord = new String[rows][columnCount];
          rs.beforeFirst();
          int i=0;
          while(rs.next()){
            for(int k=0;k<columnCount;k++) 
              tableRecord[i][k] = rs.getString(k+1);
              i++; 
          }
          dataBean.setTableRecord(tableRecord); 
          con.close();
          response.sendRedirect("byPageShow.jsp"); 
     }
     catch(Exception e){
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

