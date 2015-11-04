package com.caved_in.entityspawningmechanic;

import java.io.File;

import com.caved_in.commons.chat.Chat;
import com.caved_in.commons.entity.Entities;
import com.caved_in.commons.plugin.BukkitPlugin;
import com.caved_in.commons.time.TimeHandler;
import com.caved_in.commons.time.TimeType;
import com.caved_in.commons.world.Worlds;
import com.caved_in.entityspawningmechanic.commands.PluginCommands;
import com.caved_in.entityspawningmechanic.handlers.entity.EntityHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import com.caved_in.entityspawningmechanic.handlers.entityareas.EntityAreaHandler;
import com.caved_in.entityspawningmechanic.listeners.EventListeners;
import com.caved_in.entityspawningmechanic.tasks.SpawnAreaEntitiesRunnable;

public class EntityMechanic extends BukkitPlugin {
    public static String DATA_FOLDER = "plugins/Entity-Mechanic/";
    public static String AREA_FOLDER = "plugins/Entity-Mechanic/Areas/";

    public static EntityAreaHandler entityAreaHandler = new EntityAreaHandler();
    public static EntityHandler entityDataHandler = new EntityHandler();

    @Override
    public void startup() {

        registerListeners(
                new EventListeners()
        );

        registerCommands(
                new PluginCommands()
        );

        getThreadManager().registerSyncRepeatTask("entitySpawningThread", new SpawnAreaEntitiesRunnable(), TimeHandler.getTimeInTicks(20, TimeType.SECOND), TimeHandler.getTimeInTicks(120, TimeType.SECOND));
        Worlds.cleanAllEntities();
    }

    @Override
    public void shutdown() {
        entityAreaHandler.saveData();
        Chat.messageConsole("Cleaning all entities");
        Worlds.cleanAllEntities();
        Chat.messageConsole("All entities Cleaned");
    }

    @Override
    public String getAuthor() {
        return "Brandon Curtis";
    }

    @Override
    public void initConfig() {
        File dataFolder = new File(DATA_FOLDER);
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File AreaFolder = new File(AREA_FOLDER);
        if (!AreaFolder.exists()) {
            AreaFolder.mkdir();
        }

        entityAreaHandler.loadData();
    }
}
