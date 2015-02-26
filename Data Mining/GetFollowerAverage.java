import twitter4j.*;
import java.sql.*;
import java.util.Date;



public class GetFollowerAverage {

	public static void main(String [] args) throws TwitterException, SQLException{

		//set up DB Connection
		Connection con;
		PreparedStatement userStmt, connStmt;

		
		con = DriverManager.getConnection("jdbc:mysql://localhost/minitwitter?user=root&password=");
		//Statement stmt = con.createStatement();
		System.out.println("Created JDBC Connection");
	
			
		

		//set up Twitter Steam Connection
		Twitter twitter = TwitterFactory.getSingleton();

		StatusListener listener = new StatusListener(){
			
			int count=0;
			double average=0;
			public void onStatus(Status status) {
					//GeoLocation loc = status.getGeoLocation();
					
				

					//if(loc != null && status.getUser() != null) 
					//{	
						User u = status.getUser();
									
							
						int followers = u.getFollowersCount();
						
						average=((average*count)+followers)/(count+1);
						count++;
						System.out.println(count + "," + followers + ", AVG: " + average);
						
					//}
						

			}
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
			public void onStallWarning(StallWarning warning){
				System.out.println("Stalling.......");
			}
			public void onScrubGeo(long userId, long upToStatusId)
			{
				System.out.println("Location Deletion Messages????");
			}
		};


		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		System.out.println("Created Twitter Stream");
		
		

		twitterStream.addListener(listener);
		//twitterStream.filter(fq);
		twitterStream.sample();
		System.out.println("Starting Stream......");

		

	}
}