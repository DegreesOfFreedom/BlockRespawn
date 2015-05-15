package dev.zariem.blockrespawn;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockRespawn extends JavaPlugin implements Listener {
	
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(new BlockRespawnListener(), this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] arges) {
		// TODO
		return true;
	}
	
	@EventHandler
	public void onPlayerRightClick(PlayerInteractEvent event){
		Action action = event.getAction();
		// TODO
	}

}
