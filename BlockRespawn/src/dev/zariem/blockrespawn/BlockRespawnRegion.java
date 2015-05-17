package dev.zariem.blockrespawn;


import java.awt.font.NumericShaper.Range;
import java.util.HashMap;
import java.util.Map;

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
//	final Material material;
	final World world;
//	final long respawnSeconds;
	int x1, y1, z1, x2, y2, z2;
	Range rangeX;
	public Map<Material, Long> respawnableBlocks;
	// this map includes the Materials and their respawnTimes
	
	
	public BlockRespawnRegion(String n, World w) {
		// constructor
		this.name = n;
		this.world = w;
		respawnableBlocks = new HashMap<Material, Long>();
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
	
	
	public void listAllMaterials() {
		// prints out all the materials and their respawn times
		for (Material mat : respawnableBlocks.keySet()) {
			Bukkit.getServer().getLogger().info(ChatColor.GOLD + mat.name() + ": "
					+ ChatColor.DARK_AQUA + respawnableBlocks.get(mat));
		}
	}
	
	
	public void addRespawnableMaterial(String materialName, long respawnTime) {
		if (respawnableBlocks.containsKey(Material.getMaterial(materialName))) {
			Bukkit.getServer().getLogger().info(ChatColor.RED + "Material " + materialName + " was already in the list. "
					+ "It had a respawn time of " + respawnableBlocks.get(materialName) + " seconds.");
			respawnableBlocks.put(Material.getMaterial(materialName), respawnTime);
			Bukkit.getServer().getLogger().info(ChatColor.GREEN + "Successfully overwritten the old respawn time.");
			return;
		}
		respawnableBlocks.put(Material.getMaterial(materialName), respawnTime);
		Bukkit.getServer().getLogger().info(ChatColor.GREEN + "Added material " + materialName + " to the list.");
	}
	
	
	public void removeRespawnableMaterial(String materialName, long respawnTime) {
		if (!respawnableBlocks.containsKey(Material.getMaterial(materialName))) {
			Bukkit.getServer().getLogger().info(ChatColor.RED + "Material " + materialName + " wasn't in the list.");
			return;
		}
		respawnableBlocks.remove(Material.getMaterial(materialName));
		Bukkit.getServer().getLogger().info(ChatColor.GREEN + "Removed material " + materialName + " from the list.");
	}
	
	
	
	// Getter functions
	public String getName() {
		return this.name;
	}
	
	public World getWorld() {
		return this.world;
	}

}
