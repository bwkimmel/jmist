package ca.eandb.jmist.framework.geometry.mesh;

import java.nio.ByteBuffer;

public interface IndexReader {

  int read(ByteBuffer buffer, int offset);

}
