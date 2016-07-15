package com.my.demos.sortObjectList;

import java.io.FileReader;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.my.utils.Lilog;

public class SorterTest {
	
	public void doSort() throws Exception {
		Gson gson = new Gson();
		Users gusers = gson.fromJson(new FileReader("resources/data/users.json"), Users.class);
		List<User> users = gusers.getUsers();
		Lilog.debug(">> Sorting array size of " + users.size());
		
		// sort by name
		Lilog.debug("Sort by name");
		Collections.sort(users, User.compareName);
		for (User user:users){
			Lilog.debug(user.getName() + ", " + user.getRegdate());
		}
		Lilog.debug("---");
		
		// sort by date
		Lilog.debug("Sort by date");
		Collections.sort(users, User.compareDate);
		for (User user:users){
			Lilog.debug(user.getName() + ", " + user.getRegdate());
		}
	}

	public static void main(String[] args) {
		SorterTest app = new SorterTest();
		try {
			app.doSort();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
