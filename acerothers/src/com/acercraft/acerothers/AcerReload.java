package com.acercraft.acerothers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcerReload implements CommandExecutor{
	
	AcerOthers plugin;
	
	public AcerReload(AcerOthers instance){
		this.plugin = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String l, String[] args){
		if(command.getName().equalsIgnoreCase("acerothers")){
			if(args.length == 0){
				sender.sendMessage("Base command for acerothers, try /acerothers reload");
			}
			
			if(args.length == 1){
				if(hasPerm((Player)sender, "acerothers.reload")){
				this.plugin.reloadConfig();
				this.plugin.reloadMessageConfig();
				sender.sendMessage("AcerOthers configs reloaded");
				}
				else{
					sender.sendMessage("You dont now have permission to run this command");
				}
			}
		}
		return false;
	}
	
	public boolean hasPerm(Player p, String node){
		return p.hasPermission(node);
	}

}
