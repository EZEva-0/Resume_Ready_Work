/*
 * Evan Jon Branco
 * EJB180000
 * CS 2336.501
 * 
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.google.gson.Gson;

import org.jibble.pircbot.*;
public class MyBot extends PircBot {
    
    public MyBot() {
        this.setName("Ezrah_Bot");
        
    }
    public String[] parseString(String message) // parses the string, removing whitespace and punctuation, leaving only words.
    {
    	//the following line converts the message into an array of words, removing all punctuation and capitalization.
    	String[] words = message.replaceAll("\\p{Punct}", "").toLowerCase().split(" "); // the split method splits the array from the given delimiter    	
    	return words; // return the list for parsing
    }
    private String getFakeTwitterJson() throws IOException // used to get json objects from file, instead of url connection
    {
    	String json = "";
    	// this class reads the json object from a file, instead of from the api call, to simulate a twitter trending tags api call, more information at TwitterData.java
    	File file = new File("twitter_fake.txt"); 
    	//The following code is used to manually convert a json object to a string,             
		BufferedReader in = new BufferedReader(new FileReader(file)); // create a buffered reader to get lines of the json return
		String inputLine; // we read the json object one line at a time
		StringBuffer content = new StringBuffer(); // we then acquire the content of each line to get the actual text
		while ((inputLine = in.readLine()) != null) //finaly, we read each line one at a time
		{
			content.append(inputLine); // at add to content string the json text
		}
		in.close();	//then we close the bufferedreader

		json = content.toString(); //and finally, convert the stringbuffer to a string, so we have a json object string
    	return json;
    }
    public String doRequest(String finalURL) // This method will use the given keyword to search the array for more keywords, in an effort to produce an api call using given user data.
    {
    	String json = ""; //api requests will return JSON objects, which are converted into a string and then passed to the Gson object to convert to a java object
    	//finalURL is the url string which will be passed to the url object on creation. The url changes based on user input, and is determined in elsewhere
    	URL url; // this is the url object for http requests, it chances based on user input. It comes from the java.net.url class
    	
    	try //this try catch block is a uniform http url request logic, usable with all get requests I have included in this project.
		{//First, is creates a url and attempts to establish a connection. If it fails, an error is thrown and caught. 
			if(finalURL.equals("trends"))
			{
				// as I am unable to conduct an api call because twitter did not give me developer access, (more info in TwitterData.java), I have instead taken the example json onject on their reference site. 
				json = getFakeTwitterJson();
			}
			else 
			{
				url = new URL(finalURL);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET"); // We then specify the time of request, in our case, all requests for this project are get requests. 
            
				int status = con.getResponseCode(); //This is used for debugging, it returns 200 when a connection is established.
				System.out.println(status); //In the event a connection is not established or the https address is invalid, this will stop the json creation process and return an error code
				if(status == 404) //if the connection is sound, but the didnt get enough data to complete a request
				{
					return "404";
				}
				else if(status == -1) // if a connection error occurs / something real bad happened
				{
					return "1";
				}
				//The following code is used to manually convert a json object to a string,             
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())); // create a buffered reader to get lines of the json return
				String inputLine; // we read the json object one line at a time
				StringBuffer content = new StringBuffer(); // we then acquire the content of each line to get the actual text
				while ((inputLine = in.readLine()) != null) //finaly, we read each line one at a time
				{
					content.append(inputLine); // at add to content string the json text
				}
				in.close();	//then we close the bufferedreader
				con.disconnect();   //and disconnect from the url
				json = content.toString(); //and finally, convert the stringbuffer to a string, so we have a json object string
			}
		} 
		catch (MalformedURLException e)// if the given url doesn't work, we print a stack trace for debugging
		{				
			e.printStackTrace();
		} 
		catch (IOException e) //same as above, but only if the url connection fails
		{
			e.printStackTrace();
		}
		
		return json;
    }
    
    public ArrayList<String> getRequestAddress(String[] words, String keyword) // this method will attempt to parse the user input to acquire a specific rest request. 
    { // If not enough information is gathered to conduct a request, we pass a specific string unique to the combination of the attempted request and the missing information.
    	// The onMessage method includes logic to check for these error strings, and respond appropriately, canceling the http request and sending the user and error message with some guidance on what to do correctly 
    	
    	ArrayList<String> validURLs = new ArrayList<String>(); // at worst / best, every single word is a valid keyword, capable of satisfying an api call on it's own
    	//thus, we create an array to hold them all, so we can store each 
    	
    	String url =""; //
		if(keyword.contentEquals("weather")) //if this method is being called with the weather keyword, thus, we only care about the weather api call functionaliity.
		{
			//now we look for city, zip, state, country - valid keywords.
			//Originally, I attempted to make use of the complete functionality of the open weather api, which can find the weather based on city name, zip, state and country. 
			//For the sake of my sanity, I have chosen to not conduct natual language processing to find pronouns/acronmys of states/countries and compare them to words[].
			//so, instead, we are only looking for one pronoun, or a 5 digit number, for the city name and zip code.    			
			
			//The zip code is easy, look through the words[] to find a string that is both 5 digits long and comprised of only numbers.
			for(String zip: words)
			{
				if (zip.matches("[0-9]+") && zip.length() == 5) // This looks for a string that is comprised of only numbers and is length 5
				{
					// Thus, we have a potentially valid zip code!
					// Note, if the zip code is invalid, we will handle that in the doRequest method
    					
					//Create the api address, then place it in the array, and increment the zipPlace for the next address.
					url = "https://api.openweathermap.org/data/2.5/weather?zip=" + zip + "&units=imperal&appid=d32dda7ba1a7a73ad2f1e77f4c552339";
					validURLs.add(url);
				}
			}
			//in the case of a city name, it's a bit more difficult, without language processing or other fancy stuff, there isnt a better solution then to attempt to call the api with every word in the string and see what works
			// Of course, this solution is bad, both in the possibility it will double up on zip code url's, and in that it is just mean to intentionally make lots of bunk calls. In version 2.0, try to see if there is a better solution
			for(String possibleCity: words)
			{
				if(words.length == 1) // if the only word is the weather keyword, pass the bunk address to force the error message to occur. Otherwise, only call the error message for each incorrect addreess // zip code entered by the user. (once per other word)
				{
					url = "https://api.openweathermap.org/data/2.5/weather?q="+possibleCity+"&appid=d32dda7ba1a7a73ad2f1e77f4c552339";

				}
				if(possibleCity.equalsIgnoreCase("weather") == false)
				{
					if(possibleCity.matches("[0-9]+") == false || possibleCity.length() != 5) // used to prevent putting zip codes in as cities, which results in funcky api calls that do work, but give bad data
					{
						url = "https://api.openweathermap.org/data/2.5/weather?q="+possibleCity+"&appid=d32dda7ba1a7a73ad2f1e77f4c552339";
						validURLs.add(url); // i know the name of the array is quite odd at the moment, as we are intentionally giving it probable bunk urls, but, since a bunk url is not a fatal error, we can stop the process and send the user an error message.
					}
				}						

			}
			
		}		
		else if(keyword.contentEquals("trends")) // get top 10 twitter trending tags!
		{
			//url = "https://api.twitter.com/1.1/trends/place.json?id=1"; // this is the twitter api call for trending tags, (remember to genertate keys)
			// because twitter didnt validate my developer request by the time the project was due, I am forced to create a fake list of trending tags. More information in TwitterData.java
			url = "trends";
			validURLs.add(url);
		}
		else if(keyword.contentEquals("trivia")) // get a random trivia fact
		{
			//this will return a trivia object!
			url = "https://uselessfacts.jsph.pl//random.json?language=en"; // uses the uselessfacts api
			validURLs.add(url);
		}
		
    	return validURLs;
    }
    
    public void onMessage(String channel, String sender, String login, String hostname, String message)
    {    	
    	String[] words = parseString(message); // pass message to string parse method, which converts the message into an array, removing excess whitespace, punctuation, and unneeded punctuation
    	//NOTE, expanding functionality requires additions to the getRequest method to handle new keywords, as well as the switch statement below, in addition to adding json to java object helper classes
    	ArrayList<String> urls; // this is the url string which will be passed to the doRequest method to actually acquire a json object
    	String json = ""; // this is the unformatted json string which needs to be converted to a java object. This will be done with Gson.
    	boolean failFlag = true; // if true, there was no valid keyword found, thus, an error needs to be called. This flag prevents calling the error more than once
    	Gson gson = new Gson();
    	
    	
    	for(int i = 0; i < words.length; i++) //loop through array to find keywords. Add keywords + code to the switch statement to add functionality
    	{
    		switch(words[i]) 
    		{
    			case "weather":
    				
    				failFlag = false;
    				urls = getRequestAddress(words,"weather"); // this method will parse the user input to determine which/if a REST request should be done, and thus, which addresses should be sent the the doRequest Method
    				for(String url: urls) // for each api request, test the request for errors, and, if it is successful, create a json object, parse, and send message to user
    				{	
    					json = doRequest(url); // call a get request, or print an error message if not enough information is given by user.
    					if(json.equals("404")) // if an error occurs (bunk url, there will be many)
    					{
    						sendMessage(channel, "I'm sorry, I am unable to locate the city/zip code you specified. Please check your spelling and try again?");
    					}
    					else if(json.equals("1")) // same as above
    					{
    						sendMessage(channel, "There appears to be an error with the server, or the connection to it. Could you please try again?");
    					}
    					else
    					{
    					//assuming all works, we call the gson object we created earlier to parse the json object to a java object and print it
    					WeatherData weatherObject = gson.fromJson(json, WeatherData.class); //this gson method will convert the json object to a defined java object, in this case, WeatherData
    					sendMessage(channel, weatherObject.toString()); // finally, we use the .toString method to format our output and send it to the irc server as a message. 
    					}
					}
    			break;
    			case "trends":
    				failFlag = false;
    				urls = getRequestAddress(words,"trends"); // this method will parse the user input to determine which/if a REST request should be done, and thus, which addresses should be sent the the doRequest Method
    				for(String url: urls) // for each api request, test the request for errors, and, if it is successful, create a json object, parse, and send message to user
    				{	
    					json = doRequest(url); // call a get request, or print an error message if not enough information is given by user.
    					if(json.equals("404")) // if an error occurs (bunk url, there will be many)
    					{
    						sendMessage(channel, "I'm sorry, I am unable to get the trending tags you specified. Please check your spelling and try again?");
    					}
    					else if(json.equals("1")) // same as above
    					{
    						sendMessage(channel, "There appears to be an error with the server, or the connection to it. Could you please try again?");
    					}
    					else
    					{
    						//assuming all works, we call the gson object we created earlier to parse the json object to a java object and print it
    						TwitterData[] twitterObject = gson.fromJson(json, TwitterData[].class); //this gson method will convert the json object to a defined java object, in this case, TwitterData
    						for(int o = 0; o < 10; o++) // loop 10 times, get the top ten tag obejects
    						{
    							sendMessage(channel, "#" + (o+1) + " " + twitterObject[0].getElement(o).getName()); // give the user one message per tag, to get a nice vertical list, as well as some time delay between each. 
    							//the reason we use tiwtterOObject[0] is because we do not need the other elements of data twitter gives us, so we always are left with a one element array when cutting out everything but trending tags
    						}
    					}
					}
    			break;
    			 case "trivia": // sends the user a hello back!
    				 failFlag = false;
     				urls = getRequestAddress(words,"trivia"); // this method will parse the user input to determine which/if a REST request should be done, and thus, which addresses should be sent the the doRequest Method
     				for(String url: urls) // for each api request, test the request for errors, and, if it is successful, create a json object, parse, and send message to user
     				{	
     					json = doRequest(url); // call a get request, or print an error message if not enough information is given by user.
     					if(json.equals("404")) // if an error occurs (bunk url, there will be many)
     					{
     						sendMessage(channel, "I'm sorry, I am unable to get the trending tags you specified. Please check your spelling and try again?");
     					}
     					else if(json.equals("1")) // same as above
     					{
     						sendMessage(channel, "There appears to be an error with the server, or the connection to it. Could you please try again?");
     					}
     					else
     					{
     						//assuming all works, we call the gson object we created earlier to parse the json object to a java object and print it
     						Trivia triviaObject = gson.fromJson(json, Trivia.class); //this gson method will convert the json object to a defined java object, in this case, TwitterData
     						sendMessage(channel, triviaObject.getText());
     					}
 					} 		    	
     		    break;
    		    case "hello": // sends the user a hello back!
    		    	failFlag = false;
    		    	sendMessage(channel, "Hey " + sender + "! ");
    		    break;
    		    case "time": // sends the user the current time
    		    	failFlag = false;
    		    	String time = new java.util.Date().toString();
    		    	sendMessage(channel, sender + ": The time is now " + time);
    		    break;
    		    case "help": // lists potential functionality, weather, twitter, 2 api's, but without explaining the specifics
    		    	failFlag = false;
    		    	sendMessage(channel, "I can tell you the time! \n");
    		    	sendMessage(channel, "Or the weather!");
    		    	sendMessage(channel, "Or whats trending on twitter!");
    		    	sendMessage(channel, "Or even some fun trivia!");
    		    	sendMessage(channel, "More detailed help provided via keyword helpfull");
    		    	sendMessage(channel, "Just let me know what I can do for you. :)");
      		    break;
    		    case "helpfull": // full help desk, explaining keywords to the user
    		    	failFlag = false;
    		    	sendMessage(channel, "Sending a message with the word 'time' will provide the current time.");
    		    	sendMessage(channel, "Sending a message with the word 'weather' triggers the search for a city name, state name, country name, and zipcode. Which keywords are found determines the result.");
    		    	sendMessage(channel, "This means you can get the weather by entering keywords corresponding to City, City/State, City/State/Country or Zipcode");
    		    	sendMessage(channel, "If not enough information is given to identify a specific location, a warning will be returned instead.");
    		    	sendMessage(channel, "Sending a message with the word 'trends' will provide the top 10 trending global twitter tags.");
    		    	sendMessage(channel, "Note, It will provide only top international trends.");
    		    	sendMessage(channel, "Sending a message with the word 'trivia' will return a random trivia fact!");
    		    	break;
    		    default:    		    	
    		}
    	}
    	//this will only occur if all words are parsed and it never executes any of the case code. Ie. If we cant find any keywords at all anywhere, we send this message. This stops it from sending multiple errors
		if(failFlag == true)
			   sendMessage(channel, "I'm terribly sorry, " + sender + ", but I do not understand. If you could try again please? If you need a reminder on what specifically I can do for you, just ask for help!");
    	//failFlag = true; // reset the fail flag for the next message
    }
}