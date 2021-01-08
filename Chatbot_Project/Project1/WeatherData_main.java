/*
 * Evan Jon Branco
 * EJB180000
 * CS 2336.501
 * 
 */
public class WeatherData_main 
{
	private double temp;
	private double feels_like;
	private double temp_min;
	private double temp_max;
	private String pressure;
	private double humidity;
	
	public double getTemp() 
	{
	    return this.temp;	
    }
	public double getFeels() //it should be named getFeels_like, but is find the current name wholesome. This is a friendly chatbot after all :)
	{//Yes I know I should be more professional, but this is my project Professor! I will be livid if you dock points.
	    return this.feels_like;	
    }
	public double getHumidity() 
	{
	    return this.humidity;	
    }
}
