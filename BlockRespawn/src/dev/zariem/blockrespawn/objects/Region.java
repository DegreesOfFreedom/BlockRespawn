package dev.zariem.blockrespawn.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Region
{
	public String name;
	public Coordinate start;
	public Coordinate end;
	public UUID dimension;
	public UUID regionID;
	public String description;
	public ConcurrentHashMap<Material, Integer> blockTypes;
	
	/**
	 * Initialized on first point set
	 * setStart to be called right after
	 */
	public Region(String name) {
		this.name = name;
		this.regionID = UUID.randomUUID();
		this.description = "";
		this.blockTypes = new ConcurrentHashMap<Material, Integer>();
	}
	
	/**
	 * Returns the name of the region.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Return Integer respawnTime for Material
	 * @param name String name of Material.name();
	 * @return Integer respawnTime
	 */
	public Integer getMaterialRespawnTime(String name) {
		Material m = Material.getMaterial(name);
		if(this.blockTypes.containsKey(m)) {
			return this.blockTypes.get(m);
		} else {
			return null;
		}
	}
	
	/**
	 * Quickly check if region handles block type
	 * @param m Material of block
	 * @return Boolean true if handled by region false if not
	 */
	public Boolean containsBlock(Material m) {
		if(this.blockTypes.containsKey(m)) return true;
		return false;
	}
	
	/**
	 * Check if coordinate is within region bounds
	 * @param c Coordinate of block
	 * @return Boolean true if in bounds, false if out of bounds or no bounds set
	 */
	public Boolean coordInBounds(Coordinate c) {
		if(this.start != null && this.end != null) {
			if(c.x < this.start.x) return false;
			if(c.x > this.end.x) return false;
			if(c.y < this.start.y) return false;
			if(c.y > this.end.y) return false;
			if(c.z < this.start.z) return false;
			if(c.z > this.end.z) return false;
			return true;
		}
		return false;
	}
	
	/**
	 * Set the description for this 
	 * @param description	String
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Set start position and dimension
	 * @param l 	Location of block interaction
	 */
	public void setStart(Location l) {
		// Put Location l from block event in a Vector3/Coordinate
		this.start = new Coordinate(l.getBlockX(), l.getBlockY(), l.getBlockZ());
		// Set dimension based on block
		this.dimension = l.getWorld().getUID();
	}
	
	/**
	 * Set ending position
	 * Automatically initializes Region.fixCoordinates()
	 * @param l		Location of block interaction
	 */
	public void setEnd(Location l) {
		// Put Location l from block event in a Vector3/Coordinate
		this.end = new Coordinate(l.getBlockX(), l.getBlockY(), l.getBlockZ());
		// Make sure start is lower than end
		this.fixCoordinates();
		// Debugging output
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Created region " + this.getName());
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "with start at: " + this.start.x + "/" + this.start.y + "/" + this.start.z);
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "and end at: " + this.end.x + "/" + this.end.y + "/" + this.end.z);
	}
	
	/**
	 * Add block to region
	 * @param m
	 * @param respawnTime
	 */
	public void addBlock(Material m, Integer respawnTime) {
		this.blockTypes.put(m, respawnTime);
	}
	
	/**
	 * Fix cuboid to start<end
	 * @return		Boolean - True if fixed, false if not necessary
	 */
	public boolean fixCoordinates() {
		// Check if neither start or end are null
		if(this.start != null && this.end != null) {
			/*
			 * Assign lowest X, Y and Z to lX ,lY and lZ
			 * Assign highest X,Y and Z to hX, hY and hZ
			 */
			Integer lX = (this.start.x <= this.end.x) ? this.start.x : this.end.x;
			Integer lY = (this.start.y <= this.end.y) ? this.start.y : this.end.y;
			Integer lZ = (this.start.z <= this.end.z) ? this.start.z : this.end.z;

			Integer hX = (this.start.x > this.end.x) ? this.start.x : this.end.x;
			Integer hY = (this.start.y > this.end.y) ? this.start.y : this.end.y;
			Integer hZ = (this.start.z > this.end.z) ? this.start.z : this.end.z;
			
			/*
			 * Make Coordinate from lowest X, Y and Z and save it to start
			 * Make Coordinate from highest X, Y and Z and save it to end
			 */
			this.start = new Coordinate(lX, lY, lZ);
			this.end = new Coordinate(hX, hY, hZ);
			
			return true;
		} else {
			// Start or end null, return false
			return false;
		}
	}
	
	/**
	 * Save Region to a JSON file
	 */
	public void save() {
		JSONObject jObj = new JSONObject();
		JSONObject regionStart = new JSONObject();
		regionStart.put("x", this.start.x);
		regionStart.put("y", this.start.y);
		regionStart.put("z", this.start.z);
		jObj.put("start", regionStart);
		JSONObject regionEnd = new JSONObject();
		regionEnd.put("x", this.end.x);
		regionEnd.put("y", this.end.y);
		regionEnd.put("z", this.end.z);
		jObj.put("end", regionEnd);
		jObj.put("dimension", this.dimension.toString());
		jObj.put("regionID", this.regionID.toString());
		jObj.put("description", this.description);
		
		JSONArray jBlockTypes = new JSONArray();
		Iterator it = this.blockTypes.entrySet().iterator();
		while(it.hasNext()) {
			Entry<Material, Integer> entry = (Entry<Material, Integer>) it.next();
			Material m = entry.getKey();
			JSONObject eObj = new JSONObject();
			eObj.put("material", m.name());
			eObj.put("timer", entry.getValue());
			jBlockTypes.add(eObj);
		}
		
		jObj.put("blockTypes", jBlockTypes);
		
		String jString = jObj.toJSONString();
		try {
			FH.writeFile(jString, "BlockRespawn/regions/" + this.regionID.toString() + ".json");
		} catch (IOException e) {
			
		}
	}
	
	/**
	 * Load region from JSON file
	 * @param UUID String with region's UUID
	 */
	public void load(String UUID) {
		String jString;
		try {
			jString = FH.readFile("BlockRespawn/regions/" + UUID + ".json");
		} catch (IOException e) {
			return;
		}
		
		if(jString != null) {
			JSONObject jObj = (JSONObject) JSONValue.parse(jString);
			JSONObject regionStart = (JSONObject) jObj.get("regionStart");
			this.start = new Coordinate((int) regionStart.get("x"), (int) regionStart.get("y"), (int) regionStart.get("z"));
			JSONObject regionEnd = (JSONObject) jObj.get("regionStart");
			this.end = new Coordinate((int) regionEnd.get("x"), (int) regionEnd.get("y"), (int) regionEnd.get("z"));
			this.dimension = java.util.UUID.fromString((String) jObj.get("dimension"));
			this.regionID = java.util.UUID.fromString((String) jObj.get("regionID"));
			this.description = (String) jObj.get("description");
			ConcurrentHashMap<Material, Integer> jList = new ConcurrentHashMap<Material, Integer>();
			JSONArray jLArray = (JSONArray) jObj.get("blockTypes");
			for(Object o : jLArray) {
				JSONObject name = (JSONObject) o;
				jList.put(Material.getMaterial((String) name.get("material")), (Integer) name.get("timer"));
			}
			this.blockTypes = jList;
		}
	}
}
