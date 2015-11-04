package com.caved_in.entityspawningmechanic.data;

import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class EntityWrapper {
	private UUID entityUUID;
	private int entityId = 0;
	private boolean isElite = false;
	private boolean isBoss = false;
	private int level = 0;
	private EntityType type = EntityType.UNKNOWN;

	public EntityWrapper(LivingEntity Entity, int Level) {
		this.entityUUID = Entity.getUniqueId();
		this.entityId = Entity.getEntityId();
		this.level = Level;
		this.type = Entity.getType();
	}

	public boolean isElite() {
		return this.isElite;
	}

	public EntityType getType() {
		return this.type;
	}

	public void setElite(boolean Value) {
		this.isElite = Value;
	}

	public boolean isBoss() {
		return this.isBoss;
	}

	public void setBoss(boolean Value) {
		this.isBoss = Value;
	}

	public int getLevel() {
		return this.level;
	}

	public int getEntityId() {
		return this.entityId;
	}

	public UUID getUniqueID() {
		return this.entityUUID;
	}
}
