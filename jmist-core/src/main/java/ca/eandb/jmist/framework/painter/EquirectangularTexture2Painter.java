package ca.eandb.jmist.framework.painter;

import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.SphericalCoordinates;

public class EquirectangularTexture2Painter implements Painter {
  private final Texture2 texture;

  private final Basis3 basis;

  public EquirectangularTexture2Painter(Texture2 texture, Basis3 basis) {
    this.texture = texture;
    this.basis = basis;
  }

  @Override
  public Color getColor(SurfacePoint p, WavelengthPacket lambda) {
    SphericalCoordinates sc = SphericalCoordinates.fromCartesian(p.getNormal(), basis);
    Point2 uv = new Point2(
        (sc.azimuthal() + Math.PI) / (2.0 * Math.PI),
        1.0 - sc.polar() / Math.PI);
    return texture.evaluate(uv).sample(lambda);
  }
}
