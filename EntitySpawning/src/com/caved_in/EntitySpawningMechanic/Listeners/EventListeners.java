package com.caved_in.EntitySpawningMechanic.Listeners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.bergerkiller.bukkit.common.events.EntityRemoveEvent;
import com.caved_in.EntitySpawningMechanic.EntityMechanic;
import com.caved_in.EntitySpawningMechanic.Handlers.Data.EntityWrapper;
import com.caved_in.EntitySpawningMechanic.Handlers.Entity.EntityUtility;
import com.caved_in.EntitySpawningMechanic.Handlers.Utilities.ChatHandler;

public class EventListeners implements Listener
{
	public EventListeners(EntityMechanic Plugin)
	{
		Plugin.getServer().getPluginManager().registerEvents(this, Plugin);
	}
	
	@EventHandler
	public void creatureSpawned(CreatureSpawnEvent Event)
	{
		List<SpawnReason> allowSpawnReason = Arrays.asList(new SpawnReason[] { SpawnReason.CUSTOM, SpawnReason.SPAWNER, SpawnReason.SPAWNER_EGG,SpawnReason.BREEDING,SpawnReason.SLIME_SPLIT,SpawnReason.EGG,SpawnReason.LIGHTNING,SpawnReason.BUILD_IRONGOLEM,SpawnReason.BUILD_SNOWMAN,SpawnReason.BUILD_WITHER});
		if (!Event.getEntity().hasMetadata("NPC"))
		{
			if (!allowSpawnReason.contains(Event.getSpawnReason()))
			{
				/*
				final UUID entityID = Event.getEntity().getUniqueId();
				final String World = Event.getEntity().getWorld().getName();
				CommonUtil.nextTick(new Runnable()
				{

					@Override
					public void run()
					{
						EntityUtil.getEntity(Bukkit.getWorld(World), entityID).remove();
					}
					
				});
				*/
				Event.getEntity().remove();
			}
		}
	}
	
	@EventHandler
	public void entityDamaged(EntityDamageEvent Event)
	{
		if (Event.getEntity() instanceof LivingEntity)
		{
			LivingEntity damagedEntity = (LivingEntity)Event.getEntity();
			if (!(damagedEntity instanceof Player) && (damagedEntity.getType() != EntityType.ENDER_DRAGON))
			{
				if (damagedEntity.getCustomName() != null && !damagedEntity.getCustomName().isEmpty())
				{
					if (damagedEntity.getCustomName().contains("  "))
					{
						damagedEntity.setCustomName(EntityUtility.getNameWithoutHealthBar(damagedEntity) + "  " + EntityUtility.generateHealthBar(damagedEntity));
					}
					else
					{
						damagedEntity.setCustomName(damagedEntity.getCustomName() + "  " + EntityUtility.generateHealthBar(((Damageable)damagedEntity).getHealth() - Event.getDamage(),((Damageable)damagedEntity).getMaxHealth()));
					}
					
				}
				else
				{
					damagedEntity.remove();
				}
			}
		}
	}
	
	@EventHandler
	public void chunkLoaded(ChunkLoadEvent Event)
	{
		for(Entity entity : Event.getChunk().getEntities())
		{
			if (entity instanceof LivingEntity)
			{
				LivingEntity livingMob = (LivingEntity)entity;
				if (!livingMob.hasMetadata("NPC") && !EntityMechanic.entityDataHandler.hasWrapper(livingMob))
				{
					livingMob.remove();
				}
			}
		}
	}
	
	@EventHandler
	public void chunkUnloaded(ChunkUnloadEvent Event)
	{
		for(Entity entity : Event.getChunk().getEntities())
		{
			if (entity instanceof LivingEntity)
			{
				LivingEntity livingMob = (LivingEntity)entity;
				if (!livingMob.hasMetadata("NPC") && !EntityMechanic.entityDataHandler.hasWrapper(livingMob))
				{
					livingMob.remove();
				}
			}
		}
	}
	
	@EventHandler
	public void entityRemovedEvent(EntityRemoveEvent Event)
	{
		if (Event.getEntity() instanceof LivingEntity)
		{
			LivingEntity removedEntity = (LivingEntity)Event.getEntity();
			if (EntityMechanic.entityDataHandler.hasWrapper(removedEntity))
			{
				EntityMechanic.entityDataHandler.removeEntity(removedEntity);
			}
		}
	}
	
	@EventHandler
	public void entityDeathEvent(EntityDeathEvent Event)
	{
		if (EntityMechanic.entityDataHandler.hasWrapper(Event.getEntity()))
		{
			EntityMechanic.entityDataHandler.removeEntity(Event.getEntity());
		}
	}
}
