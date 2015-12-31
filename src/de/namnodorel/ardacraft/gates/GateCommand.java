package de.namnodorel.ardacraft.gates;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;

public class GateCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		
		//A permission is needed to use this command
		if(!sender.hasPermission(new Permission("de.namnodorel.ardacraft.gates.command"))){
			sender.sendMessage("§cDu hast nicht die notwendige Berechtigung, diesen Befehl aus zu führen!");
			return true;
		}
		
		//The player needs to enter a sub-command
		if(args.length == 0){
			sender.sendMessage("§cBitte gib ein weiteres Argument ein!");
			return true;
		}
		
		//Gives the player the Gate-Tool, which is needed to create Gates and Frames
		if(args[0].equalsIgnoreCase("wand")){
			
			//The console can't get items
			if(!(sender instanceof Player)){
				System.err.println("[GatesPlugin]: Diese Befehl kann nur von Spielern ausgeführt werden!");
			}else{
				//We know now for sure that a player used the command
				Player p = (Player)sender;
				
				//Configure the item
				ItemStack item = new ItemStack(Material.STICK);
				ItemMeta meta = item.getItemMeta();
				List<String> lore = new ArrayList<>();
				lore.add("Tool zum Erstellen von animierten Toren");
				meta.setLore(lore);
				meta.setDisplayName("Gate-Tool");
				meta.addEnchant(Enchantment.KNOCKBACK, 10, true);
				item.setItemMeta(meta);
				
				//Add the Item to the player's inventory
				p.getInventory().addItem(item);
			}
			
			return true;
		
		//Let the player create Gates
		}else if(args[0].equalsIgnoreCase("create")){
			
			//TODO Add saving in database
			
			//The console can't make selections
			if(!(sender instanceof Player)){
				System.err.println("[GatesPlugin]: Diese Befehl kann nur von Spielern ausgeführt werden!");
			}else{
				//We now know for sure, that a player entered the command
				Player p = (Player)sender;
				
				//The player needs to have made a selection, which would be saved in Data.caches
				if(!(Data.caches.containsKey(p.getName()) && Data.caches.get(p.getName()).getFirstLocation() != null && Data.caches.get(p.getName()).getSecondLocation() != null)){
					p.sendMessage("§cDu hast noch keine Markierungen gemacht!");
					return true;
				}
				
				//The player needs to enter a name for the new gate
				if(args.length == 1){
					p.sendMessage("§c/gate create <name>");
					return true;
				}
				
				//The name needs to be unique
				if(Data.gates.containsKey(args[1])){
					p.sendMessage("§cEin Tor mit diesem Namen exestiert bereits!");
					return true;
				}
				
				//Create a primitive gate
				GateEntry gate = new GateEntry(p.getName());
				gate.setArea(new Area(Data.caches.get(p.getName())));
				gate.setName(args[1]);
				
				//Add the gate to the list of gates
				Data.gates.put(args[1], gate);
				
				//Automatically set this gate as selected for the player who created it
				Data.selectedgates.put(p.getName(), gate);
				
				//Send success message
				p.sendMessage("§aDas Tor wurde hinzugefügt!");
				
				return true;
			}
		//Command to select a gate to edit it
		}else if(args[0].equalsIgnoreCase("select")){
			
			//The player needs to enter the unique name of the gate
			if(args.length == 1){
				sender.sendMessage("§c/gate select <name>");
				return true;
			}
			
			//Check wether or not the gate exists
			if(!Data.gates.containsKey(args[1])){
				sender.sendMessage("§cDieses Tor wurde nicht gefunden! Du kannst eine Liste einsehen unter §e/gate list");
			}
			
			//Get the gate specified by the player
			GateEntry g = Data.gates.get(args[1]);
			
			//Set the gate as selected for the player
			Data.selectedgates.put(sender.getName(), g);
			
			//Send success message
			sender.sendMessage("§aDu hast das Tor " + g.getName() + " ausgewählt. Sein Ersteller ist " + g.getCreator() + ", und es besitzt zurzeit " + g.getFrames().size() + " Frames, die sich in einem Abstand von " + g.getWaitMS() + "MS (20MS = 1 SEC.) bewegen.");
		
		//Command to edit most of the important settings of a gate
		}else if(args[0].equalsIgnoreCase("editor")){
			
			//The player needs to enter a sub-command
			if(args.length == 1){
				sender.sendMessage("§c/gate editor frame:mode:sound:ms");
				return true;
			}
			
			//Every subcommand needs a gate to be selected
			if(!(Data.selectedgates.containsKey(sender.getName()))){
				sender.sendMessage("§cDu hast noch kein Tor ausgewählt, auf das diese Aktion ausgeführt werden soll!");
				return true;
			}
			
			GateEntry g = Data.gates.get(Data.selectedgates.get(sender.getName()).getName());
			
			//sub-command to edit the frames of a gate, which are essential for a gate to work
			if(args[1].equalsIgnoreCase("frame")){
				
				//Here needs the player again to enter a subcommand
				if(args.length == 2){
					sender.sendMessage("§c/gate editor frame list:add:delete");
					return true;
				}
				
				//List all frames contained in this gate including their ID and Locations
				if(args[2].equalsIgnoreCase("list")){
					
					sender.sendMessage("§a==================================");
					sender.sendMessage("§aFrames für das Tor " + g.getName() + "(Ersteller " + g.getCreator() + "):");

					List<Area> frames = g.getFrames();
					Area a;
					
					//Go through all Frames and send their data to the player
					for(int i = 0; i < frames.size(); ++i){
						a = frames.get(i).getFixedArea();
						sender.sendMessage("§e" + i);
						sender.sendMessage("§eLocation 1: " + a.getFirstLocation().getX() + "//" + a.getFirstLocation().getY() + "//" + a.getFirstLocation().getZ());
						sender.sendMessage("§eLocation 2: " + a.getSecondLocation().getX() + "//" + a.getSecondLocation().getY() + "//" + a.getSecondLocation().getZ());
					}
					sender.sendMessage("§a==================================");
				}
				//Add a new Frame to the existing ones
				else if(args[2].equalsIgnoreCase("add")){
					
					//The console can't make a selection
					if(!(sender instanceof Player)){
						System.err.println("[GatesPlugin]: Diese Befehl kann nur von Spielern ausgeführt werden!");
					}else{
						//We now know for sure that a player enters the command
						Player p = (Player)sender;
						
						//The player needs to have an Area selected
						if(!(Data.caches.containsKey(p.getName()) && Data.caches.get(p.getName()).getFirstLocation() != null && Data.caches.get(p.getName()).getSecondLocation() != null)){
							p.sendMessage("§cDu hast noch keine Markierungen gemacht!");
							return true;
						}
						
						//Get the selected Area
						Area a = Data.caches.get(p.getName()).getFixedArea();
						
						//Check wether or not the selected Area has the same volume as the gate it should be added to
						if(!Manager.isFrameValid(Data.caches.get(p.getName()).getFixedArea(), Data.selectedgates.get(p.getName()).getArea())){
							p.sendMessage("§cFehler! Der neue Frame muss exakt dasselbe Volumen haben wie das Tor, und gleich ausgerichtet sein! Der Frame wird nicht hinzugefügt!");
							return true;
						}
						
						//If everything is valid, add the new Frame to the gate
						Data.selectedgates.get(p.getName()).addFrame(a);;
						
						//Send success message
						p.sendMessage("§aDer neue Frame wurde hinzugefügt!");
					}
					
				}
				//Delete an existing Frame by its ID
				else if(args[2].equalsIgnoreCase("delete")){
					
					//The player needs to enter the ID of the frame he wants to delete
					if(args.length == 3){
						sender.sendMessage("§c/gate editor frame delete <index>");
						return true;
					}
					
					//There are less then 2 frames remaining, which makes no longer sense for the gate to exist
					if(Data.selectedgates.get(sender.getName()).getFrames().size()-1 < 2){
						sender.sendMessage("§cDurch den Löschvorgang wurde die Anzahl an Frames auf 1 reduziert. "
								+ "Die Existenz des Tors ist damit nicht länger sinnvoll, es sei denn, es werden bald "
								+ "neue Frames erstellt. Ist dies nicht der Fall, bitte führe §e/gate delete <name> §caus.");
					}
					
					//If everything is valid, remove the frame
					Data.selectedgates.get(sender.getName()).removeFrame(Integer.valueOf(args[3]));
					
					//Send success message
					sender.sendMessage("§aFrame wurde gelöscht!");
					
				//The player has entered a wrong sub-command
				}else{
					sender.sendMessage("§c/gate editor frame list:add:delete");
				}
			
			//Do something with the mode of the gate (the mode sets the behavior of the gate)
			}else if(args[1].equalsIgnoreCase("mode")){
				
				//The player needs to enter a sub-command
				if(args.length == 2){
					sender.sendMessage("§c/gate editor mode set:get");
					return true;
				}
				
				//Change the mode to change the behavior of the gate
				if(args[2].equalsIgnoreCase("set")){
					
					//The player needs to specify to which mode he wants to set the gate
					if(args.length == 3){
						sender.sendMessage("§c/gate editor mode set AUTO_EVERYONE:CLICK_EVERYONE:AUTO_PERMISSION:CLICK_PERMISSION:COMMAND_ONLY");
						return true;
					}
					
					//Set the selected modes, or send a warnmessage, if the mode couldn't be found
					if(args[3].equalsIgnoreCase("AUTO_EVERYONE")){
						g.setMode(GateMode.AUTO_EVERYONE);
					}else if(args[3].equalsIgnoreCase("CLICK_EVERYONE")){
						g.setMode(GateMode.CLICK_EVERYONE);
					}else if(args[3].equalsIgnoreCase("AUTO_PERMISSION")){
						g.setMode(GateMode.AUTO_PERMISSION);
					}else if(args[3].equalsIgnoreCase("CLICK_PERMISSION")){
						g.setMode(GateMode.CLICK_PERMISSION);
					}else if(args[3].equalsIgnoreCase("COMMAND_ONLY")){
						g.setMode(GateMode.COMMAND_ONLY);
					}else{
						sender.sendMessage("§cModus konnt nicht gefunden werden");
					}
				
				//Get the current mode of the gate
				}else if(args[2].equalsIgnoreCase("get")){
					
					String strmode = g.getMode().name();
					sender.sendMessage("§aAktueller Modus: " + strmode);
				
				//The player has entered a wrong sub-command
				}else{
					sender.sendMessage("§c/gate editor mode set:get");
				}
			
			//Settings to change the sounds which are played, when the frames are copied
			}else if(args[1].equalsIgnoreCase("sound")){
				
				//The player needs to enter a sub-command
				if(args.length == 2){
					sender.sendMessage("§c/gate editor sound add:remove:list:playall:testplay");
					return true;
				}
				
				//Just test-play the sound to test if it sounds good
				if(args[2].equalsIgnoreCase("testplay")){
					
					//The console can't hear sounds
					if(!(sender instanceof Player)){
						sender.sendMessage("§cDieser Befehl ist nur für Spieler gedacht!");
						return true;
					}
					
					//We now know that the command was antered by a player
					Player p = (Player)sender;
					
					//The player needs to specify the settings for the sound
					if(args.length < 6){
						p.sendMessage("§c/gate editor sound testplay <sound> <volume> <pitch>");
						return true;
					}
					
					//Save the settings in variables
					String sound = args[3].toUpperCase();
					Float volume = Float.valueOf(args[4]);
					Float pitch = Float.valueOf(args[5]);
					
					Sound s = null;
					
					//Check wether or not the sound exists
					try{
						s = Sound.valueOf(Sound.class, sound);
					}catch(IllegalArgumentException ex){
						p.sendMessage("§cDieser Sound konnt nicht gefunden werden! Bitte achte genau auf die Schreibweise!");
						return true;
					}
					
					//If everything is valid, play the sound to the player
					p.playSound(p.getLocation(), s, volume, pitch);
					
				//Add new sounds to the list of sounds which are played, when the frames are copied
				}else if(args[2].equalsIgnoreCase("add")){
					
					//The player needs to enter settings for the sound
					if(args.length < 6){
						sender.sendMessage("§c/gate editor sound add <sound> <volume> <pitch>");
						sender.sendMessage("§cEine vollständige Liste aller verfügbaren Sounds findest du auf https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html");
						return true;
					}
					
					//Save the settings for the new sound in variables
					String sound = args[3].toUpperCase();
					Float volume = Float.valueOf(args[4]);
					Float pitch = Float.valueOf(args[5]);
					
					Sound s = null;
					
					//Check wether or not the given sound exists
					try{
						s = Sound.valueOf(Sound.class, sound);
					}catch(IllegalArgumentException ex){
						sender.sendMessage("§cDieser Sound konnt nicht gefunden werden! Bitte achte genau auf die Schreibweise!");
						return true;
					}
					
					//Save the new Sound and its settings in a ModifiedSound-Object
					ModifiedSound ms = new ModifiedSound();
					ms.setSound(s);
					ms.setVolume(volume);
					ms.setPitch(pitch);
					
					//Add the Sound to the list
					g.addSound(ms);
					
					//Send success message
					sender.sendMessage("§aEin neuer Sound mit folgenden Eigenschaften wurde hinzugefügt:");
					sender.sendMessage("§aName: " + ms.getSound().name());
					sender.sendMessage("§aLautstärke: " + ms.getVolume());
					sender.sendMessage("§aAbspielgeschwindigkeit: " + ms.getPitch());
					
					return true;
					
				//Remove an existing sound from the list of sounds which are played, when the frames are copied
				}else if(args[2].equalsIgnoreCase("remove")){
					
					//The player needs to specify, which sound he wanna remove
					if(args.length == 3){
						sender.sendMessage("§c/gate editor sound remove <index>");
						sender.sendMessage("§cFür eine vollständige Liste aller Sounds, die das aktuell ausgewählte Tor besitzt, gebe ein:");
						sender.sendMessage("§e/gate editor sound list");
						return true;
					}
					
					//Check wether or not the user entered a letter instead of a number
					try{
						Integer.valueOf(args[3]);
					}catch(Exception ex){
						sender.sendMessage("§cBitte gib eine Zahl ein!");
						return true;
					}
					
					//Check, if a sound with the given ID exists
					if(g.getSounds().size() <= Integer.valueOf(args[3])){
						sender.sendMessage("§cDas Tor beinhaltet offenbar keinen Sound mit dieser ID.");
					}
					
					//If everything is valid, remove the sound
					g.removeSound(Integer.valueOf(args[3]));
					
					//Send success message
					sender.sendMessage("§aDer Sound wurde entfernt.");
					
				//List all in the list existing sounds with their settings
				}else if(args[2].equalsIgnoreCase("list")){

					sender.sendMessage("§a==================================");
					sender.sendMessage("§aSounds für das Tor " + g.getName() + "(Ersteller " + g.getCreator() + "):");

					List<ModifiedSound> sounds = g.getSounds();
					ModifiedSound ms;
					
					//Go through all in the list existing sounds and give them and their settings to the player
					for(int i = 0; i < sounds.size(); ++i){
						ms = sounds.get(i);
						sender.sendMessage("§a" + i + " - " + ms.getSound().name() + " Lautstärke: " + ms.getVolume() + " Abspielgeschwindigkeit: " + ms.getPitch());
					}
					sender.sendMessage("§a==================================");
				
				//Plays all in the list existing sound with their settings
				}else if(args[2].equalsIgnoreCase("playall")){
					
					//The console can't hear sound
					if(!(sender instanceof Player)){
						sender.sendMessage("§cDieser Befehl ist nur für Spieler gedacht!");
						return true;
					}
					
					//We now know for sure that a player entered this command
					Player p = (Player)sender;
					
					//Go through all existing sounds in the list for the selected gate and play them to the player with their settings
					for(ModifiedSound ms : g.getSounds()){
						p.playSound(p.getLocation(), ms.getSound(), ms.getVolume(), ms.getPitch());
					}
					
					//Send success message
					p.sendMessage("§aAlle Sounds wurden mit Pitch und Volume abgespielt!");
					
				//The player has entered a wrong sub-command
				}else{
					sender.sendMessage("§c/gate editor sound add:remove:list:playall:testplay");
				}
				
			//Command to interact with the time, which the plugin waits between framecopying and gate open- and closing
			}else if(args[1].equalsIgnoreCase("ms")){
				
				//the player needs to enter a sub-command
				if(args.length == 2){
					sender.sendMessage("§c/gate editor ms get:set");
					return true;
				}
				
				//Get the currenty set time
				if(args[2].equalsIgnoreCase("get")){
					
					sender.sendMessage("§aDas momentan ausgewählte Tor hat folgende Werte gesetzt:");
					sender.sendMessage("§aMillisekunden zwischen den Frames(waitms): " + g.getWaitMS());
					sender.sendMessage("§aMillisekunden, die das Tor offen bleibt, bis es sich wieder schließt(pausems): " + g.getPauseMS());
				
				//Change the currently set time
				}else if(args[2].equalsIgnoreCase("set")){
					
					//The player needs to enter a value
					if(args.length < 5){
						sender.sendMessage("§c/gate editor ms set waitms:pausems <value>");
						return true;
					}
					
					//Edit the ms which the plugin waits between copying the frames
					if(args[3].equalsIgnoreCase("waitms")){
						
						//Check wether or not the player entered a valid number
						try{
							Integer.valueOf(args[4]);
						}catch(Exception ex){
							sender.sendMessage("§cBitte gib eine Zahl ein!");
							return true;
						}
						
						//Change settings
						g.setWaitMS(Integer.valueOf(args[4]));
						
						//Send success message
						sender.sendMessage("§awaitms wurde auf " + args[4] + " gesetzt. Das sind " + Integer.valueOf(args[4]) / 20 + " Sekunden.");
						
						return true;
						
					//Edit the ms which the plugin waits when it opened the gate until it closes the gate again
					}else if(args[3].equalsIgnoreCase("pausems")){
						
						//Check wether or not the player entered a valid number
						try{
							Integer.valueOf(args[4]);
						}catch(Exception ex){
							sender.sendMessage("§cBitte gib eine Zahl ein!");
							return true;
						}
						
						//Change settings
						g.setPauseMS(Integer.valueOf(args[4]));
						
						//Send success message
						sender.sendMessage("§apausems wurde auf " + args[4] + " gesetzt. Das sind " + Integer.valueOf(args[4]) / 20 + " Sekunden.");
					}
				
				//The player has entered a wrong sub-command
				}else{
					sender.sendMessage("§c/gate editor ms get:set");
				}
			
			//The player has entered a wrong sub-command
			}else{
				sender.sendMessage("§c/gate editor frame:mode:sound:ms");
			}
		
		//Delete one of the existing gates
		}else if(args[0].equalsIgnoreCase("delete")){
			
			//The player needs to specify, which gate he wants to delete
			if(args.length == 1){
				sender.sendMessage("§cBitte gib den Namen eines Tors an!");
				return true;
			}
			
			//Check wether or not the given gate exists
			if(!Data.gates.containsKey(args[1])){
				sender.sendMessage("§cEs wurde kein Tor mit diesem Namen gefunden (Bitte achte auf Gross- und Kleinschreibung)!"
						+ "Für eine Liste aller Tore gib ein: §e/gate list");
				return true;
			}
			
			//Set gate as deleteable
			Data.gatesmaybedeleted.add(args[1]);
			
			
			//The player needs to confirm the deleting-process to prevent mistakes
			sender.sendMessage("§eBist du dir sicher, dass du dieses Tor löschen willst? Bitte gib zur Bestätigung"
					+ "/gate confirmdelete <name> ein. Dieser Befehl wird nur für 10 Sekunden verfügbar sein.");
			
			//After 10 seconds, remove the gate from the deletable-list
			Bukkit.getScheduler().scheduleSyncDelayedTask(Gates.getPlugin(), new Runnable() {
				@Override
				public void run() {
					if(Data.gatesmaybedeleted.contains(args[1])){
						Data.gatesmaybedeleted.remove((Data.gatesmaybedeleted.indexOf(args[1])));
					}
					return;
				}				
			}, 10*20L);
			
		//Confirm a before initialized delete-process
		}else if(args[0].equalsIgnoreCase("confirmdelete")){
			
			//The player needs to specify which process he wants to confirm
			if(args.length == 1){
				sender.sendMessage("§cBitte gib den Namen eines Tors an!");
				return true;
			}
			
			//The gate needs to exist to be deleted
			if(!Data.gates.containsKey(args[1])){
				sender.sendMessage("§cEs wurde kein Tor mit diesem Namen gefunden (Bitte achte auf Gross- und Kleinschreibung)!"
						+ "Für eine Liste aller Tore gib ein: §e/gate list");
				return true;
			}
			
			//The player needs to have the /gate delete <name> - Command less then 10 seconds ago
			if(!Data.gatesmaybedeleted.contains(args[1])){
				sender.sendMessage("§cDu hast diesen Befehl fälschlich eingegeben, oder die Zeit ist abgelaufen!"
						+ "Für einen Löschvorgang führe §e/gate delete <name> §caus.");
				return true;
			}else{
				
				//Remove gate from the gates-list
				Data.gates.remove(args[1]);
				
				//Remove the gate from the selected-gates-list, so it can't be edited after it has been deleted
				for(String key : Data.selectedgates.keySet()){
					
					if(Data.selectedgates.get(key).getName().equalsIgnoreCase(args[1])){
						Data.selectedgates.remove(args[1]);
					}
					
				}
				
				//Remove gate from database
				Data.removeGateFromDatabase(args[1]);
				
				//Send success message
				sender.sendMessage("§aDas Tor wurde entfernt!");
			}
		
		//List all existing gates
		}else if(args[0].equalsIgnoreCase("list")){
			
			sender.sendMessage("§a==================================");
			sender.sendMessage("§aMomentan geladene Tore:");
			
			//Go through all existing gates an send the player the information
			for(int i = 0; i < Data.gates.values().toArray().length; ++i){
				GateEntry g = (GateEntry) Data.gates.values().toArray()[i];
				sender.sendMessage("§e" + g.getName() + " von " + g.getCreator());
			}
			sender.sendMessage("§a==================================");
		
			
		//Open a gate via command
		}else if(args[0].equalsIgnoreCase("open")){
			
			//The player needs to specifiy, which gate he wants to delete
			if(args.length == 1){
				sender.sendMessage("§cBitte gib den Namen eines Tors an!");
				return true;
			}
			
			//The gate needs to exist to be opened
			if(!Data.gates.containsKey(args[1])){
				sender.sendMessage("§cEs wurde kein Tor mit diesem Namen gefunden (Bitte achte auf Gross- und Kleinschreibung)!"
						+ "Für eine Liste aller Tore gib ein: §e/gate list");
				return true;
			}
			
			//if everything is valid, open the gate
			Data.gates.get(args[1]).open();
		
		//Close a gate via command
		}else if(args[0].equalsIgnoreCase("close")){
			
			//The player needs to specify, which gate he wants to close
			if(args.length == 1){
				sender.sendMessage("§cBitte gib den Namen eines Tors an!");
				return true;
			}
			
			//The gate needs to exist to be closed
			if(!Data.gates.containsKey(args[1])){
				sender.sendMessage("§cEs wurde kein Tor mit diesem Namen gefunden (Bitte achte auf Gross- und Kleinschreibung)!"
						+ "Für eine Liste aller Tore gib ein: §e/gate list");
				return true;
			}
			
			//If everything is valid, close the gate
			Data.gates.get(args[1]).close();
		
		//The player has entered a wrong sub-command, send warnmessage
		}else{
			sender.sendMessage("§c/gate wand:create:editor:delete:list:open:close");
		}
		
		
		return true;
	}

}
