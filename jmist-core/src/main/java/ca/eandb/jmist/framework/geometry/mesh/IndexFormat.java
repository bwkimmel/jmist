/**
 *
 */
package ca.eandb.jmist.framework.geometry.mesh;

/**
 * @author bwkimmel
 *
 */
public enum IndexFormat {
  INT32(new Int32IndexReader());

  private final IndexReader reader;

  private IndexFormat(IndexReader reader) {
    this.reader = reader;
  }

  public IndexReader createReader(int offset) {
    return offset == 0 ? reader : new OffsetIndexReader(offset, reader);
  }
}
