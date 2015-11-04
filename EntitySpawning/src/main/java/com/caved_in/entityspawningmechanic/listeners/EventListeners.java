package com.caved_in.entityspawningmechanic.listeners;

import java.util.Arrays;
import java.util.List;

import com.caved_in.commons.entity.Entities;
import com.caved_in.entityspawningmechanic.EntityMechanic;
import com.caved_in.entityspawningmechanic.handlers.entity.EntityUtilities;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class EventListeners implements Listener {
	public EventListeners() {
	}

	@EventHandler
	public void onCreatureSpawned(CreatureSpawnEvent event) {
		List<SpawnReason> allowSpawnReason = Arrays.asList(SpawnReason.CUSTOM, SpawnReason.SPAWNER, SpawnReason.SPAWNER_EGG, SpawnReason.BREEDING, SpawnReason.SLIME_SPLIT, SpawnReason.EGG, SpawnReason.LIGHTNING, SpawnReason.BUILD_IRONGOLEM, SpawnReason.BUILD_SNOWMAN, SpawnReason.BUILD_WITHER);
		if (!event.getEntity().hasMetadata("NPC")) {
			if (!allowSpawnReason.contains(event.getSpawnReason())) {
				Entities.removeEntitySafely(event.getEntity());
			}
		}
	}

	@EventHandler
	public void onEntityDamaged(EntityDamageEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			LivingEntity damagedEntity = (LivingEntity) event.getEntity();
			if (!(damagedEntity instanceof Player) && (damagedEntity.getType() != EntityType.ENDER_DRAGON)) {
				if (damagedEntity.getCustomName() != null && !damagedEntity.getCustomName().isEmpty()) {
					if (damagedEntity.getCustomName().contains("  ")) {
						damagedEntity.setCustomName(EntityUtilities.getNameWithoutHealthBar(damagedEntity) + "  " + EntityUtilities.generateHealthBar(damagedEntity));
					} else {
						damagedEntity.setCustomName(damagedEntity.getCustomName() + "  " + EntityUtilities.generateHealthBar(((Damageable) damagedEntity).getHealth() - event.getDamage(), ((Damageable) damagedEntity).getMaxHealth()));
					}

				} else {
					damagedEntity.remove();
				}
			}
		}
	}

	@EventHandler
	public void chunkLoaded(ChunkLoadEvent Event) {
		for (Entity entity : Event.getChunk().getEntities()) {
			if (entity instanceof LivingEntity) {
				LivingEntity livingMob = (LivingEntity) entity;
				if (!livingMob.hasMetadata("NPC") && !EntityMechanic.entityDataHandler.hasWrapper(livingMob)) {
					livingMob.remove();
				}
			}
		}
	}

	@EventHandler
	public void chunkUnloaded(ChunkUnloadEvent Event) {
		for (Entity entity : Event.getChunk().getEntities()) {
			if (entity instanceof LivingEntity) {
				LivingEntity livingMob = (LivingEntity) entity;
				if (!livingMob.hasMetadata("NPC") && !EntityMechanic.entityDataHandler.hasWrapper(livingMob)) {
					livingMob.remove();
				}
			}
		}
	}

	@EventHandler
	public void entityDeathEvent(EntityDeathEvent Event) {
		if (EntityMechanic.entityDataHandler.hasWrapper(Event.getEntity())) {
			EntityMechanic.entityDataHandler.removeEntity(Event.getEntity());
		}
	}
}
