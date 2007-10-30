/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.AbstractParallelizableJob;
import org.jmist.framework.PixelShader;
import org.jmist.framework.Raster;
import org.jmist.framework.TaskWorker;
import org.jmist.framework.reporting.ProgressMonitor;
import org.jmist.toolkit.Box2;
import org.jmist.toolkit.Pixel;

/**
 * A <code>ParallelizableJob</code> that renders a <code>Raster</code> image.
 * @author bkimmel
 */
public final class RasterParallelizableJob extends AbstractParallelizableJob {

	/**
	 * Creates a new <code>RasterParallelizableJob</code>.  This job will
	 * divide the image into <code>rows * cols</code> tasks to render roughly
	 * equally sized chunks of the image.
	 * @param shader The <code>PixelShader</code> to use to compute the values
	 * 		of individual <code>Pixel</code>s.
	 * @param raster The <code>Raster</code> to write the results to.
	 * @param cols The number of columns to divide the image into.
	 * @param rows The number of rows to divide the image into.
	 */
	public RasterParallelizableJob(PixelShader shader, Raster raster, int cols, int rows) {
		this.worker = new RasterTaskWorker(shader, raster.width(), raster.height());
		this.raster = raster;
		this.cols = cols;
		this.rows = rows;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#getNextTask()
	 */
	@Override
	public Object getNextTask() {

		if (this.nextRow < this.rows) {

			/* Get the next cell. */
			Cell cell = this.getCell(this.nextCol++, this.nextRow);

			/* If we're done this row, move on to the next row. */
			if (this.nextCol >= this.cols) {
				this.nextCol = 0;
				this.nextRow++;
			}

			return cell;

		} else { /* this.nextRow >= this.rows */

			/* no remaining tasks. */
			return null;

		}

	}

	/**
	 * Gets the limits of the <code>Cell</code> at the specified row and
	 * column.
	 * @param col The column index.
	 * @param row The row index.
	 * @return The <code>Cell</code> limits.
	 * @see {@link Cell}.
	 */
	private Cell getCell(int col, int row) {

		/* Figure out how big the cells should be:
		 *    - Make them as large as possible without exceeding the size
		 *      of the image.
		 *    - Allocate the remainder of the pixels (if any) to the first n
		 *      cells, where n is the remainder.
		 */

		/* Ensure that the cell is valid whose dimensions are being
		 * requested.
		 */
		assert(0 <= col && col < this.cols);
		assert(0 <= row && row < this.rows);

		int w = this.raster.width();
		int h = this.raster.height();

		/* First figure out the base dimensions of the cells and the number
		 * of remaining pixels in each dimension that need to be allocated.
		 */
		int wdivc = w / this.cols;
		int hdivr = h / this.rows;
		int wmodc = w % this.cols;
		int hmodr = h % this.rows;

		/* Calculate the upper-left corner of the cell, first assuming that
		 * there are no "remainder" pixels.. we will then adjust for this.
		 */
		int xmin = col * wdivc;
		int ymin = row * hdivr;

		/* Adjust to account for the cells above and to the left that
		 * received extra pixels.
		 */
		xmin += Math.min(col, wmodc);
		ymin += Math.min(row, hmodr);

		/* Now compute the lower right pixel, again first assuming that this
		 * cell is not to receive extra pixels.
		 */
		int xmax = xmin + wdivc - 1;
		int ymax = ymin + hdivr - 1;

		/* Add the extra pixels, if required. */
		if (col < wmodc) xmax++;
		if (row < hmodr) ymax++;

		/* Make sure the computed cell extents fall within the image. */
		assert(0 <= xmin && xmin <= xmax && xmax < w);
		assert(0 <= ymin && ymin <= ymax && ymax < h);

		return new Cell(xmin, ymin, xmax, ymax);

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#submitTaskResults(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void submitTaskResults(Object task, Object results) {

		Cell		cell		= (Cell) task;
		Pixel[]		pixels		= (Pixel[]) results;

		/* Write the submitted results to the raster. */
		this.raster.setPixels(cell.xmin, cell.ymin, cell.xmax, cell.ymax, pixels);

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#worker()
	 */
	@Override
	public TaskWorker worker() {
		return this.worker;
	}

	/**
	 * Indicates the pixel locations of a rectangular subset of a
	 * <code>Raster</code> to be rendered by a <code>RasterTaskWorker</code>.
	 * @see {@link org.jmist.framework.Raster}, {@link RasterTaskWorker}.
	 * @author bkimmel
	 */
	private static final class Cell {

		/** Left-most x-coordinate of the image to render. */
		public final int xmin;

		/** Top-most y-coordinate of the image to render. */
		public final int ymin;

		/** Right-most x-coordinate of the image to render. */
		public final int xmax;

		/** Bottom-most y-coordinate of the image to render. */
		public final int ymax;

		/**
		 * Creates a new <code>Cell</code>.
		 * @param xmin Left-most x-coordinate of the image to render.
		 * @param ymin Top-most y-coordinate of the image to render.
		 * @param xmax Right-most x-coordinate of the image to render.
		 * @param ymax Bottom-most y-coordinate of the image to render.
		 */
		public Cell(int xmin, int ymin, int xmax, int ymax) {
			this.xmin = xmin;
			this.ymin = ymin;
			this.xmax = xmax;
			this.ymax = ymax;
		}

		/**
		 * Gets the number of pixels that fall within the limits of this
		 * <code>Cell</code>.
		 * @return The number of pixels that fall within the limits of this
		 * 		<code>Cell</code>.
		 */
		public int size() {
			return (xmax - xmin + 1) * (ymax - ymin + 1);
		}

	}

	/**
	 * A <code>TaskWorker</code> that renders a rectangular subset of a
	 * <code>Raster</code> image.
	 * @author bkimmel
	 */
	private static final class RasterTaskWorker implements TaskWorker {

		/**
		 * Creates a new <code>RasterTaskWorker</code>.
		 * @param shader The <code>PixelShader</code> to use to compute the
		 * 		values of individual <code>Pixel</code>s.
		 * @param width The width of the entire <code>Raster</code> image.
		 * @param height The height of the entire <code>Raster</code> image.
		 */
		public RasterTaskWorker(PixelShader shader, int width, int height) {
			this.shader		= shader;
			this.width		= width;
			this.height		= height;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.TaskWorker#performTask(java.lang.Object, org.jmist.framework.reporting.ProgressMonitor)
		 */
		@Override
		public Object performTask(Object task, ProgressMonitor monitor) {

			Cell			cell				= (Cell) task;
			Pixel[]			pixels				= new Pixel[cell.size()];
			Box2			bounds;
			double			x0, y0, x1, y1;
			double			w					= this.width;
			double			h					= this.height;

			for (int n = 0, y = cell.ymin; y <= cell.ymax; y++) {

				if (!monitor.notifyProgress(n, pixels.length))
					return null;

				y0			= (double) y / h;
				y1			= (double) (y + 1) / h;

				for (int x = cell.xmin; x <= cell.xmax; x++, n++) {

					x0		= (double) x / w;
					x1		= (double) (x + 1) / w;

					bounds	= new Box2(x0, y0, x1, y1);

					this.shader.shadePixel(bounds, pixels[n]);

				}

			}

			monitor.notifyProgress(pixels.length, pixels.length);
			monitor.notifyComplete();

			return pixels;

		}

		/**
		 * The <code>PixelShader</code> to use to compute the values of
		 * individual <code>Pixel</code>s.
		 */
		private final PixelShader shader;

		/** The width of the entire <code>Raster</code> image. */
		private final int width;

		/** The height of the entire <code>Raster</code> image. */
		private final int height;

		/**
		 * Serialization version ID.
		 */
		private static final long serialVersionUID = 8318742231359439076L;

	}

	/**
	 * The <code>TaskWorker</code> to use to render rectangular subsets of
	 * the <code>Raster</code> image.
	 */
	private final TaskWorker worker;

	/** The <code>Raster</code> image to render to. */
	private final Raster raster;

	/** The number of columns to divide the <code>Raster</code> image into. */
	private final int cols;

	/** The number of rows to divide the <code>Raster</code> image into. */
	private final int rows;

	/** The column index of the next task to return. */
	private int nextCol = 0;

	/** The row index of the next task to return. */
	private int nextRow = 0;

}
