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
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class main extends JavaPlugin implements org.bukkit.event.Listener {

    BukkitScheduler scheduler;
    int taskId = 0;
	boolean mines = false;
	boolean boom_arrows=false;
	
	@Override
	public void onEnable()	{
		System.out.println("test1 enabled");
//		this.getCommand("test1").setExecutor((CommandExecutor)new commands());
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().broadcastMessage("test1 is enabled");
	}
	
	@Override
	public void onDisable()	{
		System.out.println("test1 disabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player)sender;

		if (cmd.getName().equalsIgnoreCase("strike")) { // If the player typed /basic then do the following, note: If you only registered this executor for one command, you don't need this
			// doSomething
			player.getWorld().strikeLightning(player.getTargetBlock((Set<Material>) null, 200).getLocation());
			// Bukkit.getServer().broadcastMessage("general message triggered");
			// player.sendMessage("you used a custom command");
		}

		//switch for boom_arrows
		if (cmd.getName().equalsIgnoreCase("boom_arrow")) {
			if (args[0].equalsIgnoreCase("on")) {
				this.boom_arrows = true;
			} else if (args[0].equalsIgnoreCase("on")) {
				this.boom_arrows = false;
			}
		}
		//controlls miner
		if (cmd.getName().equalsIgnoreCase("mines"))   {
				if(args[0].equalsIgnoreCase("on"))	{
					mines = true;
				} else if (args[0].equalsIgnoreCase("off")) {
					this.mines = false;
				}
	
			}
		
		//gives player mine_hoe
		if (this.mines) {
			if (cmd.getName().equalsIgnoreCase("mine")) {
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
			}
		} else {
			player.sendMessage("You must enable mines first using 'mines on");
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
	public void OnProjectileHitEvent(ProjectileHitEvent event)	{
		//Entity hitEntity = event.getHitEntity();
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

	}
		
}
