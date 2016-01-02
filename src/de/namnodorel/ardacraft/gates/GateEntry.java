package de.namnodorel.ardacraft.gates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("serial")
public class GateEntry implements Serializable{
	
	private Area area = new Area();
	private String name = "NAME-UNDEFINED";
	private final String creator;
	private List<Area> frames = new ArrayList<>();
	private List<ModifiedSound> sounds = new ArrayList<>();
	private Integer waitms = 20;
	private Integer pausems = 5*20;
	private Integer actualFrame = 0;
	private GateMode mode = GateMode.CLICK_EVERYONE;
	
	private boolean isMoving = false;
	
	private int mid = -1;
	
	public GateEntry(String creator){
		this.creator = creator;
	}
	
	public Area getArea(){
		return area;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreator() {
		return creator;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public List<Area> getFrames() {
		return frames;
	}
	
	public Area getFrame(int index){
		
		if(frames.size() - 1 < index){
			return null;
		}
		
		return frames.get(0);
	}
	
	public void addFrame(Area newframe){
		frames.add(newframe);
	}
	
	public void removeFrame(int index){
		
		List<Area> newframes = new ArrayList<>();
		
		for(int i = 0; i < frames.size(); ++i){
			
			if(!(i == index)){
				newframes.add(frames.get(i));
			}
			
		}
		frames = newframes;
	}

	public Integer getWaitMS() {
		return waitms;
	}

	public void setWaitMS(Integer waitms) {
		this.waitms = waitms;
	}
	
	public Integer getActualFrame() {
		return actualFrame;
	}

	public void setActualFrame(Integer actualFrame) {
		this.actualFrame = actualFrame;
	}
	
	public GateMode getMode() {
		return mode;
	}

	public void setMode(GateMode mode) {
		this.mode = mode;
	}
	
	public Integer getPauseMS() {
		return pausems;
	}

	public void setPauseMS(Integer pausems) {
		this.pausems = pausems;
	}

	public List<ModifiedSound> getSounds() {
		return sounds;
	}
	
	public void addSound(ModifiedSound sound){
		sounds.add(sound);
	}
	
	public void removeSound(Integer index){
		
		List<ModifiedSound> newsounds = new ArrayList<>();
		
		for(int i = 0; i < sounds.size(); ++i){
			
			if(!(i == index)){
				newsounds.add(sounds.get(i));
			}
			
		}
		sounds = newsounds;
		
	}
	
	public boolean isMoving(){
		return isMoving;
	}
	
	public void changeFrameTo(Integer index, Boolean playsounds){
		
		BlockAreaManager.copyBlockArea(frames.get(index), area);
		
		if(playsounds){
			playSounds();
		}
	}
	
	private void cancelMotion(){
		Bukkit.getScheduler().cancelTask(mid);
	}
	
	public boolean open(){
		
		if(isMoving){
			return false;
		}
		
		if(getActualFrame() + 1 >= frames.size()){
			return false;
		}
		
		if(frames.isEmpty()){
			return false;
		}
		
		isMoving = true;
		
		final GateEntry g = this;
		
		int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Gates.getPlugin(), new Runnable(){

			@Override
			public void run() {
				
				
				//If this is the first iteration, play no sound. That's because the first iteration replaces the frame which is already there
				boolean playsound = true;
				
				if(g.getActualFrame() - 1 < 0){
					playsound = false;
				}
				
				g.changeFrameTo(g.getActualFrame(), playsound);
				
				if(g.getActualFrame() + 1 >= g.getFrames().size()){
					g.cancelMotion();
					isMoving = false;
					return;
				}else{
					g.setActualFrame(g.getActualFrame() + 1);
				}
			}
			
			
		},0L, getWaitMS());
		
		mid = id;
		
		return true;
	}
	public boolean close(){
		
		if(isMoving){
			return false;
		}
		
		if(getActualFrame() - 1 < 0){
			return false;
		}
		
		if(frames.isEmpty()){
			return false;
		}
		
		isMoving = true;
		
		final GateEntry g = this;
		
		int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Gates.getPlugin(), new Runnable(){

			@Override
			public void run() {
				
				//If this is the first iteration, play no sound. That's because the first iteration replaces the frame which is already there
				boolean playsound = true;
				
				if(g.getActualFrame() + 1 >= g.getFrames().size()){
					playsound = false;
				}
				
				
				g.changeFrameTo(g.getActualFrame(), playsound);
				
				if(g.getActualFrame() - 1 < 0){
					g.cancelMotion();
					isMoving = false;
					return;
				}else{
					g.setActualFrame(g.getActualFrame() - 1);
				}
			}
			
			
		},0L, getWaitMS());
		
		mid = id;
		
		return true;
	}
	
	void playSounds(){
		
		//TODO Add real world name
		for(Player p : Bukkit.getServer().getWorld("RPG").getPlayers()){
			if(p.getLocation().distance(getArea().getFirstLocation()) <= 10 || p.getLocation().distance(getArea().getSecondLocation()) <= 10){
				
				for(ModifiedSound ms : getSounds()){
					p.playSound(getArea().getFirstLocation(), ms.getSound(), ms.getVolume(), ms.getPitch());
					p.playSound(getArea().getSecondLocation(), ms.getSound(), ms.getVolume(), ms.getPitch());
				}
				
			}
		}
	}
	
}
