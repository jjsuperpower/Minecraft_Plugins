package me.myself.i;

import me.myself.i.commands.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class main extends JavaPlugin implements org.bukkit.event.Listener {

	BoomArrows boomArrows;
	SharedDamage sharedDamage;
	Pits pits;
	Freeze freeze;
	SkyItems skyItems;
	Pvp pvp;
	Dechunk dechunk;
	AntiGravity antiGravity;
	AntiGravityGame antiGravityGame;
	DangerousAnimals dangerousAnimals;
	BigBoom bigBoom;
	Mines mines;
	FartTnt fartTnt;


	int DECHUNK_RADIUS = 1000;
	int PLAYER_DECHUNK_RADIUS = 4;
	boolean[][] deChunked = new boolean[2 * DECHUNK_RADIUS][2 * DECHUNK_RADIUS];
	Queue<Chunk> deChunks_Queue = new LinkedList<Chunk>();

	BukkitScheduler scheduler;
	int taskId = 0;

	@Override
	public void onEnable() {
		System.out.println("Jons_Plugin enabled");
		Bukkit.getServer().getPluginManager().registerEvents(this, this);

		boomArrows = new BoomArrows(this);
		sharedDamage = new SharedDamage(this);
		pits = new Pits(this);
		freeze = new Freeze(this);
		skyItems = new SkyItems(this);
		pvp = new Pvp(this);
		dechunk = new Dechunk(this);
		antiGravity = new AntiGravity(this);
		antiGravityGame = new AntiGravityGame(this);
		dangerousAnimals = new DangerousAnimals(this);
		bigBoom = new BigBoom(this);
		mines = new Mines(this);
		fartTnt = new FartTnt(this);

	}

	@Override
	public void onDisable() {
		System.out.println("test1 disabled");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("strike")) { 

			player.getWorld().strikeLightning(player.getTargetBlock((Set<Material>) null, 200).getLocation().add(0,3,0));
			Strike.looking_at(player);

		}

		// switch for boom_arrows
		if (cmd.getName().equalsIgnoreCase("boom_arrows")) {
			return boomArrows.cmd(args);
		}

		if (cmd.getName().equalsIgnoreCase("shared_damage")) {
			return sharedDamage.cmd(args);
		}

		if (cmd.getName().equalsIgnoreCase("pits")) {
			return pits.cmd(args);
		}

		if (cmd.getName().equalsIgnoreCase("freeze")) {
			return freeze.cmd(args);
		}

		if (cmd.getName().equalsIgnoreCase("pvp")) {
			return pvp.cmd(args);
		}

		if (cmd.getName().equalsIgnoreCase("big_boom")) {
			return bigBoom.cmd(args);
		}

		if (cmd.getName().equalsIgnoreCase("fart_tnt")) {
			fartTnt.cmd(args);
		}

		if (cmd.getName().equalsIgnoreCase("sky_items")) {
			return skyItems.cmd(args);
		}

		if (cmd.getName().equalsIgnoreCase("dechunk")) {
			return dechunk.cmd(args);
		}
		
		if (cmd.getName().equalsIgnoreCase("antigravity_game")) {
			return antiGravityGame.cmd(args);
		}

		if (cmd.getName().equalsIgnoreCase("antigravity")) {
			return antiGravity.onCommand(sender, cmd, label, args);
		}
	
		if (cmd.getName().equalsIgnoreCase("dangerous_anaimals")) {
			return dangerousAnimals.cmd(args);
		}

		if (cmd.getName().equalsIgnoreCase("mines")) {
			return mines.cmd(player, args);
		}

		return false;
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent event) {
		Player player = (Player) event.getPlayer();
		player.sendMessage("Hello Darkness my old friend");
	}


	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		mines.onInteract(event);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {

		mines.onMove(event);
		pits.onMove(event);
		freeze.onMove(event);
		fartTnt.onMove(event);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		sharedDamage.onDamage(event);

		//not usefull but good to know
		//Bukkit.getPluginManager().callEvent(new EntityDamageEvent((Entity) player, null, event.getDamage()));
		//Bukkit.getServer().broadcastMessage("Damage event called " + String.valueOf(this.damage_self_triggered));
		//Bukkit.getServer().broadcastMessage(event.getEntity().getName());
		//player.sendMessage(String.valueOf(event.getDamage()));

	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		this.pvp.onDamage(event);
		this.dangerousAnimals.onDamage(event);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Entity e = event.getEntity();
		e.getWorld().strikeLightning(e.getLocation().add(0, 10, 0));
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		this.dangerousAnimals.onEntityDeath(event.getEntity());
	}
	

	@EventHandler
	public void onPrime(ExplosionPrimeEvent event) {
		this.bigBoom.onPrime(event);
	}
}
