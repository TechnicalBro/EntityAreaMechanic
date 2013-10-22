package com.caved_in.EntitySpawningMechanic.Handlers.Entity;

import java.util.Random;

public class EntityCalculations
{
	public static int generateHealth(int EntityLevel, boolean isElite, boolean isBoss)
	{
		short Multiplier = 1;
		Multiplier += (isElite == true ? 2 : 0);
		Multiplier += (isBoss == true ? 4 : 0);
		int BaseHealth = (int)(10 + Math.pow(EntityLevel, 1.5)*5);
		int Health = (int)(vrtns(BaseHealth, BaseHealth/20));
		return Health*Multiplier;
	}

	//Calculates the variations in health of mobs that are the same lvl
	public static double vrtns(double Base, double Range)
 	{
		double Vary;
		Random Bools = new Random();
		boolean AddSub = Bools.nextBoolean();
		if (AddSub == true)
		{
			Vary = (Base + Range*Bools.nextDouble());
		}
		else
		{
			Vary = (Base - Range * Bools.nextDouble());
		}
		return Vary;
	}
}



