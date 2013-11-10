package gigabytedx;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Main extends JavaPlugin {

	public final Logger	logger				= Logger.getLogger("Minecraft");
	static String		pluginName;
	public static Main	plugin;
	boolean				f					= false;
	int					regionLimit;
	boolean				worldguardLoaded	= false;
	boolean				reload				= true;

	@Override
	public void onDisable() {

		// Output to console when onDisable() method has been executed.
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Has Been Disabled!");
		try {
			ReadAndWrite.save("regions");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEnable() {

		// Output to console when onEnable() method has been executed.
		PluginDescriptionFile pdfFile = this.getDescription();
		pluginName = pdfFile.getName();
		this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " Has Been Enabled!");

		// Save configuration file if not already saved;
		this.saveDefaultConfig();

		// Register event handling
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new Events(), this);

		// Initialize Variables
		new StaticVariables(this);

		// get region data from file
		ReadAndWrite.writeFile();
		// initialize scheduled tasks
		new SchedualedTasks(this);

	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, final String[] args) {

		// get player who sent command
		Player player = (Player) sender;

		// convert player to worldguard localplayer
		final com.sk89q.worldguard.LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(player.getPlayer());

		// if command entered was "friend" and a valid player name followed the
		// execute method
		if (commandLabel.equalsIgnoreCase("friend") && args.length == 1
				&& RegionHandling.playeronlineTest(args[0])) {
			RegionHandling.friend(lp, args[0]);

			// if a valid player name was not entered send error msg
		} else if (commandLabel.equalsIgnoreCase("friend") && args.length == 1)
			player.sendMessage(ChatColor.RED + "Player not found! Is the player online?");

		// if command is /unfriend and player exists then unfriend player
		if (commandLabel.equalsIgnoreCase("unfriend") && args.length == 1
				&& !Bukkit.getOfflinePlayer(args[0]).equals(null))
			RegionHandling.prepareUnfriend(lp, player, args);

		// send error message to player if the player cannot be found
		else if (commandLabel.equalsIgnoreCase("unfriend"))
			player.sendMessage(ChatColor.RED + "This player does not exist");

		return false;
	}

	public static void sendDebugInfo(String debugText) {

		// output debug info if debug mode is enabled.
		if (StaticVariables.isDebugModeEnabled())
			// Output received text to console with info prefix.

			System.out.println(pluginName + " " + debugText);

	}

	public static void sendSevereInfo(String debugText) {

		// Output received text to console with severe prefix.
		System.out.println(pluginName + " " + debugText);

	}

}
