/* Copyright (c) 2014 Bradley W. Kimmel
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
package ca.eandb.jmist.pipe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ca.eandb.jdcp.job.ParallelizableJob;
import ca.eandb.jdcp.job.ParallelizableJobRunner;
import ca.eandb.jmist.engine.proto.RenderEngineProtos.RenderCallback;
import ca.eandb.jmist.engine.proto.RenderEngineProtos.RenderRequest;
import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.proto.factory.ProtoRenderFactory;
import ca.eandb.util.progress.ProgressMonitor;

/**
 *
 */
public final class ConnectionHandler implements Runnable {

  private final InputStream in;

  private final OutputStream out;

  public ConnectionHandler(InputStream in, OutputStream out) {
    this.in = in;
    this.out = out;
  }

  @Override
  public void run() {
    BlockingQueue<RenderCallback> messages =
        new LinkedBlockingQueue<RenderCallback>();
    MessageWriter<RenderCallback> writer =
        new MessageWriter<RenderCallback>(messages, out);
    Thread writerThread = new Thread(writer);
    writerThread.setDaemon(true);
    writerThread.start();

    ProtoRenderFactory renderFactory = new ProtoRenderFactory();

    try {
      while (true) {
        RenderRequest request = RenderRequest.parseDelimitedFrom(in);
        if (request == null) {
          break;
        }

        try {
          Display display = new RenderCallbackDisplay(messages);
          ProgressMonitor monitor = new RenderCallbackProgressMonitor(messages);
          ParallelizableJob job =
              renderFactory.createRenderJob(request.getJob(), display);

          ParallelizableJobRunner.Builder runner =
              ParallelizableJobRunner.newBuilder()
                  .setJob(job)
                  .setProgressMonitor(monitor);
          if (request.hasThreads()) {
            runner.setMaxConcurrentWorkers(request.getThreads());
          }

          runner.build().run();
        } catch (Exception e) {
          StringWriter sw = new StringWriter();
          PrintWriter pw = new PrintWriter(sw);
          e.printStackTrace(pw);
          messages.put(
              RenderCallback.newBuilder().setError(sw.toString()).build());
        }
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    writer.shutdown();
    writerThread.interrupt();
    try {
      writerThread.join();
    } catch (InterruptedException e) {}
  }

}
