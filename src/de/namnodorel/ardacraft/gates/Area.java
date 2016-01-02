package de.namnodorel.ardacraft.gates;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;

@SuppressWarnings("serial")
public class Area implements Serializable{
	
	private Data.SerLocation first = null;
	private Data.SerLocation second = null;
	
	
	//Constructor to copy existing areas
	public Area(Area existing){
		first = new Data.SerLocation().setWorld(existing.getFirstLocation().getWorld().getName()).setX((int)existing.getFirstLocation().getX()).setY((int)existing.getFirstLocation().getY()).setZ((int)existing.getFirstLocation().getZ());
		second = new Data.SerLocation().setWorld(existing.getSecondLocation().getWorld().getName()).setX((int)existing.getSecondLocation().getX()).setY((int)existing.getSecondLocation().getY()).setZ((int)existing.getSecondLocation().getZ());
	}
	
	public Area() {
		
	}

	public Location getFirstLocation() {
		Location l = new Location(Bukkit.getWorld(first.getWorld()), first.getX(), first.getY(), first.getZ());
		return l;
	}
	public void setFirstLocation(Location first) {
		Data.SerLocation l = new Data.SerLocation().setWorld(first.getWorld().getName()).setX((int) first.getX()).setY((int)first.getY()).setZ((int)first.getZ());
		this.first = l;
	}
	public Location getSecondLocation() {
		Location l = new Location(Bukkit.getWorld(second.getWorld()), second.getX(), second.getY(), second.getZ());
		return l;
	}
	public void setSecondLocation(Location second) {
		Data.SerLocation l = new Data.SerLocation().setWorld(second.getWorld().getName()).setX((int) second.getX()).setY((int)second.getY()).setZ((int)second.getZ());
		this.second = l;
	}
	
	public Area getFixedArea(){
		
		//Try to sort the coordinates to get one with all small values and one with all big ones
		
		Area a = new Area();
		
		Location l1 = getFirstLocation();
		Location l2 = getSecondLocation();
		
		int x1, x2, y1, y2, z1, z2;
		
		x1 = Integer.max((int)l1.getX(), (int)l2.getX());
		x2 = Integer.min((int)l1.getX(), (int)l2.getX());
		
		y1 = Integer.max((int)l1.getY(), (int)l2.getY());
		y2 = Integer.min((int)l1.getY(), (int)l2.getY());
		
		z1 = Integer.max((int)l1.getZ(), (int)l2.getZ());
		z2 = Integer.min((int)l1.getZ(), (int)l2.getZ());
		
		a.setFirstLocation(new Location(Bukkit.getWorld("RPG"), x1, y1, z1));
		a.setSecondLocation(new Location(Bukkit.getWorld("RPG"), x2, y2, z2));
		
		//TODO Add real world name
		
		return a;
	}
	
	public int getXDimension(){
		
		//Calculate the x-dimension (needed to get the volume)
		
		Location first = getFixedArea().getFirstLocation();
		Location second = getFixedArea().getSecondLocation();
		
		int x1 = (int)first.getX();
		int x2 = (int)second.getX();
		
		int x = x1 - x2 + 1;
		
		return x;
	}
	public int getYDimension(){
		
		//Calculate the y-dimension (needed to get the volume)
		
		Location first = getFixedArea().getFirstLocation();
		Location second = getFixedArea().getSecondLocation();
		
		int y1 = (int)first.getY();
		int y2 = (int)second.getY();
		
		int y = y1 - y2 + 1;
		
		return y;
	}
	public int getZDimension(){
		
		//Calculate the z-dimension (needed to get the volume)
		
		Location first = getFixedArea().getFirstLocation();
		Location second = getFixedArea().getSecondLocation();
		
		int z1 = (int)first.getZ();
		int z2 = (int)second.getZ();
		
		int z = z1 - z2 + 1;
		
		return z;
	}
	
	public boolean isInside(Location loc){
		
		//Trys to figure out wether or not the given location is inside this area
		
		Area a = getFixedArea();
		
		if(a.getFirstLocation().getX() >= loc.getX() && a.getFirstLocation().getY() >= loc.getY() && a.getFirstLocation().getZ() >= loc.getZ()){
			if(a.getSecondLocation().getX() <= loc.getX() && a.getSecondLocation().getY() <= loc.getY() && a.getSecondLocation().getZ() <= loc.getZ()){
				return true;
				
			}
			
		}
		
		return false;
	}
	
}
