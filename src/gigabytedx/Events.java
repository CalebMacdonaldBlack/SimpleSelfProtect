package gigabytedx;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Events implements Listener {

	// When player starts crafting check to see whether the block they are
	// crafting is the claim stone material if so, set the meta data
	@EventHandler
	public void onCraft(PrepareItemCraftEvent e) {

		// if material of crafted item is the claimstone then set meta data
		if (e.getInventory().getResult().getType().equals(StaticVariables.getMaterial())) {
			e.getInventory().getResult().setItemMeta(ItemMetaSet.getMeta());
		}
	}

	// when an item is picked up
	@EventHandler
	public void onPickUp(PlayerPickupItemEvent e) {

		// if item picked up is the claim stone, set its meta data
		if (e.getItem().getItemStack().getType().equals(StaticVariables.getMaterial()))
			e.getItem().getItemStack().setItemMeta(ItemMetaSet.getMeta());
	}

	// when a piston has been extended
	@EventHandler
	public void pistonExtend(BlockPistonExtendEvent e) {

		// if any of the blocks moved are a claim stone then cancel the event
		for (Block x : e.getBlocks()) {
			if (x.getType().equals(StaticVariables.getMaterial()))
				e.setCancelled(true);
		}
	}

	// when a piston is retracted
	@EventHandler
	public void pistonPush(BlockPistonRetractEvent e) {

		// check to see whether the retracted block is a claim stone. if so
		// cancel the event.
		if (e.getRetractLocation().getBlock().getType().equals(StaticVariables.getMaterial()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {

		// if block placed is gold and player is not limited.
		if (e.getBlock().getType().equals(StaticVariables.getMaterial())
				&& RegionHandling.getRegionsCount(e.getPlayer()) < RegionHandling.regionCountAllowence(e
						.getPlayer())) {

			// RegionHandling.landClaimTest(e);
			RegionHandling.claimArea(e);

			// if player is trying to claim more than its region limit then
			// cancel the claim and send error msg to player
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
			// initiate delete claim method.
			RegionHandling.deleteClaimedLand(e);

			// if player does not have perms to build then cancel event.
		} else if (e.getBlock().getType().equals(StaticVariables.getMaterial())
				&& !WorldGuardPlugin.inst().canBuild(e.getPlayer(), e.getBlock().getLocation()))
			e.setCancelled(false);
	}

}
