import twitter4j.*;
import java.sql.*;
import java.util.Date;
import java.io.*;


public class CollectUserMovement {
	
	static double earthRadius = 3958.75;
	

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
	      sql = "SELECT * FROM Links";
	      ResultSet rs = stmt.executeQuery(sql);

	     // PreparedStatement findCoordinates1, findCoordinates2;
	     // findCoordinates1 = conn.prepareStatement("select lat,lon from Users where Users.sn=?");
		 // findCoordinates2 = conn.prepareStatement("select lat,lon from Users_weekend where Users_weekend.sn=?");
		
		 // PreparedStatement addCoordinates;
		 // addCoordinates = conn.prepareStatement("update Movement set lat1=?, lon1=?, lat2=?, lon2=? where sn=?");
		


	      //0, <0.1,1,10,25,50,100,500,1000,2000,3000,4000,5000,6000,7000,8000,9000,10000,11000,12240
	      int[] bucket = new int[100];
	      for(int i : bucket)
	      	i=0;

		  int count = 0;
	      //STEP 5: Extract data from result set
	      while(rs.next()){
	         //Retrieve by column name
	         //int id  = rs.getInt("id");
	         //int age = rs.getInt("age");
	         //String sn = rs.getString("sn");
	         double distance = rs.getDouble("distance");
	         if(distance==0)
	         	bucket[0]++;
	         else if(distance<=0.1)
	         	bucket[1]++;
	         else if(distance<=1)
	         	bucket[2]++;
	         else if(distance<=2)
	         	bucket[3]++;
	         else if(distance<=4)
	         	bucket[4]++;
	         else if(distance<=8)
	         	bucket[5]++;
	         else if(distance<=16)
	         	bucket[6]++;
	         else if(distance<=32)
	         	bucket[7]++;
	         else if(distance<=64)
	         	bucket[8]++;
	         else if(distance<=128)
	         	bucket[9]++;
	         else if(distance<=256)
	         	bucket[10]++;
	         else if(distance<=512)
	         	bucket[11]++;
	         else if(distance<=1024)
	         	bucket[12]++;
	         else if(distance<=2048)
	         	bucket[13]++;
	         else if(distance<=4096)
	         	bucket[14]++;
	         else if(distance<=8192)
	         	bucket[15]++;
	         else
	         	bucket[16]++;
	         

	         count++;
	         System.out.println(count + " Distance: " + distance );

	      }
	      //after processing

	      int sum=0;
	      for(int i:bucket)
	      	sum+=i;
	      System.out.println("Bucketed " + sum + " distances");


	      PrintWriter writer = new PrintWriter("links_distance_histogram.csv","UTF-8");
	      writer.println("0     ," + bucket[0]);
	      writer.println("0.1   ," + bucket[1]);
	      writer.println("1     ," + bucket[2]);
	      writer.println("2     ," + bucket[3]);
	      writer.println("4     ," + bucket[4]);
	      writer.println("8     ," + bucket[5]);
	      writer.println("16    ," + bucket[6]);
	      writer.println("32    ," + bucket[7]);
	      writer.println("64    ," + bucket[8]);
	      writer.println("128   ," + bucket[9]);
	      writer.println("256   ," + bucket[10]);
	      writer.println("512   ," + bucket[11]);
	      writer.println("1024  ," + bucket[12]);
	      writer.println("2048  ," + bucket[13]);
	      writer.println("4096  ," + bucket[14]);
	      writer.println("8192  ," + bucket[15]);
	      writer.println("12382 ," + bucket[16]);
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