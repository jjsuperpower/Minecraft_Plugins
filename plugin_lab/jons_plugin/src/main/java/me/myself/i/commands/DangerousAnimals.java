package me.myself.i.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DangerousAnimals {
    private BukkitTask hostile_id;
    private Plugin plugin;
    private int neutral;

    public DangerousAnimals(Plugin plugin) {
        this.plugin = plugin;
        this.neutral = 0;
    }

    public boolean cmd(String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("on")) {
            if (args.length == 1) {
                return false;
            }
            if (args[1].equalsIgnoreCase("easy") || args[1].equalsIgnoreCase("normal") || args[1].equalsIgnoreCase("hard") || args[1].equalsIgnoreCase("insane")) {
                setHostile(Bukkit.getPlayer(args[2]), args[1]);
                return true;
            }
            return false;
        }
        if (args[0].equalsIgnoreCase("off")) {
            setHostileOff(Bukkit.getPlayer(args[1]));
            return true;
        }
        return false;
    }

    public void setHostile(Player player, String difficulty) {
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
                            if ((difficulty == "easy") || (difficulty == "hard") || (difficulty == "insane")) {
                                m = (LivingEntity) e.getWorld().spawnEntity(e.getLocation(), EntityType.HUSK);
                                e.addPassenger(m);
                                m.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 10));
                                m.setInvulnerable(true);
                            }
                            
                            if ((difficulty == "normal") || (difficulty == "hard") || (difficulty == "insane")) {
                                m = (LivingEntity) e.getWorld().spawnEntity(e.getLocation(), EntityType.CREEPER);
                                e.addPassenger(m);
                                m.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 10));
                                m.setInvulnerable(true);
                            }
                           
                            if (difficulty == "insane") {
                                ((Creature) m).setTarget(player);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this.plugin, 2, 100);

        if (difficulty == "easy") {
            this.neutral = 1;
        } else if (difficulty == "normal") {
            this.neutral = 2;
        } else if (difficulty == "hard") {
            this.neutral = 3;
        } else if (difficulty == "insane") {
            this.neutral = 4;
        }
    }

    public void setHostileOff(Player player) {
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

    public void onDamage(EntityDamageByEntityEvent event) {
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

    public void onEntityDeath(Entity entity) {
        if (this.neutral > 0) {
            if (entity instanceof Animals) {
                for (Entity e : entity.getPassengers()) {
                    e.remove();
                }
            }
        }
    }
}