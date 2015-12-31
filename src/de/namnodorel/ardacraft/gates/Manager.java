package de.namnodorel.ardacraft.gates;

import org.bukkit.Location;

public class Manager {
	
	public static boolean isFrameValid(Area frame, Area gate){
		
		if(frame.getXDimension() != gate.getXDimension() || frame.getYDimension() != gate.getYDimension() || frame.getZDimension() != gate.getZDimension()){
			return false;
		}
		
		return true;
	}
	
	public static boolean isInsideAnyGate(Location loc){
		
		for(String key : Data.gates.keySet()){
			
			if(Data.gates.get(key).getArea().isInside(loc)){
				return true;
			}
			
		}
		
		return false;
	}
	
	public static GateEntry getGateByLocation(Location loc){

		GateEntry g = null;
		
		for(String key : Data.gates.keySet()){
			
			g = Data.gates.get(key);
			
			if(!g.getArea().isInside(loc)){
				continue;
			}else{
				break;
			}
			
		}
		
		return g;
	}
	
	public static GateEntry getNearbyGates(Location loc){
		
		for(String key : Data.gates.keySet()){
			
			if(isNearby(loc, Data.gates.get(key))){
				return Data.gates.get(key);
			}
			
		}
		
		
		return null;
	}
	
	private static boolean isNearby(Location loc, GateEntry g){
		
		if(g.getArea().getFirstLocation().distance(loc) < 5 || g.getArea().getFirstLocation().distance(loc) < 5){
			return true;
		}
		
		return false;
	}
	
}
