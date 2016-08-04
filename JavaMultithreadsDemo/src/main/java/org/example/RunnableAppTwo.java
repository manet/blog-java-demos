package org.example;

import java.util.Date;
import java.util.Random;

import org.example.data.AssetData;

import com.my.lightlogger.Logger;

public class RunnableAppTwo extends Thread {

	private Thread thread;
	private String threadName;
	
	private final Random random = new Random();

	public RunnableAppTwo(String name) {
		this.threadName = name;
		Logger.debug("Creating thread: " + name);
	}

	@Override
	public void run() {
		Date lastModified = new Date(AssetData.getInstance().getFreeTextFile().lastModified());
		Date newModified = lastModified;
		int counts = 0;
		boolean alive = true;
		
		// busy spinning. 
		do {
			if (lastModified.equals(newModified)){
				Logger.debug(this.threadName + "-" + counts + ": " + AssetData.getInstance().getFreeTextContent());
				counts++;
			}
			if (counts == 20){
				alive = false;
				Logger.debug("Bye!");
			}
			this.doSleep(1000 + random.nextInt(1000));
		} while (alive);
	}
	
	private void doSleep(long duration){
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start() {
		if (thread == null) {
			thread = new Thread(this, this.threadName);
			thread.start();
		}
	}

}
