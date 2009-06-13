package ca.eandb.jmist.framework.photonmap;

import ca.eandb.jmist.math.Point3;

public interface PhotonBuffer {

	/**
	 * Gets the location of a photon.
	 * @param index The index of the photon for which to get the coordinate.
	 * @return The <code>Point3</code> indicating the location of the specified
	 * 		photon.
	 */
	Point3 getPosition(int index);

	/**
	 * Gets a single coordinate for the location of a photon.
	 * @param index The index of the photon for which to get the coordinate.
	 * @param element The coordinate to get (0 for the x-coordinate, 1 for the
	 * 		y-coordinate, or 2 for the z-coordinate).
	 * @return The value of the coordinate for the specified photon.
	 */
	double getPosition(int index, int element);

	/**
	 * Gets the power of the specified photon.
	 * @param index The index of the photon to obtain the power of.
	 * @return The power of the specified photon.
	 */
	double getX(int index);

	/**
	 * Gets the y-coordinate of the specified photon.
	 * @param index The index of the photon to obtain the y-coordinate of.
	 * @return The y-coordinate of the specified photon.
	 */
	double getY(int index);

	/**
	 * Gets the z-coordinate of the specified photon.
	 * @param index The index of the photon to obtain the z-coordinate of.
	 * @return The z-coordinate of the specified photon.
	 */
	double getZ(int index);

	/**
	 * Gets the orientation of the dividing plane for the specified photon.
	 * This value is either 0 - perpendicular to the x-axis, 1 - perpendicular
	 * to the y-axis, or 2 - perpendicular to the z-axis.
	 * @param index The index of the photon for which to orientation of the
	 * 		dividing plane.
	 * @return The orientation of the dividing plane for the specified photon.
	 */
	short getPlane(int index);

	/**
	 * Sets the orientation of the dividiing plane for the specified photon.
	 * @param index The index of the photon for which to set the orientation of
	 * 		the dividing plane.
	 * @param plane The orientation of the dividing plane: 0 - perpendicular to
	 * 		the x-axis, 1 - perpendicular to the y-axis, or 2 - perpendicular
	 * 		to the z-axis.
	 */
	void setPlane(int index, short plane);

	/**
	 * Copies the photon at a given index to another location in the photon
	 * buffer.
	 * @param src The index of the photon the copy.
	 * @param dst The index of the location to copy the photon to.
	 */
	void copyPhoton(int src, int dst);

}