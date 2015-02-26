import twitter4j.*;
import java.sql.*;
import java.util.Date;
import java.io.*;


public class ExtractTime{
	
	static double earthRadius = 3958.75;
	public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
    
    double dLat = Math.toRadians(lat2-lat1);
    double dLng = Math.toRadians(lng2-lng1);
    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
               Math.sin(dLng/2) * Math.sin(dLng/2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return earthRadius * c;

   
    }

	public static void main(String[] args) {
	   Connection conn = null;
	   Statement stmt = null;
	   try{
	      //STEP 2: Register JDBC driver
	      Class.forName("com.mysql.jdbc.Driver");

	      //STEP 3: Open a connection
	      System.out.println("Connecting to database...");
	      //conn = DriverManager.getConnection(DB_URL,USER,PASS);
	      conn = DriverManager.getConnection("jdbc:mysql://localhost/minitwitter?user=root&password=");
		
	      //STEP 4: Execute a query
	      System.out.println("Getting Table...");
	      stmt = conn.createStatement();
	      String sql;
	      //sql = "SELECT count(*), day(time), hour(time) FROM ( SELECT * FROM TotalConnections group by fromSN) as whatevs group by year(time), month(time), day(time), hour(time)";
	      sql="SELECT count(*), day(time), hour(time), avg(distance) FROM Links group by year(time), month(time), day(time), hour(time)";
           ResultSet rs = stmt.executeQuery(sql);
	      System.out.println("Got Connections! Starting analysis...");
	     // PreparedStatement findCoordinates1, findCoordinates2;
	     // findCoordinates1 = conn.prepareStatement("select lat,lon from Users where Users.sn=?");
		 // findCoordinates2 = conn.prepareStatement("select lat,lon from Users_weekend where Users_weekend.sn=?");
		
		 // PreparedStatement addCoordinates;
		 // addCoordinates = conn.prepareStatement("update Movement set lat1=?, lon1=?, lat2=?, lon2=? where sn=?");
		
	      PrintWriter writer = new PrintWriter("data/time/Avg_Dist_Time.csv","UTF-8"); 

	      int count = 0;

	      while(rs.next()){
	       	int counts = rs.getInt("count(*)");
	       	int day = rs.getInt("day(time)");
	       	int hour = rs.getInt("hour(time)");

	       	writer.println(day + "," + hour + "," + rs.getDouble("avg(distance)"));

	         count++;
	         System.out.println(count + " hour Supdated");

	      }
	      //after processing

	     writer.close();


	      //STEP 6: Clean-up environment
	      rs.close();
	      stmt.close();
	      conn.close();
	   }catch(SQLException se){
	      //Handle errors for JDBC
	      se.printStackTrace();
	   }catch(Exception e){
	      //Handle errors for Class.forName
	      e.printStackTrace();
	   }finally{
	      //finally block used to close resources
	      try{
	         if(stmt!=null)
	            stmt.close();
	      }catch(SQLException se2){
	      }// nothing we can do
	      try{
	         if(conn!=null)
	            conn.close();
	      }catch(SQLException se){
	         se.printStackTrace();
	      }//end finally try
	   }//end try
	   System.out.println("Goodbye!");
	}//end main
}