/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.eandb.jmist.framework.accel;

import java.io.Serializable;

import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * An axis-aligned box divided into a grid of cells along each of
 * the three axes.  Used for ray tracing with spatial subdivision.
 * @author Brad Kimmel
 */
public final class Grid3 implements Serializable {

  /**
   * Initializes the bounds of the grid and the number of cells
   * in each direction.
   * @param bound The bounding box of the grid.  Must have volume greater
   *     than zero.
   * @param nx The number of cells to divide the grid into along the x-axis.
   *     Must be greater than zero.
   * @param ny The number of cells to divide the grid into along the y-axis.
   *     Must be greater than zero.
   * @param nz The number of cells to divide the grid into along the z-axis.
   *     Must be greater than zero.
   */
  public Grid3(Box3 bound, int nx, int ny, int nz) {
    assert(bound.volume() > 0.0);
    assert(nx > 0 && ny > 0 && nz > 0);

    this.bound = bound;
    this.nx = nx;
    this.ny = ny;
    this.nz = nz;
    this.dx = bound.lengthX() / (double) nx;
    this.dy = bound.lengthY() / (double) ny;
    this.dz = bound.lengthZ() / (double) nz;
  }

  /**
   * Represents a cell in the grid.
   * @author Brad Kimmel
   */
  public final class Cell {

    /**
     * Gets the index of this cell along the x-axis.
     * @return The index of this cell along the x-axis.
     */
    public int getX() {
      return cx;
    }

    /**
     * Gets the index of this cell along the y-axis.
     * @return The index of this cell along the y-axis.
     */
    public int getY() {
      return cy;
    }

    /**
     * Gets the index of this cell along the z-axis.
     * @return The index of this cell along the z-axis.
     */
    public int getZ() {
      return cz;
    }

    /**
     * Gets the bounds of this cell.
     * @return The bounding box of this cell.
     */
    public Box3 getBoundingBox() {
      if (bound == null) {
        bound = cellBounds(this.cx, this.cy, this.cz);
      }

      return bound;
    }

    /**
     * Initializes the cell indices (this constructor should
     * only be available to the parent class (Grid3)).
     * @param cx The index of this cell along the x-axis.
     * @param cy The index of this cell along the y-axis.
     * @param cz The index of this cell along the z-axis.
     */
    private Cell(int cx, int cy, int cz) {
      this.cx = cx;
      this.cy = cy;
      this.cz = cz;
    }

    /** The cell indices */
    private final int cx, cy, cz;

    /** The bounding box of this cell (only computed if requested). */
    private Box3 bound;

  }

  /**
   * An interface to allow {@link Grid3#intersect(Ray3, Interval, ca.eandb.jmist.framework.accel.Grid3.Visitor)}
   * to notify the caller about each cell passed through by the ray.
   * @see Grid3#intersect(Ray3, Interval, ca.eandb.jmist.framework.accel.Grid3.Visitor)
   */
  public static interface Visitor {

    /**
     * Notifies the caller to Grid3.intersect that the ray has
     * passed through the specified cell.  It is guaranteed that
     * grid.hasCellAt(cell.getX(), cell.getY(), cell.getZ()) will
     * return true.
     * @param ray The ray passed to {@link Grid3#intersect(Ray3, Interval, ca.eandb.jmist.framework.accel.Grid3.Visitor)}
     * @param I The interval along the ray that passes through the cell (i.e.,
     *     cell.getBoundingBox().contains(ray.pointAt(t)) if and only if
     *     I.contains(t)).
     * @param cell The cell being traversed.
     * @return A value indicating whether {@link Grid3#intersect(Ray3, Interval, ca.eandb.jmist.framework.accel.Grid3.Visitor)}
     *     should continue its traversal.
     * @see Grid3#intersect(Ray3, Interval, ca.eandb.jmist.framework.accel.Grid3.Visitor)
     * @see Grid3.Cell#getBoundingBox()
     * @see Box3#contains(Point3)
     * @see Ray3#pointAt(double)
     * @see Interval#contains(double)
     * @see Grid3#hasCellAt(int, int, int)
     */
    boolean visit(Ray3 ray, Interval I, Cell cell);

  }

  /**
   * Indicates whether this grid has a cell at the specified indices.
   * @param cx The index along the x-axis.
   * @param cy The index along the y-axis.
   * @param cz The index along the
   * @return A value indicating whether this grid has a cell at the specified
   *     indices.
   */
  public boolean hasCellAt(int cx, int cy, int cz) {
    return 0 <= cx && cx < nx && 0 <= cy && cy < ny && 0 <= cz && cz < nz;
  }

  /**
   * Computes the bounding box of a cell.  Requires that
   * this.hasCellAt(cx, cy, cz).
   * @param cx The index of the cell along the x-axis.
   * @param cy The index of the cell along the y-axis.
   * @param cz The index of the cell along the z-axis.
   * @return The bounding box of the specified cell.
   * @see #hasCellAt(int, int, int)
   */
  public Box3 cellBounds(int cx, int cy, int cz) {
    assert(this.hasCellAt(cx, cy, cz));

    return new Box3(
      bound.minimumX() + (double) cx * dx,
      bound.minimumY() + (double) cy * dy,
      bound.minimumZ() + (double) cz * dz,
      bound.minimumX() + (double) (cx + 1) * dx,
      bound.minimumY() + (double) (cy + 1) * dy,
      bound.minimumZ() + (double) (cz + 1) * dz
    );
  }

  /**
   * Gets the bounding box for this grid.
   * @return The bounding box of this grid.
   */
  public Box3 getBoundingBox() {
    return bound;
  }

  /**
   * Returns the cell that contains the specified point.
   * @param p The point to check for.
   * @return The cell that contains the specified point, or
   *     null if the point is not contained in the grid.
   */
  public Cell cellAt(Point3 p) {
    Cell cell = new Cell(
      (int) Math.floor((p.x() - bound.minimumX()) / dx),
      (int) Math.floor((p.y() - bound.minimumY()) / dy),
      (int) Math.floor((p.z() - bound.minimumZ()) / dz)
    );

    if (this.hasCellAt(cell.cx, cell.cy, cell.cz)) {
      return cell;
    }

    return null;
  }

  /**
   * Returns the cell that is nearest to the specified point.  This
   * method is guaranteed not to return null.
   * @param p The point to consider.
   * @return The cell that is nearest to the specified point.
   */
  public Cell nearestCell(Point3 p) {
    return new Cell(
      MathUtil.clamp((int) Math.floor((p.x() - bound.minimumX()) / dx), 0, nx - 1),
      MathUtil.clamp((int) Math.floor((p.y() - bound.minimumY()) / dy), 0, ny - 1),
      MathUtil.clamp((int) Math.floor((p.z() - bound.minimumZ()) / dz), 0, nz - 1)
    );
  }

  /**
   * Intersects the ray with this grid and notifies a visitor
   * for each cell traversed.
   * @param ray The ray with which to traverse the grid.
   * @param I The interval along the ray to consider.
   * @param visitor The visitor to be notified for each cell
   *     traversed.
   * @return A value indicating whether the ray intersects
   *     the grid (i.e., this.getBoundingBox().intersect(ray).intersects(I)).
   * @see #getBoundingBox()
   * @see Box3#intersect(Ray3)
   * @see Interval#intersects(Interval)
   */
  public boolean intersect(Ray3 ray, Interval I, Visitor visitor) {

    I = I.intersect(bound.intersect(ray));

    if (I.isEmpty()) { // missed the grid entirely
      return false;
    }

    Interval  cellI = new Interval(I.minimum(), I.minimum());
    Vector3    d = ray.direction();
    Point3    p = ray.pointAt(I.minimum());
    Cell    cell;
    Cell    nextCell = this.nearestCell(p);
    double    rx = p.x() - (bound.minimumX() + (double) nextCell.cx * dx);
    double    ry = p.y() - (bound.minimumY() + (double) nextCell.cy * dy);
    double    rz = p.z() - (bound.minimumZ() + (double) nextCell.cz * dz);

    do {

      cell = nextCell;

      double  tx, ty, tz, t;

      tx = (d.x() > 0.0) ? (dx - rx) / d.x() : -rx / d.x();
      ty = (d.y() > 0.0) ? (dy - ry) / d.y() : -ry / d.y();
      tz = (d.z() > 0.0) ? (dz - rz) / d.z() : -rz / d.z();

      if (tx < ty && tx < tz) {
        t = tx;
        rx = (d.x() > 0.0) ? 0.0 : dx;
        ry += t * d.y();
        rz += t * d.z();
        nextCell = new Cell(cell.cx + (d.x() > 0.0 ? 1 : -1), cell.cy, cell.cz);
      } else if (ty < tz) {
        t = ty;
        rx += t * d.x();
        ry = (d.y() > 0.0) ? 0.0 : dy;
        rz += t * d.z();
        nextCell = new Cell(cell.cx, cell.cy + (d.y() > 0.0 ? 1 : -1), cell.cz);
      } else { // tz <= tx && tz <= ty
        t = tz;
        rx += t * d.x();
        ry += t * d.y();
        rz = (d.z() > 0.0) ? 0.0 : dz;
        nextCell = new Cell(cell.cx, cell.cy, cell.cz + (d.z() > 0.0 ? 1 : -1));
      }

      cellI = new Interval(cellI.maximum(), cellI.maximum() + t);

      if (cellI.maximum() > I.maximum()) {
        cellI = cellI.intersect(I);
        visitor.visit(ray, cellI, cell);
        break;
      }


    } while (I.intersects(cellI) && visitor.visit(ray, cellI, cell) && this.hasCellAt(nextCell.cx, nextCell.cy, nextCell.cz));

    return true;

  }


  /** The bounding box of this grid. */
  private final Box3    bound;

  /** The number of cells in each direction. */
  private final int    nx, ny, nz;

  /** The dimensions of the cells along each axis. */
  private final double  dx, dy, dz;

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = -2612737653304419826L;

}
