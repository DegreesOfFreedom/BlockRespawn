package dev.zariem.blockrespawn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import dev.zariem.blockrespawn.objects.*;

public class BlockRespawn extends JavaPlugin {
	
	public static Map<String, Tuple<Integer, Region>> readyToSelect;
	// this map ensures that player who didn't issue the command can still right-click blocks with
	// wooden swords without being handled by my listener class.
	// The Tuple contains as a string the name of the newly created region.
	
	public static List<Region> regions;
	// This list will contain all the defined regions
	
	
	public void onEnable() {
		regions = new ArrayList<Region>();
		// TODO load the regions into the List
		
		readyToSelect = new HashMap<String, Tuple<Integer, Region>>();
		
		// instantiate the listener
		Bukkit.getServer().getPluginManager().registerEvents(new BlockRespawnListener(this), this);
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if (!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "The console can't use these commands! (For now)");
		}
		
		Player player = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("br")) {
			new RegionCommand(this, sender, cmd, commandLabel, args);
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
