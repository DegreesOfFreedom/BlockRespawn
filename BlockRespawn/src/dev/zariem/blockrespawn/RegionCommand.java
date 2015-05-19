package dev.zariem.blockrespawn;

import java.util.ArrayList;

import net.minecraft.server.v1_8_R2.Tuple;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.zariem.blockrespawn.objects.Region;

public class RegionCommand
{
	Player player;
	BlockRespawn blockRespawn;
	CommandSender sender;
	Command cmd;
	String commandLabel;
	String[] args;
	public RegionCommand(BlockRespawn br, CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if (!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "The console can't use these commands! (For now)");
		}
		
		this.player = (Player) sender;
		this.blockRespawn = br;
		this.sender = sender;
		this.cmd = cmd;
		this.commandLabel = commandLabel;
		this.args = args;
		
		if(args.length == 0) {
			player.sendMessage(ChatColor.RED + "Wromg number of arguments, type /br help to see all possibilities");
			return;
		}
		if(args[0].equalsIgnoreCase("help")) {
			this.sendHelpPage();
		}
		if (args[0].equalsIgnoreCase("region")) {
			if(args.length > 1) {
				switch(args[1].toLowerCase()) {
					case "new":
						if(args.length > 2) { this.newRegion(); } else { this.sendArgError(); }
						break;
					case "addblock":
						if(args.length > 3) { this.addBlock(); } else { this.sendArgError(); }
						break;
					case "save":
						if(args.length > 1) { this.saveRegion(); } else { this.sendArgError(); }
						break;
					case "help":
						this.sendHelpPage();
						break;
					default:
						this.sendHelpPage();
						break;
				}
				/*if (args.length < 2) {
					player.sendMessage(ChatColor.RED + "Wrong number of arguments! Use /br region <new <name>|edit <name>|addblock <block> <respawnTime>|save>");
					return true;
				}
				
				// TODO add a check for whether a region with this name already exists!
				if(args[1].equalsIgnoreCase("new")) {
					Region myRegion = new Region(args[2]);
					// create a region with the name defined in args[1]
					player.sendMessage(ChatColor.GREEN + "You sucessfully created the region: " + myRegion.getName());
					player.sendMessage(ChatColor.GREEN + "Please use a wooden sword and right click"
							+ " on two blocks to set the region borders.");
					// register the player for events
					readyToSelect.put(player.getName(), new Tuple<Integer, Region>(2, myRegion));
					// the 2 makes sure that the player gets removed from the map, as soon as he chose 2 blocks.
					return true;
				}
				if(args[1].equalsIgnoreCase("edit")) {
					// Load region into memory
				}
				if(args[1].equalsIgnoreCase("addblock")) {
					if (args.length < 3) {
						player.sendMessage(ChatColor.RED + "Wrong number of arguments."
								+ " Use /br region addblock <blockname> <respawnTime>");
						return true;
					}
					// now args.length is at least 5. If too many arguments are given, they will be ignored.
					
					
					Material mat = Material.getMaterial(args[2]);
					Integer respawnTime = 0;
					// TODO add a check for validity of the material!
					if (!isInteger(args[3])) {
						player.sendMessage(ChatColor.RED + "Make sure that the respawn time is a number!");
					} else {
						respawnTime = Integer.parseInt(args[3]);
					}
					if(respawnTime > 0) {
						if(readyToSelect.containsKey(player.getName())) {
							readyToSelect.get(player.getName()).b().addBlock(mat, respawnTime);
							return true;
						} else {
							return false;
						}
					}
				}
				if(args[1].equalsIgnoreCase("save")) {
					if(readyToSelect.containsKey(player.getName())) {
						if(readyToSelect.get(player.getName()).a() == 0) {
							readyToSelect.get(player.getName()).b().save();
							readyToSelect.remove(player.getName());
							return true;
						}
					}
					return false;
				}*/
				
			}
		}
	}
	
	/**
	 * Handles /br region new <name>
	 */
	public void newRegion() {
		Region myRegion = new Region(args[2]);
		// create a region with the name defined in args[1]
		player.sendMessage(ChatColor.GREEN + "You sucessfully created the region: " + myRegion.getName());
		player.sendMessage(ChatColor.GREEN + "Please use a wooden sword and right click"
				+ " on two blocks to set the region borders.");
		// register the player for events
		this.blockRespawn.readyToSelect.put(player.getName(), new Tuple<Integer, Region>(2, myRegion));
		// the 2 makes sure that the player gets removed from the map, as soon as he chose 2 blocks.
		return;
	}
	
	/**
	 * Save and unload region
	 */
	public void saveRegion() {
		if(this.getRegion() != null) {
			if(this.blockRespawn.readyToSelect.get(player.getName()).a() == 0) {
				this.getRegion().save();
				player.sendMessage(ChatColor.YELLOW + "Region "+ ChatColor.GREEN+this.getRegion().getName()+ChatColor.YELLOW+" saved and unloaded");
				this.blockRespawn.readyToSelect.remove(player.getName());
			}
		}
	}
	
	/**
	 * Add block to region
	 */
	public void addBlock() {
		if (args.length < 4) {
			player.sendMessage(ChatColor.RED + "Wrong number of arguments."
					+ " Use /br region addblock <blockname> <respawnTime>");
			return;
		}
		
		
		Material mat = Material.getMaterial(args[2]);
		Integer respawnTime = 0;
		// TODO add a check for validity of the material!
		if (!BlockRespawn.isInteger(args[3])) {
			player.sendMessage(ChatColor.RED + "Make sure that the respawn time is a number!");
		} else {
			respawnTime = Integer.parseInt(args[3]);
		}
		if(respawnTime > 0) {
			if(this.getRegion() != null) {
				this.getRegion().addBlock(mat, respawnTime);
				player.sendMessage(ChatColor.YELLOW + "Block "+ChatColor.GREEN + mat.name() + ChatColor.YELLOW + " added with respawn time of "+ChatColor.GREEN+respawnTime+ChatColor.YELLOW+" seconds");
				return;
			} else {
				player.sendMessage(ChatColor.RED + "Need to start editing a region first");
			}
		}
	}
	
	/**
	 * @return Region player working with
	 */
	public Region getRegion() {
		if(this.blockRespawn.readyToSelect.containsKey(player.getName()))
			return this.blockRespawn.readyToSelect.get(player.getName()).b();
			
		return null;
	}
	
	/**
	 * @return ArrayList<String> of commands
	 */
	public ArrayList<String> getCommands() {
		ArrayList<String> commands = new ArrayList<String>();
		commands.add("/br region new <name>");
		commands.add("/br region edit <name>");
		commands.add("/br region addblock <block> <time>");
		commands.add("/br region save");
		
		return commands;
	}
	
	/**
	 * Send player help page with all commands
	 */
	public void sendHelpPage() {
		this.player.sendMessage(ChatColor.GREEN + "Commands for BlockRespawn:");
		for(String command : this.getCommands()) {
			this.player.sendMessage(ChatColor.YELLOW + command);
		}
	}
	
	/**
	 * Wrong number of arguments
	 */
	public void sendArgError() {
		this.player.sendMessage(ChatColor.RED + "Wromg number of arguments, type /br help to see all possibilities");
	}
}
