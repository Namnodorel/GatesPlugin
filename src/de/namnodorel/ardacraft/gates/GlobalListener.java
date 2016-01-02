package de.namnodorel.ardacraft.gates;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;


public class GlobalListener implements Listener{
	
	
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent e){
		
		if(!(e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK)){
			return;
		}
		
		if(Manager.isInsideAnyGate(e.getClickedBlock().getLocation())){
			
			//The 'final' is needed to use this variable inside the scheduler
			final GateEntry g = Manager.getGateByLocation(e.getClickedBlock().getLocation());
			
			//If the Gate is in a mode which doesn't supports opening by click, just cancel the event
			if(g.getMode() == GateMode.COMMAND_ONLY || g.getMode() == GateMode.AUTO_PERMISSION || g.getMode() == GateMode.AUTO_EVERYONE){
				e.setCancelled(true);
				return;
			}
			
			//Check Permission, if needed
			if(g.getMode() == GateMode.CLICK_PERMISSION && !e.getPlayer().hasPermission("de.namnodorel.ardacraft.gates.interact." + g.getName())){
				e.setCancelled(true);
				return;
			}
			
			//If the gate is moving, return
			if(g.isMoving()){
				return;
			}
			
			//Open the Gate
			g.open();
			
			//Run the scheduler to close the gate after five seconds
			Bukkit.getScheduler().scheduleSyncDelayedTask(Gates.getPlugin(), new Runnable(){

				@Override
				public void run() {
					g.close();
					
				}
				
				//Wait, until the gate has opened (g.getWaitMS()*g.getFrames().size()) and then wait 5 seconds before closing the gate (5*20L)
			}, g.getWaitMS()*g.getFrames().size() + g.getPauseMS());
			
			//Cancel the event, no player is supposed to break Blocks inside of a gate to prevent spamming
			e.setCancelled(true);
			return;
		}
		
		//Check wether or not the player has the required Item
		if(!(e.getPlayer().getItemInHand().hasItemMeta() && e.getPlayer().getItemInHand().getItemMeta().hasLore() && e.getPlayer().getItemInHand().getItemMeta().getLore().contains("Tool zum Erstellen von animierten Toren"))){
			return;
		}
		
		Player p = e.getPlayer();
		
		//Create cache for the player, if it doesn't exist already
		if(!Data.caches.containsKey(p.getName())){
			Data.caches.put(p.getName(), new Area());
		}
		
		//Set positions
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
			Data.caches.get(p.getName()).setFirstLocation(e.getClickedBlock().getLocation());
			p.sendMessage("§aPosition 1 wurde gesetzt auf: " + e.getClickedBlock().getLocation().getX() + "//" + e.getClickedBlock().getLocation().getY() + "//" + e.getClickedBlock().getLocation().getZ());
		}else if(e.getAction() == Action.LEFT_CLICK_BLOCK){
			Data.caches.get(p.getName()).setSecondLocation(e.getClickedBlock().getLocation());
			p.sendMessage("§aPosition 2 wurde gesetzt auf: " + e.getClickedBlock().getLocation().getX() + "//" + e.getClickedBlock().getLocation().getY() + "//" + e.getClickedBlock().getLocation().getZ());
		}

		e.setCancelled(true);
		
	}
	
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		
		GateEntry g = Manager.getNearbyGates(e.getPlayer().getLocation());

		//If there is no gate in range, return
		if(g == null){
			return;
		}

		//If the gate is in any other mode than auto, return
		if(!(g.getMode() == GateMode.AUTO_EVERYONE || g.getMode() == GateMode.AUTO_PERMISSION)){
			return;
		}

		//If a permission is needed which the player doesn't have, return
		if(g.getMode() == GateMode.AUTO_PERMISSION && !e.getPlayer().hasPermission("de.namnodorel.ardacraft.gates.interact." + g.getName())){
			return;
		}

		//If the gate is moving, return
		if(g.isMoving()){
			return;
		}

		//Open the gate
		g.open();
		
		//Run the scheduler to close the gate after five seconds
		Bukkit.getScheduler().scheduleSyncDelayedTask(Gates.getPlugin(), new Runnable(){

			@Override
			public void run() {
				g.close();
				
			}
			
			//Wait, until the gate has opened (g.getWaitMS()*g.getFrames().size()) and then wait 5 seconds before closing the gate (5*20L)
		}, g.getWaitMS()*g.getFrames().size() + g.getPauseMS());

	}
}
