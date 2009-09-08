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

package ca.eandb.jmist.framework.path;

import ca.eandb.jdcp.job.AbstractParallelizableJob;
import ca.eandb.jdcp.job.TaskWorker;
import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.util.progress.ProgressMonitor;

/**
 * @author brad
 *
 */
public final class BidiPathTracerJob extends AbstractParallelizableJob {

	/** Serialization version ID. */
	private static final long serialVersionUID = -6940841062797196504L;

	private final Scene scene;

	private final ColorModel colorModel;

	private final Random random;

	private transient Raster raster;

	private final Display display;

	private final BidiPathStrategy strategy;

	private final BidiPathMeasure measure;

	private final int tasks;

	private final int width;

	private final int height;

	private final int eyePathsPerPixel;

	private final int lightPathsPerEyePath;

	private final int minPassesPerTask;

	private final int extraPasses;

	private final boolean displayPartialResults;

	private transient int tasksProvided = 0;

	private transient int tasksSubmitted = 0;

	private transient int passesSubmitted = 0;

	public BidiPathTracerJob(Scene scene, Display display, int width,
			int height, ColorModel colorModel, Random random,
			BidiPathStrategy strategy, BidiPathMeasure measure, int eyePathsPerPixel,
			int lightPathsPerEyePath, int tasks, boolean displayPartialResults) {
		this.scene = scene;
		this.display = display;
		this.colorModel = colorModel;
		this.random = random;
		this.tasks = tasks;
		this.width = width;
		this.height = height;
		this.strategy = strategy;
		this.measure = measure;
		this.eyePathsPerPixel = eyePathsPerPixel;
		this.lightPathsPerEyePath = lightPathsPerEyePath;
		this.minPassesPerTask = eyePathsPerPixel / tasks;
		this.extraPasses = eyePathsPerPixel - minPassesPerTask * tasks;
		this.displayPartialResults = displayPartialResults;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.ParallelizableJob#getNextTask()
	 */
	public synchronized Object getNextTask() throws Exception {
		if (tasksProvided < tasks) {
			return tasksProvided++ < extraPasses ? minPassesPerTask + 1
					: minPassesPerTask;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.ParallelizableJob#isComplete()
	 */
	public boolean isComplete() throws Exception {
		return tasksSubmitted == tasks;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.ParallelizableJob#submitTaskResults(java.lang.Object, java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
	 */
	public synchronized void submitTaskResults(Object task, Object results,
			ProgressMonitor monitor) throws Exception {

		int taskPasses = (Integer) task;
		Raster taskRaster = (Raster) results;

		monitor.notifyStatusChanged("Accumulating partial results...");

		passesSubmitted += taskPasses;
		if (displayPartialResults) {
			double alpha = (double) taskPasses / (double) passesSubmitted;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					raster.setPixel(x, y, raster.getPixel(x, y).times(
							1.0 - alpha).plus(
							taskRaster.getPixel(x, y).times(alpha)));
				}
			}
			display.setPixels(0, 0, raster);
		} else {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					raster.setPixel(x, y, raster.getPixel(x, y).plus(taskRaster.getPixel(x, y)));
				}
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
	 * @see ca.eandb.jdcp.job.AbstractParallelizableJob#initialize()
	 */
	@Override
	public void initialize() throws Exception {
		raster = colorModel.createRaster(width, height);
		if (displayPartialResults) {
			display.initialize(width, height, colorModel);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.AbstractParallelizableJob#finish()
	 */
	@Override
	public void finish() throws Exception {
		if (!displayPartialResults) {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					raster.setPixel(x, y, raster.getPixel(x, y).divide(eyePathsPerPixel));
				}
			}
			display.initialize(width, height, colorModel);
			display.setPixels(0, 0, raster);
		}
		display.finish();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.ParallelizableJob#worker()
	 */
	public TaskWorker worker() throws Exception {
		return new Worker();
	}

	private final class Worker implements TaskWorker {

		/** Serialization version ID. */
		private static final long serialVersionUID = -7848301189373426210L;

		private transient ThreadLocal<Raster> raster = new ThreadLocal<Raster>() {
			protected Raster initialValue() {
				return colorModel.createRaster(width, height);
			}
		};

		/* (non-Javadoc)
		 * @see ca.eandb.jdcp.job.TaskWorker#performTask(java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
		 */
		public Object performTask(Object task, ProgressMonitor monitor) {

			int		passes				= (Integer) task;
			Box2	bounds;
			double	x0, y0, x1, y1;
			double	w					= width;
			double	h					= height;
			int		numPixels			= width * height;
			int		samplesPerPixel		= passes * lightPathsPerEyePath;
			double	lightImageWeight	= 1.0 / (double) samplesPerPixel;
			Light	light				= scene.getLight();
			Lens	lens				= scene.getLens();

			raster.get().clear();

			for (int n = 0, y = 0; y < height; y++) {

				if (!monitor.notifyProgress(n, numPixels))
					return null;

				y0			= (double) y / h;
				y1			= (double) (y + 1) / h;

				for (int x = 0; x < width; x++, n++) {

					x0			= (double) x / w;
					x1			= (double) (x + 1) / w;

					bounds		= new Box2(x0, y0, x1, y1);

					for (int i = 0; i < passes; i++) {

						Point2 p			= RandomUtil.uniform(bounds, random);
						Color sample		= colorModel.sample(random);
						PathInfo path		= new PathInfo(scene, sample.getWavelengthPacket());
						PathNode eyeTail	= strategy.traceEyePath(lens, p,
													path, random);

						for (int j = 0; j < lightPathsPerEyePath; j++) {

							PathNode lightTail	= strategy.traceLightPath(
														light, path, random);

							Color score			= join(lightTail, eyeTail,
														lightImageWeight);
							if (score != null) {
								raster.get().addPixel(x, y,
										score.divide(samplesPerPixel));
							}

						}

					}

				}

			}

			monitor.notifyProgress(numPixels, numPixels);
			monitor.notifyComplete();

			return raster.get();

		}

		private void write(Point2 p, Color c) {
			Raster raster = this.raster.get();
			int w = raster.getWidth();
			int h = raster.getHeight();
			int x = MathUtil.threshold((int) Math.floor(p.x() * w), 0, w - 1);
			int y = MathUtil.threshold((int) Math.floor(p.y() * h), 0, h - 1);
			raster.addPixel(x, y, c);
		}
//
//		private void joinLightPathToEye(EyeNode eye, PathNode tail, double weight) {
//			PathNode node = tail;
//			while (node != null) {
//				double w = strategy.getWeight(node, eye);
//				if (!MathUtil.isZero(w)) {
//					Color c = PathUtil.join(eye, node);
//					if (c != null) {
//						Point2 p = eye.project(node.getPosition());
//						if (p != null) {
//							if (node instanceof LightNode) {
//								write(p, c.times(w));
//							} else {
//								write(p, c.times(weight * w));
//							}
//						}
//					}
//				}
//				node = node.getParent();
//			}
//		}
//
//		private Color joinInnerToInner(PathNode eyeNode, PathNode lightNode) {
//			assert(eyeNode.getDepth() > 0 && lightNode.getDepth() > 0);
//			double w = strategy.getWeight(lightNode, eyeNode);
//			if (!MathUtil.isZero(w)) {
//				Color c = PathUtil.join(eyeNode, lightNode);
//				return ColorUtil.mul(c, w);
//			} else {
//				return null;
//			}
//		}
//
//		private Color joinInnerToLight(ScatteringNode eyeNode, LightNode light) {
//			return joinInnerToInner(eyeNode, light);
//		}

		private Color join(PathNode lightTail, PathNode eyeTail, double lightImageWeight) {
			Color score = null;

			PathNode lightNode = lightTail;
			while (true) {

				PathNode eyeNode = eyeTail;
				while (true) {

					Color c = joinAt(lightNode, eyeNode, lightImageWeight);
					score = ColorUtil.add(score, c);

					if (eyeNode == null) {
						break;
					}
					eyeNode = eyeNode.getParent();
				} // eye path loop

				if (lightNode == null) {
					break;
				}
				lightNode = lightNode.getParent();
			} // light path loop

			return score;
		}

		private Color joinAt(PathNode lightNode, PathNode eyeNode, double lightImageWeight) {
			int l = lightNode != null ? lightNode.getDepth() : -1;
			int e = eyeNode != null ? eyeNode.getDepth() : -1;

			if (e == 0 && l == 0) {
				return joinLightToEye((LightNode) lightNode, (EyeNode) eyeNode,
						lightImageWeight);
			} else if (e <= 0 && l <= 0) {
				return null;
			} else if (e < 0) {
				return lightPathOnCamera((ScatteringNode) lightNode,
						lightImageWeight);
			} else if (l < 0) {
				return eyePathOnLight((ScatteringNode) eyeNode);
			} else if (e == 0) {
				return joinInnerToEye(lightNode, (EyeNode) eyeNode,
						lightImageWeight);
			} else if (l == 0) {
				return joinLightToInner((LightNode) lightNode, eyeNode);
			} else {
				return joinInnerToInner((ScatteringNode) lightNode, eyeNode);
			}
		}

		private Color joinInnerToInner(PathNode lightNode, PathNode eyeNode) {
			double w = strategy.getWeight(lightNode, eyeNode);
			if (w > 0.0) {//MathUtil.EPSILON) {
				Color c = measure.evaluate(lightNode, eyeNode);
				return ColorUtil.mul(c, w);
			} else {
				return null;
			}
		}

		private Color joinLightToInner(LightNode lightNode, PathNode eyeNode) {
			// TODO Ignore given light node and select a better light node
			// for the the node on the eye path we want to illuminate.
			return joinInnerToInner(lightNode, eyeNode);
		}

		private Color joinInnerToEye(PathNode lightNode, EyeNode eyeNode,
				double weight) {
			double w = strategy.getWeight(lightNode, eyeNode);
			if (w > 0.0) {//MathUtil.EPSILON) {
				Point2 p = eyeNode.project(lightNode.getPosition());
				if (p != null) {
					Color c = measure.evaluate(lightNode, eyeNode);
					c = ColorUtil.mul(c, weight * w);
					if (c != null) {
						write(p, c);
					}
				}
			}
			return null;
		}

		private Color eyePathOnLight(ScatteringNode eyeNode) {
			if (!eyeNode.isOnLightSource()) {
				return null;
			}

			double w = strategy.getWeight(null, eyeNode);
			if (w > 0.0) {// MathUtil.EPSILON) {
				Color c = measure.evaluate(null, eyeNode);
				return ColorUtil.mul(c, w);
			} else {
				return null;
			}
		}

		private Color lightPathOnCamera(ScatteringNode lightNode,
				double weight) {
			// This cannot happen because the aperture is not part of the scene
			return null;
		}

		private Color joinLightToEye(LightNode lightNode, EyeNode eyeNode,
				double weight) {
			return joinInnerToEye(lightNode, eyeNode, weight);
		}



	}

}
