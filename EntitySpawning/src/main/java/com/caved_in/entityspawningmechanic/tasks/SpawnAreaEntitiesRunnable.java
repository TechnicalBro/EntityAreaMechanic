package com.caved_in.entityspawningmechanic.tasks;

import com.caved_in.entityspawningmechanic.EntityMechanic;
import com.caved_in.entityspawningmechanic.config.EntityArea;
import org.bukkit.Bukkit;

public class SpawnAreaEntitiesRunnable implements Runnable {

	@Override
	public void run() {
		for (EntityArea area : EntityMechanic.entityAreaHandler.getAreas()) {
			if (EntityMechanic.entityAreaHandler.densityCheck(area)) {
				EntityMechanic.entityAreaHandler.spawnRandomEntity(area);
				Bukkit.getLogger().info("Spawned an entity for the area [" + area.getName() + "]");
			}
		}
	}
}
