package com.my.demos.sortObjectList;

import java.util.Comparator;
import java.util.Date;

public class User {

	String name;
	Integer age;
	String location;
	Date regdate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}

	public static Comparator<User> compareDate = new Comparator<User>() {
		@Override
		public int compare(User u1, User u2) {
			Date date1 = u1.getRegdate();
			Date date2 = u2.getRegdate();
			return date1.compareTo(date2);
		}
	};
	
	public static Comparator<User> compareDateDecending = new Comparator<User>() {
		@Override
		public int compare(User u1, User u2) {
			Date date1 = u1.getRegdate();
			Date date2 = u2.getRegdate();
			return date2.compareTo(date1);
		}
	};	

	public static Comparator<User> compareName = new Comparator<User>() {
		@Override
		public int compare(User u1, User u2) {
			String user1 = u1.getName().toUpperCase();
			String user2 = u2.getName().toUpperCase();
			return user1.compareTo(user2);
		}
	};
	
	public static Comparator<User> compareNameDecending = new Comparator<User>() {
		@Override
		public int compare(User u1, User u2) {
			String user1 = u1.getName().toUpperCase();
			String user2 = u2.getName().toUpperCase();
			return user2.compareTo(user1);
		}
	};

	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + ", location=" + location + ", regdate=" + regdate + "]";
	}
	
}