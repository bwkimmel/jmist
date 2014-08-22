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

import java.util.concurrent.BlockingQueue;

import ca.eandb.jmist.engine.proto.RenderEngineProtos.RenderCallback;
import ca.eandb.util.progress.CancelListener;
import ca.eandb.util.progress.ProgressMonitor;

/**
 *
 */
public class RenderCallbackProgressMonitor implements ProgressMonitor {

  private final BlockingQueue<RenderCallback> messages;

  public RenderCallbackProgressMonitor(BlockingQueue<RenderCallback> messages) {
    this.messages = messages;
  }

  /* (non-Javadoc)
   * @see ca.eandb.util.progress.ProgressMonitor#notifyProgress(int, int)
   */
  @Override
  public boolean notifyProgress(int value, int maximum) {
    return notifyProgress((double) value / (double) maximum);
  }

  /* (non-Javadoc)
   * @see ca.eandb.util.progress.ProgressMonitor#notifyProgress(double)
   */
  @Override
  public boolean notifyProgress(double progress) {
    try {
      messages.put(RenderCallback.newBuilder().setProgress(progress).build());
      return true;
    } catch (InterruptedException e) {
      e.printStackTrace();
      return false;
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.util.progress.ProgressMonitor#notifyIndeterminantProgress()
   */
  @Override
  public boolean notifyIndeterminantProgress() {
    return true;
  }

  /* (non-Javadoc)
   * @see ca.eandb.util.progress.ProgressMonitor#notifyComplete()
   */
  @Override
  public void notifyComplete() {
    try {
      messages.put(RenderCallback.newBuilder().setDone(true).build());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.util.progress.ProgressMonitor#notifyCancelled()
   */
  @Override
  public void notifyCancelled() {
    // nothing to do
  }

  /* (non-Javadoc)
   * @see ca.eandb.util.progress.ProgressMonitor#notifyStatusChanged(java.lang.String)
   */
  @Override
  public void notifyStatusChanged(String status) {
    // nothing to do
  }

  /* (non-Javadoc)
   * @see ca.eandb.util.progress.ProgressMonitor#isCancelPending()
   */
  @Override
  public boolean isCancelPending() {
    return false;
  }

  /* (non-Javadoc)
   * @see ca.eandb.util.progress.ProgressMonitor#addCancelListener(ca.eandb.util.progress.CancelListener)
   */
  @Override
  public void addCancelListener(CancelListener listener) {
    // nothing to do
  }

}
