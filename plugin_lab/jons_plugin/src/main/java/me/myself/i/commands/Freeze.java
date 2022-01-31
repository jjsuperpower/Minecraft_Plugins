package me.myself.i.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class Freeze implements Listener {
    Boolean freeze_on = false;

    public Freeze(Plugin p) {
        Bukkit.getServer().getPluginManager().registerEvents(this, p);
    }

    public boolean set(String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("on")) {
            this.freeze_on = true;
            return true;
        }
        if (args[0].equalsIgnoreCase("off")) {
            this.freeze_on = false;
            return true;
        }
        return false;
    }


    @EventHandler
	public void onMove(PlayerMoveEvent event) {
        if (this.freeze_on) {
			Player p = event.getPlayer();
			if (p.getGameMode() == GameMode.SURVIVAL) {
				event.setCancelled(true);
			}
		}
    }

}

