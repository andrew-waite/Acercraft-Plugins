package com.acercraft.ACPEssentials.API;

import java.io.File;
import java.io.IOException;

import org.bukkit.entity.Player;

public class UserData {
	
	public static void hasPlayedBefore(Player player) throws IOException {

		    File file = new File("plugins/ACPEssentials/userdata");
		    if (!file.isDirectory()) {
		      file.mkdir();
		    }
		    File file1 = new File("plugins/ACPEssentials/userdata/"+player.getName()+".yml");
		    if (!file1.isFile())
		      try {
		        file1.createNewFile();
		      }
		      catch (IOException e)
		      {
		    	  e.printStackTrace();
		      }
		
	}

}
