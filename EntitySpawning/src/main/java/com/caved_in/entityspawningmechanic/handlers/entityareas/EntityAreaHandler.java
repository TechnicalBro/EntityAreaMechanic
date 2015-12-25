package com.caved_in.entityspawningmechanic.handlers.entityareas;

import java.io.File;
import java.util.*;

import com.caved_in.commons.entity.CreatureBuilder;
import com.caved_in.commons.entity.Entities;
import com.caved_in.commons.location.Locations;
import com.caved_in.commons.plugin.Plugins;
import com.caved_in.commons.utilities.NumberUtil;
import com.caved_in.entityspawningmechanic.EntityMechanic;
import com.caved_in.entityspawningmechanic.config.EntityArea;
import com.caved_in.entityspawningmechanic.config.XmlEntity;
import com.caved_in.entityspawningmechanic.entity.EntityCalculations;
import com.caved_in.entityspawningmechanic.event.EntityModifiedOnSpawnEvent;
import com.caved_in.entityspawningmechanic.event.EntityToBeSpawnedEvent;
import com.caved_in.entityspawningmechanic.handlers.entity.EntityHandler;
import com.caved_in.entityspawningmechanic.handlers.entity.EntityUtilities;
import org.apache.commons.io.FileUtils;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class EntityAreaHandler {
    private HashMap<String, EntityArea> areas = new HashMap<String, EntityArea>();

    private Set<Location> nonReplaceLocations = new HashSet<>();

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

        if (enemyToSpawn == null) {
            return;
        }

        int enemyLevel = NumberUtil.getRandomInRange(enemyToSpawn.getMinLevel(), enemyToSpawn.getMaxLevel());
        boolean elite = enemyToSpawn.isElite() && NumberUtil.percentCheck(enemyToSpawn.getEliteSpawnChance());
        Location loc = entityArea.getLocationInArea();

        EntityToBeSpawnedEvent event = new EntityToBeSpawnedEvent(entityArea, enemyToSpawn.getType(), elite, enemyLevel, loc);
        Plugins.callEvent(event);

        enemyLevel = event.getLevel();

        elite = event.isElite();

        int enemyHealth = EntityCalculations.generateHealth(enemyLevel, elite, false);

        //EntityWrapper entityWrapper = new EntityWrapper(spawnedEntity, enemyLevel);
        String entityName = EntityUtilities.generateEntityName(event.getType(), elite, false, enemyLevel);

        CreatureBuilder builder = CreatureBuilder.of(event.getType());

        nonReplaceLocations.add(loc);

        LivingEntity spawned = builder.name(entityName).maxHealth(enemyHealth).health(enemyHealth).spawn(loc);

        EntityHandler.addEntity(spawned, enemyLevel);
    }

    public boolean allowReplace(Location loc) {
        Location remove = null;
        for (Location check : nonReplaceLocations) {
            if (Locations.isInRadius(check, loc, 2)) {
                remove = check;
            }
        }

        if (remove == null) {
            return true;
        }

        nonReplaceLocations.remove(remove);
        return false;
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
        if (isArea(entityArea.toLowerCase())) {
            return getAreaDensity(getArea(entityArea.toLowerCase()));
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
        if (isArea(areaName.toLowerCase())) {
            return densityCheck(getArea(areaName.toLowerCase()));
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
