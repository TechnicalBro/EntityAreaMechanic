package com.caved_in.entityspawningmechanic.config;

import com.caved_in.commons.entity.Entities;
import org.bukkit.entity.EntityType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name = "entity")
public class XmlEntity {
	@Element(name = "type")
	private String type = "";

	private EntityType entityType = EntityType.UNKNOWN;

	@Element(name = "min-level")
	private int minLevel = 1;

	@Element(name = "max-level")
	private int maxLevel = 1;

	@Element(name = "elite")
	private boolean isElite = false;

	@Element(name = "elite-spawn-chance")
	private int eliteSpawnChance = 0;

	public XmlEntity(@Element(name = "type") String type,
					 @Element(name = "min-level") int minLevel,
					 @Element(name = "max-level") int maxLevel,
					 @Element(name = "elite") boolean isElite,
					 @Element(name = "elite-spawn-chance") int eliteSpawnChance) {
		this.entityType = Entities.getTypeByName(type);
		this.type = type;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		this.isElite = isElite;
		this.eliteSpawnChance = eliteSpawnChance;
	}

	public XmlEntity(EntityType type, int minLevel, int maxLevel, boolean isElite, int eliteSpawnChance) {
		this(type.name(),minLevel,maxLevel,isElite,eliteSpawnChance);
	}

	public EntityType getType() {
		return this.entityType;
	}

	public int getMinLevel() {
		return this.minLevel;
	}

	public int getMaxLevel() {
		return this.maxLevel;
	}

	public boolean isElite() {
		return this.isElite;
	}

	public int getEliteSpawnChance() {
		return this.eliteSpawnChance;
	}
}
