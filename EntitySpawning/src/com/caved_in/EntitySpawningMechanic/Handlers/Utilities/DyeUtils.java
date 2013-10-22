package com.caved_in.EntitySpawningMechanic.Handlers.Utilities;

import org.bukkit.DyeColor;
import org.bukkit.material.Dye;

public class DyeUtils
{
	public static Dye getDye(DyeColor Color)
	{
		Dye Dye = new Dye();
		Dye.setColor(Color);
		return Dye;
	}
}
