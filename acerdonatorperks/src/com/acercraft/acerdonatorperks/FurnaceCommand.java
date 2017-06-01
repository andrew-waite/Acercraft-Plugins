package com.acercraft.acerdonatorperks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceInventory;

public class FurnaceCommand implements CommandExecutor{
	
	   public HashMap<Player, Furnace> furnacedb = new HashMap<Player, Furnace>();

	    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	        if (cmd.getName().equalsIgnoreCase("furnace")) {
	        	Player player = (Player)sender;
	            if (!hasPerm(player, "acerdonatorperks.furnace")) {
	              player.sendMessage(ChatColor.RED + "Sorry you do not have permission");
	                return true;
	            }
	            if (!(sender instanceof Player)) {
	                sender.sendMessage(ChatColor.RED + "This command is only available to players!");
	                return true;
	            }
	            if (args.length < 1) {
	                sender.sendMessage(ChatColor.RED + "/furnace set - to set a furnace to use then /furnace show - to use it");
	                return false;
	            }
	            Player p = (Player) sender;
	            String command = args[0].toLowerCase();
	            if (command.equals("set")) {
	                if (!(this.getTarget(p).getState() instanceof Furnace)) {
	                    sender.sendMessage(ChatColor.RED + "That's not a furnace! Please make sure you looking at a furnace");
	                    return true;
	                }
	                Furnace f = (Furnace) this.getTarget(p).getState();
	                furnacedb.put(p, f);
	                sender.sendMessage(ChatColor.BLUE + "Furnace set.");
	                return true;
	            } else if (command.equals("show")) {
	                if (!furnacedb.containsKey(p)) {
	                    sender.sendMessage(ChatColor.RED + "You must first set a furnace!");
	                    return true;
	                }
	                Furnace f = furnacedb.get(p);
	                if (!(f.getBlock().getState() instanceof Furnace)) {
	                    sender.sendMessage(ChatColor.RED + "The furnace is no longer there!");
	                    return true;
	                }
	                f = (Furnace) f.getBlock().getState();
	                FurnaceInventory fi = f.getInventory();
	                p.openInventory(fi);
	                sender.sendMessage(ChatColor.BLUE + "Opened your furnace for you.");
	                return true;
	            } else {
	                sender.sendMessage(ChatColor.RED + "Try " + ChatColor.GRAY + "/" + label + ChatColor.BLUE + ".");
	                return true;
	            }
	        }
	        return false;
	    }
	    
	    public boolean hasPerm(Player p, String node){
	    	return p.hasPermission(node);
	    }
	    
	    public Block getTarget(Player p) {
	        return p.getTargetBlock(AIR_MATERIALS_TARGET, 300);
	    }
	    
	    public static final Set<Integer> AIR_MATERIALS = new HashSet<Integer>();
	    public static final HashSet<Byte> AIR_MATERIALS_TARGET = new HashSet<Byte>();

	    static {
	        AIR_MATERIALS.add(Material.AIR.getId());
	        AIR_MATERIALS.add(Material.SAPLING.getId());
	        AIR_MATERIALS.add(Material.POWERED_RAIL.getId());
	        AIR_MATERIALS.add(Material.DETECTOR_RAIL.getId());
	        AIR_MATERIALS.add(Material.LONG_GRASS.getId());
	        AIR_MATERIALS.add(Material.DEAD_BUSH.getId());
	        AIR_MATERIALS.add(Material.YELLOW_FLOWER.getId());
	        AIR_MATERIALS.add(Material.RED_ROSE.getId());
	        AIR_MATERIALS.add(Material.BROWN_MUSHROOM.getId());
	        AIR_MATERIALS.add(Material.RED_MUSHROOM.getId());
	        AIR_MATERIALS.add(Material.TORCH.getId());
	        AIR_MATERIALS.add(Material.REDSTONE_WIRE.getId());
	        AIR_MATERIALS.add(Material.SEEDS.getId());
	        AIR_MATERIALS.add(Material.SIGN_POST.getId());
	        AIR_MATERIALS.add(Material.WOODEN_DOOR.getId());
	        AIR_MATERIALS.add(Material.LADDER.getId());
	        AIR_MATERIALS.add(Material.RAILS.getId());
	        AIR_MATERIALS.add(Material.WALL_SIGN.getId());
	        AIR_MATERIALS.add(Material.LEVER.getId());
	        AIR_MATERIALS.add(Material.STONE_PLATE.getId());
	        AIR_MATERIALS.add(Material.IRON_DOOR_BLOCK.getId());
	        AIR_MATERIALS.add(Material.WOOD_PLATE.getId());
	        AIR_MATERIALS.add(Material.REDSTONE_TORCH_OFF.getId());
	        AIR_MATERIALS.add(Material.REDSTONE_TORCH_ON.getId());
	        AIR_MATERIALS.add(Material.STONE_BUTTON.getId());
	        AIR_MATERIALS.add(Material.SUGAR_CANE_BLOCK.getId());
	        AIR_MATERIALS.add(Material.DIODE_BLOCK_OFF.getId());
	        AIR_MATERIALS.add(Material.DIODE_BLOCK_ON.getId());
	        AIR_MATERIALS.add(Material.TRAP_DOOR.getId());
	        AIR_MATERIALS.add(Material.PUMPKIN_STEM.getId());
	        AIR_MATERIALS.add(Material.MELON_STEM.getId());
	        AIR_MATERIALS.add(Material.VINE.getId());
	        AIR_MATERIALS.add(Material.NETHER_WARTS.getId());
	        AIR_MATERIALS.add(Material.WATER_LILY.getId());
	        AIR_MATERIALS.add(Material.SNOW.getId());

	        for (Integer integer : AIR_MATERIALS)
	            AIR_MATERIALS_TARGET.add(integer.byteValue());
	        AIR_MATERIALS_TARGET.add((byte) Material.WATER.getId());
	        AIR_MATERIALS_TARGET.add((byte) Material.STATIONARY_WATER.getId());
	    }

}
