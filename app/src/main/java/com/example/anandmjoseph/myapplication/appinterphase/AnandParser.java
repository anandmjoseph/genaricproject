/**
 * Copyright (C) 2015-2016 Anand M Joseph .
 */
package com.example.anandmjoseph.myapplication.appinterphase;

/**
 * Interface for parsing the Feeds. Created by anandmjoseph Created on 30/11/17.
 */
public interface AnandParser {

	/**
	 * Initialise any data structures used during parsing.
	 */
	void init();

	void init(Object obj);

	/**
	 * Method to parse the given FeedModel (XML / JSON). Returns the resultant of feedModel. Eg: Arraylist of Nodes, Vector, Map etc.
	 *
	 * @return
	 */
	Object parse();

	/**
	 * Method to validate the given feedModel to verify if its parsable.
	 *
	 * @return true - if parsable else False.
	 */
	boolean isFeedParsable();
}
