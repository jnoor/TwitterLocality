import twitter4j.*;
import java.sql.*;
import java.util.Date;
import java.io.*;


public class GroupByFollowers {
	
	static double earthRadius = 3958.75;
	

	public static void main(String[] args) {
	   Connection conn = null;
	   Statement stmt1 = null;
	   try{
	      //STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			//conn = DriverManager.getConnection(DB_URL,USER,PASS);
			conn = DriverManager.getConnection("jdbc:mysql://localhost/minitwitter?user=root&password=");

			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			
			String sql;
			sql = "SELECT followers, distance FROM Nodes N inner join Links L on N.sn = L.toSN order by distance";
			
			System.out.println("Executing Query for: #followers...");
			stmt1 = conn.createStatement();
			ResultSet rs = stmt1.executeQuery(sql);
			System.out.println("Got results! Starting...");

			//PrintWriter writer1 = new PrintWriter("data/followers/in/1_data.csv","UTF-8");
			//PrintWriter writer10 = new PrintWriter("data/followers/in/10_data.csv","UTF-8");
			//PrintWriter writer100 = new PrintWriter("data/followers/in/100_data.csv","UTF-8");
			//PrintWriter writer500 = new PrintWriter("data/followers/in/500_data.csv","UTF-8");
			//PrintWriter writer1000 = new PrintWriter("data/followers/in/1000_data.csv","UTF-8");
			PrintWriter writer10000 = new PrintWriter("data/followers/in/10000_data.csv","UTF-8");
			//PrintWriter writer100000 = new PrintWriter("data/followers/in/100000_data.csv","UTF-8");
			//PrintWriter writer1000000 = new PrintWriter("data/followers/in/1000000_data.csv","UTF-8");
			//PrintWriter writer10000000 = new PrintWriter("data/followers/in/10000000_data.csv","UTF-8");

			int[] bin = new int[9];
			for(int i:bin) i=0;
	

			int count=0;
			int everyOther1000=0;
			int every5_500=0;

			while(rs.next()){ 
				int followers = rs.getInt("followers");
				double distance = rs.getDouble("distance");
				count++;

				if(followers < 1)
				{
					bin[0]++;
				//	writer1.println(bin[0]+","+distance);
				}
				else if(followers < 10)
				{
					bin[1]++;
				//	writer10.println(bin[1]+","+distance);
				}
				else if(followers < 100)
				{
					bin[2]++;
				//	writer100.println(bin[2]+","+distance);
				}
				else if(followers < 500)
				{
					bin[3]++;
					every5_500++;
					if(every5_500==5)
					{
				//		writer500.println(bin[3]+","+distance);
						every5_500=0;
					}
				}
				else if(followers < 1000)
				{
					bin[4]++;
					everyOther1000++;
					if(everyOther1000==2){
					//	writer1000.println(bin[4]+","+distance);
						everyOther1000=0;	
					}
					
				}
				else if(followers < 10000)
				{
					bin[5]++;
					writer10000.println(bin[5]+","+distance);
				}
				else if(followers < 100000)
				{
					bin[6]++;
					//writer100000.println(bin[6]+","+distance);
				}
				else if(followers < 1000000)
				{
					bin[7]++;
					//writer1000000.println(bin[7]+","+distance);
				}
				else if(followers < 10000000)
				{
					bin[8]++;
					//writer10000000.println(bin[8]+","+distance);
				}
				System.out.println(count + " Followers: " + followers );
			}



			//writer1.close();
			//writer10.close();
			//writer100.close();
			//writer500.close();
			//writer1000.close();
			writer10000.close();
			//writer100000.close();
			//writer1000000.close();
			//writer10000000.close();
	
			System.out.println("Followers < 1: "+ bin[0]);
			System.out.println("Followers < 10: "+ bin[1]);
			System.out.println("Followers < 100: "+ bin[2]);
			System.out.println("Followers < 500: " + bin[3]);
			System.out.println("Followers < 1000: "+ bin[4]);
			System.out.println("Followers < 10000: "+ bin[5]);
			System.out.println("Followers < 100000: "+ bin[6]);
			System.out.println("Followers < 1000000: "+ bin[7]);
			System.out.println("Followers < 10000000: "+ bin[8]);

			rs.close();
	     


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