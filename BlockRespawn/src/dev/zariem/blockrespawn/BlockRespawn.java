package dev.zariem.blockrespawn;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.v1_8_R2.Tuple;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockRespawn extends JavaPlugin {
	
	public static Map<String, Tuple<Integer, BlockRespawnRegion>> readyToSelect;
	// this map ensures that player who didn't issue the command can still right-click blocks with
	// wooden swords without being handled by my listener class.
	
	
	public void onEnable() {
		final FileConfiguration config = this.getConfig();
		
		readyToSelect = new HashMap<String, Tuple<Integer, BlockRespawnRegion>>();
		config.options().copyDefaults(true);
		
		saveConfig();
		Bukkit.getServer().getPluginManager().registerEvents(new BlockRespawnListener(this), this);
	}
	
	public FileConfiguration config() {
		return getConfig();
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
				if (args.length < 2) {
					player.sendMessage(ChatColor.RED + "Wrong number of arguments! Use /br newregion [name]");
					return true;
				}
				
				if (getConfig().getStringList("regions").contains(args[1])) {
					player.sendMessage(ChatColor.RED + "A region of this name already exists! Choose another name.");
					return true;
				}
				
				BlockRespawnRegion region = new BlockRespawnRegion(args[1], player.getWorld());
				getConfig().set("regions." + args[1] + ".world", player.getWorld().getName());
				player.sendMessage(ChatColor.GREEN + "You sucessfully created the region: " + region.getName());
				
				player.sendMessage(ChatColor.GREEN + "Please use a wooden sword and right click"
						+ " on two blocks to set the region borders.");
				
				// register the player for events
				readyToSelect.put(player.getName(), new Tuple(2, region));
				// the 2 makes sure that the player gets removed from the map, as soon as he chose 2 blocks.
				return true;
			}
			if (args[0].equalsIgnoreCase("region")) {
				// modify a region
				if (args.length == 1) {
					// TODO add region help page
					return true;
				}
				if (args[1].equalsIgnoreCase("add")) {
					if (args.length < 5) {
						player.sendMessage(ChatColor.RED + "Wrong number of arguments."
								+ " Use /br region add [regionname] [material] [respawntime]");
						return true;
					}
					// now args.length is at least 5. If too many arguments are given, they will be ignored.
					
					
					Material mat = Material.getMaterial(args[2]);
					// TODO add a check for validity of the material!
					if (!isInteger(args[3])) {
						player.sendMessage(ChatColor.RED + "Make sure that the respawn time is a number!");
					}
				}
			}
		}
		return true;
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
