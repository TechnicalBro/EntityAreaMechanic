package com.caved_in.EntitySpawningMechanic.Commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.caved_in.EntitySpawningMechanic.EntityMechanic;
import com.caved_in.EntitySpawningMechanic.Commands.CommandController.CommandHandler;
import com.caved_in.EntitySpawningMechanic.Commands.CommandController.SubCommandHandler;
import com.caved_in.EntitySpawningMechanic.Handlers.Data.EntityWrapper;
import com.caved_in.EntitySpawningMechanic.Handlers.Data.HelpScreen;
import com.caved_in.EntitySpawningMechanic.Handlers.Entity.EntityUtility;
import com.caved_in.EntitySpawningMechanic.Handlers.EntityAreas.Area.EntityArea;
import com.caved_in.EntitySpawningMechanic.Handlers.EntityAreas.Entity.XmlEntity;
import com.caved_in.EntitySpawningMechanic.Handlers.Utilities.ChatHandler;

public class AdminCommands
{
	
	@CommandHandler(name = "areas", permission = "entitymechanic.area")
	public void AreasCommand(CommandSender Sender, String[] Args)
	{
		Sender.sendMessage(ChatHandler.formatColorCodes("&aPlease use &e/areas list&a to see the areas"));
	}
	
	@SubCommandHandler(parent = "areas", name = "list")
	public void AreasList(CommandSender Sender, String[] Args)
	{
		HelpScreen HelpScreen = new HelpScreen("Areas List");
		HelpScreen.setHeader(ChatColor.BLUE + "<name> Page <page> of <maxpage>");
		HelpScreen.setFormat("<name> -- <desc>");
		HelpScreen.setFlipColor(ChatColor.GREEN, ChatColor.DARK_GREEN);
		
		for(EntityArea Area : EntityMechanic.entityAreaHandler.getAreas())
		{
			HelpScreen.setEntry(Area.getName(), "Co-Ordinates [" + Area.getXmlLocation().getX() + ", " + Area.getXmlLocation().getY() + ", " + Area.getXmlLocation().getZ() + " {" + Area.getXmlLocation().getWorld() + "} - Total of [" + Area.getEntities().size() + "] entities.");
		}
		
		if (Args.length == 1)
		{
			HelpScreen.sendTo(Sender, 1, 5);
		}
		else
		{
			if (Args[1] != null && StringUtils.isNumeric(Args[1]))
			{
				int Page = Integer.parseInt(Args[1]);
				HelpScreen.sendTo(Sender, Page, 5);
			}
		}
	}
	
	@SubCommandHandler(parent = "areas", name = "spawnentity")
	public void SpawnEnemy(Player Player, String[] Args)
	{
		if (Args.length > 1)
		{
			if (Args[1] != null)
			{
				String Area = Args[1].toLowerCase();
				if (EntityMechanic.entityAreaHandler.isArea(Area))
				{
					EntityMechanic.entityAreaHandler.spawnRandomEntity(Area);
				}
				else
				{
					Player.sendMessage("NOT AN AREA...");
				}
			}
		}
		else
		{
			Player.sendMessage("/areas spawnenemy <Area>");
		}
	}
	
	//TODO write method to list all alive enemies with their data
	
	@SubCommandHandler(parent = "areas", name = "entities")
	public void outputEntityData(CommandSender Sender, String[] Args)
	{
		HelpScreen helpScreen = new HelpScreen("Entity Data List");
		helpScreen.setHeader(ChatColor.YELLOW + "<name> Page <page> of <maxpage>");
		helpScreen.setFormat("<name> -> <desc>");
		helpScreen.setSimpleColor(ChatColor.GREEN);
		
		for(EntityWrapper entityWrapper : EntityMechanic.entityDataHandler.getWrappers())
		{
			helpScreen.setEntry(ChatHandler.formatColorCodes(StringUtils.capitalize(entityWrapper.getType().name().toLowerCase().replace("_", " ")) + " [&b" + entityWrapper.getEntityID() + "&a]") , ChatHandler.formatColorCodes("&aElite [&e" + String.valueOf(entityWrapper.isElite()).toLowerCase() + "&a], Boss [&e" + String.valueOf(entityWrapper.isBoss()) + "&a], Level [&6" + entityWrapper.getLevel() + "&a]"));
		}
		
		if (Args.length == 1)
		{
			helpScreen.sendTo(Sender, 1, 5);
		}
		else
		{
			if (Args[1] != null && StringUtils.isNumeric(Args[1]))
			{
				int Page = Integer.parseInt(Args[1]);
				helpScreen.sendTo(Sender, Page, 5);
			}
		}
	}
	
	
	@SubCommandHandler(parent = "areas", name = "density")
	public void outputAreaDensity(Player Player, String[] Args)
	{
		if (Args.length > 1)
		{
			if (Args[1] != null)
			{
				String Area = Args[1].toLowerCase();
				if (EntityMechanic.entityAreaHandler.isArea(Area))
				{
					Player.sendMessage(ChatHandler.formatColorCodes("&aThe area&e" + Args[1] + " &a has a density of [&2" + EntityMechanic.entityAreaHandler.getAreaDensity(Area) + "&a]"));
				}
				else
				{
					Player.sendMessage(ChatHandler.formatColorCodes("&cThe area &e" + Args[1] + "&c doesn't exist, please try again."));
				}
			}
		}
		else
		{
			Player.sendMessage("/areas density <Area>");
		}
	}
	
	@SubCommandHandler(parent = "areas", name = "reload")
	public void reloadAreas(CommandSender Player, String[] Args)
	{
		EntityMechanic.entityAreaHandler.clearAreas();
		EntityMechanic.entityAreaHandler.LoadData();
		Player.sendMessage(ChatColor.GREEN + "Areas have been reloaded");
	}
	
	@SubCommandHandler(parent = "areas", name = "help")
	public void AreasHelp(CommandSender Sender, String[] Args)
	{
		HelpScreen HelpScreen = new HelpScreen("Areas Command Help");
		HelpScreen.setHeader(ChatColor.YELLOW + "<name> Page <page> of <maxpage>");
		HelpScreen.setFormat("<name> -- <desc>");
		HelpScreen.setFlipColor(ChatColor.GREEN, ChatColor.DARK_GREEN);
		
		HelpScreen.setEntry("/newarea <Name> <Radius>", ChatHandler.formatColorCodes("Used to create a new area"));
		HelpScreen.setEntry("/areas list", "View a list of all the areas, their coords, and how many mobs the area's config'd for");
		HelpScreen.setEntry("/addmob <Name> <Min> <Max> <MobType> [IsElite] [EliteChance]", "Adds an entity to an area for later spawning.");
		HelpScreen.setEntry("/areas help", "This help menu");
		HelpScreen.setEntry("/areas spawnentity <Area>", "Used to spawn an enemy in an area");
		HelpScreen.setEntry("/areas density <Area>", "Get an integer based on the density of the area");
		HelpScreen.setEntry("/areas reload", "reload all the area data from disk");
		HelpScreen.setEntry("/areas entities", "Get all the current entities and their info");

		
		if (Args.length == 1)
		{
			HelpScreen.sendTo(Sender, 1, 5);
		}
		else
		{
			if (Args[1] != null && StringUtils.isNumeric(Args[1]))
			{
				int Page = Integer.parseInt(Args[1]);
				HelpScreen.sendTo(Sender, Page, 5);
			}
		}
	}
	
	@CommandHandler(name = "newarea",permission = "entitymechanic.area.create", usage = "/newarea <AreaName> <SpawnRadius>")
	public void CreateArea(Player Player, String[] Args)
	{
		if (Args.length >= 2)
		{
			if (!Args[0].isEmpty())
			{
				String areaName = Args[0].toLowerCase();
				if (!EntityMechanic.entityAreaHandler.isArea(areaName))
				{
					if (!Args[1].isEmpty())
					{
						if (StringUtils.isNumeric(Args[1]))
						{
							int spawnRadius = Integer.parseInt(Args[1]);
							EntityArea Area = new EntityArea(areaName, EntityMechanic.entityAreaHandler.getLocation(Player.getLocation()), spawnRadius);
							EntityMechanic.entityAreaHandler.addArea(Area);
							EntityMechanic.entityAreaHandler.SaveData();
							Player.sendMessage(ChatColor.GREEN + "The Area " + ChatColor.YELLOW + areaName + ChatColor.GREEN + " has been created");
						}
						else
						{
							Player.sendMessage(ChatColor.YELLOW + "Radius is a number...");
						}
					}
					else
					{
						Player.sendMessage(ChatColor.YELLOW + "You need to include a radius");
					}
				}
				else
				{
					Player.sendMessage(ChatColor.YELLOW + "An area with that name already exists, please use another name.");
				}
			}
		}
		else
		{
			Player.sendMessage(ChatHandler.formatColorCodes("&cThe proper usage for this is &e/newarea <AreaName> <SpawnRadius &c please try again"));
		}
	}
	
	@CommandHandler(name = "addmob", permission = "eneitymechanic.area.addmob", usage = "/addmob <AreaName> <MinLevel> <MaxLevel> <MobType> <Boolean[IsElite]> <Percent[Elite Spawn Chance]>")
	public void AreaAddMob(Player Player, String[] Args)
	{
		if (Args.length >= 4)
		{
			if (!Args[0].isEmpty())
			{
				String areaName = Args[0].toLowerCase();
				if (EntityMechanic.entityAreaHandler.isArea(areaName))
				{
					if (!Args[1].isEmpty())
					{
						if (StringUtils.isNumeric(Args[1]))
						{
							int MinLevel = Integer.parseInt(Args[1]);
							if (!Args[2].isEmpty())
							{
								if (StringUtils.isNumeric(Args[2]))
								{
									int MaxLevel = Integer.parseInt(Args[2]);
									if (!Args[3].isEmpty())
									{
										String Type = Args[3];
										EntityType MobType = EntityUtility.getTypeByName(Type);
										if (MobType != null && MobType != EntityType.UNKNOWN)
										{
											if (Args.length > 4 && Args[4] != null && !Args[4].isEmpty())
											{
												String IsElite = Args[4];
												boolean Elite = false;
												if (IsElite.equalsIgnoreCase("yes") || IsElite.equalsIgnoreCase("true"))
												{
													Elite = true;
												}
												
												if (!Args[5].isEmpty())
												{
													if (StringUtils.isNumeric(Args[5]))
													{
														int eliteRate = Integer.parseInt(Args[5]);
														XmlEntity Entity = new XmlEntity(Type, MinLevel, MaxLevel, Elite, eliteRate);
														EntityMechanic.entityAreaHandler.getArea(areaName).addEntity(Entity);
														EntityMechanic.entityAreaHandler.SaveData();
														Player.sendMessage(ChatColor.GREEN + "The entity " + Type + " has been added to " + areaName);
													}
												}
												else
												{
													Player.sendMessage(ChatColor.YELLOW + "Please Include a spawn rate for the mobs");
												}
												
											}
											else
											{
												XmlEntity Entity = new XmlEntity(Type,MinLevel,MaxLevel,false,0);
												EntityMechanic.entityAreaHandler.getArea(areaName).addEntity(Entity);
												EntityMechanic.entityAreaHandler.SaveData();
												Player.sendMessage(ChatColor.GREEN + "The entity " + Type + " has been added to " + areaName);
											}
										}
										else
										{
											Player.sendMessage(ChatColor.YELLOW + "Invalid Mob Type '" + Type + "'");
										}
									}
									else
									{
										Player.sendMessage(ChatColor.YELLOW + "Include a Type.");
									}
								}
								else
								{
									Player.sendMessage(ChatColor.YELLOW + "Maximum level needs to be a number, not " + Args[2]);
								}
							}
							else
							{
								Player.sendMessage(ChatColor.YELLOW + "Include a maximum level...");
							}
						}
						else
						{
							Player.sendMessage(ChatColor.YELLOW + "Minimum level needs to be a number, not " + Args[1]);
						}
					}
					else
					{
						Player.sendMessage(ChatColor.GRAY + "Include a minimum level..");
					}
				}
				else
				{
					Player.sendMessage(areaName + " isn't a valid area...");
				}
			}
		}
	}
}
