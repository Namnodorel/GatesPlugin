package de.namnodorel.ardacraft.gates;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Data {
	
	public static HashMap<String, Area> caches = new HashMap<>();
	public static HashMap<String, GateEntry> gates = new HashMap<>();
	public static HashMap<String, GateEntry> selectedgates = new HashMap<>();
	public static List<String> gatesmaybedeleted = new ArrayList<>();
	
	public static void saveAll(){
		
		if(!Gates.getPlugin().getDataFolder().exists()){
			try {
				Files.createDirectories(Paths.get(Gates.getPlugin().getDataFolder().getAbsolutePath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Save all gates
		saveHashMap(Gates.getPlugin().getDataFolder().getAbsolutePath() + java.io.File.separator + "gates.ser", gates);
		
	}
	
	public static void loadAll(){
		//Loads existing gates
		gates = loadHashMap(Gates.getPlugin().getDataFolder().getAbsolutePath() + java.io.File.separator + "gates.ser", gates);
	}
	
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, GateEntry> loadHashMap(String name, HashMap<String, GateEntry> f){
			try {
					try {	
							FileInputStream fis = new FileInputStream(name);
							ObjectInputStream eingabe = new ObjectInputStream(fis);
							f = (HashMap<String, GateEntry>)eingabe.readObject();
							eingabe.close();
							return f;
							
					} catch(ClassNotFoundException ex){
						System.err.println("[GatePlugin]Die Tore konnten nicht geladen werden!");
						return f;
					}
							
				} catch(IOException ex){
					System.err.println("[GatePlugin]Die Tore konnten nicht geladen werden.");
					return f;
				}
	}
	
	public static boolean saveHashMap(String r, HashMap<String, GateEntry> f){
		try {
			FileOutputStream fos = new FileOutputStream(r);
			
			try {
				
				ObjectOutputStream ausgabe = new ObjectOutputStream(fos);
				ausgabe.writeObject(f);
				ausgabe.close();
			
			} catch(IOException ex){
				System.err.println("[GatePlugin][FATAL ERRROR]Die Tore konnten nicht gespeichert werden!");
				ex.printStackTrace();
				return false;
			}
			
		} catch(FileNotFoundException ex) {
			System.err.println("[GatePlugin][FATAL ERRROR]Die Tore konnten nicht gespeichert werden!");
			return false;
		}
		
		
		
		
		return true;
	}
	
	
	@SuppressWarnings("serial")
	static class SerLocation implements Serializable{
		
		private Integer X;
		private Integer Y;
		private Integer Z;
		private String world;
		
		public Integer getX() {
			return X;
		}
		public SerLocation setX(Integer x) {
			X = x;
			return this;
		}
		public Integer getY() {
			return Y;
		}
		
		public SerLocation setY(Integer y) {
			Y = y;
			return this;
		}
		public Integer getZ() {
			return Z;
		}
		public SerLocation setZ(Integer z) {
			Z = z;
			return this;
		}
		public String getWorld() {
			return world;
		}
		public SerLocation setWorld(String world) {
			this.world = world;
			return this;
		}
		
	}
}
package de.namnodorel.ardacraft.gates;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Data {
	
	public static HashMap<String, Area> caches = new HashMap<>();
	public static HashMap<String, GateEntry> gates = new HashMap<>();
	public static HashMap<String, GateEntry> selectedgates = new HashMap<>();
	public static List<String> gatesmaybedeleted = new ArrayList<>();
	
	public static void saveAll(){
		
		if(!Gates.getPlugin().getDataFolder().exists()){
			try {
				Files.createDirectories(Paths.get(Gates.getPlugin().getDataFolder().getAbsolutePath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Save all gates
		saveHashMap(Gates.getPlugin().getDataFolder().getAbsolutePath() + java.io.File.separator + "gates.ser", gates);
		
	}
	
	public static void loadAll(){
		//Loads existing gates
		gates = loadHashMap(Gates.getPlugin().getDataFolder().getAbsolutePath() + java.io.File.separator + "gates.ser", gates);
	}
	
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, GateEntry> loadHashMap(String name, HashMap<String, GateEntry> f){
			try {
					try {	
							FileInputStream fis = new FileInputStream(name);
							ObjectInputStream eingabe = new ObjectInputStream(fis);
							f = (HashMap<String, GateEntry>)eingabe.readObject();
							eingabe.close();
							return f;
							
					} catch(ClassNotFoundException ex){
						System.err.println("[GatePlugin]Die Tore konnten nicht geladen werden!");
						return f;
					}
							
				} catch(IOException ex){
					System.err.println("[GatePlugin]Die Tore konnten nicht geladen werden.");
					return f;
				}
	}
	
	public static boolean saveHashMap(String r, HashMap<String, GateEntry> f){
		try {
			FileOutputStream fos = new FileOutputStream(r);
			
			try {
				
				ObjectOutputStream ausgabe = new ObjectOutputStream(fos);
				ausgabe.writeObject(f);
				ausgabe.close();
			
			} catch(IOException ex){
				System.err.println("[GatePlugin][FATAL ERRROR]Die Tore konnten nicht gespeichert werden!");
				ex.printStackTrace();
				return false;
			}
			
		} catch(FileNotFoundException ex) {
			System.err.println("[GatePlugin][FATAL ERRROR]Die Tore konnten nicht gespeichert werden!");
			return false;
		}
		
		
		
		
		return true;
	}
	
	
	@SuppressWarnings("serial")
	static class SerLocation implements Serializable{
		
		private Integer X;
		private Integer Y;
		private Integer Z;
		private String world;
		
		public Integer getX() {
			return X;
		}
		public SerLocation setX(Integer x) {
			X = x;
			return this;
		}
		public Integer getY() {
			return Y;
		}
		
		public SerLocation setY(Integer y) {
			Y = y;
			return this;
		}
		public Integer getZ() {
			return Z;
		}
		public SerLocation setZ(Integer z) {
			Z = z;
			return this;
		}
		public String getWorld() {
			return world;
		}
		public SerLocation setWorld(String world) {
			this.world = world;
			return this;
		}
		
	}
}
