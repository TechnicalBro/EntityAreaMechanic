package com.caved_in.entityspawningmechanic.handlers.entityareas;

import java.io.File;
import java.util.*;

import com.caved_in.commons.entity.CreatureBuilder;
import com.caved_in.commons.entity.Entities;
import com.caved_in.commons.utilities.NumberUtil;
import com.caved_in.entityspawningmechanic.EntityMechanic;
import com.caved_in.entityspawningmechanic.config.EntityArea;
import com.caved_in.entityspawningmechanic.config.XmlEntity;
import com.caved_in.entityspawningmechanic.entity.EntityCalculations;
import com.caved_in.entityspawningmechanic.handlers.entity.EntityHandler;
import com.caved_in.entityspawningmechanic.handlers.entity.EntityUtilities;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.LivingEntity;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class EntityAreaHandler {
    private HashMap<String, EntityArea> areas = new HashMap<String, EntityArea>();

    public EntityAreaHandler() {

    }

    /**
     * Get a full list of the currently active entity areas
     *
     * @return
     */
    public List<EntityArea> getAreas() {
        return new ArrayList<EntityArea>(this.areas.values());
    }

    /**
     * Add an area to be handled and managed
     *
     * @param area
     */
    public void addArea(EntityArea area) {
        if (!this.areas.containsKey(area.getName())) {
            this.areas.put(area.getName(), area);
        }
    }

    /**
     * Remove an entity area
     *
     * @param area
     */
    public void removeArea(EntityArea area) {
        if (this.areas.containsValue(area)) {
            this.areas.remove(area.getName());
        }
    }

    public void removeArea(String name) {
        if (this.areas.containsKey(name)) {
            this.areas.remove(name);
        }
    }

    public void clearAreas() {
        this.saveData();
        this.areas.clear();
    }

    public EntityArea getArea(String name) {
        return this.areas.get(name);
    }

    public boolean isArea(String name) {
        return this.areas.containsKey(name);
    }

    public void saveData() {
        Serializer Serializer = new Persister();
        for (EntityArea Area : this.getAreas()) {
            try {
                Serializer.write(Area, new File(EntityMechanic.AREA_FOLDER + Area.getName() + ".xml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadData() {
        Serializer Serializer = new Persister();
        for (String file : getFiles()) {
            try {
                EntityArea Area = Serializer.read(EntityArea.class, new File(file));
                //todo Maybe make a de-serializer for this to load entities
                this.areas.put(Area.getName(), Area);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void spawnRandomEntity(EntityArea entityArea) {
        XmlEntity enemyToSpawn = entityArea.getRandomEntity();
        int enemyLevel = NumberUtil.getRandomInRange(enemyToSpawn.getMinLevel(), enemyToSpawn.getMaxLevel());
        int enemyHealth = 0;

        //EntityWrapper entityWrapper = new EntityWrapper(spawnedEntity, enemyLevel);
        String entityName = "";

        CreatureBuilder builder = CreatureBuilder.of(enemyToSpawn.getType());

        if (enemyToSpawn.isElite() && NumberUtil.percentCheck(enemyToSpawn.getEliteSpawnChance())) {
            enemyHealth = EntityCalculations.generateHealth(enemyLevel, true, false);
            entityName = EntityUtilities.generateEntityName(enemyToSpawn.getType(), true, false, enemyLevel);
        } else {
            enemyHealth = EntityCalculations.generateHealth(enemyLevel, false, false);
            entityName = EntityUtilities.generateEntityName(enemyToSpawn.getType(), false, false, enemyLevel);
        }

        LivingEntity spawned = builder.name(entityName).maxHealth(enemyHealth).health(enemyHealth).spawn(entityArea.getLocation());

        EntityHandler.addEntity(spawned, enemyLevel);
    }

    public void spawnRandomEntity(String entityArea) {
        if (this.isArea(entityArea)) {
            this.spawnRandomEntity(this.getArea(entityArea));
        }
    }


    public double getAreaDensity(EntityArea entityArea) {
        double entitySearchRadius = ((entityArea.getSpawnRadius() * 2));
        Set<LivingEntity> areaEntities = Entities.getLivingEntitiesNearLocation(entityArea.getLocation(), (int) entitySearchRadius);

        if (areaEntities.size() > 0) {
            double dividedNumbers = (entitySearchRadius * 10) / areaEntities.size();
            return dividedNumbers;
        } else {
            return entitySearchRadius * 10;
        }
    }

    public double getAreaDensity(String entityArea) {
        if (this.isArea(entityArea.toLowerCase())) {
            return this.getAreaDensity(this.getArea(entityArea.toLowerCase()));
        }
        return -1;
    }

    public boolean densityCheck(EntityArea entityArea) {
        int areaEntities = Entities.getEntitiesNearLocation(entityArea.getLocation(), (int) entityArea.getSpawnRadius()).size();
        double areaDensity = this.getAreaDensity(entityArea);
        double radiusCheck = (entityArea.getSpawnRadius() * 2) * 10;
        return (areaEntities < (entityArea.getSpawnRadius() / 2) ? (new Random().nextInt((int) radiusCheck) <= areaDensity) : false);
    }

    public boolean densityCheck(String areaName) {
        if (this.isArea(areaName.toLowerCase())) {
            return this.densityCheck(this.getArea(areaName.toLowerCase()));
        }
        return false;
    }

    public List<String> getFiles() {
        List<String> areaFiles = new ArrayList<String>();
        for (File Area : FileUtils.listFiles(new File(EntityMechanic.AREA_FOLDER), new String[]{"xml"}, false)) {
            areaFiles.add(EntityMechanic.AREA_FOLDER + Area.getName());
        }
        return areaFiles;
    }
}
