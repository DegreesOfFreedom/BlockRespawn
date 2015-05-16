package dev.zariem.blockrespawn;


import java.awt.font.NumericShaper.Range;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockRespawnRegion implements Listener {
	/*
	 * This defines a cuboid region in which a certain
	 * block type has a certain respawning time.
	 */
	
	final String name;
	final Material material;
	final World world;
	final long respawnSeconds;
	int x1, y1, z1, x2, y2, z2;
	Range rangeX;
	
	public BlockRespawnRegion(String n, World w, Material m, long respawnSeconds) {
		// constructor
		this.name = n;
		this.world = w;
		this.material = m;
		this.respawnSeconds = respawnSeconds;
	}
	
	public void defineFirstCorner(Block block) {
		// used for selecting the region
		if (block.getWorld() != this.world) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED +
					"The chosen block is not in the same world as your region!");
			return;
		}
		this.x1 = block.getX();
		this.y1 = block.getY();
		this.z1 = block.getZ();
	}
	
	public void defineSecondCorner(Block block) {
		// used for selecting the region
		if (block.getWorld() != this.world) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED +
					"The chosen block is not in the same world as your region!");
			return;
		}
		this.x2 = block.getX();
		this.y2 = block.getY();
		this.z2 = block.getZ();
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		if (BlockRespawn.readyToSelect.containsKey(name)) {
		// only if the player is ready to select
			Action action = event.getAction();
			if (action == Action.RIGHT_CLICK_BLOCK) {
				if (player.getItemInHand().getType() == Material.WOOD_SWORD) {
					
					if (BlockRespawn.readyToSelect.get(name) == 2) {
						defineFirstCorner(event.getClickedBlock());
						player.sendMessage(ChatColor.GREEN + "Selected the first block.");
						BlockRespawn.readyToSelect.put(name, 1);
						return;
					}
					
					if (BlockRespawn.readyToSelect.get(name) == 1) {
						defineSecondCorner(event.getClickedBlock());
						player.sendMessage(ChatColor.GREEN + "Selected the second block.");
						BlockRespawn.readyToSelect.remove(name);
					}
				}
			}
		}
	}
	
	// Getter functions
	public String getName() {
		return this.name;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public Material getMaterial() {
		return this.material;
	}
	
	public long getRespawnTime() {
		return this.respawnSeconds;
	}
}
