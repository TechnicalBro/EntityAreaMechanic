package com.caved_in.entityspawningmechanic.handlers.entity;

import java.util.*;

import com.caved_in.entityspawningmechanic.data.EntityWrapper;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

public class EntityHandler {
	private static Map<UUID, EntityWrapper> entityData = new HashMap<>();

	public static void addEntity(LivingEntity entity, int level) {
		entityData.put(entity.getUniqueId(), new EntityWrapper(entity, level));
	}

	public static void removeEntity(LivingEntity entity) {
		if (entityData.containsKey(entity.getUniqueId())) {
			entityData.remove(entity.getUniqueId());
		}
	}

	public static void removeEntity(UUID id) {
		if (hasWrapper(id)) {
			entityData.remove(id);
		}
	}

	public static boolean hasWrapper(LivingEntity entity) {
		return entityData.containsKey(entity.getUniqueId());
	}

	public static boolean hasWrapper(UUID id) {
		return entityData.containsKey(id);
	}

	public static EntityWrapper getWrapper(LivingEntity entity) {
		return entityData.get(entity.getUniqueId());
	}

	public static EntityWrapper getWrapper(UUID ID) {
		return entityData.get(ID);
	}

	public static Collection<EntityWrapper> getWrappers() {
		return entityData.values();
	}

	public static LivingEntity getEntityByID(UUID id) {
		for (World world : Bukkit.getWorlds()) {
			for (LivingEntity entity : world.getLivingEntities()) {
				if (entity.getUniqueId().equals(id)) {
					return entity;
				}
			}
		}
		return null;
	}
}
