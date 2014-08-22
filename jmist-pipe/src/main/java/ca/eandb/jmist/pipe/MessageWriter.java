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

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

import com.google.protobuf.MessageLite;

/**
 *
 */
public final class MessageWriter<T extends MessageLite> implements Runnable {

  private final BlockingQueue<T> messages;

  private final OutputStream output;

  private boolean done = false;

  public MessageWriter(BlockingQueue<T> messages, OutputStream output) {
    this.messages = messages;
    this.output = output;
  }

  @Override
  public void run() {
    try {
      while (!done) {
        try {
          T message = messages.take();
          message.writeDelimitedTo(output);
          output.flush();
        } catch (InterruptedException e) {}
      }

      while (true) {
        T message = messages.poll();
        if (message == null) {
          break;
        }
        message.writeDelimitedTo(output);
        output.flush();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void shutdown() {
    done = true;
  }

}
