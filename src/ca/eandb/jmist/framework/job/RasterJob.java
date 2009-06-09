/**
 *
 */
package ca.eandb.jmist.framework.job;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferDouble;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.Serializable;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

import ca.eandb.jdcp.job.AbstractParallelizableJob;
import ca.eandb.jdcp.job.TaskWorker;
import ca.eandb.jmist.framework.PixelShader;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Box2;
import ca.eandb.util.io.Archive;
import ca.eandb.util.progress.ProgressMonitor;

/**
 * A <code>ParallelizableJob</code> that renders a <code>Raster</code> image.
 * @author Brad Kimmel
 */
public final class RasterJob extends AbstractParallelizableJob implements
		Serializable {

	/**
	 * Creates a new <code>RasterJob</code>.  This job will
	 * divide the image into <code>rows * cols</code> tasks to render roughly
	 * equally sized chunks of the image.
	 * @param shader The <code>PixelShader</code> to use to compute the values
	 * 		of individual <code>Pixel</code>s.
	 * @param colorModel The <code>ColorModel</code> that describes the color
	 * 		representation for the rendered image.
	 * @param sampleModel The <code>SampleModel</code> that describes the
	 * 		dimensions of the image to be rendered.
	 * @param formatName The name of the format to save the image as.
	 * @param cols The number of columns to divide the image into.
	 * @param rows The number of rows to divide the image into.
	 */
	public RasterJob(PixelShader shader, ColorModel colorModel, SampleModel sampleModel, String formatName, int cols, int rows) {
		this.worker = new RasterTaskWorker(shader, sampleModel.getWidth(), sampleModel.getHeight());
		this.colorModel = colorModel;
		this.sampleModel = sampleModel;
		this.cols = cols;
		this.rows = rows;
		this.formatName = formatName;

		if (!colorModel.isCompatibleSampleModel(sampleModel)) {
			throw new IllegalArgumentException("Incompatible SampleModel");
		}
	}

	/**
	 * Creates a new <code>RasterJob</code>.  This job will
	 * divide the image into <code>rows * cols</code> tasks to render roughly
	 * equally sized chunks of the image.
	 * @param shader The <code>PixelShader</code> to use to compute the values
	 * 		of individual <code>Pixel</code>s.
	 * @param colorModel The <code>ColorModel</code> that describes the color
	 * 		representation for the rendered image.
	 * @param formatName The name of the format to save the image as.
	 * @param width The width of the rendered image, in pixels.
	 * @param height The height of the rendered image, in pixels.
	 * @param cols The number of columns to divide the image into.
	 * @param rows The number of rows to divide the image into.
	 */
	public RasterJob(PixelShader shader, ColorModel colorModel, String formatName, int width, int height, int cols, int rows) {
		this(shader, colorModel, colorModel.createCompatibleSampleModel(width, height), formatName, cols, rows);
	}

	/**
	 * Creates a new <code>RasterJob</code>.  This job will
	 * divide the image into <code>rows * cols</code> tasks to render roughly
	 * equally sized chunks of the image.
	 * @param shader The <code>PixelShader</code> to use to compute the values
	 * 		of individual <code>Pixel</code>s.
	 * @param formatName The name of the format to save the image as.
	 * @param width The width of the rendered image, in pixels.
	 * @param height The height of the rendered image, in pixels.
	 * @param cols The number of columns to divide the image into.
	 * @param rows The number of rows to divide the image into.
	 */
	public RasterJob(PixelShader shader, String formatName, int width, int height, int cols, int rows) {
		this(shader, new DirectColorModel(24, 0x00ff0000, 0x0000ff00, 0x000000ff), formatName, width, height, cols, rows);
	}

	/**
	 * Creates a new <code>RasterJob</code>.  This job will
	 * divide the image into <code>rows * cols</code> tasks to render roughly
	 * equally sized chunks of the image.
	 * @param shader The <code>PixelShader</code> to use to compute the values
	 * 		of individual <code>Pixel</code>s.
	 * @param width The width of the rendered image, in pixels.
	 * @param height The height of the rendered image, in pixels.
	 * @param cols The number of columns to divide the image into.
	 * @param rows The number of rows to divide the image into.
	 */
	public RasterJob(PixelShader shader, int width, int height, int cols, int rows) {
		this(shader, "png", width, height, cols, rows);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.AbstractParallelizableJob#initialize()
	 */
	@Override
	public void initialize() throws IOException {
		Point origin = new Point(0, 0);
		raster = Raster.createWritableRaster(sampleModel, origin);
		image = new BufferedImage(colorModel, raster, false, null);
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
			Rectangle cell = this.getCell(this.nextCol++, this.nextRow);

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
	 * Gets the bounds of the cell at the specified row and column.
	 * @param col The column index.
	 * @param row The row index.
	 * @return The cell bounds.
	 */
	private Rectangle getCell(int col, int row) {

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

		int w = this.image.getWidth();
		int h = this.image.getHeight();

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

		return new Rectangle(xmin, ymin, xmax - xmin + 1, ymax - ymin + 1);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ParallelizableJob#submitTaskResults(java.lang.Object, java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
	 */
	public void submitTaskResults(Object task, Object results, ProgressMonitor monitor) {

		Rectangle	cell	= (Rectangle) task;
		double[]	pixels	= (double[]) results;

		/* Write the submitted results to the raster. */
		this.raster.setPixels(cell.x, cell.y, cell.width, cell.height, pixels);

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

		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(this.formatName);
		ImageWriter writer = writers.next();
		String[] suffix = writer.getOriginatingProvider().getFileSuffixes();

		assert(suffix != null && suffix.length > 0);

		FileOutputStream fs = createFileOutputStream("raster." + suffix[0]);

		ImageIO.write(image, formatName, fs);

		fs.flush();
		fs.close();

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
		return this.worker;
	}

	/**
	 * A <code>TaskWorker</code> that renders a rectangular subset of a
	 * <code>Raster</code> image.
	 * @author Brad Kimmel
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
		 * @see ca.eandb.jmist.framework.TaskWorker#performTask(java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
		 */
		public Object performTask(Object task, ProgressMonitor monitor) {

			Rectangle		cell				= (Rectangle) task;
			ca.eandb.jmist.framework.color.ColorModel		cm					= ca.eandb.jmist.framework.color.ColorModel.getInstance();
			int				numPixels			= cell.width * cell.height;
			double[]		pixels				= new double[numPixels * cm.getNumChannels()];
			Color			pixel;
			Box2			bounds;
			double			x0, y0, x1, y1;
			double			w					= this.width;
			double			h					= this.height;
			WritableRaster	image				= createCellWritableRaster(cell, cm.getNumChannels(), pixels);

			for (int n = 0, y = cell.y; y < cell.y + cell.height; y++) {

				if (!monitor.notifyProgress(n, numPixels))
					return null;

				y0			= (double) y / h;
				y1			= (double) (y + 1) / h;

				for (int x = cell.x; x < cell.x + cell.width; x++, n++) {

					x0		= (double) x / w;
					x1		= (double) (x + 1) / w;

					bounds	= new Box2(x0, y0, x1, y1);

					pixel = shader.shadePixel(bounds);
					image.setPixel(x, y, pixel.toArray());

				}

			}

			monitor.notifyProgress(pixels.length, pixels.length);
			monitor.notifyComplete();

			return pixels;

		}

		/**
		 * Creates a <code>WritableRaster</code> for writing to a cell.
		 * @param cell The <code>Rectangle</code> describing the bounds of the
		 * 		cell for which to create the <code>WritableRaster</code>.
		 * @param bands The number of bands in the <code>WritableRaster</code>.
		 * @param buffer The array of <code>double</code>s to hold the pixel
		 * 		data.
		 * @return The new <code>WritableRaster</code>.
		 */
		private static WritableRaster createCellWritableRaster(Rectangle cell, int bands, double[] buffer) {

			int[] offsets = new int[bands];
			for (int i = 0; i < bands; i++) {
				offsets[i] = i;
			}

			SampleModel		sm		= new PixelInterleavedSampleModel(DataBuffer.TYPE_DOUBLE, cell.width, cell.height, bands, cell.width * bands, offsets);
			DataBuffer		db		= new DataBufferDouble(buffer, buffer.length);

			return Raster.createWritableRaster(sm, db, cell.getLocation());

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

	/** The name of the format in which to render the image. */
	private final String formatName;

	/**
	 * The <code>TaskWorker</code> to use to render rectangular subsets of
	 * the <code>Raster</code> image.
	 */
	private final TaskWorker worker;

	/**
	 * The <code>SampleModel</code> that describes the dimensions of the image
	 * to be rendered.
	 */
	private SampleModel sampleModel;

	/**
	 * The <code>ColorModel</code> used by the image to be rendered.
	 */
	private ColorModel colorModel;

	/** The number of columns to divide the <code>Raster</code> image into. */
	private final int cols;

	/** The number of rows to divide the <code>Raster</code> image into. */
	private final int rows;

	/** The <code>WritableRaster</code> for the image to write to. */
	private transient WritableRaster raster;

	/** The <code>BufferedImage</code> to write to. */
	private transient BufferedImage image;

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
