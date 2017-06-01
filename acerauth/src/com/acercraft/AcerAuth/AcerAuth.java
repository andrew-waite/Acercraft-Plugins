package com.acercraft.AcerAuth;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AcerAuth extends JavaPlugin implements Listener
{
	public static final Logger log = Bukkit.getLogger();
	String prefix = ChatColor.GREEN + "[" + ChatColor.BLUE + "AcerAuth" + ChatColor.GREEN + "] ";
	List<String> login = new ArrayList<String>();
	private static MessageDigest md;

	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		setupConfig();
		log.info("[AcerAuth] AcerAuth Enabled");
	}

	public void onDisable() {
		log.info("[AcerAuth] AcerAuth Disbled");
	}

	public void setupConfig() {
		if (!new File(getDataFolder(), "config.yml").exists()) {
			getConfig().options().copyDefaults(true);
			saveConfig();
			reloadConfig();
		}
	}

	public boolean hasPerm(Player p, String node) {
		return p.hasPermission(node);
	}

	public static String cryptWithMD5(String pass)
	{
		try
		{
			md = MessageDigest.getInstance("MD5");
			byte[] passBytes = pass.getBytes();
			md.reset();
			byte[] digested = md.digest(passBytes);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < digested.length; i++) {
				sb.append(Integer.toHexString(0xFF & digested[i]));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			log.severe("Could not encrypt the password!");
		}
		return null;
	}

	public boolean onCommand(CommandSender s, Command c, String l, String[] args)
	{
		if (c.getName().equalsIgnoreCase("acerauth")) 
		{
			if(args[0].equalsIgnoreCase("reload"))
			{
				Player player = (Player)s;
				if (hasPerm(player, "acerauth.reload")) 
				{
					saveConfig();
					reloadConfig();
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("changepassword"))
			{
				Player player = (Player)s;
				List<String> admins = getConfig().getStringList("Admins");
				String password = getConfig().getString(player.getName() + ".password");
				if(admins.contains(player.getName()))
				{
					if(login.contains(player.getName()))
					{
						player.sendMessage(this.prefix + ChatColor.GREEN + "Please login first by using the command " + ChatColor.RED + "/login {password}");
					}
					else
					{
						if(args.length < 4)
						{
							s.sendMessage(this.prefix + ChatColor.GREEN + "Incorrect Usage " + ChatColor.RED + "/acerauth changepassword <oldpassword> <newpassword> <confirmpassword>");
						}
						if(cryptWithMD5(args[1]) != password)
						{
							s.sendMessage(this.prefix + ChatColor.RED + "Old password incorrect");
						}
						else
						{
							if((args[2] != args[3]) || args[3] != args[2])
							{
								s.sendMessage(this.prefix + ChatColor.RED + "New passwords don't match");
							}
							else
							{
								String encryptedPassword = cryptWithMD5(args[2]);
								getConfig().set(player.getName() + ".password", encryptedPassword);
								saveConfig();
								reloadConfig();
								s.sendMessage(this.prefix + ChatColor.GREEN + "You successfully changed your password");
							}
						}
					}
				}
				else
				{
					s.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				}
			}
			s.sendMessage(ChatColor.RED + "You do not have permission to use this command");

			return true;
		}
		if (c.getName().equalsIgnoreCase("register")) 
		{
			Player p = (Player)s;
			if (this.login.contains(p.getName())) {
				if (getConfig().getString(p.getName() + ".password") == null) 
				{

					getConfig().set(p.getName() + ".password", cryptWithMD5(args[0]));
					saveConfig();
					reloadConfig();

					p.sendMessage(this.prefix + ChatColor.GREEN + "Successfully created a password now log in");
					getConfig().set(p.getName() + ".chances", Integer.valueOf(3));
					saveConfig();
					reloadConfig();
				}
				else 
				{
					p.sendMessage(this.prefix + ChatColor.GREEN + "You have already registered please login using" + ChatColor.RED + "/login {password}");
				}
			}
			else p.sendMessage(ChatColor.RED + "You do not have permission to use this command");

			return true;
		}

		if (c.getName().equalsIgnoreCase("login")) 
		{
			Player p = (Player)s;

			if (this.login.contains(p.getName())) 
			{
				if (getConfig().getString(p.getName() + ".password") != null)
				{
					String password = getConfig().getString(p.getName() + ".password");

					if (cryptWithMD5(args[0]).equals(password)) 
					{
						p.sendMessage(this.prefix + ChatColor.GREEN + "Password Accepted :D");
						this.login.remove(p.getName());
						getConfig().set(p.getName() + ".chances", Integer.valueOf(3));
						saveConfig();
						reloadConfig();
					}
					else if (!args[0].equals(password)) 
					{
						int chances = getConfig().getInt(p.getName() + ".chances");
						chances--;
						getConfig().set(p.getName() + ".chances", Integer.valueOf(chances));
						saveConfig();
						reloadConfig();
						p.sendMessage(this.prefix + ChatColor.RED + "Incorrect password you have " + String.valueOf(chances) + ChatColor.RED + " attempts remaining");

						if (chances == 0) {
							p.setBanned(true);
							p.kickPlayer("Too many incorrect password attempts");
							getConfig().set(p.getName() + ".chances", Integer.valueOf(3));
							saveConfig();
							reloadConfig();
						}
					}

				}
				else 
				{
					p.sendMessage(this.prefix + ChatColor.GREEN + "You need to register first by" + ChatColor.RED + " /register {password}");
				}
			}
			else p.sendMessage(ChatColor.RED + "You do not have permission to use this command");

			return true;
		}
		return false;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) 
	{
		String playerName = event.getPlayer().getName();
		List<String> admins = getConfig().getStringList("Admins");
		if(admins.contains(playerName))
		{
			this.login.add(playerName);
		}
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) 
	{
		if (this.login.contains(event.getPlayer().getName())) 
		{
			if ((event.getFrom().getX() != event.getTo().getX()) || (event.getFrom().getY() != event.getTo().getY()) || (event.getFrom().getZ() != event.getTo().getZ()))
			{
				Location loc = event.getFrom();
				loc.setPitch(event.getTo().getPitch());
				loc.setYaw(event.getTo().getYaw());
				event.getPlayer().teleport(loc);
				event.getPlayer().sendMessage(this.prefix + ChatColor.GREEN + "Please login first by using the command " + ChatColor.RED + "/login {password}");
			}
		}
	}

	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent event) 
	{
		if (this.login.contains(event.getPlayer().getName())) 
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(this.prefix + ChatColor.GREEN + "Please login first by using the command " + ChatColor.RED + "/login {password}");
		}
	}

	@EventHandler
	public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) 
	{
		if ((this.login.contains(event.getPlayer().getName())) && 
				(!event.getMessage().startsWith("/login")) && (!event.getMessage().startsWith("/register")))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(this.prefix + ChatColor.GREEN + "Please login first by using the command " + ChatColor.RED + "/login {password}");
		}
	}

	@EventHandler
	public void onPlayerIneteractEvent(PlayerInteractEvent event)
	{
		if ((event.getAction().equals(Action.LEFT_CLICK_AIR)) || 
				(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) || 
				(event.getAction().equals(Action.RIGHT_CLICK_AIR)) || 
				(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
		{
			if (this.login.contains(event.getPlayer().getName())) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(this.prefix + ChatColor.GREEN + "Please login first by using the command " + ChatColor.RED + "/login {password}");
			}
		}
	}
}