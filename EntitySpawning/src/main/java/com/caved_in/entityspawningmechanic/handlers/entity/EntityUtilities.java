package com.caved_in.entityspawningmechanic.handlers.entity;

import com.caved_in.commons.chat.Chat;
import com.caved_in.commons.entity.Entities;
import com.caved_in.entityspawningmechanic.data.EntityWrapper;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class EntityUtilities {
    public static String generateEntityName(EntityWrapper wrapper) {
        String entityName = StringUtils.capitalize(wrapper.getType().toString().toLowerCase().replace("_", " "));
        String parsedEntityName = "";

        parsedEntityName = (wrapper.isElite()) ? "&eElite " : "&r";
        //parsedEntityName = (Entity_Wrapper.isBoss() == true) ? "&cBoss " : "&r";
        parsedEntityName = (wrapper.isElite() && wrapper.isBoss()) ? "&0" : parsedEntityName;

        parsedEntityName = parsedEntityName + entityName + "&r &l-&r &6Lvl &r" + wrapper.getLevel();
        return Chat.format(parsedEntityName);
    }

    public static String generateEntityName(EntityType enemyType, boolean isElite, boolean isBoss, int enemyLevel) {
        String entityName = StringUtils.capitalize(enemyType.toString().toLowerCase().replace("_", " "));
        String parsedEntityName = "";

        parsedEntityName = (isElite) ? "&eElite " : "&r";
        //parsedEntityName = (isBoss == true) ? "&c" : "&r";
        parsedEntityName = (isElite && isBoss) ? "&0" : parsedEntityName;

        parsedEntityName = parsedEntityName + entityName + "&r &l-&r &6Lvl &r" + enemyLevel;
        return Chat.format(parsedEntityName);
    }

    public static String getNameWithoutHealthBar(LivingEntity Entity) {
        String entityName = Entity.getCustomName();
        entityName = StringUtils.substringBefore(entityName, "  ");
        //hatHandler.sendMessageToConsole(entityName + ChatColor.RESET + " comes from " + Entity.getCustomName());
        return entityName;
    }

    public static String getHealthBar(LivingEntity entity) {
        String bar = entity.getCustomName();
        bar = StringUtils.substringAfter(bar, "  ");
        return bar;
    }

    public static String generateHealthBar(LivingEntity entity) {
        double enemyHealthMax = ((Damageable) entity).getMaxHealth();
        double enemyHealthCurrent = ((Damageable) entity).getHealth();
        double enemyHealthPercentage = (enemyHealthCurrent / enemyHealthMax) * 100;
        //ChatHandler.sendMessageToConsole(entity.getCustomName() + " health is " + enemyHealthCurrent + " out of " + enemyHealthMax + " which is " + enemyHealthPercentage + "%");
        int healthBarAmount = (int) Math.round(enemyHealthPercentage / 10);
        return Entities.getHealthBarColor(enemyHealthPercentage) + HealthBar.getHealthBar(healthBarAmount);
    }

    /**
     * @param entityCurrentHP
     * @param entityMaxHP
     * @return
     */
    public static String generateHealthBar(double entityCurrentHP, double entityMaxHP) {
        double entityHealthPercentage = (entityCurrentHP / entityMaxHP) * 100;
        return Entities.getHealthBarColor(entityHealthPercentage) + HealthBar.getHealthBar((int) Math.round(entityHealthPercentage / 10));
    }
}
