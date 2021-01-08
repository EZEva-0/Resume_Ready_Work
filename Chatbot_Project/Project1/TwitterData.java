/*
 * Evan Jon Branco
 * EJB180000
 * CS 2336.501
 * 
 */

/*
 * in order to acquire trending tags, you must pass an authentication key, which you generate. In order to do this, twitter must give you access to the key generation app
 * Twitter has not yet responded to my request for developer access, and, given the national emergency, it seems unlikely they will respond in time for the project to be completed.
 * Thus, I have elected to create a json object in the same format as what would be given from a proper api call, and to use that instead. 
 * I understand that this is not ideal, but it is out of my control, and I hope there will be leniency given the national situation. 
 * I am using the example json object given from the twitter api website, and I am not reformatting it in any way. Therefore, I still have to make a gson object and create object classes
 * that are properly build to hold the formatted json object as java objects
 * the manipuluation of json objects once the request is made. In short, please don't dock points please I work very hard thank youuuuuuu.
 * */

public class TwitterData 
{
	Trends trends[];
	
	public Trends getElement(int i)
	{
		return this.trends[i];
	}
}