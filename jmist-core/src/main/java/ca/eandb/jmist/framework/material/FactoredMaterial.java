/**
 *
 */
package ca.eandb.jmist.framework.material;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Matrix;
import ca.eandb.jmist.math.MatrixBuffer;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.UnimplementedException;

/**
 * @author brad
 *
 */
public final class FactoredMaterial extends OpaqueMaterial {

  /** Serialization version ID. */
  private static final long serialVersionUID = -7099697194692635348L;

  private final Matrix F[];

  private final Matrix G[];

  private final Matrix u[][];

  private final Matrix v[][];

  private final Matrix Fc[];

  private final Matrix Gc[];

  private final ColorModel colorModel;

  public FactoredMaterial(Matrix F[], Matrix G[], Matrix u[][], Matrix v[][], Matrix Fc[], Matrix Gc[], ColorModel colorModel) {
    this.F = F;
    this.G = G;
    this.u = u;
    this.v = v;
    this.Fc = Fc;
    this.Gc = Gc;
    this.colorModel = colorModel;

//    for (int i = 0; i < F.length; i++) {
//      printMatrix(String.format("F[%d]", i), F[i]);
//    }
//    for (int i = 0; i < G.length; i++) {
//      printMatrix(String.format("G[%d]", i), G[i]);
//    }
//    for (int i = 0; i < Fc.length; i++) {
//      printMatrix(String.format("Fc[%d]", i), Fc[i]);
//    }
//    for (int i = 0; i < Gc.length; i++) {
//      printMatrix(String.format("Gc[%d]", i), Gc[i]);
//    }
//    System.exit(0);
  }
  public static void printMatrix(Matrix A) {
    printMatrix(A, 1.0);
  }

  public static void printMatrix(String name, Matrix A) {
    System.out.print(String.format("%s: ", name));
    printMatrix(A, 1.0);
  }

  public static void printMatrix(Matrix A, double c) {
    System.out.println("-----");
    for (int i = 0; i < A.rows(); i++) {
      for (int j = 0; j < A.columns(); j++) {
        System.out.print(String.format(" %6.3f", A.at(i, j) * c));
      }
      System.out.println();
    }
  }

  public static FactoredMaterial fromFacRep(BufferedReader r, ColorModel cm) throws IOException {
    String line;
    String param = null;
    int outerTerms = -1;
    int innerTerms = -1;
    int thetaOutCount = -1;
    int phiOutCount = -1;
    int thetaPCount = -1;
    int zCount = -1;
    int phiPCount = -1;

    double[] data = null;
    int index = 0;

    while ((line = r.readLine()) != null) {

      // skip empty lines
      line = line.trim();
      if (line.isEmpty()) {
        continue;
      }

      String entry[] = line.split(":", 2);

      if (entry.length == 2) { // header

        String key = entry[0].trim().toLowerCase();
        String value = entry[1].trim();

        if (key.equals("param")) {
          param = value.toLowerCase();
        } else if (key.startsWith("outer terms")) {
          outerTerms = Integer.valueOf(value);
        } else if (key.startsWith("inner terms")) {
          innerTerms = Integer.valueOf(value);
        } else if (key.equals("theta_out count")) {
          thetaOutCount = Integer.valueOf(value);
        } else if (key.equals("phi_out count")) {
          phiOutCount = Integer.valueOf(value);
        } else if (key.equals("theta_p count") || key.equals("theta_2 count")) {
          thetaPCount = Integer.valueOf(value);
        } else if (key.equals("z count")) {
          zCount = Integer.valueOf(value);
        } else if (key.equals("phi_p count") || key.equals("phi_2 count")) {
          phiPCount = Integer.valueOf(value);
        }

      } else if (entry.length == 1) { // data

        if (data == null) { // first line of data

          // Check that all header entries were present
          if (outerTerms <= 0 || innerTerms <= 0 || thetaOutCount <= 0 ||
              phiOutCount <= 0 || thetaPCount <= 0 || zCount <= 0 ||
              phiPCount <= 0 || param == null) {

            throw new IllegalArgumentException("Missing parameters (is file of correct format?)");

          }

          int count =
              outerTerms * (
                  thetaOutCount * phiOutCount +
                  thetaPCount * phiPCount +
                  innerTerms * (thetaPCount + phiPCount)) +
              0 * 3 * (thetaOutCount * phiOutCount + thetaPCount * phiPCount);

          System.out.println(String.format("outer terms: %d", outerTerms));
          System.out.println(String.format("inner terms: %d", innerTerms));
          System.out.println(String.format("theta_p count: %d", thetaPCount));
          System.out.println(String.format("phi_p count: %d", phiPCount));
          System.out.println(String.format("theta_out count: %d", thetaOutCount));
          System.out.println(String.format("phi_out count: %d", phiOutCount));

          System.out.println(String.format("Count = %d", count));
          data = new double[count];

        }

        String values[] = line.split("\\s+");
        for (String value : values) {
          if (index < data.length) {
            data[index] = Double.valueOf(value);
          }
          index++;
        }

      }

    }

    System.out.println(String.format("Expected data: %d", data.length));
    System.out.println(String.format("Actual data  : %d", index));

//    if (index > data.length) {
//      throw new IllegalArgumentException("Too much data");
//    }

    if (index < data.length) {
      throw new IllegalArgumentException("Not enough data");
    }

    Matrix F[] = new Matrix[outerTerms];
    Matrix G[] = new Matrix[outerTerms];
    Matrix u[][] = new Matrix[outerTerms][];
    Matrix v[][] = new Matrix[outerTerms][];

    Matrix Fc[] = new Matrix[3];
    Matrix Gc[] = new Matrix[3];

    index = 0;
    for (int i = 0; i < outerTerms; i++) {
      F[i] = new Matrix(new MatrixBuffer(
          data, thetaOutCount, phiOutCount, index, phiOutCount, 1));
      index += thetaOutCount * phiOutCount;

      G[i] = new Matrix(new MatrixBuffer(
          data, thetaPCount, phiPCount, index, phiPCount, 1));
      index += thetaPCount * phiPCount;

      u[i] = new Matrix[innerTerms];
      v[i] = new Matrix[innerTerms];

      for (int j = 0; j < innerTerms; j++) {
        u[i][j] = new Matrix(new MatrixBuffer(
            data, thetaPCount, 1, index, 1, 1));
        index += thetaPCount;

        v[i][j] = new Matrix(new MatrixBuffer(
            data, phiPCount, 1, index, 1, 1));
        index += phiPCount;
      }
    }

    int FcOffset = index;
    int GcOffset = index + thetaOutCount * phiOutCount * 3;

    for (int i = 0; i < 3; i++) {
      Fc[i] = new Matrix(new MatrixBuffer(
          data, thetaOutCount, phiOutCount, FcOffset + i, 3 * phiOutCount, 3));
      Gc[i] = new Matrix(new MatrixBuffer(
          data, thetaPCount, phiPCount, GcOffset + i, 3 * phiPCount, 3));
    }

    return new FactoredMaterial(F, G, u, v, Fc, Gc, cm);

  }

  public static FactoredMaterial fromFacRep(Reader r, ColorModel cm) throws IOException {
    if (r instanceof BufferedReader) {
      return fromFacRep((BufferedReader) r, cm);
    } else {
      return fromFacRep(new BufferedReader(r), cm);
    }
  }

  public static FactoredMaterial fromFacRep(InputStream is, ColorModel cm) throws IOException {
    return fromFacRep(new InputStreamReader(is), cm);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.material.AbstractMaterial#bsdf(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
   */
  @Override
  public Color bsdf(SurfacePoint x, Vector3 in, Vector3 out,
      WavelengthPacket lambda) {

    Vector3 p = out.unit().minus(in.unit()).unit();
    Basis3 basis = x.getShadingBasis();
    SphericalCoordinates omegaO = SphericalCoordinates.fromCartesian(out, basis).canonical();
    SphericalCoordinates omegaP = SphericalCoordinates.fromCartesian(p, basis).canonical();

    double NdotI = -basis.w().dot(in);
    double NdotO = basis.w().dot(out);
    double spf = 0.0;

    if (NdotI * NdotO < 0.0) {
      return lambda.getColorModel().getBlack(lambda);
    }

    double phiO = omegaO.azimuthal() < 0.0 ? omegaO.azimuthal() + 2.0 * Math.PI : omegaO.azimuthal();
    double phiP = omegaP.azimuthal() < 0.0 ? omegaP.azimuthal() + 2.0 * Math.PI : omegaP.azimuthal();
    phiP -= phiO;
    if (phiP < 0.0) { phiP += 2.0 * Math.PI; }

    int J = F.length;
    for (int j = 0; j < J; j++) {
      double Fj = MathUtil.bilinearInterpolate(
          0, 0.5 * Math.PI, 0.0, 2.0 * Math.PI, F[j],
          omegaO.polar(), phiO, false, true);

//      double t = (double) (F[j].rows() - 1) * omegaO.polar() / (0.5 * Math.PI);
//      if (Math.abs(t - Math.round(t)) < 0.05) {
//        Fj = 0;
//      }

//      double Gj = 0.0;
//
//      int K = u[j].length;
//      for (int k = 0; k < K; k++) {
//        double ujk = MathUtil.interpolate(0, 0.5 * Math.PI, u[j][k].elements(), omegaP.polar());
//        double vjk = MathUtil.interpolate(0, 2.0 * Math.PI, v[j][k].elements(), phiP);
//
//        Gj += ujk * vjk;
//      }
      double Gj = MathUtil.bilinearInterpolate(
          0, 0.5 * Math.PI, 0.0, 2.0 * Math.PI, G[j],
          omegaP.polar(), phiP, false, true);

      spf += Fj * Gj;
    }

    double intensity = spf / NdotI;

    double Fr = MathUtil.bilinearInterpolate(
        0, 0.5 * Math.PI, 0.0, 2.0 * Math.PI, Fc[0],
        omegaO.polar(), phiO);
    double Fg = MathUtil.bilinearInterpolate(
        0, 0.5 * Math.PI, 0.0, 2.0 * Math.PI, Fc[1],
        omegaO.polar(), phiO);
    double Fb = MathUtil.bilinearInterpolate(
        0, 0.5 * Math.PI, 0.0, 2.0 * Math.PI, Fc[2],
        omegaO.polar(), phiO);
    double Gr = MathUtil.bilinearInterpolate(
        0, 0.5 * Math.PI, 0.0, 2.0 * Math.PI, Gc[0],
        omegaP.polar(), phiP);
    double Gg = MathUtil.bilinearInterpolate(
        0, 0.5 * Math.PI, 0.0, 2.0 * Math.PI, Gc[1],
        omegaP.polar(), phiP);
    double Gb = MathUtil.bilinearInterpolate(
        0, 0.5 * Math.PI, 0.0, 2.0 * Math.PI, Gc[2],
        omegaP.polar(), phiP);

    double r = Fr * Gr * intensity;
    double g = Fg * Gg * intensity;
    double b = Fb * Gb * intensity;
//    double r = intensity;
//    double g = intensity;
//    double b = intensity;
//
//    intensity = Math.log(intensity + 1);

//    double dist = Math.abs(intensity - (1.0 / Math.PI));
//    return colorModel.getGray(Math.max(1.0 - dist,  0.0), lambda);
//    if (dist < 0.05) {
//      return colorModel.fromRGB(1.0, 0.0, 0.0).sample(lambda);
//    } else if (dist < 0.1) {
//      return colorModel.fromRGB(0.0, 1.0, 0.0).sample(lambda);
//    }
//
//
    return colorModel.fromRGB(r, g, b).sample(lambda);

  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
   */
  @Override
  public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
      WavelengthPacket lambda, double ru, double rv, double rj) {
    throw new UnimplementedException();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.material.AbstractMaterial#getScatteringPDF(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket)
   */
  @Override
  public double getScatteringPDF(SurfacePoint x, Vector3 in, Vector3 out,
      boolean adjoint, WavelengthPacket lambda) {
    throw new UnimplementedException();
  }

}
