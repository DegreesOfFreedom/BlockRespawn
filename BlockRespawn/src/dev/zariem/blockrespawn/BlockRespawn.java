package dev.zariem.blockrespawn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if (!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "The console can't use these commands! (For now)");
		}
		
		Player player = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("br")) {
			if (args.length == 0) {
				// TODO add default help page
				return true;
			}
			if (args[0].equalsIgnoreCase("newregion")) {
				if (args.length < 4) {
					player.sendMessage(ChatColor.RED + "Wrong number of arguments!"
							+ "Use /br newregion [name] [material] [respawntime]");
					return true;
				}
				if (!isInteger(args[3])) {
					player.sendMessage(ChatColor.RED + "Make sure that the respawn time is a number!");
					return true;
				}
				int time = Integer.parseInt(args[3]);
				// TODO probably add a check for the validity of the material name.
				Material mat = Material.getMaterial(args[2]);
				BlockRespawnRegion newRegion = new BlockRespawnRegion(args[1], player.getWorld(), mat, time);
				// TODO continue coding!
			}
		}
		return true;
	}
	
	@EventHandler
	public void onPlayerRightClick(PlayerInteractEvent event){
		Action action = event.getAction();
		// TODO
	}

	public static boolean isInteger(String s) {
		if (s.isEmpty()) {
			return false;
		}
		for (int i = 0; i < s.length(); i++) {
			// checks whether the character is a digit from 0 to 9
			if(Character.digit(s.charAt(i), 10) < 0) {
				return false;
			}
		}
		return true;
	}
	
}
