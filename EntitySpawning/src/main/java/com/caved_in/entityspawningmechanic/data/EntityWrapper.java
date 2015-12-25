package com.caved_in.entityspawningmechanic.data;

import java.util.UUID;

import com.caved_in.commons.chat.Chat;
import com.caved_in.commons.entity.Entities;
import com.caved_in.entityspawningmechanic.EntityMechanic;
import com.caved_in.entityspawningmechanic.handlers.entity.EntityUtilities;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class EntityWrapper {
    private UUID entityUUID;
    private int entityId = 0;
    private boolean isElite = false;
    private boolean isBoss = false;
    private int level = 0;
    private EntityType type = EntityType.UNKNOWN;

    private long lastDamaged = 0l;
    private long barExpire = 0l;

    private String healthBar = "";
    private String mobName = "";

    public EntityWrapper(LivingEntity Entity, int Level) {
        this.entityUUID = Entity.getUniqueId();
        this.entityId = Entity.getEntityId();
        this.level = Level;
        this.type = Entity.getType();
        this.mobName = EntityUtilities.getNameWithoutHealthBar(Entity);
        this.healthBar = EntityUtilities.getHealthBar(Entity);
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

    public void damaged() {
        LivingEntity entity = Entities.getEntityByUUID(entityUUID);

        lastDamaged = System.currentTimeMillis();
        barExpire = Long.sum(lastDamaged, EntityMechanic.Config.REMOVE_HEALTHBAR_TIME);

        mobName = EntityUtilities.getNameWithoutHealthBar(entity);
        healthBar = EntityUtilities.generateHealthBar(entity);

        Entities.setName(entity, String.format("%s  %s", mobName,healthBar));
//        if (customName.contains("  ")) {
//        } else {
//
//        }
//
//        if (damagedEntity.getCustomName().contains("  ")) {
//            damagedEntity.setCustomName();
//            Entities.damage(damagedEntity, 0);
//        } else {
//            damagedEntity.setCustomName(damagedEntity.getCustomName() + "  " + EntityUtilities.generateHealthBar(((Damageable) damagedEntity).getHealth() - event.getDamage(), ((Damageable) damagedEntity).getMaxHealth()));
//            Entities.damage(damagedEntity, 0);
//        }
        Chat.debug(Chat.format("Updated mob %s name to %s, healthbar to %s", entityUUID.toString(), mobName, healthBar));
    }

    public String getMobNameWithoutHealthBar() {
        if (mobName == null) {
            mobName = EntityUtilities.getNameWithoutHealthBar(getEntity());
            Chat.debug("retrieved mob name of " + mobName);
        }
        return mobName;
    }

    public LivingEntity getEntity() {
        return Entities.getEntityByUUID(entityUUID);
    }

    public String getHealthBar() {
        return healthBar;
    }

    public boolean shouldRemoveHealthBar() {
        return System.currentTimeMillis() >= barExpire;
    }
}
