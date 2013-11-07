package gigabytedx;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Main extends JavaPlugin implements Listener {

	public final Logger		logger				= Logger.getLogger("Minecraft");
	public static Main		plugin;
	boolean					f					= false;
	int						regionLimit;
	boolean					worldguardLoaded	= false;
	Material				material;
	Material				craftingMaterial;
	static List<RegionData>		regions				= new ArrayList<RegionData>();
	boolean					reload				= true;
	

	@Override
	public void onDisable() {
		
		//Output to console when onDisable() method has been executed.
		this.logger.info(StaticVariables.pdfFile.getName() + " Has Been Disabled!");
		try {
			ReadAndWrite.save("regions");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEnable() {
		
		//Output to console when onEnable() method has been executed.
		this.logger.info(StaticVariables.pdfFile.getName() + " Version " + StaticVariables.pdfFile.getVersion() + " Has Been Enabled!");
		
		//Save configuration file if not already saved;
		this.saveDefaultConfig();
		
		//Register event handling
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this, this);

		//Initialize Variables
		new StaticVariables(this);
		
		//initialize scheduled tasks
		new SchedualedTasks(this);

	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, final String[] args) {
		
		//get player who sent command
		Player player = (Player) sender;
		
		//convert player to worldguard localplayer
		final com.sk89q.worldguard.LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(player.getPlayer());

		//if command entered was "accept" and if player has been sent a request then execute method
		if (commandLabel.equalsIgnoreCase("accept") && StaticVariables.getPendingAccept().contains(player)) {
			RegionHandling.acceptRequest(lp, player);

		//if command entered was "accept" and the player is not pending a request then send a msg to them saying they don't have a request
		} else if (commandLabel.equalsIgnoreCase("accept") && !StaticVariables.getPendingAccept().contains(player))
			player.sendMessage(ChatColor.RED + "your didn't get send a request!");

		//if command entered was "friend" and a valid player name followed the execute method
		if (commandLabel.equalsIgnoreCase("friend") && args.length == 1 && RegionHandling.playeronlineTest(args[0])) {
			RegionHandling.sendRequest(lp, player, args);

		//if  a valid player name was not entered send error msg
		} else if (commandLabel.equalsIgnoreCase("friend") && args.length == 1)
			player.sendMessage(ChatColor.RED + "Player not found");
		
		//if  command was "unfriend" then attempt to unfriend the player
		try {
			if (commandLabel.equalsIgnoreCase("unfriend") && args.length == 1) {
				RegionHandling.prepareUnfriend(lp, player, args);
				
				//else if player is the owner execute method
			} else if (commandLabel.equalsIgnoreCase("unfriend") && args.length == 1)
				player.sendMessage(ChatColor.RED + "You cannot unfriend this player! he must unfriend you.");
			
			//if player cannot be found send error msg
		} catch (NullPointerException e) {
			Main.sendSevereInfo("Player " + args[0] + " could not be unfriended by " + player.getName());
			player.sendMessage(ChatColor.RED + "You cannot unfriend this person!");
		}
		return false;
	}



	
	public static void sendDebugInfo(String debugText){
		
		//output debug info if debug mode is enabled.
		if(StaticVariables.isDebugModeEnabled())
			//Output received text to console with info prefix.
			System.out.println(StaticVariables.pdfFile.getName() + " " + debugText);
		
	}
	
	public static void sendSevereInfo(String debugText){
			//Output received text to console with severe prefix.
			System.out.println(StaticVariables.pdfFile.getName() + " " + debugText);
		
	}

}
