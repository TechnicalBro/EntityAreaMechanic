package com.caved_in.EntitySpawningMechanic.Handlers.EntityAreas.Area;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.simpleframework.xml.*;

import com.caved_in.EntitySpawningMechanic.Handlers.EntityAreas.Entity.XmlEntity;
import com.caved_in.EntitySpawningMechanic.Handlers.Location.LocationHandler;

@Root(name = "area")
public class EntityArea
{
	@Element(type = XmlLocation.class)
	private XmlLocation Location;
	
	@Attribute
	private double spawnRadius = 0.0;
	
	@ElementList(entry = "entity", name = "entities", inline = true, type = XmlEntity.class)
	private List<XmlEntity> entities = new ArrayList<XmlEntity>();
	
	@Attribute(name = "name")
	private String areaName = "";
	
	public EntityArea(@Attribute(name = "name")String Name, @Element(name = "Location")XmlLocation Location, @Attribute(name = "spawnRadius")double SpawnRadius)
	{
		this.areaName = Name;
		this.Location = Location;
		this.spawnRadius = SpawnRadius;
	}
	
	public XmlEntity getRandomEntity()
	{
		return this.entities.get(new Random().nextInt(this.entities.size()));
	}
	
	public Location getLocationInArea()
	{
		return LocationHandler.getRandomLocation(this.getLocation(), (int)this.getSpawnRadius());
	}
	
	public Location getLocation()
	{
		return this.Location.getLocation();
	}
	
	public XmlLocation getXmlLocation()
	{
		return this.Location;
	}
	
	public double getSpawnRadius()
	{
		return this.spawnRadius;
	}
	
	public List<XmlEntity> getEntities()
	{
		return this.entities;
	}
	
	public void addEntity(XmlEntity Entity)
	{
		this.entities.add(Entity);
	}
	
	public void addEntity(String Type, int MinLevel, int MaxLevel, boolean isElite, int EliteSpawnChance)
	{
		this.entities.add(new XmlEntity(Type,MinLevel,MaxLevel,isElite,EliteSpawnChance));
	}
	
	public String getName()
	{
		return this.areaName;
	}
}
