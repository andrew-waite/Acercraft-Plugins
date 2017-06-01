package me.drew1080.acerlinkbutton;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AcerLinkButton extends JavaPlugin implements Listener {

	public static Logger log = Logger.getLogger("Minecraft");
	public static boolean setbutton = false;
	private FileConfiguration customConfig;
	private File customConfigFile;
	public static String message = null;

	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		SetupConfig();
		log.info("[AcerLinkButton] AcerLinkButton is has been enabled");

	}

	public void onDisable(){
		log.info("[AcerLinkButton] AcerLinkButton is has been disabled");
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block clicked = event.getClickedBlock();
			if (clicked.getType() == Material.STONE_BUTTON) {
				List<String> locations = getConfig().getStringList("Button_Locations");
				if(locations.contains(clicked.getWorld().getName() + "|" + clicked.getX() + "|" + clicked.getY() + "|" + clicked.getZ())){

					String msgtodisplay = getCustomConfig().getString(clicked.getWorld().getName() + "|" + clicked.getX() + "|" + clicked.getY() + "|" + clicked.getZ() + ".message");
					p.sendMessage(chatcolors(msgtodisplay));
					p.sendMessage(ChatColor.GREEN + "Press 'T' and click on the link above");

				}
				else{
					if(setbutton == true){
						locations.add(clicked.getWorld().getName() + "|" + clicked.getX() + "|" + clicked.getY() + "|" + clicked.getZ());
						getConfig().set("Button_Locations", locations);
						saveConfig();
						reloadConfig();
						getCustomConfig().set(clicked.getWorld().getName() + "|" + clicked.getX() + "|" + clicked.getY() + "|" + clicked.getZ() + ".message", message);
						saveCustomConfig();
						reloadConfig();
						// p.sendMessage("this is a new thing so put shit in here to save");
						p.sendMessage(ChatColor.GOLD + "[" + ChatColor.RED + "AcerLinkButton" + ChatColor.GOLD + "]" + ChatColor.GREEN + " Successfully made a link button");
						setbutton = false;
						
					}else{

					}
				}

			}
		}
	}

	 @EventHandler
	public void onPlayerButtonBreak(BlockBreakEvent event){
		if(event.getBlock().getType() == Material.STONE_BUTTON){
			if(hasperm(event.getPlayer(), "AcerLinkButton.break")){
			
				List<?> locations = getConfig().getStringList("Button_Locations");
				Block block = event.getBlock();
				locations.remove(block.getWorld().getName() + "|" + block.getX() + "|" + block.getY() + "|" + block.getZ());
			
				getConfig().set("Button_Locations", locations);
				saveConfig();
				reloadConfig();
				
				String messageremove = getCustomConfig().getString(block.getWorld().getName() + "|" + block.getX() + "|" + block.getY() + "|" + block.getZ());
				getCustomConfig().set(messageremove, null);
				saveCustomConfig();
				reloadCustomConfig();
				event.getPlayer().sendMessage(ChatColor.GOLD + "[" + ChatColor.RED + "AcerLinkButton" + ChatColor.GOLD + "]" + ChatColor.GREEN + "You broke a link button");
				
			}else{
				event.getPlayer().sendMessage(ChatColor.GOLD + "[" + ChatColor.RED + "AcerLinkButton" + ChatColor.GOLD + "]" + ChatColor.RED + " Error: You do not have permission to break this block");
			}
		}
	}  

	public boolean onCommand(CommandSender s, Command c, String l, String[] args){
		if(c.getName().equalsIgnoreCase("acerlb")&& hasperm((Player)s, "AcerLinkButton.create")){

				if(args.length == 0){
					s.sendMessage(ChatColor.GOLD + "[" + ChatColor.RED + "AcerLinkButton" + ChatColor.GOLD + "]" + ChatColor.RED + " Error: Try, " + ChatColor.AQUA + "/acerlb <message>");
					return false;
				}
					 
					s.sendMessage(ChatColor.GOLD + "[" + ChatColor.RED + "AcerLinkButton" + ChatColor.GOLD + "]" + ChatColor.GREEN + " Right click the button you want to make a link button");

					StringBuilder b = new StringBuilder();
					for (int i = 0; i < args.length; i++) {
						if (i != 0)
							b.append(" ");
						b.append(args[i]);
					}
					
					
				

					message = b.toString();
					setbutton = true;
					return true;
				
			}
		
		return false;
	}


	public void SetupConfig(){
		getCustomConfig().options().copyDefaults(true);
		saveCustomConfig();
		reloadConfig();
		getConfig().options().copyDefaults(true);
		saveConfig();
		reloadConfig();
	}

	public boolean hasperm(Player p, String node){
		return p.hasPermission(node);
	}

	public void reloadCustomConfig() {
		if (this.customConfigFile == null) {
			this.customConfigFile = new File(getDataFolder(), "messages.yml");
		}
		this.customConfig = YamlConfiguration.loadConfiguration(this.customConfigFile);

		InputStream defConfigStream = getResource("messages.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = 
					YamlConfiguration.loadConfiguration(defConfigStream);
			this.customConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getCustomConfig() {
		if (this.customConfig == null) {
			reloadCustomConfig();
		}
		return this.customConfig;
	}

	public void saveCustomConfig() {
		if ((this.customConfig == null) || (this.customConfigFile == null))
			return;
		try
		{
			getCustomConfig().save(this.customConfigFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, 
					"Could not save config to " + this.customConfigFile, ex);
		}
	}

	public String chatcolors(String something){
		return ChatColor.translateAlternateColorCodes("&".charAt(0), something);
	}

}
