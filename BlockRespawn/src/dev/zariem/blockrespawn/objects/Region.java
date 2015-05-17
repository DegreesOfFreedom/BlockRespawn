package dev.zariem.blockrespawn.objects;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Region
{
	public Coordinate start;
	public Coordinate end;
	public UUID dimension;
	public UUID regionID;
	public String description;
	public List<Material> blockTypes;
	
	/**
	 * Initialized on first point set
	 * setStart to be called right after
	 */
	public Region() {
		this.regionID = UUID.randomUUID();
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
}
