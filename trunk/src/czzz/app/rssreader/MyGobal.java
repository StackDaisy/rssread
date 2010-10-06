package czzz.app.rssreader;

import czzz.app.rssreader.data.OPMLfeed;
import czzz.app.rssreader.sax.OPMLHandler;
import android.app.Application;

public class MyGobal extends Application {
	
	private static OPMLfeed feed;
	private static OPMLHandler hand; 	

	public static void setFeed(OPMLfeed feed) {
		MyGobal.feed = feed;
	}

	public static OPMLfeed getFeed() {
		return feed;
	}

	public static void setHand(OPMLHandler hand) {
		MyGobal.hand = hand;
	}

	public static OPMLHandler getHand() {
		return hand;
	}
	
	
	

}
