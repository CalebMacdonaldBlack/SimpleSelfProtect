package gigabytedx;

import java.io.Serializable;
import java.util.Set;

public class RegionData implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	double						x1, y1, z1;
	double						x2, y2, z2;
	String						name;
	Set<String>					owners;
	Set<String>					members;
	String						world;

	public String getWorld() {

		return world;
	}

	public void setWorld(String world) {

		this.world = world;
	}

	public RegionData(String world, double x1, double y1, double z1, double x2, double y2, double z2,
			String name, Set<String> owners, Set<String> members) {

		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
		this.name = name;
		this.owners = owners;
		this.members = members;
		this.world = world;
	}

	public double getX1() {

		return x1;
	}

	public Set<String> getMembers() {

		return members;
	}

	public void setMembers(Set<String> members) {

		this.members = members;
	}

	public void setX1(double x1) {

		this.x1 = x1;
	}

	public double getY1() {

		return y1;
	}

	public void setY1(double y1) {

		this.y1 = y1;
	}

	public double getZ1() {

		return z1;
	}

	public void setZ1(double z1) {

		this.z1 = z1;
	}

	public double getX2() {

		return x2;
	}

	public void setX2(double x2) {

		this.x2 = x2;
	}

	public double getY2() {

		return y2;
	}

	public void setY2(double y2) {

		this.y2 = y2;
	}

	public double getZ2() {

		return z2;
	}

	public void setZ2(double z2) {

		this.z2 = z2;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public Set<String> getOwners() {

		return owners;
	}

	public void setOwners(Set<String> owners) {

		this.owners = owners;
	}
}