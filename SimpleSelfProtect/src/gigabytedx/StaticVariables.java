package gigabytedx;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import com.sk89q.worldedit.BlockVector;

public class StaticVariables {

	public static int			regionLimit;
	public static Material		material;
	public static Material		craftingMaterial;
	public static Main			plugin;
	public static boolean		debugModeEnabled;
	public static Configuration	conf;
	public static int			durationBetweenDataBackup;
	static List<RegionData>		regions			= new ArrayList<RegionData>();
	public static String		regionsSaveName	= "regions";

	public StaticVariables(Main plugin) {

		// retrieve plugin object from main.
		StaticVariables.plugin = plugin;

		// Initialize variables;
		setVariables();

	}

	public static String getRegionsSaveName() {

		return regionsSaveName;
	}

	public static void setRegionsSaveName(String regionsSaveName) {

		StaticVariables.regionsSaveName = regionsSaveName;
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

	public static BlockVector getVector2(Block block) {

		//return max block vector 20 blocks around the block
		BlockVector bv2 = new BlockVector(block.getLocation().getBlockX() + 20, 256, block.getLocation()
				.getBlockZ() + 20);
		return bv2;

	}

	public static BlockVector getVector1(Block block) {

		//return min block vector 20 blocks around the block
		BlockVector bv1 = new BlockVector(block.getLocation().getBlockX() - 20, 0, block.getLocation()
				.getBlockZ() - 20);
		return bv1;

	}

	public static List<RegionData> getRegions() {

		return regions;
	}

	public static void setRegions(List<RegionData> regions) {

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
