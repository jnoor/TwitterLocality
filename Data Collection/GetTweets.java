import twitter4j.*;
import java.sql.*;
import java.util.Date;



public class GetTweets {

	public static void main(String [] args) throws TwitterException, SQLException{

		//set up DB Connection
		Connection con;
		PreparedStatement userStmt, connStmt;

		
		con = DriverManager.getConnection("jdbc:mysql://localhost/minitwitter?user=root&password=");
		//Statement stmt = con.createStatement();
		System.out.println("Created JDBC Connection");
		userStmt = con.prepareStatement("insert into Users_weekend values (?, ?, ?, ?)");
		connStmt = con.prepareStatement("insert into Connections values (?, ?, ?)");
			
		

		//set up Twitter Steam Connection
		Twitter twitter = TwitterFactory.getSingleton();

		StatusListener listener = new StatusListener(){
			public void onStatus(Status status) {
					GeoLocation loc = status.getGeoLocation();
					int count=0;
					try{

						if(loc != null && status.getUser() != null) 
						{
							User u = status.getUser();
							String sn = status.getUser().getScreenName();
							//add user to Users table
							userStmt.setString(1, sn);
							userStmt.setInt(2, u.getFollowersCount());
							userStmt.setDouble(3, loc.getLatitude());
							userStmt.setDouble(4, loc.getLongitude());
							userStmt.executeUpdate();
							count=1;

							//check mentions && if reply
							UserMentionEntity[] mentions = status.getUserMentionEntities();
							String replySN = status.getInReplyToScreenName();

							//check to see if this is a reply or retweet
							if(status.isRetweet() || mentions.length != 0 || replySN != null)
							{
								String toSN;
								connStmt.setString(1, sn);
								String d = status.getCreatedAt().toString();

								System.out.println("DATE: " + d); count=0;
								connStmt.setTimestamp(3, new Timestamp(status.getCreatedAt().getTime()));


								if(status.isRetweet())
								{
									count = 2;
									Status rt = status.getRetweetedStatus();
									connStmt.setString(2, rt.getUser().getScreenName()); //potential bug if user is null
									connStmt.executeUpdate();
									onStatus(rt);
								}
								if(replySN != null)
								{
									count = 3;
									connStmt.setString(2, replySN);
									connStmt.executeUpdate();
								}
								for(UserMentionEntity usr : mentions)
								{
									count++;
									connStmt.setString(2, usr.getScreenName());
									connStmt.executeUpdate();
								}	
							}
						}
					} catch (SQLException e)
					{
						if(e.getErrorCode() == 1062) System.out.println("Ignoring Duplicate, SQL error: " + count);
						else e.printStackTrace();
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