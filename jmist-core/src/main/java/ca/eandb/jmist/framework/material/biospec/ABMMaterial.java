/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
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
package ca.eandb.jmist.framework.material.biospec;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.function.ConstantFunction1;
import ca.eandb.jmist.framework.function.PiecewiseLinearFunction1;
import ca.eandb.jmist.framework.function.ScaledFunction1;
import ca.eandb.jmist.framework.function.SumFunction1;
import ca.eandb.jmist.framework.material.OpaqueMaterial;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.jmist.util.ArrayUtil;

/**
 * A <code>Material</code> implementing the ABM-U/ABM-B light transport
 * models.
 * <p>
 * The algorithm implemented here is described in:
 * <ul>
 *   <li>
 *     G.V.G. Baranoski,
 *     <a href="http://www.npsg.uwaterloo.ca/resources/docs/rse2006.pdf">
 *       Modeling the interaction of infrared raditaion (750 to 2500 nm) with
 *       bifacial and unifacial plant leaves</a>,
 *     Remote Sensing of Environment 100:335-347, 2006.
 *   </li>
 *   <li>
 *     G.V.G. Baranoski, D. Eng,
 *     <a href="http://www.npsg.uwaterloo.ca/resources/docs/ieee07-8.pdf">
 *       An investigation on sieve and detour effects affecting the interaction
 *       of collimated and diffuse infrared radiation (750 to 2500 nm) with
 *       plant leaves</a>,
 *     IEEE Transactions on Geoscience and Remote Sensing 45(8):2593-2599,
 *     August 2007.
 *   </li>
 * </ul>
 *
 * @author Brad Kimmel
 * @see <a href="http://www.npsg.uwaterloo.ca/models/ABMU.php">Run ABM-U Online</a>
 * @see <a href="http://www.npsg.uwaterloo.ca/models/ABMB.php">Run ABM-B Online</a>
 */
public final class ABMMaterial extends OpaqueMaterial {

  /** Serialization version ID. */
  private static final long serialVersionUID = 969983861047935903L;

  /**
   * An absorbing <code>Material</code> whose thickness may be
   * varied at runtime on a per-thread basis.  This allows the thickness to
   * be determined randomly for each call to
   * <code>ABMMaterial.scatter</code>.
   * @author Brad Kimmel
   */
  private static final class VariableThicknessAbsorbingMaterial
      extends OpaqueMaterial {

    /** Serialization version ID. */
    private static final long serialVersionUID = 4413776349916702845L;

    /**
     * The <code>Function1</code> indicating the absorption coefficient
     * (in m<sup>-1</sup>).
     */
    private final Spectrum absorptionCoefficient;

    /** The per-thread thickness of the absorbing medium (in meters). */
    private final ThreadLocal<Double> thickness = new ThreadLocal<Double>();

    /**
     * Creates a new
     * <code>VariableThicknessAbsorbingMaterial</code>.
     * @param absorptionCoefficient The <code>Function1</code> indicating
     *    the absorption coefficient of the medium (in m<sup>-1</sup>).
     */
    public VariableThicknessAbsorbingMaterial(Spectrum absorptionCoefficient) {
      this.absorptionCoefficient = absorptionCoefficient;
    }

    /**
     * Sets the thickness of the absorbing medium for the current thread.
     * @param thickness The new thickness of the absorbing medium (in
     *     meters).
     */
    public void setThickness(double thickness) {
      this.thickness.set(thickness);
    }


    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
     */
    @Override
    public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
        WavelengthPacket lambda, double ru, double rv, double rj) {

      Color col = absorptionCoefficient.sample(lambda);
//      double abs = ColorUtil.getMeanChannelValue(col);
//
//      if (abs > MathUtil.EPSILON) {
//        double p = -Math.log(1.0 - rnd.next()) * Math.cos(Math.abs(x.getNormal().dot(v))) / abs;
//
//        col = col.times(-thickness).exp();
//        col = col.divide(ColorUtil.getMeanChannelValue(col));
//
//        if (p > thickness) {
//          return ScatteredRay.transmitSpecular(new Ray3(x.getPosition(), v), col, 1.0);
//        }
//      }
//
//      return null;

      col = col.times(-thickness.get() / Math.cos(Math.abs(x.getNormal().dot(v)))).exp();
      return ScatteredRay.transmitSpecular(new Ray3(x.getPosition(), v), col, 1.0);

    }

  }

  /** Array of wavelengths for the associated with the data to follow. */
  private static final double[] WAVELENGTHS = ArrayUtil.range(400e-9, 700e-9, 61); // m

  /**
   * Specific absorption coefficient for chlorophyll a+b (in
   * m<sup>2</sup> kg<sup>-1</sup>).
   */
  private static final double[] SAC_CHLOROPHYLL_AB_VALUES = { // cm^2/g (to be converted)
    73400, 67700, 62300, 58000, 55600, 53400, 52700, 52200,
    51000, 49400, 46700, 44400, 43500, 43500, 43000, 42600,
    42100, 41300, 40000, 37900, 34800, 30700, 26500, 22100,
    18300, 15400, 13600, 12700, 12200, 12000, 11800, 11800,
    12200, 13000, 14200, 15600, 16800, 17800, 18500, 18900,
    19200, 19800, 20700, 21900, 22900, 23400, 23600, 24000,
    25300, 27400, 29400, 30600, 32700, 36100, 38900, 40500,
    40300, 36900, 28200, 19100, 12600
  };

  /**
   * Specific absorption coefficient for carotenoids (in
   * m<sup>2</sup> kg<sup>-1</sup>).
   */
  private static final double[] SAC_CAROTENOIDS_VALUES = { // cm^2/g (to be converted)
     6535.506923,  7207.118815,  8017.169001,  8559.258518,
     9328.970969,  9942.106999, 10669.577847, 11562.119164,
    11975.755432, 12370.904604, 12843.907698, 13162.723765,
    13594.276407, 14057.034526, 14381.579285, 14550.187782,
    14490.659508, 14248.260257, 13773.149034, 13667.762322,
    13620.968781, 13177.895459, 12539.340804, 10989.320278,
     8895.380246,  6936.325813,  4544.066850,  3170.884351,
     2333.883282,  1824.555971,  1519.712737,  1309.073662,
     1164.526320,  1068.505480,  1011.292774,   958.240154,
      958.240154,   958.240154,   947.933115,   921.314601,
      901.556158,   883.250517,   875.085062,   875.085062,
      873.419624,   867.527208,   861.634792,   855.742376,
      849.849960,   843.957544,   838.065128,   834.592308,
      841.777197,   848.962085,   856.146973,   863.331861,
      870.516749,   881.011273,   897.284130,   913.556987,
      894.049970
  };

  /** Specific absorption coefficient for water (in m<sup>-1</sup>). */
  private static final double[] SAC_WATER_VALUES = { // cm^-1 (to be converted)
    0.000066, 0.000053, 0.000047, 0.000044,
    0.000045, 0.000048, 0.000049, 0.000053,
    0.000063, 0.000075, 0.000092, 0.000096,
    0.000098, 0.000101, 0.000106, 0.000114,
    0.000127, 0.000136, 0.000150, 0.000173,
    0.000204, 0.000256, 0.000325, 0.000396,
    0.000409, 0.000417, 0.000434, 0.000452,
    0.000474, 0.000511, 0.000565, 0.000596,
    0.000619, 0.000642, 0.000695, 0.000772,
    0.000896, 0.001100, 0.001351, 0.001672,
    0.002224, 0.002577, 0.002644, 0.002678,
    0.002755, 0.002834, 0.002916, 0.003012,
    0.003108, 0.003250, 0.003400, 0.003710,
    0.004100, 0.004290, 0.004390, 0.004480,
    0.004650, 0.004860, 0.005160, 0.005590,
    0.006240
  };

  /** The index of refraction for water. */
  public static final double[] IOR_WATER_VALUES = {
    1.346, 1.345, 1.344, 1.343, 1.342, 1.341, 1.340, 1.339,
    1.338, 1.338, 1.337, 1.336, 1.336, 1.335, 1.335, 1.334,
    1.334, 1.334, 1.334, 1.333, 1.333, 1.333, 1.333, 1.333,
    1.333, 1.333, 1.333, 1.333, 1.333, 1.333, 1.333, 1.333,
    1.333, 1.333, 1.333, 1.333, 1.333, 1.333, 1.333, 1.333,
    1.333, 1.333, 1.333, 1.333, 1.333, 1.332, 1.332, 1.332,
    1.332, 1.332, 1.332, 1.332, 1.332, 1.332, 1.332, 1.332,
    1.332, 1.332, 1.332, 1.332, 1.332
  };

  /** The index of refraction for the cuticle. */
  public static final double[] IOR_CUTICLE_VALUES = {
    1.539712, 1.539678, 1.537372, 1.536209,
    1.534937, 1.533561, 1.532009, 1.530511,
    1.529224, 1.528078, 1.527108, 1.526012,
    1.524844, 1.523591, 1.522421, 1.521544,
    1.520067, 1.519138, 1.518554, 1.517828,
    1.517050, 1.516272, 1.515421, 1.514547,
    1.513748, 1.513030, 1.512445, 1.511623,
    1.510911, 1.510230, 1.509549, 1.508952,
    1.508368, 1.507785, 1.507203, 1.506620,
    1.505908, 1.505078, 1.504297, 1.503139,
    1.502896, 1.502662, 1.502248, 1.501666,
    1.501087, 1.500568, 1.500049, 1.499530,
    1.499011, 1.498409, 1.497752, 1.497096,
    1.496439, 1.495974, 1.495609, 1.495244,
    1.494879, 1.494510, 1.494139, 1.493768,
    1.493398
  };

  static {

    // convert everything to base SI units
    for (int i = 0; i < WAVELENGTHS.length; i++) {

      // convert cm^2/g to m^2/kg  (divide by 10)
      SAC_CHLOROPHYLL_AB_VALUES[i] /= 10.0;
      SAC_CAROTENOIDS_VALUES[i] /= 10.0;

      // convert cm^-1 to m^-1  (multiply by 100)
      SAC_WATER_VALUES[i] *= 100.0;

    }

  }

  /**
   * Specific absorption coefficient of chlorophyll a+b (in
   * m<sup>2</sup> kg<sup>-1</sup>).
   */
  private static final Function1 SAC_CHLOROPHYLL_AB = new PiecewiseLinearFunction1(
      WAVELENGTHS, SAC_CHLOROPHYLL_AB_VALUES);

  /**
   * Specific absorption coefficient of carotenoids (in
   * m<sup>2</sup> kg<sup>-1</sup>).
   */
  private static final Function1 SAC_CAROTENOIDS = new PiecewiseLinearFunction1(
      WAVELENGTHS, SAC_CAROTENOIDS_VALUES);

  /** Absorption coefficient of water (in m<sup>-1</sup>). */
  private static final Function1 SAC_WATER = new PiecewiseLinearFunction1(
      WAVELENGTHS, SAC_WATER_VALUES);

  /** Index of refraction for water. */
  private static final Function1 IOR_WATER = new PiecewiseLinearFunction1(
      WAVELENGTHS, IOR_WATER_VALUES);

  /** Index of refraction for the cuticle. */
  private static final Function1 IOR_CUTICLE = new PiecewiseLinearFunction1(
      WAVELENGTHS, IOR_CUTICLE_VALUES);

  /** Index of refraction for air. */
  private static final Function1 IOR_AIR = Function1.ONE;

  /**
   * Specific absorption coefficient for protein (in
   * m<sup>2</sup> kg<sup>-1</sup>).
   */
  private static final double SAC_PROTEIN = 1.992; // m^2/kg

  /**
   * Specific absorption coefficient for cellulose+lignin (in
   * m<sup>2</sup> kg<sup>-1</sup>).
   */
  private static final double SAC_CELLULOSE_LIGNIN = 0.876; // m^2/kg

  /** Aspect ratio of cuticle undulations. */
  private double cuticleUndulationsAspectRatio = 5.0;

  /** Aspect ratio of cell caps. */
  private double epidermisCellCapsAspectRatio = 5.0;

  /** Aspect ratio of palisade mesophyll cell caps. */
  private double palisadeCellCapsAspectRatio = 1.0;

  /** Aspect ratio of spongy mesophyll cell caps. */
  private double spongyCellCapsAspectRatio = 5.0;

  /** Thickness of the whole leaf (in meters). */
  private double wholeLeafThickness = 1.66e-4; // meters

  /** Bulk density of the whole leaf (in kg m<sup>-3</sup>). */
  private double dryBulkDensity = 1.19e-5 / (4.1e-4 * 1.66e-4); // kg/m^3

  private double airVolumeFraction = 0.31;
  private double proteinFraction = 0.0;// 0.3106;
  private double celluloseFraction = 0.0;// 0.1490;
  private double ligninFraction = 0.0;// 0.0424;

  /**
   * A value indicating if the leaf is bifacial.  If so, the ABM-B model is
   * used.  Otherwise, ABM-U is used.
   */
  private boolean bifacial = true;

  private double scattererFractionInAntidermalWall = 0.3872;
  private double scattererFractionInMesophyll = 0.3872;

  private double concChlorophyllAInMesophyll = 3.978; // kg/m^3

  private double concChlorophyllBInMesophyll = 1.161; // kg/m^3

  private double concCarotenoidsInMesophyll = 1.132; // kg/m^3;

  /**
   * Total thickness of the mesophyll layers (in meters).  This value is set
   * internally, rather than directly by the user.
   */
  private double mesophyllThickness;

  /** The upper spongy mesophyll layer used in the ABM-U implementation. */
  private VariableThicknessAbsorbingMaterial topSpongyMesophyllLayer;

  /** The lower spongy mesophyll layer used in the ABM-U implementation. */
  private VariableThicknessAbsorbingMaterial bottomSpongyMesophyllLayer;

  /** The <code>LayeredMaterial</code> representing the ABM model. */
  private final LayeredMaterial subsurface = new LayeredMaterial();

  private final ColorModel colorModel;

  public ABMMaterial(ColorModel colorModel) {
    this.colorModel = colorModel;
  }

  /**
   * @return the cuticleUndulationsAspectRatio
   */
  public double getCuticleUndulationsAspectRatio() {
    return cuticleUndulationsAspectRatio;
  }


  /**
   * @param cuticleUndulationsAspectRatio the cuticleUndulationsAspectRatio to set
   */
  public void setCuticleUndulationsAspectRatio(
      double cuticleUndulationsAspectRatio) {
    this.cuticleUndulationsAspectRatio = cuticleUndulationsAspectRatio;
  }


  /**
   * @return the epidermisCellCapsAspectRatio
   */
  public double getEpidermisCellCapsAspectRatio() {
    return epidermisCellCapsAspectRatio;
  }


  /**
   * @param epidermisCellCapsAspectRatio the epidermisCellCapsAspectRatio to set
   */
  public void setEpidermisCellCapsAspectRatio(double epidermisCellCapsAspectRatio) {
    this.epidermisCellCapsAspectRatio = epidermisCellCapsAspectRatio;
  }


  /**
   * @return the palisadeCellCapsAspectRatio
   */
  public double getPalisadeCellCapsAspectRatio() {
    return palisadeCellCapsAspectRatio;
  }


  /**
   * @param palisadeCellCapsAspectRatio the palisadeCellCapsAspectRatio to set
   */
  public void setPalisadeCellCapsAspectRatio(double palisadeCellCapsAspectRatio) {
    this.palisadeCellCapsAspectRatio = palisadeCellCapsAspectRatio;
  }


  /**
   * @return the spongyCellCapsAspectRatio
   */
  public double getSpongyCellCapsAspectRatio() {
    return spongyCellCapsAspectRatio;
  }


  /**
   * @param spongyCellCapsAspectRatio the spongyCellCapsAspectRatio to set
   */
  public void setSpongyCellCapsAspectRatio(double spongyCellCapsAspectRatio) {
    this.spongyCellCapsAspectRatio = spongyCellCapsAspectRatio;
  }


  /**
   * @return the wholeLeafThickness
   */
  public double getWholeLeafThickness() {
    return wholeLeafThickness;
  }


  /**
   * @param wholeLeafThickness the wholeLeafThickness to set
   */
  public void setWholeLeafThickness(double wholeLeafThickness) {
    this.wholeLeafThickness = wholeLeafThickness;
  }


  /**
   * @return the dryBulkDensity
   */
  public double getDryBulkDensity() {
    return dryBulkDensity;
  }


  /**
   * @param dryBulkDensity the dryBulkDensity to set
   */
  public void setDryBulkDensity(double dryBulkDensity) {
    this.dryBulkDensity = dryBulkDensity;
  }


  /**
   * @return the airVolumeFraction
   */
  public double getAirVolumeFraction() {
    return airVolumeFraction;
  }


  /**
   * @param airVolumeFraction the airVolumeFraction to set
   */
  public void setAirVolumeFraction(double airVolumeFraction) {
    this.airVolumeFraction = airVolumeFraction;
  }


  /**
   * @return the proteinFraction
   */
  public double getProteinFraction() {
    return proteinFraction;
  }


  /**
   * @param proteinFraction the proteinFraction to set
   */
  public void setProteinFraction(double proteinFraction) {
    this.proteinFraction = proteinFraction;
  }


  /**
   * @return the celluloseFraction
   */
  public double getCelluloseFraction() {
    return celluloseFraction;
  }


  /**
   * @param celluloseFraction the celluloseFraction to set
   */
  public void setCelluloseFraction(double celluloseFraction) {
    this.celluloseFraction = celluloseFraction;
  }


  /**
   * @return the ligninFraction
   */
  public double getLigninFraction() {
    return ligninFraction;
  }


  /**
   * @param ligninFraction the ligninFraction to set
   */
  public void setLigninFraction(double ligninFraction) {
    this.ligninFraction = ligninFraction;
  }


  /**
   * @return the bifacial
   */
  public boolean isBifacial() {
    return bifacial;
  }


  /**
   * @param bifacial the bifacial to set
   */
  public void setBifacial(boolean bifacial) {
    this.bifacial = bifacial;
  }


  /**
   * @return the scattererFractionInAntidermalWall
   */
  public double getScattererFractionInAntidermalWall() {
    return scattererFractionInAntidermalWall;
  }


  /**
   * @param scattererFractionInAntidermalWall the scattererFractionInAntidermalWall to set
   */
  public void setScattererFractionInAntidermalWall(
      double scattererFractionInAntidermalWall) {
    this.scattererFractionInAntidermalWall = scattererFractionInAntidermalWall;
  }


  /**
   * @return the scattererFractionInMesophyll
   */
  public double getScattererFractionInMesophyll() {
    return scattererFractionInMesophyll;
  }


  /**
   * @param scattererFractionInMesophyll the scattererFractionInMesophyll to set
   */
  public void setScattererFractionInMesophyll(double scattererFractionInMesophyll) {
    this.scattererFractionInMesophyll = scattererFractionInMesophyll;
  }


  /**
   * @return the concChlorophyllAInMesophyll
   */
  public double getConcChlorophyllAInMesophyll() {
    return concChlorophyllAInMesophyll;
  }


  /**
   * @param concChlorophyllAInMesophyll the concChlorophyllAInMesophyll to set
   */
  public void setConcChlorophyllAInMesophyll(double concChlorophyllAInMesophyll) {
    this.concChlorophyllAInMesophyll = concChlorophyllAInMesophyll;
  }


  /**
   * @return the concChlorophyllBInMesophyll
   */
  public double getConcChlorophyllBInMesophyll() {
    return concChlorophyllBInMesophyll;
  }


  /**
   * @param concChlorophyllBInMesophyll the concChlorophyllBInMesophyll to set
   */
  public void setConcChlorophyllBInMesophyll(double concChlorophyllBInMesophyll) {
    this.concChlorophyllBInMesophyll = concChlorophyllBInMesophyll;
  }


  /**
   * @return the concCarotenoidsInMesophyll
   */
  public double getConcCarotenoidsInMesophyll() {
    return concCarotenoidsInMesophyll;
  }


  /**
   * @param concCarotenoidsInMesophyll the concCarotenoidsInMesophyll to set
   */
  public void setConcCarotenoidsInMesophyll(double concCarotenoidsInMesophyll) {
    this.concCarotenoidsInMesophyll = concCarotenoidsInMesophyll;
  }

  /**
   * Populates the <code>LayeredMaterial</code> according to the ABM
   * model from the model parameters.
   */
  private synchronized void build() {
    ColorModel cm = colorModel;
    subsurface.clear();

//    Function1 iorMesophyll = new AXpBFunction1(
//        (1.0 - scattererFractionInMesophyll),
//        1.5608 * scattererFractionInMesophyll,
//        IOR_WATER);

    double iorMesophyll = 1.415;

    double iorAntidermalWall = (1.0 - scattererFractionInAntidermalWall)
        * MathUtil.mean(IOR_WATER_VALUES) + 1.535
        * scattererFractionInAntidermalWall;

    double concDryMatter = dryBulkDensity / (1.0 - airVolumeFraction);

    double concProtein = concDryMatter * proteinFraction;
    double concCellulose = concDryMatter * celluloseFraction;
    double concLignin = concDryMatter * ligninFraction;

    double absProtein = concProtein * SAC_PROTEIN;
    double absCellulose = concCellulose * SAC_CELLULOSE_LIGNIN;
    double absLignin = concLignin * SAC_CELLULOSE_LIGNIN;

    Function1 mesophyllAbsorptionCoefficient = new SumFunction1()
      .addChild(new ScaledFunction1(
          concChlorophyllAInMesophyll + concChlorophyllBInMesophyll,
          SAC_CHLOROPHYLL_AB))
      .addChild(new ScaledFunction1(
          concCarotenoidsInMesophyll,
          SAC_CAROTENOIDS))
      .addChild(new ConstantFunction1(absProtein + absCellulose + absLignin))
      .addChild(SAC_WATER);

//    try {
//      OutputStream file = new FileOutputStream("/Users/brad/mesosac.csv");
//      PrintStream out = new PrintStream(new CompositeOutputStream()
//          .addChild(System.out)
//          .addChild(file));
//      for (int i = 400; i <= 700; i += 5) {
//        out.println(mesophyllAbsorptionCoefficient.evaluate(1e-9 * (double) i));
//      }
//      out.flush();
//      file.close();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

    double mesophyllFraction = bifacial ? 0.5 : 0.8;
    mesophyllThickness = mesophyllFraction * wholeLeafThickness;

    double lambda = 550e-9;
    System.out.printf("mesophyllAbsorptionCoefficient=%f", mesophyllAbsorptionCoefficient.evaluate(lambda));
    System.out.println();
    System.out.printf("mesophyllThickness=%f", mesophyllThickness);
    System.out.println();
    System.out.printf("mesophyllOpticalDepth=%f", mesophyllAbsorptionCoefficient.evaluate(lambda) * mesophyllThickness);
    System.out.println();
    System.out.printf("nCuticle=%f", IOR_CUTICLE.evaluate(lambda));
    System.out.println();
    System.out.printf("nWater=%f", IOR_WATER.evaluate(lambda));
    System.out.println();
    System.out.printf("sacWater=%f", SAC_WATER.evaluate(lambda));
    System.out.println();
    System.out.printf("nWall=%f", iorAntidermalWall);
    System.out.println();
    System.out.printf("nMesophyll=%f", iorMesophyll);
    System.out.println();
    System.out.printf("dryBulkDensity=%f", dryBulkDensity);
    System.out.println();

    double iorCuticle = MathUtil.mean(IOR_CUTICLE_VALUES);
    double iorWater = MathUtil.mean(IOR_WATER_VALUES);

    if (bifacial) {
      subsurface
        .addLayerToBottom(new ABMInterfaceMaterial(
            iorCuticle, 1.0,
            cuticleUndulationsAspectRatio,
            epidermisCellCapsAspectRatio,
            Double.POSITIVE_INFINITY,
            epidermisCellCapsAspectRatio))
        .addLayerToBottom(new ABMInterfaceMaterial(
            iorMesophyll, iorCuticle,
            epidermisCellCapsAspectRatio,
            palisadeCellCapsAspectRatio,
            epidermisCellCapsAspectRatio,
            palisadeCellCapsAspectRatio))
        .addLayerToBottom(new ABMSieveAbsorbingMaterial(
            cm.getContinuous(mesophyllAbsorptionCoefficient),
            mesophyllThickness))
        .addLayerToBottom(new ABMInterfaceMaterial(
            1.0, iorMesophyll,
            palisadeCellCapsAspectRatio,
            spongyCellCapsAspectRatio,
            palisadeCellCapsAspectRatio,
            spongyCellCapsAspectRatio))
        .addLayerToBottom(new ABMInterfaceMaterial(
            iorAntidermalWall, 1.0,
            Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY))
        .addLayerToBottom(new ABMInterfaceMaterial(
            iorCuticle, iorAntidermalWall,
            Double.POSITIVE_INFINITY,
            epidermisCellCapsAspectRatio,
            Double.POSITIVE_INFINITY,
            epidermisCellCapsAspectRatio))
        .addLayerToBottom(new ABMInterfaceMaterial(
            1.0, iorCuticle,
            epidermisCellCapsAspectRatio,
            Double.POSITIVE_INFINITY,
            epidermisCellCapsAspectRatio,
            cuticleUndulationsAspectRatio));
    } else { // unifacial
      topSpongyMesophyllLayer = new VariableThicknessAbsorbingMaterial(
          cm.getContinuous(mesophyllAbsorptionCoefficient));
      bottomSpongyMesophyllLayer = new VariableThicknessAbsorbingMaterial(
          cm.getContinuous(mesophyllAbsorptionCoefficient));
      subsurface
        .addLayerToBottom(new ABMInterfaceMaterial(
            iorCuticle, 1.0,
            cuticleUndulationsAspectRatio,  // \/
            epidermisCellCapsAspectRatio,   // \
            Double.POSITIVE_INFINITY,       // /
            epidermisCellCapsAspectRatio    // /\
            ))
        .addLayerToBottom(new ABMInterfaceMaterial(
            iorMesophyll, iorCuticle,
            epidermisCellCapsAspectRatio,
            spongyCellCapsAspectRatio,
            epidermisCellCapsAspectRatio,
            spongyCellCapsAspectRatio))
        .addLayerToBottom(topSpongyMesophyllLayer)
        .addLayerToBottom(new ABMInterfaceMaterial(
            1.0, iorMesophyll,
            spongyCellCapsAspectRatio,
            spongyCellCapsAspectRatio,
            spongyCellCapsAspectRatio,
            spongyCellCapsAspectRatio))
        .addLayerToBottom(new ABMInterfaceMaterial(
            iorMesophyll, 1.0,
            spongyCellCapsAspectRatio,
            spongyCellCapsAspectRatio,
            spongyCellCapsAspectRatio,
            spongyCellCapsAspectRatio))
        .addLayerToBottom(bottomSpongyMesophyllLayer)
        .addLayerToBottom(new ABMInterfaceMaterial(
            iorCuticle, iorMesophyll,
            spongyCellCapsAspectRatio,
            epidermisCellCapsAspectRatio,
            spongyCellCapsAspectRatio,
            epidermisCellCapsAspectRatio))
        .addLayerToBottom(new ABMInterfaceMaterial(
            1.0, iorCuticle,
            epidermisCellCapsAspectRatio,
            Double.POSITIVE_INFINITY,
            epidermisCellCapsAspectRatio,
            cuticleUndulationsAspectRatio));

    }
    //System.exit(1);
  }

  /**
   * Checks if the layers have been created yet.  Creates them if they have
   * not been.
   */
  private synchronized void checkBuild() {
    if (subsurface.getNumLayers() == 0) {
      build();
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
   */
  @Override
  public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
      WavelengthPacket lambda, double ru, double rv, double rj) {

    if (subsurface.getNumLayers() == 0) { // check once before proceeding to
                                        // avoid unnecessary synchronization.
      checkBuild();
    }

    if (!bifacial) {
      double split = rj;
      topSpongyMesophyllLayer.setThickness(split * mesophyllThickness);
      bottomSpongyMesophyllLayer.setThickness((1.0 - split) * mesophyllThickness);
    }

    return subsurface.scatter(x, v, adjoint, lambda, ru, rv, rj);
  }

}
