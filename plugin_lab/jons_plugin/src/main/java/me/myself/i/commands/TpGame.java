package me.myself.i.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.chat.hover.content.Item;

import org.bukkit.NamespacedKey;
import org.bukkit.WorldBorder;
import org.bukkit.block.Biome;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.concurrent.ThreadLocalRandom;

public class TpGame extends BukkitRunnable {
	Plugin p;
	boolean enabled;
	
	//variable for world spawn location
	int spawnX = 0;
	int spawnZ = 0;
	int maxOriginRadius = 100000;
	int worldBoarderDiameter = 1000;
	// int timeBetweenTeleport = 12000; 	// run every 10 minutes
	int TIME_BETWEEN_TELEPORTS = 6000; 	// run every 5 minutes
	BukkitTask runnableID;

    public TpGame(Plugin p) {
		this.p = p;
		this.enabled = false;
	}


	public boolean cmd(String[] args) {
		if (args.length == 1) {
			if (args[0].equals("start")) {
				if (!enabled) {
					mkEndPortalCraftRecipe();
					Bukkit.getServer().broadcastMessage("Starting the teleport game");
					enabled = true;
					run();
					return true;
				}
			} else if (args[0].equals("stop")) {
				if (enabled) {
					disable();
					return true;
				}
			}
		}else{
			if (enabled) {
				// We r gonna stop
				disable();
			}else{
				 // We r gonna start
				Bukkit.getServer().broadcastMessage("Starting the teleport game");
				enabled = true;
				run();
			}
			return true;
		}
		return false;
	}

	public void mkEndPortalCraftRecipe() {
		// add crafting recipe for to make end portal frame from coblestone and ender eye
		// ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(p, "end_portal_frame"), new ItemStack(Material.END_PORTAL_FRAME, 1));
		
		Bukkit.getServer().broadcastMessage("Making the end portal frame recipe");
		NamespacedKey recipeKey = new NamespacedKey(p, "end_portal_frame");
		ShapedRecipe recipe = new ShapedRecipe(recipeKey, new ItemStack(Material.END_PORTAL_FRAME, 1));
		recipe.shape("BGB", "BEB", "BOB");
		recipe.setIngredient('E', Material.ENDER_EYE);
		recipe.setIngredient('B', Material.BIRCH_PLANKS);
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('G', Material.GLASS);
		Bukkit.getServer().addRecipe(recipe);
		ItemStack is = new ItemStack(Material.KNOWLEDGE_BOOK);
		KnowledgeBookMeta meta = (KnowledgeBookMeta) is.getItemMeta();
		meta.addRecipe(recipeKey);
		Bukkit.getServer().broadcastMessage("Added the end portal frame recipe to the knowledge book");
	}

	private void disable() {
		enabled = false;
		runnableID.cancel();

		// turn off the world boarder
		Bukkit.getWorld("world").getWorldBorder().setSize(30000000);
	}


	public void run() {

		runnableID =  new BukkitRunnable() {
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

				int randSpawnX, randSpawnZ, spawnY;
				int numAttempts = 0;
				Biome biome;
				do {
					randSpawnX = ThreadLocalRandom.current().nextInt(-maxOriginRadius, maxOriginRadius);
					randSpawnZ = ThreadLocalRandom.current().nextInt(-maxOriginRadius, maxOriginRadius);
					spawnY = Bukkit.getWorld("world").getHighestBlockYAt(randSpawnX, randSpawnZ)+1;

					biome = Bukkit.getWorld("world").getBiome(randSpawnX, spawnY, randSpawnZ);
					numAttempts++;
					if (numAttempts > 10){
						break;
					}
				} while (biome == Biome.OCEAN || biome == Biome.DEEP_OCEAN || biome == Biome.RIVER || biome == Biome.FROZEN_OCEAN || biome == Biome.FROZEN_RIVER || biome == Biome.LUKEWARM_OCEAN || biome == Biome.COLD_OCEAN || biome == Biome.WARM_OCEAN || biome == Biome.DEEP_LUKEWARM_OCEAN || biome == Biome.DEEP_COLD_OCEAN || biome == Biome.DEEP_WARM_OCEAN);
				// disable world boarder
				Bukkit.getWorld("world").getWorldBorder().setSize(30000000);

				// set world spawn location
				Bukkit.getWorld("world").setSpawnLocation(randSpawnX, spawnY, randSpawnZ);

				// for all players in the overworld teleport them to the new spawn location and set their spawn location
				for (Player a_player : Bukkit.getWorld("world").getPlayers()) {
					Bukkit.getScheduler().runTask(p, () -> {
						//a_player.setGameMode(org.bukkit.GameMode.SPECTATOR);
						a_player.sendMessage("Teleporting you to the new spawn location");
						Location loc = Bukkit.getWorld("world").getSpawnLocation();
						a_player.teleport(loc);
						a_player.setBedSpawnLocation(loc);
					});
				}

				WorldBorder border = Bukkit.getWorld("world").getWorldBorder();

				// set the world boarder
				border.setCenter(randSpawnX, randSpawnZ);

				// set the world boarder warning distance
				border.setWarningDistance(10);

				// set the world boarder warning time
				border.setWarningTime(10);


				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// set the world boarder size
				border.setSize(worldBoarderDiameter);
				
			}
            }.runTaskTimerAsynchronously(this.p,  0,  TIME_BETWEEN_TELEPORTS);
		
	}
}