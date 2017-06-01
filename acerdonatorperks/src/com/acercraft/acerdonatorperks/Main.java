package com.acercraft.acerdonatorperks;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.acercraft.acerdonatorperks.acercrzyfeet.CrazyFeetJoinListener;
import com.acercraft.acerdonatorperks.acercrzyfeet.CrazyFeetListener;
import com.acercraft.acerdonatorperks.acercrzyfeet.Commands.CrazyFireCommands;
import com.acercraft.acerdonatorperks.acercrzyfeet.Commands.CrazyMagicCommands;
import com.acercraft.acerdonatorperks.acercrzyfeet.Commands.CrazyPearlCommands;
import com.acercraft.acerdonatorperks.acercrzyfeet.Commands.CrazySmokeCommands;
import com.acercraft.acerdonatorperks.acercrzyfeet.Commands.Auto.CrazyAutoFire;
import com.acercraft.acerdonatorperks.acercrzyfeet.Commands.Auto.CrazyAutoMagic;
import com.acercraft.acerdonatorperks.acercrzyfeet.Commands.Auto.CrazyAutoPearl;
import com.acercraft.acerdonatorperks.acercrzyfeet.Commands.Auto.CrazyAutoSmoke;
import com.acercraft.acerdonatorperks.acercrzyfeet.Commands.Util.CrazyCheckCommands;
import com.acercraft.acerdonatorperks.acercrzyfeet.Commands.Util.CrazyDisableCommands;
import com.acercraft.acerdonatorperks.acercrzyfeet.Commands.Util.CrazyFeetCommands;
import com.acercraft.acerdonatorperks.acercrzyfeet.Commands.Util.Auto.CrazyAutoCheck;
import com.acercraft.acerdonatorperks.acercrzyfeet.Commands.Util.Auto.CrazyAutoDisable;
import com.acercraft.acerdonatorperks.acercrzyfeet.Util.Files.CrazyAutoFireFile;
import com.acercraft.acerdonatorperks.acercrzyfeet.Util.Files.CrazyAutoMagicFile;
import com.acercraft.acerdonatorperks.acercrzyfeet.Util.Files.CrazyAutoPearlFile;
import com.acercraft.acerdonatorperks.acercrzyfeet.Util.Files.CrazyAutoSmokeFile;

public class Main extends JavaPlugin{

	public static final Logger log = Logger.getLogger("Minecraft");
	public static Permission permission = null;

	public final CrazyFeetListener playerListener = new CrazyFeetListener();
	public final CrazyFeetJoinListener playerJoinListener = new CrazyFeetJoinListener();
	public final ClimbListener alisten = new ClimbListener(this);
	public final MessageListener pl = new MessageListener(this);

	public static ArrayList<Player> CrazyFire;
	public static ArrayList<Player> CrazySmoke;
	public static ArrayList<Player> CrazyMagic;
	public static ArrayList<Player> CrazyPearl;

	public static ArrayList<String> cFPlayers;
	public static ArrayList<String> cSPlayers;
	public static ArrayList<String> cPPlayers;
	public static ArrayList<String> cMPlayers;

	public final ChatColor red = ChatColor.RED;
	public final ChatColor yellow = ChatColor.YELLOW;
	public final ChatColor green = ChatColor.GREEN;
	public final ChatColor purple = ChatColor.DARK_PURPLE;

	private CrazyFeetCommands cFeetC;
	private CrazyCheckCommands cCheckC;
	private CrazyDisableCommands cDisableC;

	private CrazyFireCommands cFireC;
	private CrazySmokeCommands cSmokeC;
	private CrazyMagicCommands cMagicC;
	private CrazyPearlCommands cPearlC;

	private CrazyAutoFire cAutoFire;
	private CrazyAutoSmoke cAutoSmoke;
	private CrazyAutoPearl cAutoPearl;
	private CrazyAutoMagic cAutoMagic;

	private CrazyAutoCheck cAutoCheck;
	private CrazyAutoDisable cAutoDisable;

	private WorkBench workbench;
	private FurnaceCommand furnace;
	private AcerNearCommand acernear;
	private ClimbCommand climb;
	public static CrazyAutoFireFile aFireP;
	public static CrazyAutoSmokeFile aSmokeP;
	public static CrazyAutoPearlFile aPearlP;
	public static CrazyAutoMagicFile aMagicP;



	private File autoFirePlayers;
	private File autoSmokePlayers;
	private File autoPearlPlayers;
	private File autoMagicPlayers;

	public static ArrayList<Integer> noVineBlocks = new ArrayList<Integer>();
	public static ArrayList<String> climbingPlayers = new ArrayList<String>();
	public static boolean usePerms = false;


	public void onEnable(){
		
		setupConfig();
		PluginManager pm = this.getServer().getPluginManager();

		CrazyFire = new ArrayList<Player>();
		CrazySmoke = new ArrayList<Player>();
		CrazyMagic = new ArrayList<Player>();
		CrazyPearl = new ArrayList<Player>();

		cFPlayers = CrazyAutoFireFile.cFPlayers;
		cSPlayers = CrazyAutoSmokeFile.cSPlayers;
		cPPlayers = CrazyAutoPearlFile.cPPlayers;
		cMPlayers = CrazyAutoMagicFile.cMPlayers;

		cFireC = new CrazyFireCommands();
		cSmokeC = new CrazySmokeCommands();
		cMagicC = new CrazyMagicCommands();
		cPearlC = new CrazyPearlCommands();

		cFeetC = new CrazyFeetCommands();
		cCheckC = new CrazyCheckCommands();
		cDisableC = new CrazyDisableCommands();

		cAutoFire = new CrazyAutoFire(this);
		cAutoSmoke = new CrazyAutoSmoke(this);
		cAutoPearl = new CrazyAutoPearl(this);
		cAutoMagic = new CrazyAutoMagic(this);

		cAutoCheck = new CrazyAutoCheck();
		cAutoDisable = new CrazyAutoDisable(this);

		workbench = new WorkBench();
		furnace = new FurnaceCommand();
		acernear = new AcerNearCommand(this);
		climb = new ClimbCommand(this);

		getCommand("CrazyFeet").setExecutor(cFeetC);
		getCommand("CrazyCheck").setExecutor(cCheckC);
		getCommand("CrazyDisable").setExecutor(cDisableC);

		getCommand("CrazyFire").setExecutor(cFireC);
		getCommand("CrazySmoke").setExecutor(cSmokeC);
		getCommand("CrazyMagic").setExecutor(cMagicC);
		getCommand("CrazyPearl").setExecutor(cPearlC);

		getCommand("CrazyAutoFire").setExecutor(cAutoFire);
		getCommand("CrazyAutoSmoke").setExecutor(cAutoSmoke);
		getCommand("CrazyAutoPearl").setExecutor(cAutoPearl);
		getCommand("CrazyAutoMagic").setExecutor(cAutoMagic);

		getCommand("CrazyAutoCheck").setExecutor(cAutoCheck);
		getCommand("CrazyAutoDisable").setExecutor(cAutoDisable);

		getCommand("workbench").setExecutor(workbench);
		getCommand("furnace").setExecutor(furnace);
		getCommand("acernear").setExecutor(acernear);
		getCommand("climb").setExecutor(climb);
		getCommand("anvil").setExecutor(new AnvilCommand());

		pm.registerEvents(this.playerListener, this);
		pm.registerEvents(this.playerJoinListener, this);
		pm.registerEvents(alisten, this);
		pm.registerEvents(pl, this);

		String folder = this.getDataFolder().getAbsolutePath();
		(new File(folder)).mkdir();

		autoFirePlayers = new File(folder+File.separator+"AutoFirePlayers.txt");
		autoSmokePlayers = new File(folder+File.separator+"AutoSmokePlayers.txt");
		autoPearlPlayers = new File(folder+File.separator+"AutoPearlPlayers.txt");
		autoMagicPlayers = new File(folder+File.separator+"AutoMagicPlayers.txt");

		aFireP = new CrazyAutoFireFile(autoFirePlayers);
		aSmokeP = new CrazyAutoSmokeFile(autoSmokePlayers);
		aPearlP = new CrazyAutoPearlFile(autoPearlPlayers);
		aMagicP = new CrazyAutoMagicFile(autoMagicPlayers);

		aFireP.loadAutoFirePlayers();
		aSmokeP.loadAutoSmokePlayers();
		aPearlP.loadAutoPearlPlayers();
		aMagicP.loadAutoMagicPlayers();

		defineNoVineBlocks();
		usePerms = getConfig().getBoolean("usePermissions");

		log.info("[AcerDonatorPerks] AcerDonatorPerks is enabled");
	}

	public void onDisable(){
		noVineBlocks = null;
		climbingPlayers = null;
		log.info("[AcerDonatorPerks] AcerDonatorPerks is disabled");

	}

	public CrazyAutoFireFile getAFirePlayers() {
		return aFireP;
	}

	public CrazyAutoSmokeFile getASmokePlayers() {
		return aSmokeP;
	}

	public CrazyAutoPearlFile getAPearlPlayers() {
		return aPearlP;
	}

	public CrazyAutoMagicFile getAMagicPlayers() {
		return aMagicP;
	}

	public void setupConfig(){
		if(!new File(getDataFolder(), "config.yml").exists()){
			log.info("[AcerDonatorPerks] Detected no config.yml file.");
			log.info("[AcerDonatorPerks] Generating one now...");
			
			getConfig().set("defaultNear", Double.valueOf(50));
			getConfig().set("maxNear", Double.valueOf(1000));
			getConfig().set("Join", String.valueOf("A join message %player%"));
			getConfig().set("Leave", String.valueOf("A quit message %player%"));

		/*	String[] groups = permission.getGroups();
			List<String> list = Arrays.asList(groups);
			for(int i = 0; i < list.size(); i++){
				String groupname = list.get(i);
				this.getConfig().set(groupname+".defaultNear", Double.valueOf(50));
				this.getConfig().set(groupname+".maxNear",Double.valueOf(1000));
				this.saveConfig();
				this.reloadConfig();
			} */


			getConfig().options().copyDefaults(true);
			saveConfig();
			reloadConfig();
			log.info("[AcerDonatorPerks] Config.yml successfully recreated");
		}
	}

	public void defineNoVineBlocks() {
		noVineBlocks.add(Integer.valueOf(Material.THIN_GLASS.getId()));
		noVineBlocks.add(Integer.valueOf(44));
		noVineBlocks.add(Integer.valueOf(126));
		noVineBlocks.add(Integer.valueOf(Material.WOOD_STAIRS.getId()));
		noVineBlocks.add(Integer.valueOf(Material.JUNGLE_WOOD_STAIRS.getId()));
		noVineBlocks.add(Integer.valueOf(Material.BIRCH_WOOD_STAIRS.getId()));
		noVineBlocks.add(Integer.valueOf(Material.SPRUCE_WOOD_STAIRS.getId()));
		noVineBlocks.add(Integer.valueOf(Material.COBBLESTONE_STAIRS.getId()));
		noVineBlocks.add(Integer.valueOf(Material.BRICK_STAIRS.getId()));
		noVineBlocks.add(Integer.valueOf(Material.WOOD_STAIRS.getId()));
		noVineBlocks.add(Integer.valueOf(Material.SMOOTH_STAIRS.getId()));
		noVineBlocks.add(Integer.valueOf(Material.NETHER_BRICK_STAIRS.getId()));
		noVineBlocks.add(Integer.valueOf(Material.SANDSTONE_STAIRS.getId()));
		noVineBlocks.add(Integer.valueOf(Material.FENCE.getId()));
		noVineBlocks.add(Integer.valueOf(Material.FENCE_GATE.getId()));
		noVineBlocks.add(Integer.valueOf(Material.NETHER_FENCE.getId()));
		noVineBlocks.add(Integer.valueOf(Material.LADDER.getId()));
		noVineBlocks.add(Integer.valueOf(Material.VINE.getId()));
		noVineBlocks.add(Integer.valueOf(Material.BED.getId()));
		noVineBlocks.add(Integer.valueOf(Material.BED_BLOCK.getId()));
		noVineBlocks.add(Integer.valueOf(Material.IRON_FENCE.getId()));
		noVineBlocks.add(Integer.valueOf(Material.SNOW.getId()));
		noVineBlocks.add(Integer.valueOf(Material.SIGN.getId()));
		noVineBlocks.add(Integer.valueOf(Material.LEVER.getId()));
		noVineBlocks.add(Integer.valueOf(Material.TRAP_DOOR.getId()));
		noVineBlocks.add(Integer.valueOf(Material.PISTON_EXTENSION.getId()));
		noVineBlocks.add(Integer.valueOf(Material.PISTON_MOVING_PIECE.getId()));
		noVineBlocks.add(Integer.valueOf(Material.TRIPWIRE_HOOK.getId()));
		noVineBlocks.add(Integer.valueOf(93));
		noVineBlocks.add(Integer.valueOf(94));
		noVineBlocks.add(Integer.valueOf(Material.BOAT.getId()));
		noVineBlocks.add(Integer.valueOf(Material.MINECART.getId()));
		noVineBlocks.add(Integer.valueOf(Material.CAKE.getId()));
		noVineBlocks.add(Integer.valueOf(Material.CAKE_BLOCK.getId()));
		noVineBlocks.add(Integer.valueOf(Material.WATER.getId()));
		noVineBlocks.add(Integer.valueOf(Material.STATIONARY_WATER.getId()));
		noVineBlocks.add(Integer.valueOf(Material.LAVA.getId()));
		noVineBlocks.add(Integer.valueOf(Material.STATIONARY_LAVA.getId()));
	}

}
