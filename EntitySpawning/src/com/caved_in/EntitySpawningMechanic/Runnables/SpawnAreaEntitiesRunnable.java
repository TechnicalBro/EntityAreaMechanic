package com.caved_in.EntitySpawningMechanic.Runnables;

import org.bukkit.Bukkit;

import com.caved_in.EntitySpawningMechanic.EntityMechanic;
import com.caved_in.EntitySpawningMechanic.Handlers.EntityAreas.Area.EntityArea;

public class SpawnAreaEntitiesRunnable implements Runnable
{

	@Override
	public void run()
	{
		for(EntityArea Area : EntityMechanic.entityAreaHandler.getAreas())
		{
			if (EntityMechanic.entityAreaHandler.densityCheck(Area))
			{
				EntityMechanic.entityAreaHandler.spawnRandomEntity(Area);
				Bukkit.getLogger().info("Spawned an Entity for the area [" + Area.getName() + "]");
			}
		}
	}
}
