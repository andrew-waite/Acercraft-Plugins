package com.acercraft.AcerStaffChat;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AcerStaffChat extends JavaPlugin{
	
	private ArrayList<String> adminList = new ArrayList<String>();
	public String PREIFX = ChatColor.RED + "[StaffChat] ";
	
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(new StaffChatListener(this), this);
		getCommand("staffchat").setExecutor(new StaffChatCommand(this));
	}
	
	public void onDisable()
	{
		
	}
	
	public ArrayList<String> getAdminList()
	{
		return adminList;
	}
	
	public void addAdminList(String string)
	{
		if(!(this.adminList.contains(string)))
		{
			this.adminList.add(string);
		}
		else
		{
			
		}
	}
	
	public void removeAdminList(String string)
	{
		if(this.adminList.contains(string))
		{
			this.adminList.remove(string);
		}
		else
		{
			
		}
	}
	
	public boolean hasPermission(Player player, String node)
	{
		return player.hasPermission(node);
	}
}
