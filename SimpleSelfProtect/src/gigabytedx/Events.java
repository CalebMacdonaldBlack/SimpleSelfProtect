package gigabytedx;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;


public class Events {

	@EventHandler
	public void onCraft(PrepareItemCraftEvent e) {

		if (e.getInventory().getResult().getType().equals(StaticVariables.getMaterial())) {
			e.getInventory().getResult().setItemMeta(getMeta());
		}
	}

	@EventHandler
	public void onPickUp(PlayerPickupItemEvent e) {

		if (e.getItem().getItemStack().getType().equals(StaticVariables.getMaterial()))
			e.getItem().getItemStack().setItemMeta(getMeta());
	}

	


	@EventHandler
	public void pistonExtend(BlockPistonExtendEvent e) {

		for (Block x : e.getBlocks()) {
			if (x.getType().equals(StaticVariables.getMaterial()))
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void pistonPush(BlockPistonRetractEvent e) {

		if (e.getRetractLocation().getBlock().getType().equals(StaticVariables.getMaterial()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {

		if (e.getBlock().getType().equals(StaticVariables.getMaterial())
				&& WorldGuardPlugin.inst().getRegionManager(e.getBlock().getWorld())
						.getRegionCountOfPlayer(WorldGuardPlugin.inst().wrapPlayer(e.getPlayer())) < regionLimit) {

			landClaimTest(e);

		} else if (e.getBlock().getType().equals(StaticVariables.getMaterial())) {
			e.getPlayer().sendMessage(ChatColor.RED + "Your trying to claim too much land!");
			e.setCancelled(true);
		}
	}





	@EventHandler
	public void blockDestroy(BlockBreakEvent e) {

		// if block broken is a gold block
		if (e.getBlock().getType().equals(StaticVariables.getMaterial())
				&& WorldGuardPlugin.inst().canBuild(e.getPlayer(), e.getBlock().getLocation())) {
			deleteClaimedLand(e);

		} else if (e.getBlock().getType().equals(StaticVariables.getMaterial())
				&& !WorldGuardPlugin.inst().canBuild(e.getPlayer(), e.getBlock().getLocation()))
			e.setCancelled(false);
	}
}
