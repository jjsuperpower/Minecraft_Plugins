package jons_plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class main extends JavaPlugin implements org.bukkit.event.Listener {

	boolean mines = false;
	boolean boom_arrows = false;
	boolean damage_self_triggered = false; // stops recursive damage which would be BAD
	boolean shared_damage_on = false;
	boolean pits_on = false;

	BukkitScheduler scheduler;
    int taskId = 0;

	@Override
	public void onEnable() {
		System.out.println("test1 enabled");
		// this.getCommand("test1").setExecutor((CommandExecutor)new commands());
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().broadcastMessage("test1 is enabled");
	}

	@Override
	public void onDisable() {
		System.out.println("test1 disabled");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("strike")) { // If the player typed /basic then do the following, note: If
														// you only registered this executor for one command, you don't
														// need this
			// doSomething
			player.getWorld().strikeLightning(player.getTargetBlock((Set<Material>) null, 200).getLocation());
			// Bukkit.getServer().broadcastMessage("general message triggered");
			// player.sendMessage("you used a custom command");
		}

		// switch for boom_arrows
		if (cmd.getName().equalsIgnoreCase("boom_arrows")) {
			if (args.length == 0) {
				return false;
			}
			if (args[0].equalsIgnoreCase("on")) {
				this.boom_arrows = true;
				return true;
			}
			if (args[0].equalsIgnoreCase("off")) {
				this.boom_arrows = false;
				return true;
			}
		}

		if (cmd.getName().equalsIgnoreCase("shared_damage")) {
			if (args.length == 0) {
				return false;
			}
			if (args[0].equalsIgnoreCase("on")) {
				this.shared_damage_on = true;
				return true;
			}
			if (args[0].equalsIgnoreCase("off")) {
				this.shared_damage_on = false;
				return true;
			}
		}

		if (cmd.getName().equalsIgnoreCase("pits")) {
			if (args.length == 0) {
				return false;
			}
			if (args[0].equalsIgnoreCase("on")) {
				this.pits_on = true;
				return true;
			}
			if (args[0].equalsIgnoreCase("off")) {
				this.pits_on = false;
				return true;
			}
		}

		// controlls miner
		if (cmd.getName().equalsIgnoreCase("mines")) {
			if (args.length == 0) {
				return false;
			}
			if (args[0].equalsIgnoreCase("on")) {
				mines = true;
			} else if (args[0].equalsIgnoreCase("off")) {
				this.mines = false;
				return true;
			} else if (args[0].equalsIgnoreCase("give")) {
				if (this.mines == false) {
					player.sendMessage("Automaticaly turning mines on");
					this.mines = true;
				}
				ItemStack boomHoe = new ItemStack(Material.GOLD_HOE);
				ItemMeta boomHoeMeta = boomHoe.getItemMeta();
				boomHoe.setDurability((short) 0);
				boomHoeMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Miner");
				List<String> lore = new ArrayList<String>();
				lore.add("");
				lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "Layes Mines");
				boomHoeMeta.setLore(lore);
				boomHoe.setItemMeta(boomHoeMeta);
				player.getWorld().dropItem(player.getLocation(), boomHoe);
				return true;
			}
		}
				
		return true;
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent event) {
		Player player = (Player) event.getPlayer();
		player.sendMessage("Hello Darkness my old friend");
	}

	//for boom arrows
	@EventHandler
	public void OnProjectileHitEvent(ProjectileHitEvent event) {
		//Entity hitEntity = event.getHitEntity();
		if (this.boom_arrows)  {
			Block block = event.getHitBlock();
			if (this.boom_arrows) {
				if (block != null) {
					block.getWorld().createExplosion(block.getLocation(), (float) 1.0);
				} else {
					Entity entity = event.getHitEntity();
					entity.getWorld().createExplosion(entity.getLocation(), (float) 1.0);
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event)	{
		if(this.mines)	{
			Player player = event.getPlayer();
			// player.sendMessage("boomHoe event triggered");
			ItemStack item = player.getInventory().getItemInHand();
			if(item.getItemMeta().getDisplayName().contains("Miner"))	{
				if(item.getItemMeta().hasLore())	{
					if(item.getDurability() < 20)	{
						// player.sendMessage("if statment true");
						Location locLooking = player.getTargetBlock((Set<Material>) null, 10).getLocation();
						Block tntBlock = locLooking.add(0, -1, 0).getBlock();
						tntBlock.setType(Material.TNT);
						

						short Durability = item.getDurability();
						Durability++;
						item.setDurability(Durability);
					} else {
						player.getInventory().remove(item);
					}
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {

		//handles mines
		if (this.mines) {
			Player player = event.getPlayer();
			Location pLocation = player.getLocation().clone();

			for (int i = 0; i < 4; i++) {
				if (pLocation.add(0, -1, 0).getBlock().getType() == Material.TNT) {
					player.sendMessage("Found a mine, bye bye");
					pLocation.getBlock().setType(Material.AIR);
					player.getWorld().createExplosion(pLocation, (float) 4.0);
				}
			}

		}
		if (this.pits_on) {
			Location loc = event.getPlayer().getLocation();
			int player_loc_y = (int) loc.getY();
			loc.setY((double) 1);

			//this took a while to get right
			new BukkitRunnable() {
				public void run() {
					loc.add(0, 1, 0);
					if (loc.getBlock().getType() != Material.OBSIDIAN) {
						loc.getBlock().setType(Material.AIR);
					}
					if ((int) loc.getY() >= player_loc_y) {
						cancel();
					}
				}
			}.runTaskTimer(this, 15, 5);
		}
	}


	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (this.shared_damage_on) {
			Entity entity = event.getEntity();
			if (!this.damage_self_triggered && entity instanceof Player) {
				this.damage_self_triggered = true;
				for (Player player : Bukkit.getOnlinePlayers()) {
					player.damage(event.getDamage());
				}
				this.damage_self_triggered = false;
				event.setCancelled(true);
			}
		}

		//not usefull but good to know
		//Bukkit.getPluginManager().callEvent(new EntityDamageEvent((Entity) player, null, event.getDamage()));
		//Bukkit.getServer().broadcastMessage("Damage event called " + String.valueOf(this.damage_self_triggered));
		//Bukkit.getServer().broadcastMessage(event.getEntity().getName());
		//player.sendMessage(String.valueOf(event.getDamage()));
	}
		
}
