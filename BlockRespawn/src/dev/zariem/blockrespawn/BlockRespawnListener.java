package dev.zariem.blockrespawn;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R2.Tuple;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import dev.zariem.blockrespawn.objects.Region;

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
					
					Region region = BlockRespawn.readyToSelect.get(name).b();
					Block block = event.getClickedBlock();
					Location loc = block.getLocation();
					
					if (BlockRespawn.readyToSelect.get(name).a() == 2) {
						region.setStart(loc);
						player.sendMessage(ChatColor.GREEN + "Selected the first block.");
						BlockRespawn.readyToSelect.put(name, new Tuple<Integer, Region>(1, region));
						return;
					}
					
					if (BlockRespawn.readyToSelect.get(name).a() == 1) {
						region.setEnd(loc);
						player.sendMessage(ChatColor.GREEN + "Selected the second block.");
						BlockRespawn.readyToSelect.put(name, new Tuple<Integer, Region>(0, region));
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
