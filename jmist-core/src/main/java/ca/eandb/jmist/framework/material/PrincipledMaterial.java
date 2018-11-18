package ca.eandb.jmist.framework.material;

import org.apache.commons.math3.util.FastMath;

import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.ShaderParameter;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.UnimplementedException;

public class PrincipledMaterial extends OpaqueMaterial {

  private final Painter baseColorParam;
  private final ShaderParameter subsurfaceParam;
  private final ShaderParameter metallicParam;
  private final ShaderParameter specularParam;
  private final ShaderParameter specularTintParam;
  private final ShaderParameter roughnessParam;
  private final ShaderParameter anisotropicParam;
  private final ShaderParameter sheenParam;
  private final ShaderParameter sheenTintParam;
  private final ShaderParameter clearcoatParam;
  private final ShaderParameter clearcoatGlossParam;

  public static final class Builder {
    private Painter baseColorParam = Painter.BLACK;
    private ShaderParameter subsurfaceParam = ShaderParameter.ZERO;
    private ShaderParameter metallicParam = ShaderParameter.ZERO;
    private ShaderParameter specularParam = ShaderParameter.ZERO;
    private ShaderParameter specularTintParam = ShaderParameter.ZERO;
    private ShaderParameter roughnessParam = ShaderParameter.ZERO;
    private ShaderParameter anisotropicParam = ShaderParameter.ZERO;
    private ShaderParameter sheenParam = ShaderParameter.ZERO;
    private ShaderParameter sheenTintParam = ShaderParameter.ZERO;
    private ShaderParameter clearcoatParam = ShaderParameter.ZERO;
    private ShaderParameter clearcoatGlossParam = ShaderParameter.ZERO;

    private Builder() {}

    public Builder setBaseColor(Painter baseColorParam) {
      this.baseColorParam = baseColorParam;
      return this;
    }

    public Builder setSubsurface(ShaderParameter subsurfaceParam) {
      this.subsurfaceParam = subsurfaceParam;
      return this;
    }

    public Builder setMetallic(ShaderParameter metallicParam) {
      this.metallicParam = metallicParam;
      return this;
    }

    public Builder setSpecular(ShaderParameter specularParam) {
      this.specularParam = specularParam;
      return this;
    }

    public Builder setSpecularTint(ShaderParameter specularTintParam) {
      this.specularTintParam = specularTintParam;
      return this;
    }

    public Builder setRoughness(ShaderParameter roughnessParam) {
      this.roughnessParam = roughnessParam;
      return this;
    }

    public Builder setAnisotropic(ShaderParameter anisotropicParam) {
      this.anisotropicParam = anisotropicParam;
      return this;
    }

    public Builder setSheen(ShaderParameter sheenParam) {
      this.sheenParam = sheenParam;
      return this;
    }

    public Builder setSheenTint(ShaderParameter sheenTintParam) {
      this.sheenTintParam = sheenTintParam;
      return this;
    }

    public Builder setClearcoat(ShaderParameter clearcoatParam) {
      this.clearcoatParam = clearcoatParam;
      return this;
    }

    public Builder setClearcoatGloss(ShaderParameter clearcoatGlossParam) {
      this.clearcoatGlossParam = clearcoatGlossParam;
      return this;
    }

    public PrincipledMaterial build() {
      return new PrincipledMaterial(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  private PrincipledMaterial(Builder b) {
    this.baseColorParam = b.baseColorParam;
    this.subsurfaceParam = b.subsurfaceParam;
    this.metallicParam = b.metallicParam;
    this.specularParam = b.specularParam;
    this.specularTintParam = b.specularTintParam;
    this.roughnessParam = b.roughnessParam;
    this.anisotropicParam = b.anisotropicParam;
    this.sheenParam = b.sheenParam;
    this.sheenTintParam = b.sheenTintParam;
    this.clearcoatParam = b.clearcoatParam;
    this.clearcoatGlossParam = b.clearcoatGlossParam;
  }

  private static Color mix(Color a, Color b, double t) {
    if (t < MathUtil.TINY_EPSILON) {
      return a;
    } else if (t > 1.0 - MathUtil.TINY_EPSILON) {
      return b;
    }
    return a.times(1.0 - t).plus(b.times(t));
  }

  private static double mix(double a, double b, double t) {
    return a * (1.0 - t) + b * t;
  }

  private static double pow5(double x) {
    double x2 = x * x;
    return x2 * x2 * x;
  }

  private static double sqr(double x) {
    return x * x;
  }

  private static double fresnel(double dot) {
    return pow5(1.0 - MathUtil.clamp(dot, 0.0, 1.0));
  }

  private static double gtr2Aniso(double ndoth, double hdotx, double hdoty, double ax, double ay) {
    double hdotx2 = sqr(hdotx);
    double hdoty2 = sqr(hdoty);
    double ndoth2 = sqr(ndoth);
    double ax2 = sqr(ax);
    double ay2 = sqr(ay);
    return 1.0 / (Math.PI * ax * ay * sqr(hdotx2 / ax2 + hdoty2 / ay2 + ndoth2));
  }

  private static double ggxAniso(double ndotv, double vdotx, double vdoty, double ax, double ay) {
    return 2.0 * ndotv / (ndotv + FastMath.sqrt(sqr(vdotx*ax) + sqr(vdoty*ay) + sqr(ndotv)));
  }

  private static double ggx(double ndotv, double alpha) {
    double a = sqr(alpha);
    double b = sqr(ndotv);
    return 2.0 * ndotv / (ndotv + FastMath.sqrt(a + b - a*b));
  }

  private static double gtr1(double ndoth, double a) {
    if (a >= 1.0) return 1.0 / Math.PI;
    double a2 = sqr(a);
    double t = 1.0 + (a2 - 1.0) * sqr(ndoth);
    return (a2 - 1.0) / (Math.PI * FastMath.log(a2) * t);
  }

  @Override
  public Color bsdf(SurfacePoint x, Vector3 in, Vector3 out, WavelengthPacket lambda) {
    double specular = specularParam.evaluate(x);
    double specularTint = specularTintParam.evaluate(x);
    double metallic = metallicParam.evaluate(x);
    double roughness = roughnessParam.evaluate(x);
    double sheenTint = sheenTintParam.evaluate(x);
    double anisotropic = anisotropicParam.evaluate(x);
    double sheen = sheenParam.evaluate(x);
    double clearcoat = clearcoatParam.evaluate(x);
    double clearcoatGloss = clearcoatGlossParam.evaluate(x);
    double subsurface = subsurfaceParam.evaluate(x);

    Color baseColor = baseColorParam.getColor(x, lambda);
    Color whiteColor = Spectrum.WHITE.sample(lambda);

    double lum = baseColor.luminance();
    Color tintColor = lum > MathUtil.TINY_EPSILON ? baseColor.divide(lum) : whiteColor;

    Color specColor = mix(mix(whiteColor, tintColor, specularTint).times(specular * 0.08), baseColor, metallic);
    Color sheenColor = mix(whiteColor, tintColor, sheenTint);

    Basis3 sb = x.getShadingBasis();

    Vector3 l = in.opposite();
    Vector3 v = out;
    Vector3 h = l.plus(v).unit();
    Vector3 n = sb.w();
    Vector3 X = sb.u();
    Vector3 Y = sb.v();
    double ndotl = n.dot(l);
    double ndotv = n.dot(v);
    double ndoth = n.dot(h);
    double hdotv = h.dot(v);
    double hdotx = h.dot(X);
    double hdoty = h.dot(Y);

    // Diffuse
    double fd90 = 0.5 + 2.0 * hdotv * hdotv * roughness;
    double fl = fresnel(ndotl);
    double fv = fresnel(ndotv);
    double fd = mix(1.0, fd90, fl) * mix(1.0, fd90, fv);

    // Hanrahan-Krueger-based scattering
    double fss90 = hdotv * hdotv * roughness;
    double fss = mix(1.0, fss90, fl) * mix(1.0, fss90, fv);
    double ss = 1.25 * (fss * (1.0 / (ndotl + ndotv) - 0.5) + 0.5);

    // specular
    double alpha = sqr(roughness);
    double aspect = FastMath.sqrt(1.0 - 0.9 * anisotropic);
    double ax = Math.max(MathUtil.BIG_EPSILON, alpha / aspect);
    double ay = Math.max(MathUtil.BIG_EPSILON, alpha * aspect);
    double Ds = gtr2Aniso(ndoth, hdotx, hdoty, ax, ay);
    double fh = fresnel(hdotv);
    Color Fs = mix(specColor, whiteColor, fh);

    double Gs = ggxAniso(ndotl, l.dot(X), l.dot(Y), ax, ay) *
        ggxAniso(ndotv, v.dot(X), v.dot(Y), ax, ay);

    // sheen
    Color Fsheen = sheenColor.times(fh * sheen);

    // clearcoat (ior = 1.5 -> F0 = 0.04)
    double Dr = gtr1(ndoth, mix(0.1,0.001, clearcoatGloss));
    double Fr = mix(0.04, 1.0, fh);
    double Gr = ggx(ndotl, 0.25) * ggx(ndotv, 0.25);

    return baseColor.times((1.0 / Math.PI) * mix(fd, ss, subsurface)).plus(Fsheen).times(1.0 - metallic)
        .plus(Fs.times(Gs * Ds / (4.0 * ndotl * ndotv)))
        .plus(whiteColor.times(0.25 * clearcoat * Gr * Fr * Dr / (4.0 * ndotl * ndotv)));
  }

  @Override
  public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint, WavelengthPacket lambda, double ru, double rv, double rj) {
    double specular = specularParam.evaluate(x);
    double specularTint = specularTintParam.evaluate(x);
    double metallic = metallicParam.evaluate(x);
    double roughness = roughnessParam.evaluate(x);
    double sheenTint = sheenTintParam.evaluate(x);
    double anisotropic = anisotropicParam.evaluate(x);
    double sheen = sheenParam.evaluate(x);
    double clearcoat = clearcoatParam.evaluate(x);
    double clearcoatGloss = clearcoatGlossParam.evaluate(x);
    double subsurface = subsurfaceParam.evaluate(x);

    Color baseColor = baseColorParam.getColor(x, lambda);
    Color whiteColor = Spectrum.WHITE.sample(lambda);

    double baseLum = baseColor.luminance();
    Color tintColor = baseLum > MathUtil.TINY_EPSILON ? baseColor.divide(baseLum) : whiteColor;

    Color specColor = mix(mix(whiteColor, tintColor, specularTint).times(specular * 0.08), baseColor, metallic);
    Color sheenColor = mix(whiteColor, tintColor, sheenTint);

    Point3 p = x.getPosition();
    Basis3 sb = x.getShadingBasis();
    Vector3 n = sb.w();
    double ndotv = -n.dot(v);
    double fv = fresnel(ndotv);

    double wd = 0.5 * (1.0 - metallic);
    double ws = 0.5 * (1.0 + metallic) / (1.0 + clearcoat);
    double wc = 1.0 - wd - ws;

    if (rj < wd) {
      Ray3 ray = new Ray3(p, RandomUtil.diffuse(ru, rv).toCartesian(sb));

      Vector3 l = ray.direction();
      Vector3 h = v.opposite().plus(l).unit();
      double hdotv = -h.dot(v);
      double ndotl = n.dot(l);

      // Diffuse
      double fd90 = 0.5 + 2.0 * hdotv * hdotv * roughness;
      double fl = fresnel(ndotl);
      double fd = mix(1.0, fd90, fl) * mix(1.0, fd90, fv);

      // Hanrahan-Krueger-based scattering
      double fss90 = hdotv * hdotv * roughness;
      double fss = mix(1.0, fss90, fl) * mix(1.0, fss90, fv);
      double ss = 1.25 * (fss * (1.0 / (ndotl + ndotv) - 0.5) + 0.5);

      double fh = fresnel(hdotv);
      Color Fsheen = sheenColor.times(fh * sheen);

      Color color = baseColor.times((1.0 / Math.PI) * mix(fd, ss, subsurface)).plus(Fsheen).times(Math.PI / wd);
      return ScatteredRay.diffuse(ray, color, wd / Math.PI);

    } else if (rj < wd + ws) {
      double alpha = sqr(roughness);
      double aspect = FastMath.sqrt(1.0 - 0.9 * anisotropic);
      double ax = Math.max(MathUtil.BIG_EPSILON, alpha / aspect);
      double ay = Math.max(MathUtil.BIG_EPSILON, alpha * aspect);

      double k = FastMath.sqrt(rv / (1.0 - rv));
      Vector3 X = sb.u();
      Vector3 Y = sb.v();

      double cos = FastMath.cos(2.0 * Math.PI * ru);
      double sin = FastMath.sin(2.0 * Math.PI * ru);

      Vector3 h = X.times(k * ax * cos).plus(Y.times(k * ay * sin)).plus(n).unit();
      Vector3 l = Optics.reflect(v, h);
      if (x.getNormal().dot(l) < 0.0) {
        return null;
      }

      Ray3 ray = new Ray3(p, l);

      double hdotl = h.dot(l);
      double ndotl = n.dot(l);
      double fh = fresnel(hdotl);
      Color Fs = mix(specColor, whiteColor, fh);

      double Gs = ggxAniso(ndotl, l.dot(X), l.dot(Y), ax, ay) *
          ggxAniso(ndotv, -v.dot(X), -v.dot(Y), ax, ay);

      double ndoth = n.dot(h);
      double hdotx = h.dot(X);
      double hdoty = h.dot(Y);
      double Ds = gtr2Aniso(ndoth, hdotx, hdoty, ax, ay);

      return ScatteredRay.glossy(ray, Fs.times(Gs / (ws * ndoth)), ws * Ds * ndoth / (4.0 * hdotl));
    } else {
      double alpha = sqr(roughness);
      double a2 = sqr(alpha);

      double cos = FastMath.sqrt((1.0 - FastMath.pow(a2, 1.0 - rv)) / (1.0 - a2));
      double phi = 2.0 * Math.PI * ru;

      SphericalCoordinates hsph = new SphericalCoordinates(FastMath.acos(cos), phi);
      Vector3 h = hsph.toCartesian(sb);
      Vector3 l = Optics.reflect(v, h);
      if (x.getNormal().dot(l) < 0.0) {
        return null;
      }

      Ray3 ray = new Ray3(p, l);
      double ndoth = n.dot(h);
      double ndotl = n.dot(l);
      double hdotl = h.dot(l);
      double fh = fresnel(h.dot(l));
      double Dr = gtr1(ndoth, mix(0.1,0.001, clearcoatGloss));
      double Fr = mix(0.04, 1.0, fh);
      double Gr = ggx(ndotl, 0.25) * ggx(ndotv, .25);
      return ScatteredRay.specular(ray, whiteColor.times(Fr * Gr / (wc * ndoth)), wc * Dr * ndoth / (4.0 * hdotl));
    }
  }

  @Override
  public double getScatteringPDF(SurfacePoint x, Vector3 in, Vector3 out, boolean adjoint, WavelengthPacket lambda) {
    throw new UnimplementedException();
  }
}
