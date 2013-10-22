package com.caved_in.EntitySpawningMechanic.Handlers.Utilities;

import java.util.Random;

public class NumberUtils
{
	public static int getRandomInRange(int Minimum, int Maximum)
	{
		return new Random().nextInt((Maximum - Minimum) + 1) + Minimum;
	}
	
	public static boolean percentCheck(int Percentage)
	{
		return new Random().nextInt(101) <= Percentage;
	}
}
