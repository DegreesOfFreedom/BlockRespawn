package dev.zariem.blockrespawn.objects;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;

public class Region
{
	public Coordinate start;
	public Coordinate end;
	public UUID dimension;
	public long regionID;
	public String description;
	public List<Material> blockTypes;
	
	public Region() {
		this.regionID = System.currentTimeMillis();
	}
	
	public void setStart(Location l) {
		// Put Location l from block event in a Vector3/Coordinate
		this.start = new Coordinate(l.getBlockX(), l.getBlockY(), l.getBlockZ());
		// Set dimension based on block
		this.dimension = l.getWorld().getUID();
	}
	
	public void setEnd(Location l) {
		// Put Location l from block event in a Vector3/Coordinate
		this.end = new Coordinate(l.getBlockX(), l.getBlockY(), l.getBlockZ());
		// Make sure start is lower than end
		this.fixCoordinates();
	}
	
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
}
