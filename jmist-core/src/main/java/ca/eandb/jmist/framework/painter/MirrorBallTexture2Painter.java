package ca.eandb.jmist.framework.painter;

import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Vector3;

public class MirrorBallTexture2Painter implements Painter {
  private final Texture2 texture;

  private final Basis3 basis;

  public MirrorBallTexture2Painter(Texture2 texture, Basis3 basis) {
    this.texture = texture;
    this.basis = basis;
  }

  @Override
  public Color getColor(SurfacePoint p, WavelengthPacket lambda) {
    Vector3 h = basis.w().plus(p.getNormal()).unit();
    Point2 uv = new Point2(0.5 * (h.x() + 1.0), 1.0 - 0.5 * (h.y() + 1.0));
    return texture.evaluate(uv).sample(lambda);
  }
}
