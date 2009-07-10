/*
 * Copyright (c) 2008 Bradley W. Kimmel
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

package ca.eandb.jmist.framework.job;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

import ca.eandb.jdcp.job.AbstractParallelizableJob;
import ca.eandb.jdcp.job.TaskWorker;
import ca.eandb.jmist.framework.Emitter;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.NearestIntersectionRecorder;
import ca.eandb.jmist.framework.Photon;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.ScatteredRays;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.shader.MinimalShadingContext;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.progress.ProgressMonitor;

/**
 * @author brad
 *
 */
public final class LightTracingJob extends AbstractParallelizableJob implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 159143623050840832L;

	private final Scene scene;

	private final ColorModel colorModel;

	private final Random random;

	private final String formatName;

	private transient Raster raster;

	private final int photons;

	private final int tasks;

	private final int minPhotonsPerTask;

	private final int extraPhotons;

	private transient int tasksProvided = 0;

	private transient int tasksSubmitted = 0;

	private final SampleModel imageSampleModel;

	private final java.awt.image.ColorModel imageColorModel;

	public LightTracingJob(Scene scene, SampleModel imageSampleModel, java.awt.image.ColorModel imageColorModel, String formatName, ColorModel colorModel, Random random, int photons, int tasks) {
		if (!imageColorModel.isCompatibleSampleModel(imageSampleModel)) {
			throw new IllegalArgumentException("imageColorModel incompatible with imageSampleModel");
		}

		this.scene = scene;
		this.colorModel = colorModel;
		this.imageSampleModel = imageSampleModel;
		this.imageColorModel = imageColorModel;
		this.formatName = formatName;
		this.random = random;
		this.photons = photons;
		this.tasks = tasks;
		this.minPhotonsPerTask = photons / tasks;
		this.extraPhotons = photons - minPhotonsPerTask;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.ParallelizableJob#getNextTask()
	 */
	@Override
	public Object getNextTask() throws Exception {
		if (tasksProvided < tasks) {
			return tasksProvided++ < extraPhotons ? minPhotonsPerTask + 1
					: minPhotonsPerTask;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.ParallelizableJob#isComplete()
	 */
	@Override
	public boolean isComplete() throws Exception {
		return tasksSubmitted == tasks;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.AbstractParallelizableJob#initialize()
	 */
	@Override
	public void initialize() throws Exception {
		raster = colorModel.createRaster(imageSampleModel.getWidth(), imageSampleModel.getHeight());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.ParallelizableJob#submitTaskResults(java.lang.Object, java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
	 */
	@Override
	public void submitTaskResults(Object task, Object results,
			ProgressMonitor monitor) throws Exception {

		Raster taskRaster = (Raster) results;

		int height = imageSampleModel.getHeight();
		int width = imageSampleModel.getWidth();

		monitor.notifyStatusChanged("Accumulating partial results...");

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				raster.setPixel(x, y, raster.getPixel(x, y).plus(taskRaster.getPixel(x, y)));
			}
		}

		monitor.notifyProgress(++tasksSubmitted, tasks);
		if (tasksSubmitted == tasks) {
			monitor.notifyStatusChanged("Ready to write results");
		} else {
			monitor.notifyStatusChanged("Waiting for partial results");
		}

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.AbstractParallelizableJob#finish()
	 */
	@Override
	public void finish() throws Exception {
		Point origin = new Point(0, 0);
		WritableRaster imageRaster = java.awt.image.Raster.createWritableRaster(imageSampleModel, origin);
		BufferedImage image = new BufferedImage(imageColorModel, imageRaster, false, null);

		int width = imageSampleModel.getWidth();
		int height = imageSampleModel.getHeight();
		int bands = colorModel.getNumChannels();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color color = raster.getPixel(x, y).divide(photons);
				for (int b = 0; b < bands; b++) {
					imageRaster.setSample(x, y, b, color.getValue(b));
				}
			}
		}


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
	 * @see ca.eandb.jdcp.job.ParallelizableJob#worker()
	 */
	@Override
	public TaskWorker worker() throws Exception {
		return new Worker();
	}

	private final class Worker implements TaskWorker {

		/**
		 *
		 */
		private static final long serialVersionUID = -7848301189373426210L;

		private transient Light light;

		private transient Sphere target;

		private transient SceneElement root;

		private transient Lens lens;

		private synchronized void ensureInitialized() {
			if (root == null) {
				light = scene.getLight();
				target = scene.boundingSphere();
				root = scene.getRoot();
				lens = scene.getLens();
			}
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jdcp.job.TaskWorker#performTask(java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
		 */
		@Override
		public Object performTask(Object task, ProgressMonitor monitor) {

			int photons = (Integer) task;
			int width = imageSampleModel.getWidth();
			int height = imageSampleModel.getHeight();
			Raster raster = colorModel.createRaster(width, height);

			ensureInitialized();

			for (int i = 0; i < photons; i++) {
				if (i % 10000 == 0) {
					if (!monitor.notifyProgress(i, photons)) {
						monitor.notifyCancelled();
						return null;
					}
				}

				Color sample = colorModel.sample(random);
				WavelengthPacket lambda = sample.getWavelengthPacket();
				Emitter emitter = light.sample(random);

				Photon photon = emitter.emit(target, lambda, random);
				Ray3 ray = photon.ray();

				sample = sample.times(photon.power());

				{
					Point3 p = photon.position();
					Lens.Projection proj = lens.project(p);
					if (proj != null) {
						Point3 q = proj.pointOnLens();
						if (root.visibility(p, q)) {
							Vector3 out = p.unitVectorTo(q);
							Point2 ndc = proj.pointOnImagePlane();
							Color color = emitter.getEmittedRadiance(out, lambda);
							double atten = proj.importance();
							Color result = color.times(sample).times(atten*0.5);

							int rx = (int) Math.floor(ndc.x() * raster.getWidth());
							int ry = (int) Math.floor(ndc.y() * raster.getHeight());

							raster.setPixel(rx, ry, raster.getPixel(rx, ry).plus(result));
						}
					}

				}

				sample = sample.times(0.5);

				while (true) {
					Vector3 v = ray.direction();
					Intersection x = NearestIntersectionRecorder.computeNearestIntersection(ray, root);

					if (x == null) {
						break;
					}
					ShadingContext context = new MinimalShadingContext(random);
					x.prepareShadingContext(context);
					context.getModifier().modify(context);

					Material material = context.getMaterial();

					/* trace to camera. */
					Point3 p = context.getPosition();
					Lens.Projection proj = lens.project(p);
					if (proj != null) {
						Point3 q = proj.pointOnLens();
						if (root.visibility(p, q)) {
							Vector3 out = p.unitVectorTo(q);
							Point2 ndc = proj.pointOnImagePlane();
							Color bsdf = material.scattering(context, v, out, lambda);
							double atten = proj.importance() * Math.abs(out.dot(context.getShadingNormal()));
							Color result = bsdf.times(sample).times(atten*0.5);

							int rx = (int) Math.floor(ndc.x() * raster.getWidth());
							int ry = (int) Math.floor(ndc.y() * raster.getHeight());

							raster.setPixel(rx, ry, raster.getPixel(rx, ry).plus(result));
						}
					}


					/* trace scattered ray. */
					ScatteredRays scat = new ScatteredRays(context, v, lambda, random, material);
					ScatteredRay sr = scat.getRandomScatteredRay(true);

					if (sr == null) {
						break;
					}

					ray = sr.getRay();
					sample = sample.times(sr.getColor()).times(0.5);
				}

			}

			monitor.notifyProgress(photons, photons);
			monitor.notifyComplete();

			return raster;

		}

	}

}
