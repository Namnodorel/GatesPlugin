package de.namnodorel.ardacraft.gates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Data {
	
	public static HashMap<String, Area> caches = new HashMap<>();
	public static HashMap<String, GateEntry> gates = new HashMap<>();
	public static HashMap<String, GateEntry> selectedgates = new HashMap<>();
	public static List<String> gatesmaybedeleted = new ArrayList<>();
	
	public static void saveAll(){
		//Saves positions of gates, custom sounds etc.
	}
	
	public static void loadAll(){
		//Loads existing gates, custom sounds etc.
	}
	
	public static void removeGateFromDatabase(String name){
		//Removes Gate from Database (the name is unique!)
	}
	
}
