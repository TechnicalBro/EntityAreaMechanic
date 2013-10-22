package com.caved_in.EntitySpawningMechanic.Handlers.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ChatHandler
{
	/*
	public static String formatColorCodes(String str)
	{
		char[] chars = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
				'a', 'b', 'c', 'd', 'e', 'f', 'n', 'r', 'l', 'k', 'o', 'm'};
		char[] array = str.toCharArray();
		for (int t = 0; t < array.length - 1; t++)
		{
			if (array[t] == '&')
			{
				for (char c : chars)
				{
					if (c == array[t + 1])
					{
						array[t] = '�';
					}
				}
			}
		}
		return new String(array);
	}
	*/
	
	public static String formatColorCodes(String stringToFormat)
	{
		return ChatColor.translateAlternateColorCodes('&',stringToFormat);
	}
	public static void sendMessageToConsole(String Message)
	{
		Bukkit.getConsoleSender().sendMessage(Message);
	}
}