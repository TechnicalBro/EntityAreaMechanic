package com.caved_in.EntitySpawningMechanic.Handlers.Utilities;

public class TimeUtils
{
	public static long getTimeInTicks(double Amount, TimeType Type)
	{
		switch (Type)
		{
			case Second:
				return (long)((20) * Amount);
			case Minute:
				return (long)((20 * 60) * Amount);
			case Hour:
				return (long)(((20 * 60) * 60) * Amount);
			default:
				return 0L;
		}
	}
	
	public static int getTimeInTicks(int Amount, TimeType Type)
	{
		switch (Type)
		{
		case Second:
			return (20) * Amount;
		case Minute:
			return (20 * 60) * Amount;
		case Hour:
			return ((20 * 60) * 60) * Amount;
		default:
			return 0;
		}
	}
	
	public enum TimeType
	{
		Second,
		Minute,
		Hour
	}
}
