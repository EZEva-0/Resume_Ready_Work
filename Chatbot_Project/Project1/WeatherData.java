/*
 * Evan Jon Branco
 * EJB180000
 * CS 2336.501
 * 
 */

public class WeatherData //The weather api I use returns 6 objects and one array, as well as a number of isolated data elements. 
{//	However, I don't have to build classes for all of the objects, as Gson will helpfully ignore any json objects that cannot be converted to java objects. 
	//The same extends to json arrays, but not isolated json elements not in objects or arrays. Thus, while I can eliminated any objects or arrays I dont need, with my current implementation, I will have some unnecessary variables. 
	//Of course, there are also varibles contained in the objects that I do not use either, so I also need to learn how to remove fields I do not care about or skip fields. Something to learn for version 2.0.
	
	//weather object
	private WeatherData_Weather weather[]; // this is an array because certain weather phenomena may not occur, and thus fewer elements may be passed if nothing is happening. I do not have error handling for this, as it is uncommon.
	// Version 2.0 must account for this however.
	
	//base, used to idenifty the weather station, unused in this program, but cannot be skipped using my current understanding of json. NOTE// Learn how to ignore this in version 2.0
	private String base;
	
	// Object containing temperature and other weather data
	private WeatherData_main main;
	
	//visibility - not needed for this project, but I can't access the following elements without reading this. (at least with my current understanding. Always learning!)
	private String visibility;
	//dt - same as above
	private String dt;

	
	//City data elements
	private String timeZone;
	private String id_city;
	private String name; // only this is used
	
	// connection element
	public int cod; // this is used to determine if an error has occured, such as an invalid zip code was entered.
	
	@Override
    public String toString()
	{ //note, I would prefer to do this with indentation, freenode does not have such functionality to the best of my understanding, so the bot can't indent. Oh well.
		return "The weather in " + name + " is " + Math.round(9.0/5.0 * (main.getTemp() - 273) + 32) + " degrees fahrenheit. It feels like "+ Math.round(9.0/5.0 * (main.getFeels() - 273) + 32) +" however, with a humidity of "+ main.getHumidity() + "%. The weather conditions are " + weather[0].getMain();
		//note, yes, we do round to the nearest degree, as is standard. Expect some incorrectness. Especially if you use google, it seems to round differently then weather.com. I can be certain we will be accurate to within one degree.
	} 
}
