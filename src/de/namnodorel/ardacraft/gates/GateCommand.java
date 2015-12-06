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
		
		if(!sender.hasPermission(new Permission("de.namnodorel.ardacraft.gates.command"))){
			sender.sendMessage("§cDu hast nicht die notwendige Berechtigung, diesen Befehl aus zu führen!");
			return true;
		}
		
		if(args.length == 0){
			sender.sendMessage("§cBitte gib ein weiteres Argument ein!");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("wand")){
			
			if(!(sender instanceof Player)){
				System.err.println("[GatesPlugin]: Diese Befehl kann nur von Spielern ausgeführt werden!");
			}else{
				Player p = (Player)sender;
				ItemStack item = new ItemStack(Material.STICK);
				ItemMeta meta = item.getItemMeta();
				List<String> lore = new ArrayList<>();
				lore.add("Tool zum Erstellen von animierten Toren");
				//lore.add("Aktueller Modus:");
				//lore.add("Schritt 1 (Markieren des Torbereichs)");
				meta.setLore(lore);
				meta.setDisplayName("Gate-Tool");
				meta.addEnchant(Enchantment.KNOCKBACK, 10, true);
				item.setItemMeta(meta);
				p.getInventory().addItem(item);
			}
			
			return true;
		}else if(args[0].equalsIgnoreCase("create")){
			
			//TODO Add saving in database
			
			if(!(sender instanceof Player)){
				System.err.println("[GatesPlugin]: Diese Befehl kann nur von Spielern ausgeführt werden!");
			}else{
				Player p = (Player)sender;
				
				if(!(Data.caches.containsKey(p.getName()) && Data.caches.get(p.getName()).getFirstLocation() != null && Data.caches.get(p.getName()).getSecondLocation() != null)){
					p.sendMessage("§cDu hast noch keine Markierungen gemacht!");
					return true;
				}
				
				if(args.length == 1){
					p.sendMessage("§c/gate create <name>");
					return true;
				}
				
				if(Data.gates.containsKey(args[1])){
					p.sendMessage("§cEin Tor mit diesem Namen exestiert bereits!");
					return true;
				}
				
				GateEntry gate = new GateEntry(p.getName());
				gate.setArea(new Area(Data.caches.get(p.getName())));
				gate.setName(args[1]);
				
				Data.gates.put(args[1], gate);
				
				Data.selectedgates.put(p.getName(), gate);
				
				p.sendMessage("§aDas Tor wurde hinzugefügt!");
				p.sendMessage("§aAls nächstes solltest du die Animation dazu");
				p.sendMessage("§aeinfügen. Der Befehl dazu lautet:");
				p.sendMessage("§e/gate editor frame add");
				p.sendMessage("§cGib diesen Befehl aber erst ein, wenn du den");
				p.sendMessage("§cBereich markiert hast!");
				p.sendMessage("§aDer neue Frame muss dieselben Maße haben wie das");
				p.sendMessage("§aOriginaltor, und gleich ausgerichtet sein!");
				
			}
		}else if(args[0].equalsIgnoreCase("select")){
			
			if(args.length == 1){
				sender.sendMessage("§c/gate select <name>");
				return true;
			}
			
			if(!Data.gates.containsKey(args[1])){
				sender.sendMessage("§cDieses Tor wurde nicht gefunden! Du kannst eine Liste einsehen unter §e/gate list");
			}
			
			GateEntry g = Data.gates.get(args[1]);
			
			Data.selectedgates.put(sender.getName(), g);
			
			sender.sendMessage("§aDu hast das Tor " + g.getName() + " ausgewählt. Sein Ersteller ist " + g.getCreator() + ", und es besitzt zurzeit " + g.getFrames().size() + " Frames, die sich in einem Abstand von " + g.getWaitMS() + "MS (20MS = 1 SEC.) bewegen.");
		
		}else if(args[0].equalsIgnoreCase("editor")){
			
			if(args.length == 1){
				sender.sendMessage("§c/gate editor frame:mode:sound");
				return true;
			}
			
			if(!(Data.selectedgates.containsKey(sender.getName()))){
				sender.sendMessage("§cDu hast noch kein Tor ausgewählt, auf das diese Aktion ausgeführt werden soll!");
				return true;
			}
			
			GateEntry g = Data.gates.get(Data.selectedgates.get(sender.getName()).getName());
			
			if(args[1].equalsIgnoreCase("frame")){
				
				if(args.length == 2){
					sender.sendMessage("§c/gate editor frame list:add:delete");
					return true;
				}
				
				if(args[2].equalsIgnoreCase("list")){
					
					sender.sendMessage("§a==================================");
					sender.sendMessage("§aFrames für das Tor " + g.getName() + "(Ersteller " + g.getCreator() + "):");

					List<Area> frames = g.getFrames();
					Area a;
					
					for(int i = 0; i < frames.size(); ++i){
						a = frames.get(i).getFixedArea();
						sender.sendMessage("§e" + i);
						sender.sendMessage("§eLocation 1: " + a.getFirstLocation().getX() + "//" + a.getFirstLocation().getY() + "//" + a.getFirstLocation().getZ());
						sender.sendMessage("§eLocation 2: " + a.getSecondLocation().getX() + "//" + a.getSecondLocation().getY() + "//" + a.getSecondLocation().getZ());
					}
					sender.sendMessage("§a==================================");
				}
				
				if(args[2].equalsIgnoreCase("add")){
					
					if(!(sender instanceof Player)){
						System.err.println("[GatesPlugin]: Diese Befehl kann nur von Spielern ausgeführt werden!");
					}else{
						Player p = (Player)sender;
						
						if(!(Data.caches.containsKey(p.getName()) && Data.caches.get(p.getName()).getFirstLocation() != null && Data.caches.get(p.getName()).getSecondLocation() != null)){
							p.sendMessage("§cDu hast noch keine Markierungen gemacht!");
							return true;
						}
						
						Area a = Data.caches.get(p.getName()).getFixedArea();
						
						if(!Manager.isFrameValid(Data.caches.get(p.getName()).getFixedArea(), Data.selectedgates.get(p.getName()).getArea())){
							p.sendMessage("§cFehler! Der neue Frame muss exakt dasselbe Volumen haben wie das Tor, und gleich ausgerichtet sein! Der Frame wird nicht hinzugefügt!");
							return true;
						}
						
						Data.selectedgates.get(p.getName()).addFrame(a);;
						
						p.sendMessage("§aDer neue Frame wurde hinzugefügt!");
					}
					
				}
				
				if(args[2].equalsIgnoreCase("delete")){
					
					if(args.length == 3){
						sender.sendMessage("§c/gate editor frame delete <index>");
						return true;
					}
					
					if(Data.selectedgates.get(sender.getName()).getFrames().size()-1 < 2){
						sender.sendMessage("§cDurch den Löschvorgang wurde die Anzahl an Frames auf 1 reduziert. "
								+ "Die Existenz des Tors ist damit nicht länger sinnvoll, es sei denn, es werden bald "
								+ "neue Frames erstellt. Ist dies nicht der Fall, bitte führe §e/gate delete <name> §caus.");
					}
					
					Data.selectedgates.get(sender.getName()).removeFrame(Integer.valueOf(args[3]));
					
					sender.sendMessage("§aFrame wurde gelöscht!");
				}
				
			}else if(args[1].equalsIgnoreCase("mode")){
				
				if(args.length == 2){
					sender.sendMessage("§c/gate editor mode set:get");
					return true;
				}
				
				if(args[2].equalsIgnoreCase("set")){
					
					if(args.length == 3){
						sender.sendMessage("§c/gate editor mode set AUTO_EVERYONE:CLICK_EVERYONE:AUTO_PERMISSION:CLICK_PERMISSION:COMMAND_ONLY");
						return true;
					}
					
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
					
				}else if(args[2].equalsIgnoreCase("get")){
					
					String strmode = g.getMode().name();
					
					sender.sendMessage("§aAktueller Modus: " + strmode);
					
				}
				
			}else if(args[1].equalsIgnoreCase("sound")){
				
				if(args.length == 2){
					sender.sendMessage("§c/gate editor sound add:remove:list:playall:testplay");
					return true;
				}
				
				if(args[2].equalsIgnoreCase("testplay")){
					
					if(!(sender instanceof Player)){
						sender.sendMessage("§cDieser Befehl ist nur für Spieler gedacht!");
						return true;
					}
					
					Player p = (Player)sender;
					
					if(args.length < 6){
						p.sendMessage("§c/gate editor sound testplay <sound> <volume> <pitch>");
						return true;
					}
					
					String sound = args[3].toUpperCase();
					Float volume = Float.valueOf(args[4]);
					Float pitch = Float.valueOf(args[5]);
					
					Sound s = null;
					
					try{
						s = Sound.valueOf(Sound.class, sound);
					}catch(IllegalArgumentException ex){
						p.sendMessage("§cDieser Sound konnt nicht gefunden werden! Bitte achte genau auf die Schreibweise!");
						return true;
					}
					
					p.playSound(p.getLocation(), s, volume, pitch);
					
					
				}else if(args[2].equalsIgnoreCase("add")){
					
					if(args.length < 6){
						sender.sendMessage("§c/gate editor sound add <sound> <volume> <pitch>");
						sender.sendMessage("§cEine vollständige Liste aller verfügbaren Sounds findest du auf https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html");
						return true;
					}
					
					String sound = args[3].toUpperCase();
					Float volume = Float.valueOf(args[4]);
					Float pitch = Float.valueOf(args[5]);
					
					Sound s = null;
					
					try{
						s = Sound.valueOf(Sound.class, sound);
					}catch(IllegalArgumentException ex){
						sender.sendMessage("§cDieser Sound konnt nicht gefunden werden! Bitte achte genau auf die Schreibweise!");
						return true;
					}
					
					ModifiedSound ms = new ModifiedSound();
					ms.setSound(s);
					ms.setVolume(volume);
					ms.setPitch(pitch);
					
					g.addSound(ms);
					
					sender.sendMessage("§aEin neuer Sound mit folgenden Eigenschaften wurde hinzugefügt:");
					sender.sendMessage("§aName: " + ms.getSound().name());
					sender.sendMessage("§aLautstärke: " + ms.getVolume());
					sender.sendMessage("§aAbspielgeschwindigkeit: " + ms.getPitch());
				}else if(args[2].equalsIgnoreCase("remove")){
					
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
					
					if(g.getSounds().size() <= Integer.valueOf(args[3])){
						sender.sendMessage("§cDas Tor beinhaltet offenbar keinen Sound mit dieser ID.");
					}
					
					g.removeSound(Integer.valueOf(args[3]));
					sender.sendMessage("§aDer Sound wurde entfernt.");
					
				}else if(args[2].equalsIgnoreCase("list")){

					sender.sendMessage("§a==================================");
					sender.sendMessage("§aSounds für das Tor " + g.getName() + "(Ersteller " + g.getCreator() + "):");

					List<ModifiedSound> sounds = g.getSounds();
					ModifiedSound ms;
					
					for(int i = 0; i < sounds.size(); ++i){
						ms = sounds.get(i);
						sender.sendMessage("§a" + i + " - " + ms.getSound().name() + " Lautstärke: " + ms.getVolume() + " Abspielgeschwindigkeit: " + ms.getPitch());
					}
					sender.sendMessage("§a==================================");
					
				}else if(args[2].equalsIgnoreCase("playall")){
					
					if(!(sender instanceof Player)){
						sender.sendMessage("§cDieser Befehl ist nur für Spieler gedacht!");
						return true;
					}
					
					Player p = (Player)sender;
					
					for(ModifiedSound ms : g.getSounds()){
						p.playSound(p.getLocation(), ms.getSound(), ms.getVolume(), ms.getPitch());
					}
					
					p.sendMessage("§aAlle Sounds wurden mit Pitch und Volume abgespielt!");
				}
				
			}
			
		}else if(args[0].equalsIgnoreCase("delete")){
			
			if(args.length == 1){
				sender.sendMessage("§cBitte gib den Namen eines Tors an!");
				return true;
			}
			
			if(!Data.gates.containsKey(args[1])){
				sender.sendMessage("§cEs wurde kein Tor mit diesem Namen gefunden (Bitte achte auf Gross- und Kleinschreibung)!"
						+ "Für eine Liste aller Tore gib ein: §e/gate list");
				return true;
			}
			
			Data.gatesmaybedeleted.add(args[1]);
			sender.sendMessage("§eBist du dir sicher, dass du dieses Tor löschen willst? Bitte gib zur Bestätigung"
					+ "/gate confirmdelete <name> ein. Dieser Befehl wird nur für 10 Sekunden verfügbar sein.");
			
			
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Gates.getPlugin(), new Runnable() {
				@Override
				public void run() {
					if(Data.gatesmaybedeleted.contains(args[1])){
						Data.gatesmaybedeleted.remove((Data.gatesmaybedeleted.indexOf(args[1])));
					}
					return;
				}				
			}, 10*20L);
		}else if(args[0].equalsIgnoreCase("confirmdelete")){
			
			if(args.length == 1){
				sender.sendMessage("§cBitte gib den Namen eines Tors an!");
				return true;
			}
			
			if(!Data.gates.containsKey(args[1])){
				sender.sendMessage("§cEs wurde kein Tor mit diesem Namen gefunden (Bitte achte auf Gross- und Kleinschreibung)!"
						+ "Für eine Liste aller Tore gib ein: §e/gate list");
				return true;
			}
			
			if(!Data.gatesmaybedeleted.contains(args[1])){
				sender.sendMessage("§cDu hast diesen Befehl fälschlich eingegeben, oder die Zeit ist abgelaufen!"
						+ "Für einen Löschvorgang führe §e/gate delete <name> §caus.");
				return true;
			}else{
				
				Data.gates.remove(args[1]);
				
				for(String key : Data.selectedgates.keySet()){
					
					if(Data.selectedgates.get(key).getName().equalsIgnoreCase(args[1])){
						
					}
					
				}
				
				Data.removeGateFromDatabase(args[1]);
				
				sender.sendMessage("§aDas Tor wurde entfernt!");
			}
			
		}else if(args[0].equalsIgnoreCase("list")){
			
			sender.sendMessage("§a==================================");
			sender.sendMessage("§aMomentan geladene Tore:");
			
			for(int i = 0; i < Data.gates.values().toArray().length; ++i){
				GateEntry g = (GateEntry) Data.gates.values().toArray()[i];
				sender.sendMessage("§e" + g.getName() + " von " + g.getCreator());
			}
			sender.sendMessage("§a==================================");
			
		}else if(args[0].equalsIgnoreCase("open")){
			
			if(args.length == 1){
				sender.sendMessage("§cBitte gib den Namen eines Tors an!");
				return true;
			}
			
			if(!Data.gates.containsKey(args[1])){
				sender.sendMessage("§cEs wurde kein Tor mit diesem Namen gefunden (Bitte achte auf Gross- und Kleinschreibung)!"
						+ "Für eine Liste aller Tore gib ein: §e/gate list");
				return true;
			}
			
			
			Data.gates.get(args[1]).open();
			
		}else if(args[0].equalsIgnoreCase("close")){
			
			if(args.length == 1){
				sender.sendMessage("§cBitte gib den Namen eines Tors an!");
				return true;
			}
			
			if(!Data.gates.containsKey(args[1])){
				sender.sendMessage("§cEs wurde kein Tor mit diesem Namen gefunden (Bitte achte auf Gross- und Kleinschreibung)!"
						+ "Für eine Liste aller Tore gib ein: §e/gate list");
				return true;
			}
			
			
			Data.gates.get(args[1]).close();
		}
		
		
		return true;
	}

}
