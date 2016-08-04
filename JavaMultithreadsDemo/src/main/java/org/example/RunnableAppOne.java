package org.example;

import java.util.List;
import java.util.Random;

import org.example.data.AssetData;
import org.example.data.Person;

import com.my.lightlogger.Logger;

/**
 * Create Thread class by implements Runnable.
 * 
 * @author Manet Yim
 *
 */
public class RunnableAppOne implements Runnable {

	// declare Thread object
	private Thread thread;
	// give it a name
	private String threadName;
	// misc: use for random sleep
	private final Random random = new Random();

	/**
	 * Set thread name
	 * 
	 * @param name
	 */
	public RunnableAppOne(String name) {
		this.threadName = name;
		Logger.debug("Creating thread: " + name);
	}

	@Override
	public void run() {
		List<Person> people = AssetData.getInstance().getPeople();
		for (Person person : people) {
			Logger.debug(this.threadName + ": " + person.getId() + ": " + person.getFirstName());
			this.sleep(100 + random.nextInt(100));
		}
	}

	private void sleep(long duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method will check and run this thread as new process if it hasn't
	 * been running.
	 */
	public void start() {
		if (thread == null) {
			thread = new Thread(this, this.threadName);
			thread.start();
		}
	}

}
