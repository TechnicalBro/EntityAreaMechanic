package com.caved_in.entityspawningmechanic.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.caved_in.commons.config.XmlLocation;
import com.caved_in.commons.location.Locations;
import org.bukkit.Location;
import org.simpleframework.xml.*;

@Root(name = "area")
public class EntityArea {
    private static Random random = new Random();

    @Element(name = "center", type = XmlLocation.class)
    private XmlLocation location;

    @Attribute(name = "spawn-radius")
    private int spawnRadius = 0;

    @ElementList(entry = "entity", name = "entities", inline = true, type = XmlEntity.class, required = false)
    private List<XmlEntity> entities = new ArrayList<XmlEntity>();

    @Attribute(name = "name")
    private String areaName = "";

    public EntityArea(@Attribute(name = "name") String areaName, @Element(name = "center", type = XmlLocation.class) XmlLocation location, @Attribute(name = "spawn-radius") int spawnRadius,@ElementList(entry = "entity", name = "entities", inline = true, type = XmlEntity.class,required = false)List<XmlEntity> entities) {
        this.areaName = areaName;
        this.location = location;
        this.spawnRadius = spawnRadius;
        if (entities != null) {
            this.entities = entities;
        }
    }

    public EntityArea(String areaName, Location loc, int spawnRadius) {
        this(areaName, new XmlLocation(loc), spawnRadius, null);
    }

    public XmlEntity getRandomEntity() {
        if (entities == null || entities.size() == 0) {
            return null;
        }
        return entities.get(random.nextInt(entities.size()));
    }

    public Location getLocationInArea() {
        //todo investigate spawning in center instead of random radius.
        return Locations.getRandomLocation(location, (int) getSpawnRadius());
    }

    public XmlLocation getLocation() {
        return location;
    }

    public double getSpawnRadius() {
        return spawnRadius;
    }

    public List<XmlEntity> getEntities() {
        return entities;
    }

    public void addEntity(XmlEntity Entity) {
        entities.add(Entity);
    }

    public void addEntity(String type, int minLevel, int maxLevel, boolean isElite, int eliteSpawnChance) {
        addEntity(new XmlEntity(type, minLevel, maxLevel, isElite, eliteSpawnChance));
    }

    public String getName() {
        return this.areaName;
    }
}
