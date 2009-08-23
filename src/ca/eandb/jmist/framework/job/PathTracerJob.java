/**
 *
 */
package ca.eandb.jmist.framework.job;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.Serializable;

import ca.eandb.jdcp.job.AbstractParallelizableJob;
import ca.eandb.jdcp.job.TaskWorker;
import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.gi.EyeNode;
import ca.eandb.jmist.framework.gi.LightNode;
import ca.eandb.jmist.framework.gi.PathNodeFactory;
import ca.eandb.jmist.framework.gi.PathUtil;
import ca.eandb.jmist.framework.gi.ScatteringNode;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.Point2;
import ca.eandb.util.io.Archive;
import ca.eandb.util.progress.ProgressMonitor;

/**
 * A <code>ParallelizableJob</code> that renders a <code>Raster</code> image.
 * @author Brad Kimmel
 */
public final class PathTracerJob extends AbstractParallelizableJob {

	/**
	 * Creates a new <code>RasterJob</code>.  This job will
	 * divide the image into <code>rows * cols</code> tasks to render roughly
	 * equally sized chunks of the image.
	 * @param colorModel The <code>ColorModel</code> to use to render the
	 * 		image.
	 * @param pixelShader The <code>PixelShader</code> to use to compute the
	 * 		values of individual <code>Pixel</code>s.
	 * @param formatName The name of the format to save the image as.
	 * @param width The width of the rendered image, in pixels.
	 * @param height The height of the rendered image, in pixels.
	 * @param cols The number of columns to divide the image into.
	 * @param rows The number of rows to divide the image into.
	 */
	public PathTracerJob(ColorModel colorModel, Display display, int width, int height, int cols, int rows) {
		this.colorModel = colorModel;
		this.width = width;
		this.height = height;
		this.cols = cols;
		this.rows = rows;
		this.display = display;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.AbstractParallelizableJob#initialize()
	 */
	@Override
	public void initialize() throws IOException {
		display.initialize(width, height, colorModel);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.AbstractParallelizableJob#restoreState(java.io.ObjectInput)
	 */
	@Override
	public void restoreState(ObjectInput input) throws Exception {
		super.restoreState(input);
		this.initialize();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ParallelizableJob#getNextTask()
	 */
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
	 * Defines the region of the image that a task should perform.
	 * @author Brad Kimmel
	 */
	private static final class Cell implements Serializable {

		/** Serialization version ID. */
		private static final long serialVersionUID = 3873435167338343617L;

		/** The x-coordinate of the upper left corner of the cell. */
		final int x;

		/** The y-coordinate of the upper left-corner of the cell. */
		final int y;

		/** The width of the cell, in pixels. */
		final int width;

		/** The height of the cell, in pixels. */
		final int height;

		/**
		 * Creates a new <code>Cell</code>.
		 * @param x The x-coordinate of the upper left corner of the cell.
		 * @param y The y-coordinate of the upper left-corner of the cell.
		 * @param width The width of the cell, in pixels.
		 * @param height The height of the cell, in pixels.
		 */
		Cell(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

	};

	/**
	 * Gets the bounds of the cell at the specified row and column.
	 * @param col The column index.
	 * @param row The row index.
	 * @return The cell bounds.
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

		/* First figure out the base dimensions of the cells and the number
		 * of remaining pixels in each dimension that need to be allocated.
		 */
		int wdivc = width / this.cols;
		int hdivr = height / this.rows;
		int wmodc = width % this.cols;
		int hmodr = height % this.rows;

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
		assert(0 <= xmin && xmin <= xmax && xmax < width);
		assert(0 <= ymin && ymin <= ymax && ymax < height);

		return new Cell(xmin, ymin, xmax - xmin + 1, ymax - ymin + 1);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ParallelizableJob#submitTaskResults(java.lang.Object, java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
	 */
	public void submitTaskResults(Object task, Object results, ProgressMonitor monitor) {

		Cell	cell	= (Cell) task;
		Raster	pixels	= (Raster) results;

		/* Write the submitted results to the raster. */
		display.setPixels(cell.x, cell.y, pixels);

		/* Update the progress monitor. */
		monitor.notifyProgress(++this.tasksComplete, this.rows * this.cols);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ParallelizableJob#isComplete()
	 */
	public boolean isComplete() {
		return this.tasksComplete >= (this.rows * this.cols);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ParallelizableJob#finish()
	 */
	public void finish() throws IOException {
		display.finish();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.AbstractParallelizableJob#archiveState(ca.eandb.util.io.Archive)
	 */
	@Override
	protected void archiveState(Archive ar) throws IOException {
		nextCol = ar.archiveInt(nextCol);
		nextRow = ar.archiveInt(nextRow);
		tasksComplete = ar.archiveInt(tasksComplete);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ParallelizableJob#worker()
	 */
	public TaskWorker worker() {
		return new Worker(colorModel, width, height);
	}

	/**
	 * A <code>TaskWorker</code> that renders a rectangular subset of a
	 * <code>Raster</code> image.
	 * @author Brad Kimmel
	 */
	private static final class Worker implements TaskWorker {

		/** The <code>ColorModel</code> to use to render this image. */
		private final ColorModel colorModel;

		/** The width of the image to render, in pixels. */
		private final int width;

		/** The height of the image to render, in pixels. */
		private final int height;

		private int numberOfLightSamples;

		private final PathNodeFactory nodes = null;

		/**
		 * Creates a new <code>RasterTaskWorker</code>.
		 * @param colorModel The <code>ColorModel</code> to use to render this
		 * 		image.
		 * @param width The width of the image to render, in pixels.
		 * @param height The height of the image to render, in pixels.
		 */
		public Worker(ColorModel colorModel,
				int width, int height) {
			this.colorModel = colorModel;
			this.width = width;
			this.height = height;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.TaskWorker#performTask(java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
		 */
		public Object performTask(Object task, ProgressMonitor monitor) {

			Cell	cell				= (Cell) task;
			int		numPixels			= cell.width * cell.height;
			Box2	bounds;
			double	x0, y0, x1, y1;
			double	w					= width;
			double	h					= height;
			Raster	raster				= colorModel.createRaster(cell.width, cell.height);

			for (int n = 0, y = cell.y; y < cell.y + cell.height; y++) {

				if (!monitor.notifyProgress(n, numPixels))
					return null;

				y0			= (double) y / h;
				y1			= (double) (y + 1) / h;

				for (int x = cell.x; x < cell.x + cell.width; x++, n++) {

					x0				= (double) x / w;
					x1				= (double) (x + 1) / w;

					bounds			= new Box2(x0, y0, x1, y1);
					Point2 p		= RandomUtil.uniform(bounds, Random.DEFAULT);
					Color sample	= colorModel.sample(Random.DEFAULT);
					EyeNode eye		= nodes.sampleEye(p, sample);
					Color score		= tracePixel(eye);

					if (score != null) {
						raster.addPixel(x - cell.x, y - cell.x, score);
					}

				}

			}

			monitor.notifyProgress(numPixels, numPixels);
			monitor.notifyComplete();

			return raster;

		}

		private Color tracePixel(EyeNode eye) {
			Color score = null; // black
			ScatteringNode node = eye.expand(Random.DEFAULT);
			while (node != null) {
				score = ColorUtil.add(score, source(node));
				if (!node.atInfinity()) {
					score = ColorUtil.add(score, traceLights(node));
				}
				node = node.expand(Random.DEFAULT);
			}
			return score;
		}

		private Color source(ScatteringNode node) {
			if (node.getDepth() <= 1 || numberOfLightSamples == 0) {
				return node.getEmittedRadiance().times(node.getValue());
			} else {
				return null; // black
			}
		}

		private Color traceLights(ScatteringNode node) {
			Color score = null; // black
			if (numberOfLightSamples > 0) {
				for (int i = 0; i < numberOfLightSamples; i++) {
					score = ColorUtil.add(score, traceLight(node));
				}
				score = ColorUtil.div(score, numberOfLightSamples);
			}
			return score;
		}

		private Color traceLight(ScatteringNode node) {
			WavelengthPacket lambda = node.getValue().getWavelengthPacket();
			Color white = colorModel.getWhite(lambda);
			LightNode emit = nodes.sampleLight(white, Random.DEFAULT);
			return (emit != null) ? PathUtil.join(emit, node) : null;
		}

		/**
		 * Serialization version ID.
		 */
		private static final long serialVersionUID = 8318742231359439076L;

	}

	/** The <code>Display</code> to render the image to. */
	private final Display display;

	/** The <code>ColorModel</code> to use to render this image. */
	private final ColorModel colorModel;

	/** The width of the image to render, in pixels. */
	private final int width;

	/** The height of the image to render, in pixels. */
	private final int height;

	/** The number of columns to divide the <code>Raster</code> image into. */
	private final int cols;

	/** The number of rows to divide the <code>Raster</code> image into. */
	private final int rows;

	/** The column index of the next task to return. */
	private transient int nextCol = 0;

	/** The row index of the next task to return. */
	private transient int nextRow = 0;

	/** The number of tasks that have been completed. */
	private transient int tasksComplete = 0;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 9173731839475893020L;

}
