package dev.zariem.blockrespawn;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R2.Tuple;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockRespawnListener implements Listener {
	
	BlockRespawn plugin;
	
	public BlockRespawnListener(BlockRespawn instance) {
		this.plugin = instance;
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
					
					BlockRespawnRegion region = BlockRespawn.readyToSelect.get(name).b();
					Block block = event.getClickedBlock();
					
					if (BlockRespawn.readyToSelect.get(name).a() == 2) {
						region.defineFirstCorner(block);
						player.sendMessage(ChatColor.GREEN + "Selected the first block.");
						// adding the coordinates to the configuration file
						plugin.config().addDefault("regions." + region.getName() + ".x1", block.getX());
						plugin.config().addDefault("regions." + region.getName() + ".y1", block.getY());
						plugin.config().addDefault("regions." + region.getName() + ".z1", block.getZ());
						BlockRespawn.readyToSelect.put(name, new Tuple(1, region));
						return;
					}
					
					if (BlockRespawn.readyToSelect.get(name).a() == 1) {
						region.defineSecondCorner(block);
						player.sendMessage(ChatColor.GREEN + "Selected the second block.");
						// adding the coordinates to the configuration file
						plugin.config().addDefault("regions." + region.getName() + ".x2", block.getX());
						plugin.config().addDefault("regions." + region.getName() + ".y2", block.getY());
						plugin.config().addDefault("regions." + region.getName() + ".z2", block.getZ());
						BlockRespawn.readyToSelect.remove(name);
						return;
					}
				}
			}
		}
	}
	
	
/*	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if (event.isCancelled()){
			return;
		}
		final String world = event.getPlayer().getWorld().getName();
		final Location loc = event.getBlock().getLocation();
		final Material mat = event.getBlock().getType();
		Bukkit.getServer().getScheduler().runTaskLater(new BlockRespawn(), new Runnable(){
			public void run() {
				Bukkit.getWorld(world).getBlockAt(loc).setType(mat);
				Bukkit.getServer().broadcastMessage(ChatColor.GOLD + mat.toString() + " Block was reset after 5 seconds!");
			}
		}, 100);
	}
*/	
}
