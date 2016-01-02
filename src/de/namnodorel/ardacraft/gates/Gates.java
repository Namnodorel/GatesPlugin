package de.namnodorel.ardacraft.gates;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Gates extends JavaPlugin{
	
	private static Gates plugin;
	
	@Override
	public void onEnable(){
		
		plugin = this;
		
		Data.loadAll();
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new GlobalListener(), this);
		
		this.getCommand("gate").setExecutor(new GateCommand());
		
		//Save gates all 10 minutes
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

			@Override
			public void run() {
				System.out.println("Saving gates...");
				Data.saveAll();
				
			}
			
			
		}, 20L*60*20, 20L*60*10);
	}
	
	@Override
	public void onDisable(){
		
		Data.saveAll();
		
	}
	
	public static Gates getPlugin(){
		return plugin;
	}
}
