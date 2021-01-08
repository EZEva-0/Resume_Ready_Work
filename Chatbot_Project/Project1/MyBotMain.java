/*
 * Evan Jon Branco
 * EJB180000
 * CS 2336.501
 * 
 */

import org.jibble.pircbot.*;

public class MyBotMain {
    
    public static void main(String[] args) throws Exception {
        
        // Now start our bot up.
        MyBot bot = new MyBot();
        
        // Enable debugging output.
        bot.setVerbose(true);
        
        // Connect to the IRC server.
        bot.connect("irc.freenode.net");

        //bot.
        // Join the #pircbot channel.
        bot.joinChannel("#Ezrah");
        bot.sendMessage("#Ezrah", "Howdy! I'm Ezrah, a chat bot build by Evan Jon Branco for CS 2336.501!");
        bot.sendMessage("#Ezrah", "Enter any message and I'll respond to the best of my ability!");
        
    }
    
}