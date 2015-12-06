package de.namnodorel.ardacraft.gates;

import org.bukkit.Location;

public class BlockAreaManager {
	
	@SuppressWarnings("deprecation")
	public static void copyBlockArea(Area from, Area to){
		
		from = from.getFixedArea();
		to = to.getFixedArea();
		
		Location l1;
		Location l2;
		
		for(int dimx = 0; dimx < from.getXDimension(); ++dimx){
			
			for(int dimy = 0; dimy < from.getYDimension(); ++dimy){
				
				for(int dimz = 0; dimz < from.getZDimension(); ++dimz){
					
					l1 = new Location(to.getFirstLocation().getWorld(), to.getFirstLocation().getX() - dimx, to.getFirstLocation().getY() - dimy, to.getFirstLocation().getZ() - dimz);
					l2 = new Location(from.getFirstLocation().getWorld(), from.getFirstLocation().getX() - dimx, from.getFirstLocation().getY() - dimy, from.getFirstLocation().getZ() - dimz);
					
					l1.getBlock().setType(l2.getBlock().getType());
					l1.getBlock().setData(l2.getBlock().getData());
					
					//TODO Add EntitySupport
					
				}
				
			}
			
		}
		
	}
	
}
