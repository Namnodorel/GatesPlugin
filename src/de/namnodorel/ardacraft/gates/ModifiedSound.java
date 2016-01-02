package de.namnodorel.ardacraft.gates;

import java.io.Serializable;

import org.bukkit.Sound;

//Class used to save a sound together with its settings
@SuppressWarnings("serial")
public class ModifiedSound implements Serializable{
	
	private Sound sound = null;
	private Float volume = 1.0F;
	private Float pitch = 1.0F;
	public Sound getSound() {
		return sound;
	}
	public void setSound(Sound sound) {
		this.sound = sound;
	}
	public Float getVolume() {
		return volume;
	}
	public void setVolume(Float volume) {
		this.volume = volume;
	}
	public Float getPitch() {
		return pitch;
	}
	public void setPitch(Float pitch) {
		this.pitch = pitch;
	}
	
}
