import twitter4j.*;
import java.sql.*;
import java.util.Date;
import java.io.*;


public class ExtractData {
	
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
	      sql = "SELECT * FROM Links";
	      ResultSet rs = stmt.executeQuery(sql);
	      System.out.println("Got Links! Starting analysis...");
	     // PreparedStatement findCoordinates1, findCoordinates2;
	     // findCoordinates1 = conn.prepareStatement("select lat,lon from Users where Users.sn=?");
		 // findCoordinates2 = conn.prepareStatement("select lat,lon from Users_weekend where Users_weekend.sn=?");
		
		 // PreparedStatement addCoordinates;
		 // addCoordinates = conn.prepareStatement("update Movement set lat1=?, lon1=?, lat2=?, lon2=? where sn=?");
		
	      PreparedStatement addDistance;
	      addDistance = conn.prepareStatement("update Links set distance=? where fromSN=? and toSN=? and time=?");


	      PreparedStatement getUserInfo;
	      getUserInfo = conn.prepareStatement("select * from Nodes where sn=?");

	      

		  int count = 0;
	      //STEP 5: Extract data from result set
	      while(rs.next()){
	         //Retrieve by column name
	         //int id  = rs.getInt("id");
	         //int age = rs.getInt("age");


	      	////if(count == 1) System.exit(1);
	         String fromsn = rs.getString("fromSN");
	         String tosn = rs.getString("toSN");
	         Timestamp time = rs.getTimestamp("time");

	         double fromLat, fromLon, toLat, toLon;

	         fromLat=fromLon=toLat=toLon=0;

	         ResultSet fromUser, toUser;
	         getUserInfo.setString(1,fromsn);
	         fromUser = getUserInfo.executeQuery();
	         
	         
	         if(fromUser.next())
         	 {
         		fromLat = fromUser.getDouble("lat");
         		fromLon = fromUser.getDouble("lon");
             }
             else
             {
             	System.out.println("ERROR - COULD NOT FIND USER!!!");
             }

             getUserInfo.setString(1,tosn);
	         toUser = getUserInfo.executeQuery();
             if(toUser.next())
             {
             	toLat = toUser.getDouble("lat");
         		toLon = toUser.getDouble("lon");
             }
             else
             {
             	System.out.println("ERROR - COULD NOT FIND USER!!!");
             }

             addDistance.setDouble(1, distFrom(fromLat, fromLon, toLat, toLon));
             addDistance.setString(2, fromsn);
             addDistance.setString(3, tosn);
             addDistance.setTimestamp(4, time);
             addDistance.executeUpdate();
	         count++;
	         System.out.println(count + " updated");

	      }
	      //after processing

	     


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