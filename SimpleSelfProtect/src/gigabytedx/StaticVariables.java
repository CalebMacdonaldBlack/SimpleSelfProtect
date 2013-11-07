package gigabytedx;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import com.sk89q.worldedit.BlockVector;

public class StaticVariables {

	public static int			regionLimit;
	public static Material		material;
	public static Material		craftingMaterial;
	public static Main					plugin;
	public static boolean		debugModeEnabled;
	public static Configuration	conf;
	public static int			durationBetweenDataBackup;
	static List<region>			regions	= new ArrayList<region>();
	public static PluginDescriptionFile	pdfFile	= plugin.getDescription();
	public static ArrayList<Player>		pendingAccept		= new ArrayList<Player>();
	public static ArrayList<Player>		friendSender		= new ArrayList<Player>();
	public static String regionsSaveName = "regions";

	public StaticVariables(Main plugin) {

		// retrieve plugin object from main.
		StaticVariables.plugin = plugin;

		// Initialize variables;
		setVariables();

	}
	
	
	public static ArrayList<Player> getPendingAccept() {
	
		return pendingAccept;
	}

	
	
	public static String getRegionsSaveName() {
	
		return regionsSaveName;
	}


	
	public static void setRegionsSaveName(String regionsSaveName) {
	
		StaticVariables.regionsSaveName = regionsSaveName;
	}


	public static void setPendingAccept(ArrayList<Player> pendingAccept) {
	
		StaticVariables.pendingAccept = pendingAccept;
	}

	
	public static ArrayList<Player> getFriendSender() {
	
		return friendSender;
	}

	
	public static void setFriendSender(ArrayList<Player> friendSender) {
	
		StaticVariables.friendSender = friendSender;
	}

	private void setVariables() {

		// create configuration object and set to public variable.
		conf = plugin.getConfig();

		// set maximum amount of regions a player or group can claim from
		// configuration object
		regionLimit = (int) conf.get("regionLimit");

		// Set the material of the "claim block" from the configuration file
		material = Material.getMaterial((String) conf.get("claimBlock"));

		// set debug mode from configuration file
		debugModeEnabled = conf.getBoolean("DebugMode");

		durationBetweenDataBackup = conf.getInt("durationBetweenDataBackupInSeconds");

	}
	
	public BlockVector getVector2(Block block) {

		BlockVector bv2 = new BlockVector(block.getLocation().getBlockX() + 20, 256, block.getLocation()
				.getBlockZ() + 20);
		return bv2;

	}

	public BlockVector getVector1(Block block) {

		BlockVector bv1 = new BlockVector(block.getLocation().getBlockX() - 20, 0, block.getLocation()
				.getBlockZ() - 20);
		return bv1;

	}

	
	public static List<region> getRegions() {
	
		return regions;
	}

	
	public static void setRegions(List<region> regions) {
	
		StaticVariables.regions = regions;
	}

	

	public static boolean isDebugModeEnabled() {

		return debugModeEnabled;
	}

	public static Configuration getConf() {

		return conf;
	}

	public static int getDurationBetweenDataBackup() {

		return durationBetweenDataBackup;
	}

	public static int getRegionLimit() {

		return regionLimit;
	}

	public static Material getMaterial() {

		return material;
	}

	public static Material getCraftingMaterial() {

		return craftingMaterial;
	}
	
	
}
