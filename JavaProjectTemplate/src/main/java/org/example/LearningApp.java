package org.example;

import java.util.Collections;
import java.util.List;

import org.example.data.AssetData;
import org.example.data.Person;

import com.my.lightlogger.Logger;

/**
 * Starter class where your focus begins
 * 
 * @author [You Name]
 *
 */
public class LearningApp {
	
	/**
	 * Add code that matters to your learning
	 * @return
	 */
	public boolean someMethod() {
		return true;
	}
	
	/**
	 * Start code that matters to your learning
	 * 
	 * @param args
	 */
	public static void main(String...args){
		
		// Knowing that some data is ready to use
		List<Person> people = AssetData.getInstance().getPeople();
		
		// Learn how Collections.shuffle works
		Collections.shuffle(people);
		
		// Learn the enhanced for loop syntax
		for (Person person:people){
			Logger.debug(person.getId() + ":" +person.getFirstName());
		}
		
	}
	
}
