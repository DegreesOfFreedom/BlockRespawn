package dev.zariem.blockrespawn.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
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
	public ArrayList<Material> blockTypes;
	
	/**
	 * Initialized on first point set
	 * setStart to be called right after
	 */
	public Region(String name) {
		this.name = name;
		this.regionID = UUID.randomUUID();
	}
	
	/**
	 * Returns the name of the region.
	 */
	public String getName() {
		return this.name;
	}
	
	
	// TODO change the blockTypes array to a HashMap containing "name" as key, and "respawnTime" as value
	
	
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
		for (Material m : this.blockTypes) {
			jBlockTypes.add(m.name());
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
			ArrayList<Material> jList = new ArrayList<Material>();
			JSONArray jLArray = (JSONArray) jObj.get("blockTypes");
			for(Object o : jLArray) {
				String name = (String) o;
				jList.add(Material.getMaterial(name));
			}
			this.blockTypes = jList;
		}
	}
}
