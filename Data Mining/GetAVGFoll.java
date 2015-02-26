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
		userStmt = con.prepareStatement("insert into Users_weekend values (?, ?, ?, ?)");
		connStmt = con.prepareStatement("insert into Connections2 values (?, ?, ?)");
			
		

		//set up Twitter Steam Connection
		Twitter twitter = TwitterFactory.getSingleton();

		StatusListener listener = new StatusListener(){
			public void onStatus(Status status) {
					GeoLocation loc = status.getGeoLocation();
					int count=0;
					long sum=0;
				

					if(loc != null && status.getUser() != null) 
					{	
						User u = status.getUser();
									
							
						int followers = u.getFollowersCount();
						count++;
						sum+=followers;
						
						System.out.println(sum/followers);
						
					}
						

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

		double[][] filtergeo = {{-180,-90},{180,90}};
		FilterQuery fq = new FilterQuery();
		fq.locations(filtergeo);

		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		System.out.println("Created Twitter Stream");
		
		

		twitterStream.addListener(listener);
		twitterStream.filter(fq);
		twitterStream.sample();
		System.out.println("Starting Stream......");








		// Query query = new Query("RT");
		// query.setCount(1);
		// GeoLocation geo = new GeoLocation(34.0678340, -118.450705); //519 Landfair Ave
		// query.setGeoCode(geo, 20, Query.MILES); 
		// GeoQuery gq = new GeoQuery(geo);
		// ResponseList<Place> mygeo = twitter.reverseGeoCode(gq);
		// ResponseList<Place> mygeo2 = twitter.searchPlaces(gq);

		// twitter.searchUsers("test", page#)
		// twitter.showUser(userID | "screenname")

		// QueryResult result = twitter.search(query);
		

	}
}