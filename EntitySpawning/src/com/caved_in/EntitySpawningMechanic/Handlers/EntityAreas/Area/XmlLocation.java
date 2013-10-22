package com.caved_in.EntitySpawningMechanic.Handlers.EntityAreas.Area;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.simpleframework.xml.Element;

public class XmlLocation
{
	
	@Element
	private double X = 0.0;
	
	@Element
	private double Y = 0.0;
	
	@Element
	private double Z = 0.0;
	
	@Element(name = "world")
	private String World = "";
	
	public XmlLocation(@Element(name = "X") double X, @Element(name = "Y") double Y, @Element(name = "Z")double Z, @Element(name = "world") String World)
	{
		this.World = World;
		this.X = X;
		this.Y = Y;
		this.Z = Z;
	}
	
	public Location getLocation()
	{
		return new Location(Bukkit.getWorld(this.World),this.X,this.Y,this.Z);
	}
	
	public String getWorld()
	{
		return this.World;
	}
	
	public double getX()
	{
		return this.X;
	}
	
	public double getY()
	{
		return this.Y;
	}
	
	public double getZ()
	{
		return this.Z;
	}
}
