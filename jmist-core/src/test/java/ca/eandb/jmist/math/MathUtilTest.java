package ca.eandb.jmist.math;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class MathUtilTest {

  @Test
  void testInterpolate() {
    double[] xs = new double[]{1.0, 2.0, 4.0, 8.0};
    double[] ys = new double[]{1.0, 4.0, 16.0, 64.0};
    List<Double> xlist = new ArrayList<>();
    List<Double> ylist = new ArrayList<>();
    for (int i = 0; i < xs.length; i++) {
      xlist.add(xs[i]);
      ylist.add(ys[i]);
    }
    int steps = 5;
    for (int i = 0; i < xs.length - 1; i++) {
      for (int si = 0; si <= steps; si++) {
        double b = (double) si / (double) steps;
        double a = 1.0 - b;
        double x = xs[i] * a + xs[i + 1] * b;
        double want = ys[i] * a + ys[i + 1] * b;
        double got = MathUtil.interpolate(xs, ys, x);
        assertTrue(MathUtil.equal(got, want),
            () -> String.format("interpolate(xs, ys, %f) = %f, want %f", x, got, want));
        double gotList = MathUtil.interpolate(xlist, ylist, x);
        assertTrue(MathUtil.equal(gotList, want),
            () -> String.format("interpolate(xlist, ylist, %f) = %f, want %f", x, gotList, want));
      }
    }
  }

  @Test
  void testInterpolateWrapped() {
    double[] xs = new double[]{1.0, 2.0, 4.0, 8.0};
    double[] ys = new double[]{1.0, 4.0, 16.0};
    double[] ys0 = new double[]{1.0, 4.0, 16.0, 1.0};
    List<Double> xlist = new ArrayList<>();
    List<Double> ylist = new ArrayList<>();
    for (int i = 0; i < xs.length; i++) {
      xlist.add(xs[i]);
    }
    for (int i = 0; i < ys.length; i++) {
      ylist.add(ys[i]);
    }
    int steps = 5;
    for (int i = 0; i < xs.length - 1; i++) {
      for (int si = 0; si <= steps; si++) {
        double b = (double) si / (double) steps;
        double a = 1.0 - b;
        double x = xs[i] * a + xs[i + 1] * b;
        double want = MathUtil.interpolate(xs, ys0, x);
        double got = MathUtil.interpolateWrapped(xs, ys, x);
        assertTrue(MathUtil.equal(got, want),
            () -> String.format("interpolateWrapped(xs, ys, %f) = %f, want %f", x, got, want));
        double gotList = MathUtil.interpolateWrapped(xlist, ylist, x);
        assertTrue(MathUtil.equal(gotList, want),
            () -> String.format("interpolateWrapped(xlist, ylist, %f) = %f, want %f", x, gotList, want));
      }
    }
  }

  @Test
  void testBilinearInterpolate() {
    double[] xs0 = new double[]{1.0, 2.0, 3.0};
    double[] ys0 = new double[]{11.0, 12.0, 13.0};
    double[] xs1 = new double[]{1.0, 2.0, 3.0, 4.0};
    double[] ys1 = new double[]{11.0, 12.0, 13.0, 14.0};
    boolean[] bools = new boolean[]{false, true};
    List<Double> xlist0 = new ArrayList<>();
    List<Double> ylist0 = new ArrayList<>();
    List<Double> xlist1 = new ArrayList<>();
    List<Double> ylist1 = new ArrayList<>();
    for (double x : xs0) {
      xlist0.add(x);
    }
    for (double y : ys0) {
      ylist0.add(y);
    }
    for (double x : xs1) {
      xlist1.add(x);
    }
    for (double y : ys1) {
      ylist1.add(y);
    }
    MatrixBuffer buf = MatrixBuffer.rowMajor(3, 3);
    buf.set(0, 0, 1);
    buf.set(0, 1, 2);
    buf.set(0, 2, 3);
    buf.set(1, 0, 4);
    buf.set(1, 1, 5);
    buf.set(1, 2, 6);
    buf.set(2, 0, 7);
    buf.set(2, 1, 8);
    buf.set(2, 2, 9);
    Matrix z = new Matrix(buf);
    int steps = 5;
    for (boolean wrapX : bools) {
      double[] xs = wrapX ? xs1 : xs0;
      List<Double> xlist = wrapX ? xlist1 : xlist0;
      for (boolean wrapY : bools) {
        double[] ys = wrapY ? ys1 : ys0;
        List<Double> ylist = wrapY ? ylist1 : ylist0;
        for (int i = 0; i < xs.length - 1; i++) {
          for (int j = 0; j < ys.length - 1; j++) {
            for (int si = 0; si <= steps; si++) {
              for (int sj = 0; sj <= steps; sj++) {
                double bi = (double) si / (double) steps;
                double bj = (double) sj / (double) steps;
                double ai = 1.0 - bi;
                double aj = 1.0 - bj;
                int i1 = (i + 1) % z.rows();
                int j1 = (j + 1) % z.columns();
                double want = z.at(i, j) * ai * aj + z.at(i1, j) * bi * aj +
                              z.at(i, j1) * ai * bj + z.at(i1, j1) * bi * bj;
                double x = xs[i] * ai + xs[i1] * bi;
                double y = ys[j] * aj + ys[j1] * bj;
                double got = MathUtil.bilinearInterpolate(xs, ys, z, x, y, wrapX, wrapY);
                assertTrue(MathUtil.equal(got, want),
                    () -> String.format("bilinearInterpolate(xs, ys, z, %f, %f, %b, %b) = %f, want %f", x, y, wrapX, wrapY, got, want));
                double gotList = MathUtil.bilinearInterpolate(xlist, ylist, z, x, y, wrapX, wrapY);
                assertTrue(MathUtil.equal(gotList, want),
                    () -> String.format("bilinearInterpolate(xlist, ylist, z, %f, %f, %b, %b) = %f, want %f", x, y, wrapX, wrapY, gotList, want));
              }
            }
          }
        }
      }
    }
  }

}
