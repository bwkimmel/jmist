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
package ca.eandb.jmist.framework.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ca.eandb.jdcp.job.AbstractParallelizableJob;
import ca.eandb.jdcp.job.TaskWorker;
import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.RasterUtil;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorMeasure;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.color.measure.LuminanceColorMeasure;
import ca.eandb.jmist.framework.job.bidi.BidiPathStrategy;
import ca.eandb.jmist.framework.job.bidi.MeasurementContributionMeasure;
import ca.eandb.jmist.framework.job.bidi.MultipleImportanceSamplingStrategy;
import ca.eandb.jmist.framework.job.bidi.PathMeasure;
import ca.eandb.jmist.framework.job.mlt.PathMutator;
import ca.eandb.jmist.framework.path.Path;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.path.PathNode;
import ca.eandb.jmist.framework.random.RandomAdapter;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.framework.random.SimpleRandom;
import ca.eandb.jmist.framework.random.ThreadLocalRandom;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.util.DoubleArray;
import ca.eandb.util.UnexpectedException;
import ca.eandb.util.progress.ProgressMonitor;

/**
 * A <code>ParallelizableJob</code> that implements the Metropolis Light
 * Transport algorithm.
 * @author Brad Kimmel
 */
public final class MetropolisLightTransportJob extends
    AbstractParallelizableJob {

  /** Serialization version ID. */
  private static final long serialVersionUID = 93596290493205783L;

  /** The <code>Scene</code> to be rendered. */
  private final Scene scene;

  /** The <code>Display</code> to render to. */
  private final Display display;

  /** The <code>ColorModel</code> to use. */
  private final ColorModel colorModel;

  /** The <code>PathMutator</code> to apply. */
  private final PathMutator mutator;

  /** The <code>PathMeasure</code> to apply. */
  private final PathMeasure pathMeasure = MeasurementContributionMeasure.getInstance();

  /** The <code>ColorMeasure</code> to apply. */
  private final ColorMeasure colorMeasure = LuminanceColorMeasure.getInstance();

  /** The <code>BidiPathStrategy</code> to use to generate initial paths. */
  private final BidiPathStrategy strategy = MultipleImportanceSamplingStrategy.usePowerHeuristic(10, 10);

  /** The <code>Random</code> number generator to use for mutations. */
  private final Random random = new SimpleRandom();

  /** The width of the image to render, in pixels. */
  private final int width;

  /** The height of the image to render, in pixels. */
  private final int height;

  /** The number of mutations to apply to each initial path. */
  private final int mutationsPerSeed;

  /** The number of initial paths to generate. */
  private final int numberOfSeeds;

  /**
   * The number of tasks (seeding tasks) to divide the work of generating
   * initial paths into.
   */
  private final int seedTasks;

  /**
   * The number of light path/eye path pairs that each seeding task should
   * generate before resampling to select the initial paths.
   */
  private final int pairsPerSeedTask;

  /**
   * The minimum number of initial paths that each seeding task should
   * generate.
   */
  private final int minSeedsPerSeedTask;

  /**
   * The number of seeding tasks that should generate one more than the
   * minimum number of initial paths, so as to arrive at the appropriate
   * number ({@link #numberOfSeeds}) of initial paths.
   */
  private final int extraSeeds;

  /**
   * The next seed that should be supplied to the random number generator to
   * generate the next sequence of light path/eye path pairs.
   */
  private long nextRandomSeed = 0;

  /**
   * The number of seeding tasks that have been returned by
   * {@link #getNextTask()}.
   */
  private int seedTasksProvided;

  /**
   * The number of Metropolis Light Transport task whose results have been
   * submitted.
   */
  private int mltTasksSubmitted;

  /**
   * A value indicating if partial results should be written to the display
   * as the results of each Metropolis task is submitted.
   */
  private final boolean displayPartialResults;

  /** The <code>Raster</code> to write to as task results are submitted. */
  private transient Raster image = null;

  /**
   * The total number of tasks (seeding tasks and MLT tasks) that whose
   * results have been submitted.
   */
  private transient int tasksSubmitted = 0;

  /**
   * A <code>Queue</code> containing the <code>PathSeed</code>s returned by
   * seeding tasks that have not yet been supplied as Metropolis Light
   * Transport tasks via {@link #getNextTask()}.
   */
  private final Queue<PathSeed> seeds = new LinkedList<PathSeed>();

  public MetropolisLightTransportJob(Scene scene, Display display,
      ColorModel colorModel, PathMutator mutator, int width, int height,
      int mutationsPerSeed, int numberOfSeeds, int seedTasks,
      int pairsPerSeedTask, boolean displayPartialResults) {
    this.scene = scene;
    this.display = display;
    this.colorModel = colorModel;
    this.mutator = mutator;
    this.width = width;
    this.height = height;
    this.mutationsPerSeed = mutationsPerSeed;
    this.numberOfSeeds = numberOfSeeds;
    this.seedTasks = seedTasks;
    this.pairsPerSeedTask = pairsPerSeedTask;
    this.displayPartialResults = displayPartialResults;

    this.minSeedsPerSeedTask = numberOfSeeds / seedTasks;
    this.extraSeeds = numberOfSeeds % seedTasks;
  }

  private final PathNode generateLightPath(Random rnd, WavelengthPacket lambda) {
    Light light = scene.getLight();
    PathInfo pathInfo = new PathInfo(scene, lambda);
    return strategy.traceLightPath(light, pathInfo, rnd);
  }

  private final PathNode generateEyePath(Random rnd, WavelengthPacket lambda) {
    Lens lens = scene.getLens();
    Point2 p = RandomUtil.canonical2(rnd);
    PathInfo pathInfo = new PathInfo(scene, lambda);
    return strategy.traceEyePath(lens, p, pathInfo, rnd);
  }

  private static final class PathSeed implements Serializable {
    private static final long serialVersionUID = 2446876045946528984L;
    public long randomSeed;
    public int lightPathLength;
    public int eyePathLength;
  };

  private static final class SeedTaskInfo implements Serializable {
    private static final long serialVersionUID = 7253264352598221713L;
    public long initialRandomSeed;
    public int numPathSeeds;
  };

  private final Path generatePath(long seed) {
    Random rnd = new RandomAdapter(new java.util.Random(seed));
    Color sample = colorModel.sample(rnd);
    WavelengthPacket lambda = sample.getWavelengthPacket();
    PathNode lightTail = generateLightPath(rnd, lambda);
    PathNode eyeTail = generateEyePath(rnd, lambda);

    if (lightTail != null && lightTail.getDepth() > 0 && lightTail.isAtInfinity()) {
      lightTail = lightTail.getParent();
    }
    if (eyeTail != null && eyeTail.getDepth() > 0 && eyeTail.isAtInfinity()) {
      eyeTail = eyeTail.getParent();
    }
    return new Path(lightTail, eyeTail);
  }


  private final Path generatePath(PathSeed seed) {
    Path path = generatePath(seed.randomSeed);
    return path.slice(seed.lightPathLength, seed.eyePathLength);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jdcp.job.ParallelizableJob#getNextTask()
   */
  public synchronized Object getNextTask() throws Exception {
    if (seedTasksProvided < seedTasks) {
      SeedTaskInfo info = new SeedTaskInfo();
      info.initialRandomSeed = nextRandomSeed;
      info.numPathSeeds = (seedTasksProvided < extraSeeds)
          ? minSeedsPerSeedTask + 1
          : minSeedsPerSeedTask;
      seedTasksProvided++;
      nextRandomSeed += (long) pairsPerSeedTask;
      return info;
    } else {
      return seeds.poll();
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jdcp.job.ParallelizableJob#isComplete()
   */
  public boolean isComplete() throws Exception {
    return tasksSubmitted >= (numberOfSeeds + seedTasks);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jdcp.job.ParallelizableJob#submitTaskResults(java.lang.Object, java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
   */
  @SuppressWarnings("unchecked")
  public void submitTaskResults(Object task, Object results,
      ProgressMonitor monitor) throws Exception {

    if (task instanceof PathSeed) {
      submitTaskResults_MLT((Raster) results);
    } else if (task instanceof SeedTaskInfo) {
      submitTaskResults_generateSeeds((Collection<PathSeed>) results);
    } else {
      throw new IllegalArgumentException("Unrecognized task type");
    }

    monitor.notifyProgress(++tasksSubmitted, seedTasks + numberOfSeeds);
  }

  private void submitTaskResults_MLT(Raster results) {
    mltTasksSubmitted++;
    if (displayPartialResults) {
      double alpha = 1.0 / (double) mltTasksSubmitted;
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          image.setPixel(x, y, image.getPixel(x, y).times(
              1.0 - alpha).plus(
              results.getPixel(x, y).times(alpha)));
        }
      }
      display.setPixels(0, 0, image);
    } else {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          image.addPixel(x, y, results.getPixel(x, y));
        }
      }
    }
  }

  private void submitTaskResults_generateSeeds(Collection<PathSeed> results) {
      seeds.addAll(results);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jdcp.job.AbstractParallelizableJob#finish()
   */
  @Override
  public void finish() throws Exception {
    if (!displayPartialResults) {
      display.initialize(width, height, colorModel);
      display.setPixels(0, 0, image);
    }

    display.finish();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jdcp.job.AbstractParallelizableJob#initialize()
   */
  @Override
  public void initialize() throws Exception {
    image = colorModel.createRaster(width, height);
    if (displayPartialResults) {
      display.initialize(width, height, colorModel);
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jdcp.job.ParallelizableJob#worker()
   */
  public TaskWorker worker() throws Exception {
    return new Worker();
  }

  private final class Worker implements TaskWorker {

    /** Serialization version ID. */
    private static final long serialVersionUID = 2227396964245126946L;

    private final ThreadLocal<Raster> raster = new ThreadLocal<Raster>() {
      protected Raster initialValue() {
        return colorModel.createRaster(width, height);
      }
    };

    private final Random random = new ThreadLocalRandom(
        MetropolisLightTransportJob.this.random);

    public Object performTask(Object task, ProgressMonitor monitor) {
      assert(task != null);
      if (task instanceof PathSeed) {
        try {
          return performTask_MLT((PathSeed) task, mutationsPerSeed,
              monitor);
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
      } else if (task instanceof SeedTaskInfo) {
        SeedTaskInfo info = (SeedTaskInfo) task;
        return performTask_generateSeeds(info.initialRandomSeed,
            info.numPathSeeds, monitor);
      } else {
        throw new IllegalArgumentException("Unrecognized task type");
      }
    }

    private Object performTask_generateSeeds(long initialRandomSeed,
        int numPathSeeds, ProgressMonitor monitor) {

      monitor.notifyStatusChanged("Generating seeds for MLT");

      int callbackInterval = Math.min(1000,
          Math.max(1, pairsPerSeedTask / 100));
      int nextCallback = 0;

      DoubleArray weight = new DoubleArray();
      short[] lightPathLength = new short[pairsPerSeedTask];
      short[] eyePathLength = new short[pairsPerSeedTask];

      long randomSeed = initialRandomSeed;
      for (int i = 0; i < pairsPerSeedTask; i++) {
        if (--nextCallback <= 0) {
          if (!monitor.notifyProgress(i, pairsPerSeedTask)) {
            monitor.notifyCancelled();
            return null;
          }
          nextCallback = callbackInterval;
        }

        Path path = generatePath(randomSeed++);

        /* store information about the path so we don't have to
         * regenerate it during the resampling phase.
         */
        if (path.getLightPathLength() > Short.MAX_VALUE) {
          throw new UnexpectedException("Light subpath too long.");
        }
        if (path.getEyePathLength() > Short.MAX_VALUE) {
          throw new UnexpectedException("Eye subpath too long.");
        }
        lightPathLength[i] = (short) path.getLightPathLength();
        eyePathLength[i] = (short) path.getEyePathLength();

        join(path.getLightTail(), path.getEyeTail(), weight);
      }

      monitor.notifyStatusChanged("Resampling MLT seeds");

      double totalWeight = MathUtil.sum(weight);
      double scale = (double) numPathSeeds / totalWeight;
      double x = 0.5;
      int x0 = (int) Math.floor(x);
      int x1;
      List<PathSeed> seeds = new ArrayList<PathSeed>();

      for (int i = 0, n = 0; i < pairsPerSeedTask; i++) {
        int s0 = lightPathLength[i];
        int t0 = eyePathLength[i];
        for (int s = s0; s >= -1; s--) {
          for (int t = t0; t >= -1; t--, n++) {
            x += scale * weight.get(n);
            x1 = (int) Math.floor(x);
            for (int j = x0; j < x1; j++) {
              PathSeed seed = new PathSeed();
              seed.randomSeed = initialRandomSeed + (long) i;
              seed.lightPathLength = s;
              seed.eyePathLength = t;
              seeds.add(seed);
            }
            x0 = x1;
          }
        }
      }

      monitor.notifyProgress(pairsPerSeedTask, pairsPerSeedTask);
      monitor.notifyComplete();

      return seeds;

    }

    private void join(PathNode lightTail, PathNode eyeTail,
        DoubleArray weights) {

      PathNode lightNode = lightTail;
      while (true) {

        PathNode eyeNode = eyeTail;
        while (true) {
          Color c = pathMeasure.evaluate(lightNode, eyeNode);
          weights.add(colorMeasure.evaluate(c));

          if (eyeNode == null) {
            break;
          }
          eyeNode = eyeNode.getParent();
        }

        if (lightNode == null) {
          break;
        }
        lightNode = lightNode.getParent();
      }

    }

    private Object performTask_MLT(PathSeed seed, int mutations,
        ProgressMonitor monitor) {

      int callbackInterval = Math  .min(10000,
          Math.max(1, mutations / 100));
      int nextCallback = 0;

      Path x = generatePath(seed);
      Path y;
      Color c = null;

      for (int i = 0; i < mutations; i++) {
        if (--nextCallback <= 0) {
          if (!monitor.notifyProgress(i, mutations)) {
            monitor.notifyCancelled();
            return null;
          }
          nextCallback = callbackInterval;
        }
        y = mutate(x);
        if (c == null || y != x) {
          c = x.measure(pathMeasure);
          c = ColorUtil.div(c, colorMeasure.evaluate(c));
        }
        x = y;

        record(x, raster.get(), c);
      }

      monitor.notifyProgress(mutations, mutations);
      monitor.notifyComplete();

      return raster.get();
    }

    private void record(Path x, Raster image, Color c) {
      Point2 p = x.getPointOnImagePlane();
      if (p != null) {
        RasterUtil.addPixel(image, p, c);
      }
    }

    private Path mutate(Path x) {
      Path y = mutator.mutate(x, random);
      if (y == null || y == x) {
        return x;
      }

      double a = a(x, y);
      return RandomUtil.bernoulli(a, random) ? y : x;
    }

    private double a(Path x, Path y) {
      double fy = f(y);
      if (fy <= 0.0) {
        return 0.0;
      }

      double tyx = mutator.getTransitionPDF(y, x);
      if (tyx <= 0.0) {
        return 0.0;
      }

      double fx = f(x);
      double txy = mutator.getTransitionPDF(x, y);

      return Math.min(1.0, (fy * tyx) / (fx * txy));
    }

    private double f(Path x) {
      Color c = x.measure(pathMeasure);
      return colorMeasure.evaluate(c);
    }

  }

  private final void testGeneratePath() {
    java.util.Random rnd = new java.util.Random();

    for (int i = 0; i < 1000; i++) {
      long seed = rnd.nextLong();
      Path x = generatePath(seed);
      for (int j = 0; j < 100; j++) {
        Path y = generatePath(seed);
        if (x.getLength() != y.getLength() || x.getEyePathLength() != y.getEyePathLength()) {
          System.out.println("FAIL");
          return;
        }
      }
    }

    System.out.println("PASS");
  }

}
