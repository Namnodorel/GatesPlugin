package de.namnodorel.ardacraft.gates;

import java.io.Serializable;

public enum GateMode implements Serializable{
	
	//Gate automatically opens for everyone
	AUTO_EVERYONE, 
	//Gate opens on click for everyone
	CLICK_EVERYONE,
	//Gate opens automatically for everyone with permission
	AUTO_PERMISSION,
	//Gate opens on click for everyone with permission
	CLICK_PERMISSION,
	//Gate just opens and closes with the command /gate close:open <name>
	COMMAND_ONLY
	
}
