package de.namnodorel.ardacraft.gates;

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
	}
	
	@Override
	public void onDisable(){
		
		Data.saveAll();
		
	}
	
	public static Gates getPlugin(){
		return plugin;
	}
}
