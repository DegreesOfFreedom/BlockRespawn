package dev.zariem.blockrespawn;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockRespawnRegion {
	/*
	 * This defines a cuboid region in which a certain
	 * block type has a certain respawning time.
	 */
	
	final String name;
	final Material material;
	final World world;
	final long respawnSeconds;
	int x1, y1, z1, x2, y2, z2;
	
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
