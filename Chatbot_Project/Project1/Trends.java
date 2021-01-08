/*
 * Evan Jon Branco
 * EJB180000
 * CS 2336.501
 * 
 */

/*
 * this is the object that actually stores tag data
 * */
public class Trends 
{	
	private String name; // tag name
	private String url; // tag url
	private boolean promoted; // is this twitter promoted?
	private String query; // query string, for other requests
	private int volume; // number of tweets


	public String getName()
	{
		return this.name;
	}
	
}
