package gigabytedx;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class ItemMetaSet {

	public static ItemMeta getMeta() {

		ItemStack is = new ItemStack(StaticVariables.material);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Claim Stone");
		meta.setLore(getMetaList());
		return meta;
	}

	private static List<String> getMetaList() {

		List<String> list = new ArrayList<>();
		list.add(ChatColor.DARK_GREEN + "Place this block to claim ");
		list.add(ChatColor.DARK_GREEN + "the land around it. This");
		list.add(ChatColor.DARK_GREEN + "Claim Stone claims a ");
		list.add(ChatColor.DARK_GREEN + "20x20 zone around the block.");
		list.add(ChatColor.DARK_GREEN + "Type '/friend friendsname'");
		list.add(ChatColor.DARK_GREEN + "to allow a friend to build");
		list.add(ChatColor.DARK_GREEN + "in your land. You can type");
		list.add(ChatColor.DARK_GREEN + "'/unfriend friendsname'");
		list.add(ChatColor.DARK_GREEN + "to remove them.");
		return list;

	}
}
