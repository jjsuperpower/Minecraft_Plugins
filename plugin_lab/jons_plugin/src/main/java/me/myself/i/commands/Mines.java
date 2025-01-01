package me.myself.i.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Mines implements Listener {
    private boolean mines;

    public Mines(Plugin plugin) {
        this.mines = false;
    }

    public boolean cmd(Player player, String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("on")) {
            this.mines = true;
        } else if (args[0].equalsIgnoreCase("off")) {
            this.mines = false;
            return true;
        } else if (args[0].equalsIgnoreCase("give")) {
            if (!this.mines) {
                player.sendMessage("Automatically turning mines on");
                this.mines = true;
            }
            ItemStack boomHoe = new ItemStack(Material.GOLD_HOE);
            ItemMeta boomHoeMeta = boomHoe.getItemMeta();
            boomHoe.setDurability((short) 0);
            boomHoeMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Miner");
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "Lays Mines");
            boomHoeMeta.setLore(lore);
            boomHoe.setItemMeta(boomHoeMeta);
            player.getWorld().dropItem(player.getLocation(), boomHoe);
            return true;
        }
        return false;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (this.mines) {
            Player player = event.getPlayer();
            Location pLocation = player.getLocation().clone();

            for (int i = 0; i < 4; i++) {
                if (pLocation.add(0, -1, 0).getBlock().getType() == Material.TNT) {
                    player.sendMessage("Found a mine, bye bye");
                    pLocation.getBlock().setType(Material.AIR);
                    player.getWorld().createExplosion(pLocation, 4.0f);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (this.mines) {
            Player player = event.getPlayer();
            // player.sendMessage("boomHoe event triggered");
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item != null && item.getType() == Material.GOLD_HOE && item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + "Miner")) {
                player.getWorld().getBlockAt(player.getLocation().add(0, -1, 0)).setType(Material.TNT);
            }
        }
    }
}