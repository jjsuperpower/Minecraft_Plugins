package me.myself.i;

import me.myself.i.commands.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class main extends JavaPlugin implements org.bukkit.event.Listener {

	boolean mines = false;
	boolean boom_arrows = false;
	boolean damage_self_triggered = false; // stops recursive damage which would be BAD
	boolean shared_damage_on = false;
	boolean pits_on = false;
	boolean pvp = true;
	boolean dechunk = false;
	boolean hostile = false;
	boolean fart_tnt = false;
	boolean big_boom = false;
	BukkitTask hostile_id;
	int neutral = 0;
	BukkitTask gravity_id;
	BukkitTask gravity_game_id;
	BukkitTask sky_items_id;
	BukkitTask sty_items_game_id;

	BoomArrows boomArrows;
	SharedDamage sharedDamage;
	Pits pits;
	Freeze freeze;
	SkyItems skyItems;


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

		//initialize array the keeps track whick chunks have been deleted
		for (int i = 0; i < 2 * DECHUNK_RADIUS; i++) {
			for (int j = 0; j < 2 * DECHUNK_RADIUS; j++) {
				deChunked[i][j] = false;
			}
		}
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
			return boomArrows.set(args);
		}

		if (cmd.getName().equalsIgnoreCase("shared_damage")) {
			return sharedDamage.set(args);
		}

		if (cmd.getName().equalsIgnoreCase("pits")) {
			return pits.set(args);
		}

		if (cmd.getName().equalsIgnoreCase("freeze")) {
			return freeze.set(args);
		}

		if (cmd.getName().equalsIgnoreCase("pvp")) {
			
		}

		if (cmd.getName().equalsIgnoreCase("big_boom")) {
			
		}

		if (cmd.getName().equalsIgnoreCase("fart_tnt")) {

		}

		if (cmd.getName().equalsIgnoreCase("sky_items")) {
			return skyItems.set(args);
		}

		if (cmd.getName().equalsIgnoreCase("dechunk")) {

			//checks chunks around player
			new BukkitRunnable() {
				public void run() {
					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
						//Bukkit.getServer().broadcastMessage("Checking  " + player.getName());
						//Bukkit.getServer().broadcastMessage("array length =  " + String.valueOf(count));
						Chunk c = player.getLocation().getChunk();
						int x = c.getX();
						int z = c.getZ();
						for (int i = -1 * PLAYER_DECHUNK_RADIUS; i < PLAYER_DECHUNK_RADIUS + 1; i++) {
							for (int j = -1 * PLAYER_DECHUNK_RADIUS; j < PLAYER_DECHUNK_RADIUS + 1; j++) {
								//Bukkit.getServer().broadcastMessage("Checking  " + String.valueOf(x + i) + " " + String.valueOf(z + i));
								if (deChunked[x + i + DECHUNK_RADIUS][z + j + DECHUNK_RADIUS] == false) {
									deChunked[x + i + DECHUNK_RADIUS][z + j + DECHUNK_RADIUS] = true;
									deChunks_Queue.add(player.getWorld().getChunkAt(x + i, z + j));
								}
							}
						}
					}
				}
			}.runTaskTimer(this, 5, 40);

			//deletes chunks
			new BukkitRunnable() {
				int rand_numb;
				Material m;
				Chunk c;

				public void run() {

					c = deChunks_Queue.poll();
					if ((c != null) && c.isLoaded()) {
						rand_numb = ThreadLocalRandom.current().nextInt(1, 100 + 1);
						if (rand_numb < 85) {
							rand_numb = ThreadLocalRandom.current().nextInt(1, 100 + 1);
							if (rand_numb < 101) {
								for (int x = 0; x < 16; x++) {
									for (int z = 0; z < 16; z++) {
										for (int y = -64; y < 200; y++) {
											m = c.getBlock(x, y, z).getType();
											//using OR is faster than AND
											if ((m == Material.OBSIDIAN) || (m == Material.ENDER_PORTAL_FRAME)
													|| (m == Material.ENDER_PORTAL) || (m == Material.MOB_SPAWNER)
													|| (m == Material.NETHER_BRICK))
												;
											else {
												c.getBlock(x, y, z).setType(Material.AIR);
											}
										}
									}
								}
								//Bukkit.getServer().broadcastMessage(
								//"DeChunked " + String.valueOf(c.getX()) + " " + String.valueOf(c.getZ()));
							}
						}
					}
				}
			}.runTaskTimer(this, 1, 15);
		}
		
		if (cmd.getName().equalsIgnoreCase("antigravity_game")) {
			if (args.length == 0) {
				return false;
			}
			if (args[0].equalsIgnoreCase("on")) {
				Bukkit.getServer().broadcastMessage("starting game");
				if (this.gravity_game_id != null) {
					this.gravity_game_id.cancel();
				}
				this.gravity_game_id = new BukkitRunnable() {
					World over_world = Bukkit.getServer().getWorlds().get(0);
					World nether_world = Bukkit.getServer().getWorlds().get(1);
					World end_world = Bukkit.getServer().getWorlds().get(2);

					int rand_int = ThreadLocalRandom.current().nextInt(120, 360);
					int counter = 0;
					boolean gravity = true;

	public void run() {
						if (gravity && (counter < rand_int)) {
							counter += 1;

						} else if (gravity && (counter >= rand_int)) {
							counter = 0;
							gravity = false;
							rand_int = ThreadLocalRandom.current().nextInt(0, 60);
							Bukkit.getServer().broadcastMessage("Gravity Flipped");
						}

						if (!gravity && (counter < rand_int)) {
							counter += 1;

							for (Entity e : over_world.getEntities()) {
								e.setGravity(false);
								e.setVelocity(e.getVelocity().add(new Vector(0.0, 0.2, 0.0)));
							}

							for (Entity e : nether_world.getEntities()) {
								e.setGravity(false);
								e.setVelocity(e.getVelocity().add(new Vector(0.0, 0.2, 0.0)));
							}

							for (Entity e : end_world.getEntities()) {
								e.setGravity(false);
								e.setVelocity(e.getVelocity().add(new Vector(0.0, 0.2, 0.0)));
							}

						} else if (!gravity && (counter >= rand_int)) {
							counter = 0;
							gravity = true;

							for (Entity e : over_world.getEntities()) {
								e.setGravity(true);
							}

							for (Entity e : nether_world.getEntities()) {
								e.setGravity(true);
							}

							for (Entity e : end_world.getEntities()) {
								e.setGravity(true);
							}

							rand_int = ThreadLocalRandom.current().nextInt(120, 360);
							Bukkit.getServer().broadcastMessage("Gravity Restored");
						}
					}
				}.runTaskTimer(this, 3, 10);
			}
			if (args[0].equalsIgnoreCase("off")) {
				World over_world = Bukkit.getServer().getWorlds().get(0);
				World nether_world = Bukkit.getServer().getWorlds().get(1);
				World end_world = Bukkit.getServer().getWorlds().get(2);
				if (this.gravity_game_id != null) {
					this.gravity_game_id.cancel();
				}

				for (Player p : over_world.getPlayers()) {
					p.setGravity(true);
				}

				for (Player p : nether_world.getPlayers()) {
					p.setGravity(true);
				}

				for (Player p : end_world.getPlayers()) {
					p.setGravity(true);
				}
			}
		}

		if (cmd.getName().equalsIgnoreCase("antigravity")) {
			if (args.length == 0) {
				return false;
			}
			if (args[0].equalsIgnoreCase("on")) {
				if (this.gravity_id != null) {
					this.gravity_id.cancel();
				}
				this.gravity_id = new BukkitRunnable() {
					World w = player.getWorld();
					int arg = Integer.parseInt(args[1]);

					public void run() {
						for (Entity e : w.getEntities()) {
							e.setGravity(false);
							if (arg != 0)
								e.setVelocity(new Vector(0.0, arg * 0.01, 0.0));
						}
					}
				}.runTaskTimer(this, 2, 10);
			}
			if (args[0].equalsIgnoreCase("off")) {
				World w = player.getWorld();
				if (this.gravity_id != null) {
					this.gravity_id.cancel();
				}
				for (Entity e : w.getEntities()) {
					e.setGravity(true);
				}
			}
		}
	
		if (cmd.getName().equalsIgnoreCase("dangerous_anaimals")) {
			
			if (args.length == 0) {
				return false;
			}
			if (args[0].equalsIgnoreCase("on")) {

				if (args[1].equalsIgnoreCase("neutral")) {
					if(args[2].equalsIgnoreCase("easy"))
						this.neutral = 1;
					if(args[2].equalsIgnoreCase("medium"))
						this.neutral = 2;
					if(args[2].equalsIgnoreCase("hard"))
						this.neutral = 3;
					if(args[2].equalsIgnoreCase("insane"))
						this.neutral = 3;	
				}

				if (args[1].equalsIgnoreCase("hostile")) {
					this.hostile = true;

					if (args[2].equalsIgnoreCase("easy")) {
						if (this.hostile_id != null) {
							this.hostile_id.cancel();
						}
						this.hostile_id = new BukkitRunnable() {
							World w = player.getWorld();
							LivingEntity m;

	public void run() {
								for (LivingEntity e : w.getLivingEntities()) {
									if (e instanceof Animals) {
										if (e.getPassengers().size() == 0) {
											m = (LivingEntity) e.getWorld().spawnEntity(e.getLocation(),EntityType.HUSK);
											e.addPassenger(m);
											m.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 10));
											m.setInvulnerable(true);
										}
									}
								}
							}
						}.runTaskTimer(this, 2, 100);
					}
					
					if (args[2].equalsIgnoreCase("medium")) {
						if (this.hostile_id != null) {
							this.hostile_id.cancel();
						}
						this.hostile_id = new BukkitRunnable() {
							World w = player.getWorld();
							LivingEntity m;

							public void run() {
								for (LivingEntity e : w.getLivingEntities()) {
									if (e instanceof Animals) {
										if (e.getPassengers().size() == 0) {
											m = (LivingEntity) e.getWorld().spawnEntity(e.getLocation(),EntityType.CREEPER);
											e.addPassenger(m);
											m.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 10));
											m.setInvulnerable(true);
										}
									}
								}
							}
						}.runTaskTimer(this, 2, 100);
					}
					
					if (args[2].equalsIgnoreCase("hard")) {
						if (this.hostile_id != null) {
							this.hostile_id.cancel();
						}
						this.hostile_id = new BukkitRunnable() {
							World w = player.getWorld();
							LivingEntity m;

							public void run() {
								for (LivingEntity e : w.getLivingEntities()) {
									if (e instanceof Animals) {
										if (e.getPassengers().size() == 0) {
											m = (LivingEntity) e.getWorld().spawnEntity(e.getLocation(),EntityType.HUSK);
											e.addPassenger(m);
											m.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 10));
											m.setInvulnerable(true);
											m = (LivingEntity) e.getWorld().spawnEntity(e.getLocation(),EntityType.CREEPER);
											e.addPassenger(m);
											m.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 10));
											m.setInvulnerable(true);
										}
									}
								}
							}
						}.runTaskTimer(this, 2, 100);
					}
					
					if (args[2].equalsIgnoreCase("insane")) {
						if (this.hostile_id != null) {
							this.hostile_id.cancel();
						}
						this.hostile_id = new BukkitRunnable() {
							World w = player.getWorld();
							LivingEntity m;

							public void run() {
								for (LivingEntity e : w.getLivingEntities()) {
									if (e instanceof Animals) {
										if (e.getPassengers().size() == 0) {
											m = (LivingEntity) e.getWorld().spawnEntity(e.getLocation(),EntityType.HUSK);
											e.addPassenger(m);
											m.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 10));
											m.setInvulnerable(true);
											((Creature) m).setTarget(player);
											m = (LivingEntity) e.getWorld().spawnEntity(e.getLocation(),EntityType.CREEPER);
											e.addPassenger(m);
											m.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 10));
											m.setInvulnerable(true);
											((Creature) m).setTarget(player);
										}
									}
								}
							}
						}.runTaskTimer(this, 2, 100);
					}
				}
			}
			
			if (args[0].equalsIgnoreCase("off")) {
				this.hostile = false;
				this.neutral = 0;
				if (this.hostile_id != null) {
					this.hostile_id.cancel();
				}
				
				for (LivingEntity e : player.getWorld().getLivingEntities()) {
					if (e.getPassengers().size() > 0) {
						for (Entity e_ : e.getPassengers()) {
							e_.remove();
						}
					}
				}
			}

			// LivingEntity z = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), EntityType.HUSK);
			// LivingEntity x = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), EntityType.CREEPER);
			// LivingEntity c = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), EntityType.COW);
			// c.addPassenger(z);
			// c.addPassenger(x);
			// // ((Creature) z).setTarget((LivingEntity) player);
			// z.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 10));
			// z.setInvulnerable(true);		
			// // z.setGlowing(true);
			// this.hostile = true;
		}

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


	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (this.mines) {
			Player player = event.getPlayer();
			// player.sendMessage("boomHoe event triggered");
			ItemStack item = player.getInventory().getItemInHand();
			if (item.getItemMeta().getDisplayName().contains("Miner")) {
				if (item.getItemMeta().hasLore()) {
					if (item.getDurability() < 20) {
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
			loc.setY((double) -59);

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

		// spawn tnt when sneaking
		if (this.fart_tnt) {
			if (event.getPlayer().isSneaking()) {
				Entity tnt = event.getPlayer().getWorld().spawn(event.getPlayer().getLocation(), TNTPrimed.class);
				((TNTPrimed) tnt).setFuseTicks(100);

			}
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

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (!this.pvp) {
			Entity entity = event.getEntity();
			if (entity instanceof Player) {
				event.setCancelled(true);
			}
		}

		if (this.neutral > 0) {
			if (event.getEntity() instanceof Animals) {
				if (event.getDamager() instanceof Player) {
					LivingEntity e = (LivingEntity) event.getEntity();
					if (e.getPassengers().size() == 0) {
						LivingEntity m;
						if (this.neutral == 1) {
							m = (LivingEntity) e.getWorld().spawnEntity(e.getLocation(), EntityType.HUSK);
							e.addPassenger(m);
							m.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 10));
							m.setInvulnerable(true);
						}
						if (this.neutral == 2) {
							m = (LivingEntity) e.getWorld().spawnEntity(e.getLocation(), EntityType.CREEPER);
							e.addPassenger(m);
							m.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 10));
							m.setInvulnerable(true);
						}
						if (this.neutral == 3) {
							m = (LivingEntity) e.getWorld().spawnEntity(e.getLocation(), EntityType.HUSK);
							e.addPassenger(m);
							m.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 10));
							m.setInvulnerable(true);
							m = (LivingEntity) e.getWorld().spawnEntity(e.getLocation(), EntityType.CREEPER);
							e.addPassenger(m);
							m.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 10));
							m.setInvulnerable(true);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Entity e = event.getEntity();
		e.getWorld().strikeLightning(e.getLocation().add(0, 10, 0));
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (this.hostile || (this.neutral > 0)) {
			Entity e = event.getEntity();
			if (e instanceof LivingEntity) {
				LivingEntity le = (LivingEntity) e;
				if (le.getPassengers().size() > 0) {
					for (Entity e_ : le.getPassengers()) {
						e_.remove();
					}
				}
			}
		}
	}
	

	@EventHandler
	public void onPrime(ExplosionPrimeEvent event) {
		if(this.big_boom)	{
			event.setRadius(128);
			event.setFire(true);
		}
	}
}
