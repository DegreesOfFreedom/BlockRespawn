package dev.zariem.blockrespawn;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockRespawnListener implements Listener {
	
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
	
}
