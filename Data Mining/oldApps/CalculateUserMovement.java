import twitter4j.*;
import java.sql.*;
import java.util.Date;



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
	      System.out.println("Creating statement...");
	      stmt = conn.createStatement();
	      String sql;
	      sql = "SELECT * FROM Movement";
	      ResultSet rs = stmt.executeQuery(sql);

	     // PreparedStatement findCoordinates1, findCoordinates2;
	     // findCoordinates1 = conn.prepareStatement("select lat,lon from Users where Users.sn=?");
		 // findCoordinates2 = conn.prepareStatement("select lat,lon from Users_weekend where Users_weekend.sn=?");
		
		 // PreparedStatement addCoordinates;
		 // addCoordinates = conn.prepareStatement("update Movement set lat1=?, lon1=?, lat2=?, lon2=? where sn=?");
		
	      PreparedStatement addDistance;
	      addDistance = conn.prepareStatement("update Movement set distance=? where sn=?");

		  int count = 0;
	      //STEP 5: Extract data from result set
	      while(rs.next()){
	         //Retrieve by column name
	         //int id  = rs.getInt("id");
	         //int age = rs.getInt("age");
	         String sn = rs.getString("sn");
	         double lat1 = rs.getDouble("lat1");
	         double lon1 = rs.getDouble("lon1");
	         double lat2 = rs.getDouble("lat2");
	         double lon2 = rs.getDouble("lon2");
	         
	         addDistance.setDouble(1, distFrom(lat1, lon1, lat2, lon2));
	         addDistance.setString(2, sn);

	         addDistance.executeUpdate();
	         count++;
	         System.out.println(count + " updated distance sn: " + sn );

	         // if(coord.next() && coord2.next()){
	         // 	double lat1 = coord.getDouble("lat");
	         // 	double lon1 = coord.getDouble("lon");
	         // 	double lat2 = coord2.getDouble("lat");
	         // 	double lon2 = coord2.getDouble("lon");
	         	
	         // 	addCoordinates.setDouble(1,lat1);
	         // 	addCoordinates.setDouble(2,lon1);
	         // 	addCoordinates.setDouble(3,lat2);
	         // 	addCoordinates.setDouble(4,lon2);
	         // 	addCoordinates.setString(5,sn);
	         // 	addCoordinates.executeUpdate();
	         // 	count++;
	         // 	System.out.println(count + " updated sn: " + sn);		         
	         // }
	         // //String last = rs.getString("last");

	         //Display values
	         //System.out.print("ID: " + id);
	         //System.out.print(", Age: " + age);
	         //System.out.print(", First: " + first);
	        // System.out.println("sn: " + sn);
	      }
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