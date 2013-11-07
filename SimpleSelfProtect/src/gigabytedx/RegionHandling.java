package gigabytedx;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;


public class RegionHandling {

	public static void setRegions() {

		//loop through all the regions in the regions object retrieved from the data file and create them
		for (region r : StaticVariables.getRegions()) {
			
			//create a protected cuboid region to hold all the region data
			ProtectedCuboidRegion pcr;
			try {
				
				//attempt to set regions
				WorldGuardPlugin.inst().getRegionManager(Bukkit.getWorld(r.getWorld())).addRegion(
								pcr = new ProtectedCuboidRegion(r.getName(), new BlockVector(r.getX1(), r.getY1(), r.getZ1()),new BlockVector(r.getX2(), r.getY2(), r.getZ2())));

				//create object to hold players that have permission to build in the region
				DefaultDomain owners = new DefaultDomain();
				DefaultDomain members = new DefaultDomain();
				
				//add the owners to the default domain object from regions object that was retrieved from the save file
				for (String s : r.getOwners()) {
					owners.addPlayer(s);
				}
				
				//add members to the default domain object from regions object that was retrieved from the save file
				for (String s : r.getMembers()) {
					members.addPlayer(s);
				}
				
				//set default domain of owners to their region
				pcr.setOwners(owners);
				pcr.setOwners(members);
				
			} catch (NullPointerException e) {
				//if worldguard was not loaded, reload bukkit
				
				//output error msg to console
				Main.sendSevereInfo("Worldguard not loaded properly! attempting to fix the issue. Plugins reloading in 5 seconds.");
				
				//start timer that reloads the plugins
				SchedualedTasks.runReload();
			}
		}

	}
	
	public void saveRegion(ProtectedCuboidRegion pcr, String world) {

		//set first vector
		double x1 = pcr.getMinimumPoint().getX();
		double y1 = pcr.getMinimumPoint().getY();
		double z1 = pcr.getMinimumPoint().getZ();

		//set second vector
		double x2 = pcr.getMaximumPoint().getX();
		double y2 = pcr.getMaximumPoint().getY();
		double z2 = pcr.getMaximumPoint().getZ();

		//set owners and region name
		String name = pcr.getId();
		DefaultDomain owners = pcr.getOwners();
		DefaultDomain members = pcr.getMembers();

		//add region to regions object
		StaticVariables.regions.add(new region(world, x1, y1, z1, x2, y2, z2, name, owners.getPlayers(), members.getPlayers()));
		
		
		try {
			//attempt to save to regions file
			ReadAndWrite.save(StaticVariables.getRegionsSaveName());
			
		} catch (FileNotFoundException e) {
			//send error message to console if saving failed
			Main.sendSevereInfo("A problem occurred attempting to read from the region data file : FileNotFoundException");
			e.printStackTrace();
		}
	}
	
	private void removeRegion(ProtectedCuboidRegion pcr) {

		try {
			
			//loop through all of the regions to find a specific one
			for (region r : StaticVariables.regions) {
				
				//check to see if name of the current region in the loop matches the one pending deletion 
				if (r.getName().equals(pcr.getId())) {
					
					//remove region from object
					StaticVariables.regions.remove(r);
					
					try {
						//attempt to save the data to the regions file
						ReadAndWrite.save(StaticVariables.getRegionsSaveName());
						Main.sendDebugInfo("Region removed successfully");
						
						
					} catch (FileNotFoundException e) {
						
						//send error msg to console if the fileNotFoundException occurs
						Main.sendSevereInfo("A problem occurred attempting to read from the region data file : FileNotFoundException");
						e.printStackTrace();
					}
				}

			}
		} catch (ConcurrentModificationException e) {
			//send error message
			Main.sendDebugInfo("A ConcurrentModificationException has been handled");
		}
	}
	

	public static void prepareUnfriend(LocalPlayer lp, Player player, String[] args) {

		if (unfriend(lp, args[0])) {
			Bukkit.getPlayer(lp.getName()).sendMessage(	ChatColor.GOLD + "You have unfriended " + ChatColor.BLUE + args[0]	+ ChatColor.GOLD + ".");
			Bukkit.getPlayer(args[0]).sendMessage(ChatColor.RED + "You have been unfriended by " + ChatColor.BLUE + lp.getName()+ ChatColor.RED + ".");
		} else
			Bukkit.getPlayer(lp.getName()).sendMessage(
					ChatColor.RED + "Player " + ChatColor.BLUE + args[0] + ChatColor.RED + " not found.");

	}

	public static void sendRequest(LocalPlayer lp, Player player, String[] args) {

		StaticVariables.getPendingAccept().add(StaticVariables.getPendingAccept().size(), Bukkit.getPlayer(args[0]));
		StaticVariables.getFriendSender().add(StaticVariables.getFriendSender().size(), player);
		Bukkit.getPlayer(lp.getName()).sendMessage(
				ChatColor.GOLD + "You sent a request to " + ChatColor.BLUE + args[0] + ChatColor.GOLD + ".");
		Bukkit.getPlayer(args[0]).sendMessage(
				ChatColor.BLUE + player.getName() + ChatColor.GOLD
						+ " sent you a request to be join his team! Type /accept to join!");

	}

	public static void acceptRequest(LocalPlayer lp, Player player) {

		friend(lp, StaticVariables.getFriendSender().get(StaticVariables.getPendingAccept().indexOf(player)).getName());
		StaticVariables.getFriendSender().remove(StaticVariables.getPendingAccept().indexOf(player));
		StaticVariables.getPendingAccept().remove(player);

	}

	public static boolean playeronlineTest(String args) {

		try {
			Bukkit.getPlayer(args).isOnline();
			return true;
		} catch (NullPointerException n) {
			return false;
		}
	}

	private static void friend(LocalPlayer lp, String args) {

		setFriends(lp, args);
		Bukkit.getPlayer(lp.getName()).sendMessage(ChatColor.GOLD + "You added " + args + " to your group!");

	}

	private static boolean unfriend(LocalPlayer lp, String args) {

		List<ProtectedRegion> list = new ArrayList<ProtectedRegion>();
		if (this.getConfig().getList(lp.getName()).contains(lp.getName()))
			if (removeBuildPerms(list, lp, args))
				return true;
		return false;
	}

	private static boolean removeBuildPerms(List<ProtectedRegion> list, LocalPlayer lp, String args) {

		if (getConfig().contains(args)) {
			this.getConfig().getList(lp.getName()).remove((args));
			this.getConfig().set(args + "group", args);
			this.saveConfig();

			removeFriendFromRegion(lp, args);
			return true;
		}
		return false;

	}

	private void removeFriendFromRegion(LocalPlayer lp, String args) {

		//create hashmap object to hold regions
		Map<String, ProtectedRegion> regions;
		
		//loop through each world and get region manager
		for (World x : Bukkit.getWorlds()) {
			regions = WorldGuardPlugin.inst().getRegionManager(x).getRegions();

			//loop through each region for that world 
			for (String s : regions.keySet()) {
				
				if (WorldGuardPlugin.inst().getRegionManager(x).getRegion(s).isOwner(WorldGuardPlugin.inst().wrapPlayer(Bukkit.getPlayer(args)))){
					WorldGuardPlugin.inst().getRegionManager(x).getRegion(s).getMembers().removePlayer(name);
				}

			}
		}

	}

	private static void setFriends(LocalPlayer lp, String args) {
		
		//create hashmap object to hold regions
		Map<String, ProtectedRegion> regions;

		//loop through each world and get region manager
		for (World x : Bukkit.getWorlds()) {
			regions = WorldGuardPlugin.inst().getRegionManager(x).getRegions();
			
			//loop through each region for that world 
			for (String s : regions.keySet()) {
				
				//check if player sending command owns the region
				if (WorldGuardPlugin.inst().getRegionManager(x).getRegion(s).isOwner(WorldGuardPlugin.inst().wrapPlayer(Bukkit.getPlayer(args)))) {
					
					//add the friend as a member to that region
					DefaultDomain temp = WorldGuardPlugin.inst().getRegionManager(x).getRegion(s).getMembers();
					temp.addPlayer(args);
				}
			}
		}
	}
	
	private void deleteClaimedLand(BlockBreakEvent e) {

		if (checkPlayerHasSavedData(e.getPlayer())) {

			ProtectedCuboidRegion pcr = new ProtectedCuboidRegion(e.getBlock().getLocation().toString(),
					getVector1(e.getBlock()), getVector2(e.getBlock()));
			WorldGuardPlugin.inst().getRegionManager(e.getBlock().getWorld()).removeRegion(pcr.getId());
			removeRegion(pcr);
			e.getPlayer().sendMessage(
					ChatColor.GOLD + "You destroyed a claim stone. This area is no longer protected!");
		}
	}

	public boolean isProtected(Location l, BlockPlaceEvent e) {

		ApplicableRegionSet arSet = WorldGuardPlugin.inst().getRegionManager(e.getBlock().getWorld())
				.getApplicableRegions(l);

		if (arSet.size() == 0 || isPlayerOwner(e))
			return false;
		return true;
	}

	public boolean isLandClaimed(Location l, BlockPlaceEvent e) {

		for (int x = (int) (l.getX() - 20); x <= l.getX() + 20; x++) {
			for (int z = (int) (l.getZ() - 20); z <= l.getZ() + 20; z++) {
				if (isProtected(new Location(e.getBlock().getWorld(), x, 10, z), e) == true)
					return true;
			}

		}
		return false;

	}
	
	private void landClaimTest(BlockPlaceEvent e) {

		if (isLandClaimed(e.getBlock().getLocation(), e) == false && e.getBlock().getType() == material
				|| e.getBlock().getType() == material && isPlayerOwner(e))
			claimArea(e);
		else if (e.getBlock().getType() == material) {

			e.getPlayer().sendMessage(ChatColor.RED + "You cannot claim on other peoples land!");
			e.setCancelled(true);

		}

	}

	private boolean isPlayerOwner(BlockPlaceEvent e) {

		ApplicableRegionSet arSet = WorldGuardPlugin.inst().getRegionManager(e.getBlock().getWorld())
				.getApplicableRegions(e.getBlock().getLocation());

		if (arSet.isOwnerOfAll(WorldGuardPlugin.inst().wrapPlayer(e.getPlayer()))) {
			return true;
		}
		return false;
	}

	private void claimArea(BlockPlaceEvent e) {

		createList(e.getPlayer().getName());
		ProtectedCuboidRegion pcr = new ProtectedCuboidRegion(e.getBlock().getLocation().toString(),
				getVector1(e.getBlock()), getVector2(e.getBlock()));
		createRegion(pcr, e);

	}

	private void createRegion(ProtectedCuboidRegion pcr, BlockPlaceEvent e) {

		if (!WorldGuardPlugin.inst().getRegionManager(e.getBlock().getWorld())
				.overlapsUnownedRegion(pcr, WorldGuardPlugin.inst().wrapPlayer(e.getPlayer()))) {
			pcr.setFlag(DefaultFlag.FAREWELL_MESSAGE, ChatColor.GRAY + "You have left a claimed area!");
			pcr.setFlag(DefaultFlag.GREET_MESSAGE, ChatColor.GRAY + "You have entered a claimed area!");
			addOwners(pcr, e.getPlayer().getName());
			WorldGuardPlugin.inst().getRegionManager(e.getBlock().getWorld()).addRegion(pcr);
			saveRegion(pcr, e.getBlock().getWorld().getName());
			e.getPlayer().sendMessage(ChatColor.GOLD + "You have successfully claimed this area!");
		} else {
			e.getPlayer().sendMessage(ChatColor.RED + "You cannot claim on other peoples land!");
			e.setCancelled(true);
		}

	}

	private void addOwners(ProtectedCuboidRegion pcr, String string) {

		createList(string);
		if (hasGroup(string)) {
			pcr.setOwners(getDomain((String) this.getConfig().get(string + "group")));
		} else {
			this.getConfig().set(string + "group", string);
			this.saveConfig();
			pcr.setOwners(getDomain((String) this.getConfig().get(string + "group")));
		}
	}
}
