package com.caved_in.EntitySpawningMechanic;

import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.PluginBase;
import com.caved_in.EntitySpawningMechanic.Commands.AdminCommands;
import com.caved_in.EntitySpawningMechanic.Commands.CommandController;
import com.caved_in.EntitySpawningMechanic.Handlers.Entity.EntityHandler;
import com.caved_in.EntitySpawningMechanic.Handlers.Entity.EntityUtility;
import com.caved_in.EntitySpawningMechanic.Handlers.EntityAreas.EntityAreaHandler;
import com.caved_in.EntitySpawningMechanic.Handlers.Utilities.ChatHandler;
import com.caved_in.EntitySpawningMechanic.Handlers.Utilities.TimeUtils;
import com.caved_in.EntitySpawningMechanic.Handlers.Utilities.TimeUtils.TimeType;
import com.caved_in.EntitySpawningMechanic.Listeners.EventListeners;
import com.caved_in.EntitySpawningMechanic.Runnables.RunnableManager;
import com.caved_in.EntitySpawningMechanic.Runnables.SpawnAreaEntitiesRunnable;

public class EntityMechanic extends PluginBase
{
	public static String DATA_FOLDER = "plugins/Entity-Mechanic/";
	public static String AREA_FOLDER = "plugins/Entity-Mechanic/Areas/";
	
	public static EntityAreaHandler entityAreaHandler = new EntityAreaHandler();
	public static EntityHandler entityDataHandler = new EntityHandler();
	public static RunnableManager runnableManager;

	@Override
	public boolean command(CommandSender arg0, String arg1, String[] arg2)
	{
		return false;
	}

	@Override
	public void disable()
	{
		entityAreaHandler.SaveData();
		ChatHandler.sendMessageToConsole("Cleaning all entities");
		EntityUtility.cleanAllEntities();
		ChatHandler.sendMessageToConsole("All entities Cleaned");
		HandlerList.unregisterAll(this);
	}

	@Override
	public void enable()
	{
		runnableManager = new RunnableManager(this);
		CommandController.registerCommands(this, new AdminCommands());
		File DataFolder = new File(DATA_FOLDER);
		if (!DataFolder.exists())
		{
			DataFolder.mkdir();
		}
		
		File AreaFolder = new File(AREA_FOLDER);
		if (!AreaFolder.exists())
		{
			AreaFolder.mkdir();
		}
		
		entityAreaHandler.LoadData();
		runnableManager.RegisterSynchRepeatTask("entitySpawningThread", new SpawnAreaEntitiesRunnable(), TimeUtils.getTimeInTicks(20,TimeType.Second), TimeUtils.getTimeInTicks(120, TimeType.Second));
		new EventListeners(this);
		EntityUtility.cleanAllEntities();
	}

	@Override
	public int getMinimumLibVersion()
	{
		return Common.VERSION;
	}
}
