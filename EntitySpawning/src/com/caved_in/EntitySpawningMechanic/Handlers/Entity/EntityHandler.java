package com.caved_in.EntitySpawningMechanic.Handlers.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import com.caved_in.EntitySpawningMechanic.Handlers.Data.EntityWrapper;

public class EntityHandler
{
	private HashMap<UUID, EntityWrapper> EntityData = new HashMap<UUID,EntityWrapper>();
	
	public EntityHandler()
	{
		
	}
	
	/**
	 * Add an entity to be handled
	 * @param Entity
	 * @param Level
	 */
	public void addEntity(LivingEntity Entity, int Level)
	{
		this.EntityData.put(Entity.getUniqueId(), new EntityWrapper(Entity, Level));
	}
	
	
	/**
	 * Remove a LivingEntity from the Handler
	 * @param Entity
	 */
	public void removeEntity(LivingEntity Entity)
	{
		if (this.EntityData.containsKey(Entity.getUniqueId()))
		{
			this.EntityData.remove(Entity.getUniqueId());
		}
	}
	
	/**
	 * Remove an entities wrapper based on ID
	 * @param ID
	 */
	public void removeEntity(UUID ID)
	{
		if (this.hasWrapper(ID))
		{
			this.EntityData.remove(ID);
		}
	}
	
	/**
	 * Check if an entity has a wrapper
	 * @param Entity
	 * @return
	 */
	public boolean hasWrapper(LivingEntity Entity)
	{
		return this.EntityData.containsKey(Entity.getUniqueId());
	}
	
	/**
	 * Check if a wrapper exists for the given UUID
	 * @param ID
	 * @return
	 */
	public boolean hasWrapper(UUID ID)
	{
		return this.EntityData.containsKey(ID);
	}
	
	/**
	 * Get the wrapper for the living entity (if one exists)
	 * @param Entity
	 * @return
	 */
	public EntityWrapper getWrapper(LivingEntity Entity)
	{
		if (this.hasWrapper(Entity))
		{
			return this.EntityData.get(Entity.getUniqueId());
		}
		return null;
	}
	
	/**
	 * Get the wrapper for the UUID (if one exists)
	 * @param ID
	 * @return
	 */
	public EntityWrapper getWrapper(UUID ID)
	{
		if (this.hasWrapper(ID))
		{
			return this.EntityData.get(ID);
		}
		return null;
	}
	
	/**
	 * Gets all the wrappers that are currently instanced
	 * @return
	 */
	public List<EntityWrapper> getWrappers()
	{
		return new ArrayList<EntityWrapper>(this.EntityData.values());
	}
	
	/**
	 * Attempt to get livingEntity by ID
	 * @param ID
	 * @return
	 */
	public LivingEntity getEntityByID(UUID ID)
	{
		for (World World : Bukkit.getWorlds())
		{
			for(LivingEntity Entity : World.getLivingEntities())
			{
				if (Entity.getUniqueId().equals(ID))
				{
					return Entity;
				}
			}
		}
		return null;
	}
}
