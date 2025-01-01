package me.myself.i.commands;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

public class Dechunk extends BukkitRunnable {
    private static final int PLAYER_DECHUNK_RADIUS = 4; 
    private static final int DECHUNK_RADIUS = 1000;
    private boolean[][] deChunked;
    private Queue<Chunk> deChunks_Queue;
	Plugin p;

    public Dechunk(Plugin p) {
		this.p = p;
		deChunked = new boolean[2 * DECHUNK_RADIUS][2 * DECHUNK_RADIUS];
		deChunks_Queue = new LinkedList<Chunk>();

		//initialize array the keeps track whick chunks have been deleted
		for (int i = 0; i < 2 * DECHUNK_RADIUS; i++) {
			for (int j = 0; j < 2 * DECHUNK_RADIUS; j++) {
				deChunked[i][j] = false;
			}
		}
    }

	public boolean cmd(String[] args) {
		this.run();
		return true;
	}
	


    public void run() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            Chunk c = player.getLocation().getChunk();
            int x = c.getX();
            int z = c.getZ();
            for (int i = -1 * PLAYER_DECHUNK_RADIUS; i < PLAYER_DECHUNK_RADIUS + 1; i++) {
                for (int j = -1 * PLAYER_DECHUNK_RADIUS; j < PLAYER_DECHUNK_RADIUS + 1; j++) {
                    if (!deChunked[x + i + DECHUNK_RADIUS][z + j + DECHUNK_RADIUS]) {
                        deChunked[x + i + DECHUNK_RADIUS][z + j + DECHUNK_RADIUS] = true;
                        deChunks_Queue.add(player.getWorld().getChunkAt(x + i, z + j));
                    }
                }
            }
        }

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
		}.runTaskTimer(this.p, 1, 15);
	}
}