package com.acercraft.AcerMOTD;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AcerMOTD extends JavaPlugin
  implements Listener
{
  List<String> motds = new ArrayList<String>();

  public void onEnable() { saveDefaultConfig();
    Bukkit.getPluginManager().registerEvents(this, this);
    this.motds = getConfig().getStringList("Messages"); }

  @EventHandler
  public void onServerPing(ServerListPingEvent event)
  {
    int motdPick = new Random().nextInt(this.motds.size());
    String motd = (String)this.motds.get(motdPick);
    motd = ChatColor.translateAlternateColorCodes('&', motd);
    event.setMotd(motd);
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("acermotd")) {
      if (sender.hasPermission("acermotd.reload")) {
        getPluginLoader().disablePlugin(this);
        getPluginLoader().enablePlugin(this);
        sender.sendMessage(ChatColor.GOLD + "AcerMOTD reloaded!");
      }
      else {
        sender.sendMessage(ChatColor.GOLD + "You do not have permission to do this!");
      }
    }
    return true;
  }
}