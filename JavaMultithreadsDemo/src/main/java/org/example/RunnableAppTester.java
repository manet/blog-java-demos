package org.example;

public class RunnableAppTester {
	public static void main (String...strings){
		RunnableAppOne app1Thread1 = new RunnableAppOne("App1.Thread.1");
		app1Thread1.start();
		
		RunnableAppOne app1Thread2 = new RunnableAppOne("App1.Thread.2");
		app1Thread2.start();
		
		RunnableAppTwo app2Thread1 = new RunnableAppTwo("App2.Thread.1");
		app2Thread1.start();
	}
}
