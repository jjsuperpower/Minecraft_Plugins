
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.block.Block;


public class main extends JavaPlugin implements Listener	{

    BukkitScheduler scheduler;
    int taskId = 0;
	boolean mines = false;
	
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

		if (cmd.getName().equalsIgnoreCase("test1")) { // If the player typed /basic then do the following, note: If you only registered this executor for one command, you don't need this
			// doSomething
			player.getWorld().strikeLightning(player.getTargetBlock((Set<Material>) null, 200).getLocation());
			// Bukkit.getServer().broadcastMessage("general message triggered");
			// player.sendMessage("you used a custom command");
			
			ItemStack iron = new ItemStack(Material.IRON_INGOT,5);
			player.getWorld().dropItem(player.getLocation(), iron);
		}

        if (cmd.getName().equalsIgnoreCase("off_tnt"))   {
            player.sendMessage("off_tnt");
            player.sendMessage( Integer.toString(this.taskId));
            scheduler.cancelTask(this.taskId);
        }

        if (cmd.getName().equalsIgnoreCase("on_tnt"))   {
            this.scheduler = this.getServer().getScheduler();
            this.taskId = scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                        player.getTargetBlock((Set<Material>) null, 200).setType(Material.TNT);
                }
            }, 0L, 2L);
		}
		
		if (cmd.getName().equalsIgnoreCase("compass"))   {
			player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.COMPASS));
			player.setCompassTarget(new Location(player.getWorld(),0,0,0));
		}

		if (cmd.getName().equalsIgnoreCase("mine"))   {
			ItemStack boomHoe = new ItemStack(Material.GOLD_HOE);
			ItemMeta boomHoeMeta = boomHoe.getItemMeta();

			boomHoe.setDurability((short)0);
			boomHoeMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Miner");
			List<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "Layes Mines");
			boomHoeMeta.setLore(lore);
			boomHoe.setItemMeta(boomHoeMeta);
			player.getWorld().dropItem(player.getLocation(), boomHoe);
		}

		// if (cmd.getName().equalsIgnoreCase("miner"))   {
		// 	if(args[0].equalsIgnoreCase("on"))	{
		// 		mines = true;
		// 	} else if(args[1].equalsIgnoreCase(off))

		// }


		return true;
	}


	
	

// 	@EventHandler
// 	public void myEvent(BlockBreakEvent event)	{
// 		Player player = (Player) event.getPlayer();
// 		player.sendMessage("Block event trigered");
// 		ItemStack iron = new ItemStack(Material.IRON_INGOT,5);
// 		Bukkit.getServer().broadcastMessage("event triggered");
// //		player.getWorld().strikeLightning(player.getTargetBlock((Set<Material>) null, 200).getLocation());
// 		event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), iron);
// 		event.getBlock().setType(Material.AIR);
		
//		player.getWorld().dropItem(player.getLocation(), iron);
//			event.setCancelled(true);
	
	@EventHandler
	public void onLogin(PlayerJoinEvent event) {
		Player player = (Player) event.getPlayer();
		// player.sendMessage("Hello Darkness my old friend");
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event)	{
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

	@EventHandler
	public void onMove(PlayerMoveEvent event)	{
		Player player = event.getPlayer();
		boolean foundTNT = false;
		Location pLocation = player.getLocation().clone();

		for(int i = 0; i < 4; i++)	{

			if(pLocation.add(0,-1,0).getBlock().getType() == Material.TNT)	{
				player.sendMessage("found tnt, bye bye");
				pLocation.getBlock().setType(Material.AIR);
				player.getWorld().createExplosion(pLocation, (float) 4.0);
			}
		}

	}
		
}


