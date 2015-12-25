package com.caved_in.entityspawningmechanic;

import java.io.File;

import com.caved_in.commons.chat.Chat;
import com.caved_in.commons.plugin.BukkitPlugin;
import com.caved_in.commons.time.TimeHandler;
import com.caved_in.commons.time.TimeType;
import com.caved_in.commons.world.Worlds;
import com.caved_in.entityspawningmechanic.commands.PluginCommands;
import com.caved_in.entityspawningmechanic.handlers.entity.EntityHandler;

import com.caved_in.entityspawningmechanic.handlers.entityareas.EntityAreaHandler;
import com.caved_in.entityspawningmechanic.listeners.EntityMechanicListeners;
import com.caved_in.entityspawningmechanic.tasks.RemoveEntityHealthBarRunnable;
import com.caved_in.entityspawningmechanic.tasks.SpawnAreaEntitiesRunnable;

public class EntityMechanic extends BukkitPlugin {
    public static String DATA_FOLDER = "plugins/Entity-Mechanic/";
    public static String AREA_FOLDER = "plugins/Entity-Mechanic/Areas/";

    public static EntityAreaHandler entityAreaHandler = new EntityAreaHandler();
    public static EntityHandler entityDataHandler = new EntityHandler();

    @Override
    public void startup() {

        registerListeners(
                new EntityMechanicListeners()
        );

        registerCommands(
                new PluginCommands()
        );

        Worlds.cleanAllEntities();

        getThreadManager().registerSyncRepeatTask("entitySpawningThread", new SpawnAreaEntitiesRunnable(), TimeHandler.getTimeInTicks(20, TimeType.SECOND), TimeHandler.getTimeInTicks(120, TimeType.SECOND));
//        getThreadManager().registerSyncRepeatTask("entityRenameThread")
        RemoveEntityHealthBarRunnable.getInstance().runTaskTimer(this, TimeHandler.getTimeInTicks(5, TimeType.SECOND), TimeHandler.getTimeInTicks(3, TimeType.SECOND));
        debug("Registered all the threads for entity management!");
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

    public static class Config {

        /*
        How long after an entities last hit until their health bar is removed
         */
        public static final long REMOVE_HEALTHBAR_TIME = TimeHandler.getTimeInMilles(5, TimeType.SECOND);

    }
}
