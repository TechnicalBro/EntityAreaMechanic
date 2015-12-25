package com.caved_in.entityspawningmechanic.commands;

import com.caved_in.commons.chat.Chat;
import com.caved_in.commons.command.Arg;
import com.caved_in.commons.command.Command;
import com.caved_in.commons.entity.Entities;
import com.caved_in.commons.menu.HelpScreen;
import com.caved_in.commons.player.Players;
import com.caved_in.entityspawningmechanic.EntityMechanic;
import com.caved_in.entityspawningmechanic.config.EntityArea;
import com.caved_in.entityspawningmechanic.config.XmlEntity;
import com.caved_in.entityspawningmechanic.data.EntityWrapper;
import com.caved_in.entityspawningmechanic.handlers.entity.EntityHandler;
import com.caved_in.entityspawningmechanic.handlers.entityareas.EntityAreaHandler;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PluginCommands {

    private HelpScreen pluginCommandHelpScreen = null;

    public PluginCommands() {
        //Initialize the plugin command help screen.
        pluginCommandHelpScreen = new HelpScreen("Areas Command Help");
        pluginCommandHelpScreen.setHeader(ChatColor.YELLOW + "<name> Page <page> of <maxpage>");
        pluginCommandHelpScreen.setFormat("<name> -- <desc>");
        pluginCommandHelpScreen.setFlipColor(ChatColor.GREEN, ChatColor.DARK_GREEN);

        pluginCommandHelpScreen.setEntry("/newarea <Name> <Radius>", Chat.format("Used to create a new area"));
        pluginCommandHelpScreen.setEntry("/areas list", "View a list of all the areas, their coords, and how many mobs the area's config'd for");
        pluginCommandHelpScreen.setEntry("/addmob <Name> <Min> <Max> <MobType> [IsElite] [EliteChance]", "Adds an entity to an area for later spawning.");
        pluginCommandHelpScreen.setEntry("/areas help", "This help menu");
        pluginCommandHelpScreen.setEntry("/areas spawnentity <Area>", "Used to spawn an enemy in an area");
        pluginCommandHelpScreen.setEntry("/areas density <Area>", "Get an integer based on the density of the area");
        pluginCommandHelpScreen.setEntry("/areas reload", "reload all the area data from disk");
        pluginCommandHelpScreen.setEntry("/areas entities", "Get all the current entities and their info");

    }

    @Command(identifier = "areas", permissions = "entitymechanic.area")
    public void onAreasCommand(CommandSender Sender) {

        Chat.message(Sender, "&aPlease use &e/areas list&a to see the areas");
    }

    @Command(identifier = "areas list")
    public void onAreasListCommand(CommandSender sender, @Arg(name = "page", def = "1") int page) {
        HelpScreen helpScreen = new HelpScreen("Areas List");
        helpScreen.setHeader(ChatColor.BLUE + "<name> Page <page> of <maxpage>");
        helpScreen.setFormat("<name> -- <desc>");
        helpScreen.setFlipColor(ChatColor.GREEN, ChatColor.DARK_GREEN);

        //todo optimize
        for (EntityArea Area : EntityMechanic.entityAreaHandler.getAreas()) {
            helpScreen.setEntry(Area.getName(), "Co-Ordinates [" + Area.getLocation().getX() + ", " + Area.getLocation().getY() + ", " + Area.getLocation().getZ() + " {" + Area.getLocation().getWorld() + "} - Total of [" + Area.getEntities().size() + "] entities.");
        }

        helpScreen.sendTo(sender, page, 5);
    }

    @Command(identifier = "areas spawnentity")
    public void onAreasSpawnEntity(Player player, @Arg(name = "area name") String area,@Arg(name = "amount",def="10")int amount) {
        if (!EntityMechanic.entityAreaHandler.isArea(area)) {
            Chat.message(player, String.format("&c%s&e is not a valid area. &aUse &e/areas list&a to view all areas.", area));
            return;
        }

        for (int i = 0; i < amount; i++) {
            EntityMechanic.entityAreaHandler.spawnRandomEntity(area);
        }
    }

    //TODO write method to list all alive enemies with their data

    @Command(identifier = "areas entities")
    public void onAreasEntitiesCommand(CommandSender sender, @Arg(name = "page", def = "1") int page) {
        HelpScreen helpScreen = new HelpScreen("Entity data List");
        helpScreen.setHeader(ChatColor.YELLOW + "<name> Page <page> of <maxpage>");
        helpScreen.setFormat("<name> -> <desc>");
        helpScreen.setSimpleColor(ChatColor.GREEN);

        for (EntityWrapper entityWrapper : EntityHandler.getWrappers()) {
            helpScreen.setEntry(Chat.format(StringUtils.capitalize(entityWrapper.getType().name().toLowerCase().replace("_", " ")) + " [&b" + entityWrapper.getEntityId() + "&a]"), Chat.format("&aElite [&e" + String.valueOf(entityWrapper.isElite()).toLowerCase() + "&a], Boss [&e" + String.valueOf(entityWrapper.isBoss()) + "&a], Level [&6" + entityWrapper.getLevel() + "&a]"));
        }

        helpScreen.sendTo(sender, page, 5);
    }


    @Command(identifier = "areas density")
    public void onAreasDensitycommand(Player player, @Arg(name = "area") String area) {
        if (EntityMechanic.entityAreaHandler.isArea(area)) {
            Chat.message(player, Chat.format("&aThe area &e%s &a has a density of [&2%s&a]", area, EntityMechanic.entityAreaHandler.getAreaDensity(area)));
        } else {
            Chat.message(player, Chat.format("&cThe area &e%s&c doesn't exist, please try again.", area));
        }
    }

    @Command(identifier = "areas reload")
    public void reloadAreas(CommandSender sender) {
        EntityMechanic.entityAreaHandler.clearAreas();
        EntityMechanic.entityAreaHandler.loadData();
        Chat.message(sender, "&aAreas have been reloaded");
    }

    @Command(identifier = "areas help")
    public void onAreasHelp(CommandSender Sender, @Arg(name = "page", def = "1") int page) {
        pluginCommandHelpScreen.sendTo(Sender, page, 5);
    }

    @Command(identifier = "areas teleport")
    public void onAreasTeleportCommand(Player player, @Arg(name = "area name")String area) {
        if (!EntityMechanic.entityAreaHandler.isArea(area)) {
            Chat.format(player, "&cArea &e%s &cdoesn't exist", area);
            return;
        }

        EntityArea ea = EntityMechanic.entityAreaHandler.getArea(area);
        Players.teleport(player,ea.getLocation());
        //todo add spehul effekts
    }

    @Command(identifier = "newarea", permissions = "entitymechanic.area.create", description = "/newarea <AreaName> <SpawnRadius>")
    public void createArenaCommand(Player player, @Arg(name = "area")String areaName, @Arg(name = "radius")int radius) {
        if (EntityMechanic.entityAreaHandler.isArea(areaName)) {
            Chat.message(player,"&eAn area with that name already exists");
            return;
        }

        EntityArea area = new EntityArea(areaName,player.getLocation(),radius);
        EntityMechanic.entityAreaHandler.addArea(area);
        EntityMechanic.entityAreaHandler.saveData();
        Chat.message(player,Chat.format("&aThe Area &e%s&a has been created!",areaName));
    }

    @Command(identifier = "addmob", permissions = "entitymechanic.spawning.addmob", description = "/addmob <AreaName> <MinLevel> <MaxLevel> <MobType> <Boolean[IsElite]> <Percent[Elite Spawn Chance]>")
    public void onAddMobCommand(Player player, @Arg(name = "area")String areaName,@Arg(name = "min level")int minLevel, @Arg(name = "max level")int maxlevel, @Arg(name = "type")EntityType type, @Arg(name = "elite",def = "n")boolean elite, @Arg(name = "elite spawn chance",def = "0")int eliteChance) {
        if (!EntityMechanic.entityAreaHandler.isArea(areaName)) {
            Chat.message(player, Chat.format("&c%s&e is not a valid area.",areaName));
            return;
        }

        EntityArea area = EntityMechanic.entityAreaHandler.getArea(areaName);
        XmlEntity entity = new XmlEntity(type,minLevel,maxlevel,elite,eliteChance);

        area.addEntity(entity);

        Chat.message(player,Chat.format("&aThe mob &e%s&a has been added to &b%s", Entities.getDefaultName(type),areaName));
    }
}
