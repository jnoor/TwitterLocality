import java.net.*;
import java.io.*;


public class tlocality {
	
  static String apiKeyStr = "Rah3elQx9UdjQCfl2CNnBOUs4";
  static String apiSecretStr = "bMI9g4aQJ9CGIa2n4PQyfKTjqnplVaHH6xd7WZW2J0J4MYgjLA";
  static String accessTokenStr = "28293717-4hWpQanpcQWVdrvaXmEKpDqnBJtj2laRAt2WCnkFM";
  static String accessTokenSecretStr = "lB58NGGz55YJeeG21HLRhTeZbVRoP9r8a6BF5JlB1YhBp";
 
 
  public static void main(String[] args) {
  	HttpURLConnection connection = null;
	try{  
	    String target = "http://api.twitter.com/1.1/search/tweets.json?q=coachella&count=1";
	    URL url = new URL(target);

	    connection = (HttpURLConnection)url.openConnection();
	 	connection.setRequestMethod("GET");
	 	connection.setDoInput(true);
	 	connection.setDoOutput(true);

	 	DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
	 	wr.writeBytes("");
	 	wr.flush();
	 	wr.close();

	 	
		InputStream is = connection.getInputStream();

	 	
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;

		StringBuffer response = new StringBuffer(); 
		while((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
	  	}
		rd.close();
		System.out.println(response.toString());
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	finally{
	  	if(connection != null) connection.disconnect();
	}
  }

}