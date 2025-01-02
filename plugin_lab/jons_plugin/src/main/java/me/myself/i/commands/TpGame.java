package me.myself.i.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.NamespacedKey;

import java.util.concurrent.ThreadLocalRandom;

public class TpGame extends BukkitRunnable {
	Plugin p;
	boolean enabled;
	
	//variable for world spawn location
	int spawnX = 0;
	int spawnZ = 0;
	int maxOriginRadius = 100000;
	int worldBoarderRadius = 500;
	int timeBetweenTeleport = 12000; 	// run every 10 minutes
	BukkitTask runnableID;

    public TpGame(Plugin p) {
		this.p = p;
		this.enabled = false;
	}


	public boolean cmd(String[] args) {
		if (args.length == 1) {
			if (args[0].equals("start")) {
				if (!enabled) {
					enabled = true;
					run();
					return true;
				}
			} else if (args[0].equals("stop")) {
				if (enabled) {
					enabled = false;
					runnableID.cancel();
					return true;
				}
			}
		}
		return false;
	}

	public void mkEndPortalCraftRecipe() {
		// add crafting recipe for to make end portal frame from coblestone and ender eye
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(p, "end_portal_frame"), new ItemStack(Material.END_PORTAL_FRAME, 1));
		recipe.shape("BGB", "BEB", "BOB");
		recipe.setIngredient('E', Material.ENDER_EYE);
		recipe.setIngredient('C', Material.COBBLESTONE);
		recipe.setIngredient('B', Material.BIRCH_PLANKS);
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('G', Material.GLASS);
		Bukkit.getServer().addRecipe(recipe);
	}
	

	public void run() {
		runnableID = new BukkitRunnable() {
			public void run() {
				// print a countdown message from 10 seconds to 0
				for (int i = 10; i >= 0; i--) {
					Bukkit.getServer().broadcastMessage("Teleporting players in " + i + " seconds");
					// wait 1 second
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// print a message to all players
				Bukkit.getServer().broadcastMessage("Teleporting players to new spawn location");


				int randSpawnX = ThreadLocalRandom.current().nextInt(-maxOriginRadius, maxOriginRadius);
				int randSpawnZ = ThreadLocalRandom.current().nextInt(-maxOriginRadius, maxOriginRadius);
				int spawnY = Bukkit.getWorld("world").getHighestBlockYAt(randSpawnX, randSpawnZ);

				// set world spawn location
				Bukkit.getWorld("world").setSpawnLocation(randSpawnX, spawnY, randSpawnZ);

				// for all players in the overworld teleport them to the new spawn location and set their spawn location
				for (Player a_player : Bukkit.getWorld("world").getPlayers()) {
					a_player.teleport(Bukkit.getWorld("world").getSpawnLocation());
					a_player.setBedSpawnLocation(Bukkit.getWorld("world").getSpawnLocation());
				}

				// set the world boarder
				Bukkit.getWorld("world").getWorldBorder().setCenter(randSpawnX, randSpawnZ);

				// set the world boarder size
				Bukkit.getWorld("world").getWorldBorder().setSize(worldBoarderRadius);

				// set the world boarder warning distance
				Bukkit.getWorld("world").getWorldBorder().setWarningDistance(10);

				// set the world boarder warning time
				Bukkit.getWorld("world").getWorldBorder().setWarningTime(10);


			}
            }.runTaskTimer(this.p,  200,  1200);
	}
}