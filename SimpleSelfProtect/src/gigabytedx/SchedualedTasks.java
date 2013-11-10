package gigabytedx;

import java.io.FileNotFoundException;

import org.bukkit.Bukkit;

public class SchedualedTasks {

	public static Main	plugin;

	public SchedualedTasks(Main plugin) {

		// retrieve plugin object from main.
		SchedualedTasks.plugin = plugin;
		InitiateTasks();
	}

	private void InitiateTasks() {

		// run task that does a scheduled back up
		InitiateBackUpTask();

	}

	private void InitiateBackUpTask() {

		// create scheduled task to back up region data
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

			// method to run every repeat.
			@Override
			public void run() {

				try {
					ReadAndWrite.save("regions");
				} catch (FileNotFoundException e) {
					// if file wasn't found send error msg to console
					Main.sendSevereInfo("Could not back up! File Not Found!");
					e.printStackTrace();
				}

			}
		}, StaticVariables.getDurationBetweenDataBackup() * 20,
				StaticVariables.getDurationBetweenDataBackup() * 20);

	}

	public static void runReload() {

		// when worldguard has failed to load reload the server.

		// create delayed task
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

			@Override
			public void run() {

				// send msg to console to reload the server
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "rl");

			}
		}, 100L);
	}
}
