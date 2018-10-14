/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2018 Bradley W. Kimmel
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

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ca.eandb.jdcp.job.AbstractParallelizableJob;
import ca.eandb.jdcp.job.TaskWorker;
import ca.eandb.jmist.framework.measurement.CollectorSphere;
import ca.eandb.jmist.framework.measurement.IntegerSensorArray;
import ca.eandb.jmist.framework.measurement.Photometer;
import ca.eandb.jmist.framework.scatter.SurfaceScatterer;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.util.DoubleArray;
import ca.eandb.util.io.Archive;
import ca.eandb.util.progress.ProgressMonitor;

/**
 * A <code>ParallelizableJob</code> that measures the scattering distribution of
 * <code>SurfaceScatterer</code>s using a virtual goniophotometer [1].<br>
 * <br>
 * [1] Krishnaswamy, A.; Baranoski, G.V.G.; Rokne, J.G.,
 *     <a href="http://dx.doi.org/10.1080/10867651.2004.10504894">Improving the
 *     Reliability/Cost Ratio of Goniophotometric Comparisons</a>, Journal of
 *     Graphics Tools 9(3):1-20, 2004.
 */
public final class PhotometerJob extends AbstractParallelizableJob {

  /** A builder for creating <code>PhotometerJob</code>s. */
  public static final class Builder {
    private final List<SurfaceScatterer> specimens = new ArrayList<>();
    private final List<SphericalCoordinates> incidentAngles = new ArrayList<>();
    private final DoubleArray wavelengths = new DoubleArray();
    private long samplesPerMeasurement = 1;
    private long samplesPerTask = 0;
    private long tasksPerMeasurement = 1;
    private CollectorSphere collector = CollectorSphere.NULL;

    private Builder() {}

    /**
     * Builds a new <code>PhotometerJob</code>.
     * @return The new <code>PhotometerJob</code>.
     */
    public PhotometerJob build() {
      if (samplesPerTask == 0) {
        samplesPerTask = samplesPerMeasurement / tasksPerMeasurement;
      }
      return new PhotometerJob(
          specimens.toArray(new SurfaceScatterer[0]),
          incidentAngles.toArray(new SphericalCoordinates[0]),
          wavelengths.toDoubleArray(), samplesPerMeasurement, samplesPerTask,
          collector);
    }

    /**
     * Adds a <code>SurfaceScatterer</code> to be measured.
     * @param specimen The <code>SurfaceScatterer</code> to be measured.
     * @return This <code>Builder</code>.
     */
    public Builder addSpecimen(SurfaceScatterer specimen) {
      specimens.add(specimen);
      return this;
    }

    /**
     * Adds an incident angle to direct incident light from.
     * @param incidentAngle The <code>SphericalCoordinates</code> for the
     *     incident angle.
     * @return This <code>Builder</code>.
     */
    public Builder addIncidentAngle(SphericalCoordinates incidentAngle) {
      incidentAngles.add(incidentAngle);
      return this;
    }

    /**
     * Adds a wavelength to use for measurement.
     * @param wavelength The wavelength.
     * @return This <code>Builder</code>.
     */
    public Builder addWavelength(double wavelength) {
      wavelengths.add(wavelength);
      return this;
    }

    /**
     * Adds all of the specified wavelengths to use for measurement.
     * @param wavelengths An array of wavelengths.
     * @return This <code>Builder</code>.
     */
    public Builder addWavelengths(double[] wavelengths) {
      this.wavelengths.addAll(wavelengths);
      return this;
    }

    /**
     * Sets the number of samples to use for each measurement.
     * @param samplesPerMeasurement The number of samples to use.
     * @return This <code>Builder</code>.
     */
    public Builder setSamplesPerMeasurement(long samplesPerMeasurement) {
      this.samplesPerMeasurement = samplesPerMeasurement;
      return this;
    }

    /**
     * Sets the number of samples to use for each task.  If this is not set,
     * it defaults to a value determined by the number of tasks per measurement.
     * The default number of tasks per measurement is 1.
     * @param samplesPerTask The number of samples to use.
     * @return This <code>Builder</code>.
     * @see #setTasksPerMeasurement(long)
     */
    public Builder setSamplesPerTask(long samplesPerTask) {
      this.samplesPerTask = samplesPerTask;
      return this;
    }

    /**
     * Sets the number of tasks to divide each measurement into.  The default is
     * 1.  This is only relevant if the number of samples per task is not set
     * explicitly.
     * @param tasksPerMeasurement The number of tasks to divide each measurement
     *     into.
     * @return This <code>Builder</code>.
     * @see #setSamplesPerTask(long)
     */
    public Builder setTasksPerMeasurement(long tasksPerMeasurement) {
      this.samplesPerTask = 0;
      this.tasksPerMeasurement = tasksPerMeasurement;
      return this;
    }

    /**
     * Sets the collector sphere to use.
     * @param collector The <code>CollectorSphere</code> to use.
     * @return This <code>Builder</code>.
     */
    public Builder setCollector(CollectorSphere collector) {
      this.collector = collector;
      return this;
    }
  }

  /**
   * Returns a new builder to create a <code>PhotometerJob</code>.
   * @return The new <code>Builder</code>.
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  private PhotometerJob(SurfaceScatterer[] specimens,
      SphericalCoordinates[] incidentAngles, double[] wavelengths,
      long samplesPerMeasurement, long samplesPerTask,
      CollectorSphere collector) {
    this.worker = new PhotometerTaskWorker(collector);
    this.specimens = specimens;
    this.incidentAngles = incidentAngles;
    this.wavelengths = wavelengths;
    this.samplesPerMeasurement = samplesPerMeasurement;
    this.samplesPerTask = samplesPerTask;
    this.totalTasks =
        specimens.length * wavelengths.length * incidentAngles.length
        * ((int) (samplesPerMeasurement / samplesPerTask)
            + ((samplesPerMeasurement % samplesPerTask) > 0 ? 1 : 0));
  }

  @Override
  public void initialize() {
    this.results = new IntegerSensorArray[
        wavelengths.length * incidentAngles.length * specimens.length];
    for (int i = 0; i < this.results.length; i++) {
      this.results[i] = new IntegerSensorArray(worker.collector);
    }
  }

  @Override
  public synchronized Object getNextTask() {
    if (outstandingSamplesPerMeasurement < samplesPerMeasurement) {
      PhotometerTask task = this.getPhotometerTask(this.nextMeasurementIndex);
      if (++this.nextMeasurementIndex >= this.results.length) {
        this.outstandingSamplesPerMeasurement += this.samplesPerTask;
        this.nextMeasurementIndex = 0;
      }
      return task;
    } else {
      return null;
    }
  }

  private PhotometerTask getPhotometerTask(int measurementIndex) {
    return new PhotometerTask(
        this.getSpecimen(measurementIndex),
        this.getIncidentAngle(measurementIndex),
        this.getWavelength(measurementIndex),
        Math.min(samplesPerTask, samplesPerMeasurement - outstandingSamplesPerMeasurement),
        measurementIndex
    );
  }

  private SurfaceScatterer getSpecimen(int measurementIndex) {
    return this.specimens[measurementIndex / (wavelengths.length * incidentAngles.length)];
  }

  private SphericalCoordinates getIncidentAngle(int measurementIndex) {
    int specimenMeasurementIndex = measurementIndex % (wavelengths.length * incidentAngles.length);
    return this.incidentAngles[specimenMeasurementIndex / this.wavelengths.length];
  }

  private double getWavelength(int measurementIndex) {
    int specimenMeasurementIndex = measurementIndex % (wavelengths.length * incidentAngles.length);
    return this.wavelengths[specimenMeasurementIndex % this.wavelengths.length];
  }

  @Override
  public void submitTaskResults(Object task, Object results,
      ProgressMonitor monitor) {
    PhotometerTask info = (PhotometerTask) task;
    IntegerSensorArray sensorArray = (IntegerSensorArray) results;
    this.results[info.measurementIndex].merge(sensorArray);
    monitor.notifyProgress(++this.tasksReturned, this.totalTasks);
  }

  @Override
  public boolean isComplete() {
    return this.tasksReturned >= this.totalTasks;
  }

  @Override
  public void finish() {
    PrintStream out = new PrintStream(createFileOutputStream("photometer.csv"));
    this.writeColumnHeadings(out);

    for (int specimenIndex = 0, n = 0; specimenIndex < this.specimens.length;
         specimenIndex++) {

      for (int incidentAngleIndex = 0;
           incidentAngleIndex < this.incidentAngles.length;
           incidentAngleIndex++) {
        SphericalCoordinates incidentAngle = this.incidentAngles[incidentAngleIndex];

        for (int wavelengthIndex = 0; wavelengthIndex < this.wavelengths.length;
             wavelengthIndex++, n++) {
          double wavelength = this.wavelengths[wavelengthIndex];
          IntegerSensorArray sensorArray = this.results[n];

          for (int sensor = 0; sensor < worker.collector.sensors(); sensor++) {
            SphericalCoordinates exitantAngle = worker.collector.getSensorCenter(sensor);
            double solidAngle = worker.collector.getSensorSolidAngle(sensor);
            double projectedSolidAngle = worker.collector.getSensorProjectedSolidAngle(sensor);
            long hits = sensorArray.hits(sensor);
            double reflectance = (double) hits / (double) this.samplesPerMeasurement;

            out.printf(
                "%d,%f,%f,%e,%d,%f,%f,%f,%f,%d,%d,%f,%e,%e",
                specimenIndex,
                incidentAngle.polar(),
                incidentAngle.azimuthal(),
                wavelength,
                sensor,
                exitantAngle.polar(),
                exitantAngle.azimuthal(),
                solidAngle,
                projectedSolidAngle,
                this.samplesPerMeasurement,
                hits,
                reflectance,
                reflectance / projectedSolidAngle,
                reflectance / solidAngle
            );
            out.println();
          }
        }
      }
    }
    out.close();
  }

  /**
   * Writes the CSV column headings to the result stream.
   * @param out The <code>PrintStream</code> to write the column headings to.
   */
  private void writeColumnHeadings(PrintStream out) {
    out.print("\"Specimen\",");
    out.print("\"Incident Polar (radians)\",");
    out.print("\"Incident Azimuthal (radians)\",");
    out.print("\"Wavelength (m)\",");
    out.print("\"Sensor\",");
    out.print("\"Exitant Polar (radians)\",");
    out.print("\"Exitant Azimuthal (radians)\",");
    out.print("\"Solid Angle (sr)\",");
    out.print("\"Projected Solid Angle (sr)\",");
    out.print("\"Samples\",");
    out.print("\"Hits\",");
    out.print("\"Reflectance\",");
    out.print("\"BSDF\",");
    out.print("\"SPF\"");
    out.println();
  }

  @Override
  protected void archiveState(Archive ar) throws IOException, ClassNotFoundException {
    results = (IntegerSensorArray[]) ar.archiveObject(results);
    nextMeasurementIndex = ar.archiveInt(nextMeasurementIndex);
    outstandingSamplesPerMeasurement = ar.archiveLong(outstandingSamplesPerMeasurement);
    tasksReturned = ar.archiveInt(tasksReturned);
  }

  @Override
  public TaskWorker worker() {
    return this.worker;
  }

  private static class PhotometerTask implements Serializable {
    public final SurfaceScatterer specimen;
    public final SphericalCoordinates incident;
    public final double wavelength;
    public final long samples;
    public final int measurementIndex;

    public PhotometerTask(SurfaceScatterer specimen,
        SphericalCoordinates incident, double wavelength, long samples,
        int measurementIndex) {
      this.specimen = specimen;
      this.incident = incident;
      this.wavelength = wavelength;
      this.samples = samples;
      this.measurementIndex = measurementIndex;
    }

    /** Serialization version ID. */
    private static final long serialVersionUID = 4380806718949297914L;
  }

  private static class PhotometerTaskWorker implements TaskWorker, Serializable {

    /**
     * Creates a new <code>PhotometerTaskWorker</code>.
     * @param specimen The <code>SurfaceScatterer</code> to be measured.
     * @param collector The prototype <code>CollectorSphere</code> from
     *     which clones are constructed to record hits to.
     */
    public PhotometerTaskWorker(CollectorSphere collector) {
      this.collector = collector;
    }

    @Override
    public Object performTask(Object task, ProgressMonitor monitor) {
      Photometer photometer = new Photometer(collector);
      PhotometerTask info = (PhotometerTask) task;

      photometer.setSpecimen(info.specimen);
      photometer.setIncidentAngle(info.incident);
      photometer.setWavelength(info.wavelength);
      photometer.castPhotons(info.samples, monitor);

      return photometer.getSensorArray();
    }

    /**
     * The prototype <code>CollectorSphere</code> from which clones are
     * constructed to record hits to.
     */
    private final CollectorSphere collector;

    /** Serialization version ID. */
    private static final long serialVersionUID = -7564344525831112742L;

  }

  /** The <code>TaskWorker</code> that performs the work for this job. */
  private final PhotometerTaskWorker worker;

  private final SurfaceScatterer[] specimens;
  private final double[] wavelengths;
  private final SphericalCoordinates[] incidentAngles;
  private final long samplesPerMeasurement;
  private final long samplesPerTask;
  private final int totalTasks;
  private transient IntegerSensorArray[] results;
  private transient int nextMeasurementIndex = 0;
  private transient long outstandingSamplesPerMeasurement = 0;
  private transient int tasksReturned = 0;

  /** Serialization version ID. */
  private static final long serialVersionUID = -5367125667531320522L;

}
