package com.caved_in.EntitySpawningMechanic.Handlers.EntityAreas;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.caved_in.EntitySpawningMechanic.EntityMechanic;
import com.caved_in.EntitySpawningMechanic.Handlers.Entity.EntityCalculations;
import com.caved_in.EntitySpawningMechanic.Handlers.Entity.EntityUtility;
import com.caved_in.EntitySpawningMechanic.Handlers.EntityAreas.Area.EntityArea;
import com.caved_in.EntitySpawningMechanic.Handlers.EntityAreas.Area.XmlLocation;
import com.caved_in.EntitySpawningMechanic.Handlers.EntityAreas.Entity.XmlEntity;
import com.caved_in.EntitySpawningMechanic.Handlers.Location.LocationHandler;
import com.caved_in.EntitySpawningMechanic.Handlers.Utilities.NumberUtils;

public class EntityAreaHandler
{
	private HashMap<String, EntityArea> Areas = new HashMap<String, EntityArea>();
	
	public EntityAreaHandler()
	{
		
	}
	
	/**
	 * Get a full list of the currently active entity areas
	 * @return
	 */
	public List<EntityArea> getAreas()
	{
		return new ArrayList<EntityArea>(this.Areas.values());
	}
	
	/**
	 * Add an area to be handled and managed
	 * @param Area
	 */
	public void addArea(EntityArea Area)
	{
		if (!this.Areas.containsKey(Area.getName()))
		{
			this.Areas.put(Area.getName(),Area);
		}
	}
	
	/**
	 * Remove an entity area
	 * @param Area
	 */
	public void removeArea(EntityArea Area)
	{
		if (this.Areas.containsValue(Area))
		{
			this.Areas.remove(Area.getName());
		}
	}
	
	public void removeArea(String Name)
	{
		if (this.Areas.containsKey(Name))
		{
			this.Areas.remove(Name);
		}
	}
	
	public void clearAreas()
	{
		this.SaveData();
		this.Areas.clear();
	}
	
	public EntityArea getArea(String Name)
	{
		return this.Areas.get(Name);
	}
	
	public boolean isArea(String Name)
	{
		return this.Areas.containsKey(Name);
	}
	
	public void SaveData()
	{
		Serializer Serializer = new Persister();
		for(EntityArea Area : this.getAreas())
		{
			try
			{
				Serializer.write(Area, new File(EntityMechanic.AREA_FOLDER + Area.getName() + ".xml"));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void LoadData()
	{
		Serializer Serializer = new Persister();
		for(String File : this.getFiles())
		{
			try
			{
				EntityArea Area = Serializer.read(EntityArea.class, new File(File));
				//todo Maybe make a de-serializer for this to load entities
				this.Areas.put(Area.getName(), Area);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public XmlLocation getLocation(Location Location)
	{
		return new XmlLocation(Location.getX(),Location.getY(),Location.getZ(),Location.getWorld().getName());
	}
	
	public void spawnRandomEntity(EntityArea entityArea)
	{
		XmlEntity enemyToSpawn = entityArea.getRandomEntity();
		int enemyLevel = NumberUtils.getRandomInRange(enemyToSpawn.getMinimumLevel(), enemyToSpawn.getMaximumLevel());
		int enemyHealth = 0;
		LivingEntity spawnedEntity = EntityUtility.spawnLivingEntity(entityArea.getLocationInArea(),enemyToSpawn.getType());
		//EntityWrapper entityWrapper = new EntityWrapper(spawnedEntity, enemyLevel);
		String entityName = "";
		if (enemyToSpawn.isElite() && NumberUtils.percentCheck(enemyToSpawn.getEliteSpawnChance()))
		{
			enemyHealth = EntityCalculations.generateHealth(enemyLevel, true, false);
			entityName = EntityUtility.generateEntityName(enemyToSpawn.getType(), true, false, enemyLevel);
		}
		else
		{
			enemyHealth = EntityCalculations.generateHealth(enemyLevel, false, false);
			entityName = EntityUtility.generateEntityName(enemyToSpawn.getType(), false, false, enemyLevel);
		}
		
		EntityUtility.setMaxHealth(spawnedEntity, enemyHealth);
		EntityUtility.setName(spawnedEntity, entityName, false);
		EntityUtility.setCurrentHealth(spawnedEntity, enemyHealth);
		EntityMechanic.entityDataHandler.addEntity(spawnedEntity, enemyLevel);
	}
	
	public void spawnRandomEntity(String entityArea)
	{
		if (this.isArea(entityArea))
		{
			this.spawnRandomEntity(this.getArea(entityArea));
		}
	}
	
	
	public double getAreaDensity(EntityArea entityArea)
	{
		double entitySearchRadius = ((entityArea.getSpawnRadius() * 2));
		List<LivingEntity> areaEntities = LocationHandler.getEntitiesNearLocation(entityArea.getLocation(), (int)entitySearchRadius);
		if (areaEntities.size() > 0)
		{
			double dividedNumbers = (entitySearchRadius * 10) / areaEntities.size();
			return dividedNumbers;
		}
		else
		{
			return entitySearchRadius * 10;
		}
	}
	
	public double getAreaDensity(String entityArea)
	{
		if (this.isArea(entityArea.toLowerCase()))
		{
			return this.getAreaDensity(this.getArea(entityArea.toLowerCase()));
		}
		return -1;
	}
	
	public boolean densityCheck(EntityArea entityArea)
	{
		int areaEntities = LocationHandler.getEntitiesNearLocation(entityArea.getLocation(), (int)entityArea.getSpawnRadius()).size();
		double areaDensity = this.getAreaDensity(entityArea);
		double radiusCheck = (entityArea.getSpawnRadius() * 2) * 10;
		return (areaEntities < (entityArea.getSpawnRadius() / 2) ? (new Random().nextInt((int)radiusCheck) <= areaDensity) : false);
	}
	
	public boolean densityCheck(String Area)
	{
		if (this.isArea(Area.toLowerCase()))
		{
			return this.densityCheck(this.getArea(Area.toLowerCase()));
		}
		return false;
	}
	
	public List<String> getFiles()
	{
		List<String> AreaFiles = new ArrayList<String>();
		for(File Area : FileUtils.listFiles(new File(EntityMechanic.AREA_FOLDER), new String[] { "xml" }, false))
		{
			AreaFiles.add(EntityMechanic.AREA_FOLDER + Area.getName());
		}
		return AreaFiles;
	}
}
