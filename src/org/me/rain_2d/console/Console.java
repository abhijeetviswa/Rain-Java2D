package org.me.rain_2d.console;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Console
{
	static List<ConsoleListener> listeners = new ArrayList<ConsoleListener>();
	static Thread thConsoleListener;
	static boolean continueListening;
	
	public static void startListening()
	{
		continueListening = true;
		thConsoleListener = new Thread()
		{
			public void run()
			{
				Scanner scanner = new Scanner(System.in);
				while(continueListening)
				{
					if (scanner.hasNext())
					{
						for (ConsoleListener listener:listeners)
						{
							listener.ConsoleInput(scanner.nextLine());
						}
					}
				}
				scanner.close();
			}
		};
		thConsoleListener.start();
	}
	
	public static void stopListening()
	{
		continueListening = false;
	}
	
	public static void addConsoleListener(ConsoleListener listener)
	{
		if (listener == null) return;

		listeners.add(listener);
	}
}
