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

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;

import ca.eandb.jdcp.job.AbstractParallelizableJob;
import ca.eandb.jdcp.job.TaskWorker;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.measurement.CollectorSphere;
import ca.eandb.jmist.framework.measurement.ColorSensorArray;
import ca.eandb.jmist.framework.measurement.MaterialPhotometer;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.util.io.Archive;
import ca.eandb.util.progress.ProgressMonitor;

/**
 * A <code>ParallelizableJob</code> that measures the scattering distribution of
 * <code>Material</code>s using a virtual goniophotometer [1].<br/>
 * <br/>
 * [1] Krishnaswamy, A.; Baranoski, G.V.G.; Rokne, J.G.,
 *     <a href="http://dx.doi.org/10.1080/10867651.2004.10504894">Improving the
 *     Reliability/Cost Ratio of Goniophotometric Comparisons</a>, Journal of
 *     Graphics Tools 9(3):1-20, 2004.
 */
public final class MaterialPhotometerJob extends AbstractParallelizableJob {

  /** Serialization version ID. */
  private static final long serialVersionUID = -1521758677633805555L;

  public MaterialPhotometerJob(Material[] specimens,
      SphericalCoordinates[] incidentAngles, WavelengthPacket[] wavelengths,
      long samplesPerMeasurement, long samplesPerTask,
      CollectorSphere collector) {
    this.worker = new PhotometerTaskWorker(collector);
    this.specimens = specimens.clone();
    this.incidentAngles = incidentAngles.clone();
    this.wavelengths = wavelengths.clone();
    this.samplesPerMeasurement = samplesPerMeasurement;
    this.samplesPerTask = samplesPerTask;
    this.totalTasks = specimens.length
        * wavelengths.length
        * incidentAngles.length
        * ((int) (samplesPerMeasurement / samplesPerTask) +
            ((samplesPerMeasurement % samplesPerTask) > 0 ? 1 : 0));
  }

  public MaterialPhotometerJob(Material specimen,
      SphericalCoordinates[] incidentAngles, WavelengthPacket[] wavelengths,
      long samplesPerMeasurement, long samplesPerTask,
      CollectorSphere collector) {
    this(new Material[]{ specimen }, incidentAngles, wavelengths,
         samplesPerMeasurement, samplesPerTask, collector);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jdcp.job.AbstractParallelizableJob#initialize()
   */
  @Override
  public void initialize() {
    this.results = new ColorSensorArray[
        wavelengths.length * incidentAngles.length * specimens.length];
    for (int i = 0; i < this.results.length; i++) {
      ColorModel colorModel = this.getWavelength(i).getColorModel();
      this.results[i] =
          new ColorSensorArray(worker.collector.sensors(), colorModel);
      maxChannels = Math.max(maxChannels, colorModel.getNumChannels());
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.ParallelizableJob#getNextTask()
   */
  public Object getNextTask() {
    if (!this.isComplete()) {
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
        this.samplesPerTask,
        measurementIndex);
  }

  private Material getSpecimen(int measurementIndex) {
    return this.specimens[measurementIndex /
                          (wavelengths.length * incidentAngles.length)];
  }

  private SphericalCoordinates getIncidentAngle(int measurementIndex) {
    int specimenMeasurementIndex =
        measurementIndex % (wavelengths.length * incidentAngles.length);
    return this.incidentAngles[specimenMeasurementIndex / wavelengths.length];
  }

  private WavelengthPacket getWavelength(int measurementIndex) {
    int specimenMeasurementIndex =
        measurementIndex % (wavelengths.length * incidentAngles.length);
    return this.wavelengths[specimenMeasurementIndex % wavelengths.length];
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.ParallelizableJob#submitTaskResults(java.lang.Object, java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
   */
  public void submitTaskResults(Object task, Object results,
      ProgressMonitor monitor) {
    PhotometerTask info = (PhotometerTask) task;
    ColorSensorArray sensorArray = (ColorSensorArray) results;
    this.results[info.measurementIndex].merge(sensorArray);
    monitor.notifyProgress(++this.tasksReturned, this.totalTasks);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.ParallelizableJob#isComplete()
   */
  public boolean isComplete() {
    return this.outstandingSamplesPerMeasurement >= this.samplesPerMeasurement;
  }

  private final String colorToCSV(Color color) {
    double[] values = color.toArray();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < maxChannels; i++) {
      if (i > 0) {
        sb.append(",");
      }
      if (i < values.length) {
        sb.append(values[i]);
      }
    }
    return sb.toString();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.ParallelizableJob#finish()
   */
  public void finish() {
    PrintStream out = new PrintStream(createFileOutputStream("photometer.csv"));
    writeColumnHeadings(out);

    for (int specimenIndex = 0, n = 0;
         specimenIndex < specimens.length;
         specimenIndex++) {
      for (int incidentAngleIndex = 0;
           incidentAngleIndex < incidentAngles.length;
           incidentAngleIndex++) {
        SphericalCoordinates incidentAngle = incidentAngles[incidentAngleIndex];

        for (int wavelengthIndex = 0;
             wavelengthIndex < wavelengths.length;
             wavelengthIndex++, n++) {
          ColorSensorArray sensorArray = results[n];

          for (int sensor = 0; sensor < worker.collector.sensors(); sensor++) {
            SphericalCoordinates exitantAngle =
                worker.collector.getSensorCenter(sensor);
            double solidAngle = worker.collector.getSensorSolidAngle(sensor);
            double projectedSolidAngle =
                worker.collector.getSensorProjectedSolidAngle(sensor);
            Color raw = sensorArray.getTotalWeight(sensor);
            Color reflectance = raw.divide(outstandingSamplesPerMeasurement);

            out.printf(
                "%d,%f,%f,%d,%d,%f,%f,%f,%f,%d,%s,%s,%s,%s",
                specimenIndex,
                incidentAngle.polar(),
                incidentAngle.azimuthal(),
                wavelengthIndex,
                sensor,
                exitantAngle.polar(),
                exitantAngle.azimuthal(),
                solidAngle,
                projectedSolidAngle,
                outstandingSamplesPerMeasurement,
                colorToCSV(raw),
                colorToCSV(reflectance),
                colorToCSV(reflectance.divide(projectedSolidAngle)),
                colorToCSV(reflectance.divide(solidAngle)));
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
    out.print("\"Wavelength Packet\",");
    out.print("\"Sensor\",");
    out.print("\"Exitant Polar (radians)\",");
    out.print("\"Exitant Azimuthal (radians)\",");
    out.print("\"Solid Angle (sr)\",");
    out.print("\"Projected Solid Angle (sr)\",");
    out.print("\"Samples\",");

    for (int i = 0; i < maxChannels; i++) {
      out.printf("\"Raw (%s)\",", wavelengths.length > 1
          ? Integer.toString(i)
          : wavelengths[0].getColorModel().getChannelName(i));
    }
    for (int i = 0; i < maxChannels; i++) {
      out.printf("\"Reflectance (%s)\",", wavelengths.length > 1
          ? Integer.toString(i)
          : wavelengths[0].getColorModel().getChannelName(i));
    }
    for (int i = 0; i < maxChannels; i++) {
      out.printf("\"BSDF (%s)\",", wavelengths.length > 1
          ? Integer.toString(i)
          : wavelengths[0].getColorModel().getChannelName(i));
    }
    for (int i = 0; i < maxChannels - 1; i++) {
      out.printf("\"SPF (%s)\",", wavelengths.length > 1
          ? Integer.toString(i)
          : wavelengths[0].getColorModel().getChannelName(i));
    }
    out.printf("\"SPF (%s)\"", wavelengths.length > 1
        ? Integer.toString(maxChannels - 1)
        : wavelengths[0].getColorModel().getChannelName(maxChannels - 1));
    out.println();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jdcp.job.AbstractParallelizableJob#archiveState(ca.eandb.util.io.Archive)
   */
  @Override
  protected void archiveState(Archive ar) throws IOException,
      ClassNotFoundException {
    results = (ColorSensorArray[]) ar.archiveObject(results);
    nextMeasurementIndex = ar.archiveInt(nextMeasurementIndex);
    outstandingSamplesPerMeasurement =
        ar.archiveLong(outstandingSamplesPerMeasurement);
    tasksReturned = ar.archiveInt(tasksReturned);
    maxChannels = ar.archiveInt(maxChannels);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.ParallelizableJob#worker()
   */
  public TaskWorker worker() {
    return this.worker;
  }

  private static class PhotometerTask implements Serializable {

    /** Serialization version ID. */
    private static final long serialVersionUID = 4238727637644746732L;

    public final Material specimen;
    public final SphericalCoordinates incident;
    public final WavelengthPacket wavelength;
    public final long samples;
    public final int measurementIndex;

    public PhotometerTask(Material specimen,
        SphericalCoordinates incident, WavelengthPacket wavelength,
        long samples, int measurementIndex) {
      this.specimen = specimen;
      this.incident = incident;
      this.wavelength = wavelength;
      this.samples = samples;
      this.measurementIndex = measurementIndex;
    }

  }

  private static class PhotometerTaskWorker implements TaskWorker, Serializable {

    /** Serialization version ID. */
    private static final long serialVersionUID = -6729377656284858068L;

    /**
     * Creates a new <code>PhotometerTaskWorker</code>.
     * @param specimen The <code>SurfaceScatterer</code> to be measured.
     * @param collector The prototype <code>CollectorSphere</code> from
     *     which clones are constructed to record hits to.
     */
    public PhotometerTaskWorker(CollectorSphere collector) {
      this.collector = collector;
    }

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.TaskWorker#performTask(java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
     */
    public Object performTask(Object task, ProgressMonitor monitor) {
      PhotometerTask info = (PhotometerTask) task;
      MaterialPhotometer photometer =
          new MaterialPhotometer(collector, info.wavelength.getColorModel());

      photometer.setSpecimen(info.specimen);
      photometer.setIncidentAngle(info.incident);
      photometer.setWavelengthPacket(info.wavelength);
      photometer.castPhotons(info.samples, monitor);

      return photometer.getSensorArray();
    }

    /**
     * The prototype <code>CollectorSphere</code> from which clones are
     * constructed to record hits to.
     */
    private final CollectorSphere collector;

  }

  /** The <code>TaskWorker</code> that performs the work for this job. */
  private final PhotometerTaskWorker worker;

  private final Material[] specimens;
  private final WavelengthPacket[] wavelengths;
  private final SphericalCoordinates[] incidentAngles;
  private final long samplesPerMeasurement;
  private final long samplesPerTask;
  private final int totalTasks;
  private transient int maxChannels;
  private transient ColorSensorArray[] results;
  private transient int nextMeasurementIndex = 0;
  private transient long outstandingSamplesPerMeasurement = 0;
  private transient int tasksReturned = 0;

}