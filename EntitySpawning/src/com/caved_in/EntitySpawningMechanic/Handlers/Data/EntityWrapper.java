package com.caved_in.EntitySpawningMechanic.Handlers.Data;

import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class EntityWrapper
{
	private UUID EntityUUID;
	private int EntityID = 0;
	private boolean isElite = false;
	private boolean isBoss = false;
	private int EntityLevel = 0;
	private EntityType entityType = EntityType.UNKNOWN;
	
	public EntityWrapper(LivingEntity Entity,int Level)
	{
		this.EntityUUID = Entity.getUniqueId();
		this.EntityID = Entity.getEntityId();
		this.EntityLevel = Level;
		this.entityType = Entity.getType();
	}
	
	public boolean isElite()
	{
		return this.isElite;
	}
	
	public EntityType getType()
	{
		return this.entityType;
	}
	
	public void setElite(boolean Value)
	{
		this.isElite = Value;
	}
	
	public boolean isBoss()
	{
		return this.isBoss;
	}
	
	public void setBoss(boolean Value)
	{
		this.isBoss = Value;
	}
	
	public int getLevel()
	{
		return this.EntityLevel;
	}
	
	public int getEntityID()
	{
		return this.EntityID;
	}
	
	public UUID getUniqueID()
	{
		return this.EntityUUID;
	}
}
