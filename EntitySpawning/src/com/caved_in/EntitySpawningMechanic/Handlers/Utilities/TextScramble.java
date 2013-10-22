package com.caved_in.EntitySpawningMechanic.Handlers.Utilities;

import java.util.*;

public class TextScramble
{
	public static String Scramble(String Text)
	{
		StringBuilder Scrambled = new StringBuilder();
		if (Text.contains(" "))
		{
			String[] Words = Text.split(" ");
			for(String word : Words)
			{
				ArrayList<Character> chars = new ArrayList<Character>(word.length());
				for (char c : word.toCharArray())
				{
				   chars.add(c);
				}
				Collections.shuffle(chars);
				char[] shuffled = new char[chars.size()];
				for ( int i = 0; i < shuffled.length; i++ )
				{
				   shuffled[i] = chars.get(i);
				}
				Scrambled.append(" ").append(shuffled);
			}
			return Scrambled.toString();
		}
		else
		{
			ArrayList<Character> chars = new ArrayList<Character>(Text.length());
			for (char c : Text.toCharArray())
			{
			   chars.add(c);
			}
			Collections.shuffle(chars);
			char[] shuffled = new char[chars.size()];
			for ( int i = 0; i < shuffled.length; i++ )
			{
			   shuffled[i] = chars.get(i);
			}
			return new String(shuffled);
		}
	}
}
