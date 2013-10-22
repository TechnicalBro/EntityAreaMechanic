package com.caved_in.EntitySpawningMechanic.Handlers.EntityAreas.Entity;

import org.bukkit.entity.EntityType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.caved_in.EntitySpawningMechanic.Handlers.Entity.EntityUtility;

@Root(name = "entity")
public class XmlEntity
{
	@Element(name = "type")
	private String Type = "";
	
	private EntityType eType = EntityType.UNKNOWN;
	
	@Element(name = "minLevel")
	private int MinimumLevel = 1;
	
	@Element(name = "maxLevel")
	private int MaximumLevel = 1;
	
	@Element(name = "elite")
	private boolean isElite = false;
	
	@Element(name = "eliteSpawnChance")
	private int eliteSpawnChance = 0;
	
	/**
	 * 
	 * @param Type
	 * @param MinLevel
	 * @param MaxLevel
	 * @param isElite
	 * @param EliteSpawnChance
	 */
	public XmlEntity(@Element(name = "type") String Type,
			@Element(name = "minLevel")int MinLevel,
			@Element(name = "maxLevel")int MaxLevel,
			@Element(name = "elite")boolean isElite,
			@Element(name = "eliteSpawnChance")int EliteSpawnChance)
	{
		this.eType = EntityUtility.getTypeByName(Type);
		this.Type = Type;
		this.MinimumLevel = MinLevel;
		this.MaximumLevel = MaxLevel;
		this.isElite = isElite;
		this.eliteSpawnChance = EliteSpawnChance;
	}
	
	public EntityType getType()
	{
		return this.eType;
	}
	
	public int getMinimumLevel()
	{
		return this.MinimumLevel;
	}
	
	public int getMaximumLevel()
	{
		return this.MaximumLevel;
	}
	
	public boolean isElite()
	{
		return this.isElite;
	}
	
	public int getEliteSpawnChance()
	{
		return this.eliteSpawnChance;
	}
}
