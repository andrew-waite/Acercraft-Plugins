package com.acercraft.AcerPlots;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class AcerPlots extends JavaPlugin {
	
	public String PREFIX = null;
	
		public void onEnable(){
			
			this.PREFIX = getConfig().getString("Prefix");
			this.getWorldGuard();
			this.setupConfig();
		}
		
		public void onDisable(){
			
		}
		
		public boolean onCommand(CommandSender sender, Command command, String l, String[] args){
			if(command.getName().equalsIgnoreCase("plot")){
				if(args.length < 3){
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.PREFIX + ChatColor.RED + "/plot {add/remove} {name} {regionname} - Adds/removes people from your plot"));
				}
				if(args.length == 3){
					if(args[0].equalsIgnoreCase("add")){
						Player addedplayer = Bukkit.getServer().getPlayerExact(args[1]);
						if(addedplayer != null){
							World world = ((Player)sender).getWorld();
							this.addMember(addedplayer, args[2], (Player)sender, world);
						}else{
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.PREFIX + ChatColor.RED + "The player you are trying to add is not online"));
						}
						
					}else
					if(args[0].equalsIgnoreCase("remove")){
						Player addedplayer = Bukkit.getServer().getPlayerExact(args[1]);
						if(addedplayer != null){
							World world = ((Player)sender).getWorld();
							this.removeMember(addedplayer, args[2], (Player)sender, world);
						}else{
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.PREFIX + ChatColor.RED + "The player you are trying to add is not online"));
						}
					}
					else
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.PREFIX + ChatColor.RED + "/plot {add/remove} {name} {regionname} - Adds/removes people from your plot"));
				}
			}
			return false;
		}
	
	   private WorldGuardPlugin getWorldGuard() {
	        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	            return null; 
	        }
	   
	        return (WorldGuardPlugin) plugin;
	    }
	   
	 
		public boolean addMember(Player player, String regionName, Player executed, World world) {
	        WorldGuardPlugin worldGuard = getWorldGuard();
	        
	        RegionManager rm = worldGuard.getRegionManager(world);
	        ProtectedRegion region = rm.getRegion(regionName);
	       
	        if(region == null){
	        	executed.sendMessage(ChatColor.translateAlternateColorCodes('&', this.PREFIX + ChatColor.RED + "That plot does not exist"));
	        	return false;
	        }
	        if(!region.isOwner(executed.getName())){
	        	executed.sendMessage(ChatColor.translateAlternateColorCodes('&', this.PREFIX + ChatColor.RED + "You have to be the owner of a plot to add other players to it"));
	        	return false;
	        }
	        else{
		        DefaultDomain domain = region.getMembers();
		        if(domain.size() == getConfig().getInt("Limit_of_players")){
		        	executed.sendMessage(ChatColor.translateAlternateColorCodes('&', this.PREFIX + ChatColor.RED + "Sorry you can not add anymore players to your plot"));
		        	return false;
		        }
		        domain.addPlayer(player.getName());
	        	region.setMembers(domain);
	        	executed.sendMessage(ChatColor.translateAlternateColorCodes('&', this.PREFIX + ChatColor.GREEN + "Successfully added " + player.getName() + " to your plot"));
	        }
	        
	       
	        try {
	            rm.save();
	        } catch (ProtectionDatabaseException e) {
	            return false;
	        }
	        return true;
	        }
		
		public boolean removeMember(Player player, String regionName, Player executed, World world) {
	        WorldGuardPlugin worldGuard = getWorldGuard();
	        
	        RegionManager rm = worldGuard.getRegionManager(world);
	        ProtectedRegion region = rm.getRegion(regionName);
	       
	        if(region == null){
	        	executed.sendMessage(ChatColor.translateAlternateColorCodes('&', this.PREFIX + ChatColor.RED + "That plot does not exist"));
	        	return false;
	        }
	        if(!region.isOwner(executed.getName())){
	        	executed.sendMessage(ChatColor.translateAlternateColorCodes('&', this.PREFIX + ChatColor.RED + "You have to be the owner of a plot to remove other players to it"));
	        	return false;
	        }
	        else{
		        DefaultDomain domain = region.getMembers();
		        domain.removePlayer(player.getName());
	        	region.setMembers(domain);
	        	executed.sendMessage(ChatColor.translateAlternateColorCodes('&', this.PREFIX + ChatColor.GREEN + "Successfully removed " + player.getName() + " to your plot"));
	        }
	        
	       
	        try {
	            rm.save();
	        } catch (ProtectionDatabaseException e) {
	            return false;
	        }
	        return true;
	        }
		
	    
	    public void setupConfig(){
	    	if(!(new File(getDataFolder(), "config.yml").exists())){
	    		saveDefaultConfig();
	    	}
	    }

}
