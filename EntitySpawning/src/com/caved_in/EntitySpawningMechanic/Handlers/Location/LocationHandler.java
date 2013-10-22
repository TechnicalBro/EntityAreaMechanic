package com.caved_in.EntitySpawningMechanic.Handlers.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;

public class LocationHandler
{
	public static List<LivingEntity> getEntitiesNearLocation(Location Location, int Radius)
	{
		List<LivingEntity> Return = new ArrayList<LivingEntity>();
		for (LivingEntity Entity : Location.getWorld().getLivingEntities())
		{
			if (Entity.getLocation().distance(Location) < Radius)
			{
				Return.add(Entity);
			}
		}
		return Return;
	}
	
	public static Location getRandomLocation(Location Center, int Radius)
	{
		Random rand = new Random();
		double angle = rand.nextDouble()*360; //Generate a random angle
		double x = Center.getX() + (rand.nextDouble()*Radius*Math.cos(Math.toRadians(angle)));
		double z = Center.getZ() + (rand.nextDouble()*Radius*Math.sin(Math.toRadians(angle)));
		double y = Center.getY();
		if (Center.getWorld().getBlockAt(Center).getType() == Material.AIR && Center.getWorld().getBlockAt(Center).getRelative(BlockFace.UP).getType() == Material.AIR)
		{
			return new Location(Center.getWorld(),x,y,z);
		}
		else
		{
			Block emptySpace = getNearestEmptySpace(Center.getWorld().getBlockAt(new Location(Center.getWorld(),x,y,z)),Radius);
			if (emptySpace != null)
			{
				return emptySpace.getLocation();
			}
			else
			{
				return new Location(Center.getWorld(),x,(Center.getWorld().getHighestBlockYAt((int)x,(int)z)),z);
			}
		}
	}
	
	public static Block getNearestEmptySpace(Block b, int maxradius) {
        BlockFace[] faces = {BlockFace.UP, BlockFace.NORTH, BlockFace.EAST};
        BlockFace[][] orth = {{BlockFace.NORTH, BlockFace.EAST}, {BlockFace.UP, BlockFace.EAST}, {BlockFace.NORTH, BlockFace.UP}};
        for (int r = 0; r <= maxradius; r++) {
            for (int s = 0; s < 6; s++) {
                BlockFace f = faces[s%3];
                BlockFace[] o = orth[s%3];
                if (s >= 3)
                    f = f.getOppositeFace();
                Block c = b.getRelative(f, r);
                for (int x = -r; x <= r; x++) {
                    for (int y = -r; y <= r; y++) {
                        Block a = c.getRelative(o[0], x).getRelative(o[1], y);
                        if (a.getTypeId() == 0 && a.getRelative(BlockFace.UP).getTypeId() == 0)
                            return a;
                    }
                }
            }
        }
        return null;// no empty space within a cube of (2*(maxradius+1))^3
    }
}
