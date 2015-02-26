import twitter4j.*;
import java.sql.*;
import java.util.Date;
import java.io.*;


public class HistogramData {
	
	static double earthRadius = 3958.75;
	

	public static void main(String[] args) {
	   Connection conn = null;
	  //  Statement stmt = null;
	   try{
	      //STEP 2: Register JDBC driver
	      Class.forName("com.mysql.jdbc.Driver");

	      //STEP 3: Open a connection
	      System.out.println("Connecting to database...");
	      //conn = DriverManager.getConnection(DB_URL,USER,PASS);
	      conn = DriverManager.getConnection("jdbc:mysql://localhost/minitwitter?user=root&password=");
		
	      //STEP 4: Execute a query
	      System.out.println("Creating statement...");
	      System.out.println("Starting!");
	      PreparedStatement stmt = conn.prepareStatement("select distance from Links L join Nodes N on L.fromSN=N.sn and N.lat>? and N.lon>? and N.lat<? and N.lon<? order by distance");
	      String sql;
	      sql = "SELECT * FROM Links order by distance";
	    
	   	 // String locations[] = {"UCLA", "Lebanon", "Manhattan", "Los_Angeles", "Buenos_Aires", "Delhi", "Guangzhou_Hong_Kong", 
	   	  //	"Tokyo", "Paris", "Bangkok", "Hawaii", "USA", "UK", "Japan", "Taiwan", "India", "Australia", "Russia", "South_Korea"};

	      String locations[] ={"Russia"};

	      double[][] latlonloc = new double[][]{
	      	//{ 34.0545083,-118.4556627, 34.0807443,-118.4354067},
	      	//{ 33.0639242,  35.0216675, 34.5608594,  36.4663696},
	      	//{ 40.6881880, -74.0219307, 40.8793863, -73.9099216},
	      	//{ 33.9125941,-118.5074615, 34.1220370,-118.1627655},
	      	//{-35.0389920, -59.0652466,-34.3842460, -57.7963257},
	      	//{ 28.2777771,  76.7037964, 28.9072061,  77.4440002},
	      	//{ 22.0500048, 112.9147339, 23.3624286, 114.3923950},
	      	//{ 35.4382955, 139.4371033, 35.9023999, 140.1072693},
	      	//{ 48.6510570,   1.9679260, 49.1116357,   2.6792908},
	      	//{ 13.3789317, 100.2310181, 14.0619881, 100.8462524},
	      	//{ 18.3649526,-160.6475830, 22.6342927,-154.1656494},
	      	//{ 23.8054496,-124.9365234, 50.5692829, -66.6650391},
	      	//{ 49.6391772,  -8.4594727, 58.9500082,   1.9555664},
	      	//{ 32.0639556, 130.8471680, 40.9135126, 142.5805664},
	      	//{ 21.6778483, 119.7399902, 25.4432746, 122.3107910},
	      	//{  7.6238869,  68.9501953, 33.5780147,  88.5498047},
	      	//{ -40,  110, -10, 156},
	      	{ 41,  26, 64,120},
	      	//{ 33.9980273, 125.3210449, 38.6940850 ,129.6990967}  	
	      };


	      for(int i=0; i<locations.length; i++)
	      {
	      	String loc= locations[i];
	      
	      	System.out.println("Executing Query for: " + loc + "...");

	      	PrintWriter writer = new PrintWriter("data/"+loc+"_data.csv","UTF-8");

	      	stmt.setDouble(1, latlonloc[i][0]);
			stmt.setDouble(2, latlonloc[i][1]);
			stmt.setDouble(3, latlonloc[i][2]);
			stmt.setDouble(4, latlonloc[i][3]);
			ResultSet rs = stmt.executeQuery();
			int count=0;
			while(rs.next()){ 
	        	double distance = rs.getDouble("distance");
	         	count++;
	         	writer.println(count + ","+ distance);
	         	System.out.println(count + " Distance: " + distance );
	      	}
	      	System.out.println(loc+": "+count +" matches");
	      	writer.close();
	      	rs.close();
	      }



	      //STEP 6: Clean-up environment
	     
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
	         if(conn!=null)
	            conn.close();
	      }catch(SQLException se){
	         se.printStackTrace();
	      }//end finally try
	   }//end try
	   System.out.println("Goodbye!");
	}//end main
}