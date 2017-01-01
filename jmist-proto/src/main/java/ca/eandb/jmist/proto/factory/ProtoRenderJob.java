package ca.eandb.jmist.proto.factory;

import java.io.ObjectInput;
import java.io.ObjectOutput;

import ca.eandb.jdcp.job.HostService;
import ca.eandb.jdcp.job.ParallelizableJob;
import ca.eandb.jdcp.job.TaskWorker;
import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.proto.RenderProtos.RenderJob;
import ca.eandb.util.UnexpectedException;
import ca.eandb.util.progress.ProgressMonitor;

public final class ProtoRenderJob implements ParallelizableJob {

  /** Serialization version ID. */
  private static final long serialVersionUID = 1859470656255473405L;

  private final RenderJob spec;

  private final Display display;

  private transient ParallelizableJob inner = null;

  private transient TaskWorker worker = null;

  public ProtoRenderJob(RenderJob spec, Display display) {
    this.spec = spec;
    this.display = display;
  }

  private ParallelizableJob inner() {
    if (inner == null) {
      synchronized (this) {
        if (inner == null) {
          ProtoRenderFactory factory = new ProtoRenderFactory();
          inner = factory.createRenderJob(spec, display);
        }
      }
    }
    return inner;
  }

  @Override
  public void setHostService(HostService host) {
    inner().setHostService(host);
  }

  @Override
  public void initialize() throws Exception {
    inner().initialize();
  }

  @Override
  public void finish() throws Exception {
    inner().finish();
  }

  @Override
  public void saveState(ObjectOutput output) throws Exception {
    inner().saveState(output);
  }

  @Override
  public void restoreState(ObjectInput input) throws Exception {
    inner().restoreState(input);
  }

  @Override
  public Object getNextTask() throws Exception {
    return inner().getNextTask();
  }

  @Override
  public void submitTaskResults(Object task, Object results,
      ProgressMonitor monitor) throws Exception {
    inner().submitTaskResults(task, results, monitor);
  }

  @Override
  public boolean isComplete() throws Exception {
    return inner().isComplete();
  }

  @Override
  public TaskWorker worker() throws Exception {
    if (worker == null) {
      synchronized (this) {
        if (worker == null) {
          worker = new ProtoTaskWorker(spec, inner());
        }
      }
    }
    return worker;
  }

  private static final class ProtoTaskWorker implements TaskWorker {

    /** Serialization version ID. */
    private static final long serialVersionUID = -2701314005957875972L;

    private final RenderJob spec;

    private transient ParallelizableJob job;

    private transient TaskWorker inner = null;

    public ProtoTaskWorker(RenderJob spec, ParallelizableJob job) {
      this.spec = spec;
      this.job = job;
    }

    private TaskWorker inner() throws Exception {
      if (inner == null) {
        synchronized (this) {
          if (inner == null) {
            if (job == null) {
              ProtoRenderFactory factory = new ProtoRenderFactory();
              job = factory.createRenderJob(spec, Display.NULL);
            }
            inner = job.worker();
          }
        }
      }
      return inner;
    }

    @Override
    public Object performTask(Object task, ProgressMonitor monitor) {
      try {
        return inner().performTask(task, monitor);
      } catch (Exception e) {
        throw new UnexpectedException(e);
      }
    }

  }

}
