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
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.function.SellmeierFunction1;
import ca.eandb.jmist.math.Interval;

/**
 * Static methods for creating <code>Material</code>s representing optical
 * glasses from the Schott catalog at
 * <a href="http://schott.com/optics_devices/english/download/index.html">
 * http://schott.com/optics_devices/english/download/index.html</a>.
 * @author Brad Kimmel
 */
public final class SchottMaterials {

	private static final Interval WAVELENGTH_RANGE = new Interval(0.365e-6, 2.3e-6);

	private static final double[] B_F2 = new double[]{ 1.34533359E+00, 2.09073176E-01, 9.37357162E-01 };
	private static final double[] B_F2HT = new double[]{ 1.34533359E+00, 2.09073176E-01, 9.37357162E-01 };
	private static final double[] B_F5 = new double[]{ 1.31044630E+00, 1.96034260E-01, 9.66129770E-01 };
	private static final double[] B_K10 = new double[]{ 1.15687082E+00, 6.42625444E-02, 8.72376139E-01 };
	private static final double[] B_K7 = new double[]{ 1.12735550E+00, 1.24412303E-01, 8.27100531E-01 };
	private static final double[] B_KZFS12 = new double[]{ 1.55624873E+00, 2.39769276E-01, 9.47887658E-01 };
	private static final double[] B_KZFSN5 = new double[]{ 1.47727858E+00, 1.91686941E-01, 8.97333608E-01 };
	private static final double[] B_LAFN7 = new double[]{ 1.66842615E+00, 2.98512803E-01, 1.07743760E+00 };
	private static final double[] B_LF5 = new double[]{ 1.28035628E+00, 1.63505973E-01, 8.93930112E-01 };
	private static final double[] B_LLF1 = new double[]{ 1.21640125E+00, 1.33664540E-01, 8.83399468E-01 };
	private static final double[] B_N_BAF10 = new double[]{ 1.58514950E+00, 1.43559385E-01, 1.08521269E+00 };
	private static final double[] B_N_BAF4 = new double[]{ 1.42056328E+00, 1.02721269E-01, 1.14380976E+00 };
	private static final double[] B_N_BAF51 = new double[]{ 1.51503623E+00, 1.53621958E-01, 1.15427909E+00 };
	private static final double[] B_N_BAF52 = new double[]{ 1.43903433E+00, 9.67046052E-02, 1.09875818E+00 };
	private static final double[] B_N_BAK1 = new double[]{ 1.12365662E+00, 3.09276848E-01, 8.81511957E-01 };
	private static final double[] B_N_BAK2 = new double[]{ 1.01662154E+00, 3.19903051E-01, 9.37232995E-01 };
	private static final double[] B_N_BAK4 = new double[]{ 1.28834642E+00, 1.32817724E-01, 9.45395373E-01 };
	private static final double[] B_N_BALF4 = new double[]{ 1.31004128E+00, 1.42038259E-01, 9.64929351E-01 };
	private static final double[] B_N_BALF5 = new double[]{ 1.28385965E+00, 7.19300942E-02, 1.05048927E+00 };
	private static final double[] B_N_BASF2 = new double[]{ 1.53652081E+00, 1.56971102E-01, 1.30196815E+00 };
	private static final double[] B_N_BASF64 = new double[]{ 1.65554268E+00, 1.71319770E-01, 1.33664448E+00 };
	private static final double[] B_N_BK10 = new double[]{ 8.88308131E-01, 3.28964475E-01, 9.84610769E-01 };
	private static final double[] B_N_BK7 = new double[]{ 1.03961212E+00, 2.31792344E-01, 1.01046945E+00 };
	private static final double[] B_N_F2 = new double[]{ 1.39757037E+00, 1.59201403E-01, 1.26865430E+00 };
	private static final double[] B_N_FK5 = new double[]{ 8.44309338E-01, 3.44147824E-01, 9.10790213E-01 };
	private static final double[] B_N_FK51A = new double[]{ 9.71247817E-01, 2.16901417E-01, 9.04651666E-01 };
	private static final double[] B_N_K5 = new double[]{ 1.08511833E+00, 1.99562005E-01, 9.30511663E-01 };
	private static final double[] B_N_KF9 = new double[]{ 1.19286778E+00, 8.93346571E-02, 9.20819805E-01 };
	private static final double[] B_N_KZFS11 = new double[]{ 1.33222450E+00, 2.89241610E-01, 1.15161734E+00 };
	private static final double[] B_N_KZFS2 = new double[]{ 1.23697554E+00, 1.53569376E-01, 9.03976272E-01 };
	private static final double[] B_N_KZFS4 = new double[]{ 1.35055424E+00, 1.97575506E-01, 1.09962992E+00 };
	private static final double[] B_N_KZFS5 = new double[]{ 1.47460789E+00, 1.93584488E-01, 1.26589974E+00 };
	private static final double[] B_N_KZFS8 = new double[]{ 1.62693651E+00, 2.43698760E-01, 1.62007141E+00 };
	private static final double[] B_N_LAF2 = new double[]{ 1.80984227E+00, 1.57295550E-01, 1.09300370E+00 };
	private static final double[] B_N_LAF21 = new double[]{ 1.87134529E+00, 2.50783010E-01, 1.22048639E+00 };
	private static final double[] B_N_LAF33 = new double[]{ 1.79653417E+00, 3.11577903E-01, 1.15981863E+00 };
	private static final double[] B_N_LAF34 = new double[]{ 1.75836958E+00, 3.13537785E-01, 1.18925231E+00 };
	private static final double[] B_N_LAF35 = new double[]{ 1.51697436E+00, 4.55875464E-01, 1.07469242E+00 };
	private static final double[] B_N_LAF36 = new double[]{ 1.85744228E+00, 2.94098729E-01, 1.16615417E+00 };
	private static final double[] B_N_LAF7 = new double[]{ 1.74028764E+00, 2.26710554E-01, 1.32525548E+00 };
	private static final double[] B_N_LAK10 = new double[]{ 1.72878017E+00, 1.69257825E-01, 1.19386956E+00 };
	private static final double[] B_N_LAK12 = new double[]{ 1.17365704E+00, 5.88992398E-01, 9.78014394E-01 };
	private static final double[] B_N_LAK14 = new double[]{ 1.50781212E+00, 3.18866829E-01, 1.14287213E+00 };
	private static final double[] B_N_LAK21 = new double[]{ 1.22718116E+00, 4.20783743E-01, 1.01284843E+00 };
	private static final double[] B_N_LAK22 = new double[]{ 1.14229781E+00, 5.35138441E-01, 1.04088385E+00 };
	private static final double[] B_N_LAK33A = new double[]{ 1.44116999E+00, 5.71749501E-01, 1.16605226E+00 };
	private static final double[] B_N_LAK34 = new double[]{ 1.26661442E+00, 6.65919318E-01, 1.12496120E+00 };
	private static final double[] B_N_LAK7 = new double[]{ 1.23679889E+00, 4.45051837E-01, 1.01745888E+00 };
	private static final double[] B_N_LAK8 = new double[]{ 1.33183167E+00, 5.46623206E-01, 1.19084015E+00 };
	private static final double[] B_N_LAK9 = new double[]{ 1.46231905E+00, 3.44399589E-01, 1.15508372E+00 };
	private static final double[] B_N_LASF31A = new double[]{ 1.96485075E+00, 4.75231259E-01, 1.48360109E+00 };
	private static final double[] B_N_LASF40 = new double[]{ 1.98550331E+00, 2.74057042E-01, 1.28945661E+00 };
	private static final double[] B_N_LASF41 = new double[]{ 1.86348331E+00, 4.13307255E-01, 1.35784815E+00 };
	private static final double[] B_N_LASF43 = new double[]{ 1.93502827E+00, 2.36629350E-01, 1.26291344E+00 };
	private static final double[] B_N_LASF44 = new double[]{ 1.78897105E+00, 3.86758670E-01, 1.30506243E+00 };
	private static final double[] B_N_LASF45 = new double[]{ 1.87140198E+00, 2.67777879E-01, 1.73030008E+00 };
	private static final double[] B_N_LASF46A = new double[]{ 2.16701566E+00, 3.19812761E-01, 1.66004486E+00 };
	private static final double[] B_N_LASF9 = new double[]{ 2.00029547E+00, 2.98926886E-01, 1.80691843E+00 };
	private static final double[] B_N_PK51 = new double[]{ 1.15610775E+00, 1.53229344E-01, 7.85618966E-01 };
	private static final double[] B_N_PK52A = new double[]{ 1.02960700E+00, 1.88050600E-01, 7.36488165E-01 };
	private static final double[] B_N_PSK3 = new double[]{ 8.87272110E-01, 4.89592425E-01, 1.04865296E+00 };
	private static final double[] B_N_PSK53A = new double[]{ 1.38121836E+00, 1.96745645E-01, 8.86089205E-01 };
	private static final double[] B_N_SF1 = new double[]{ 1.60865158E+00, 2.37725916E-01, 1.51530653E+00 };
	private static final double[] B_N_SF10 = new double[]{ 1.62153902E+00, 2.56287842E-01, 1.64447552E+00 };
	private static final double[] B_N_SF11 = new double[]{ 1.73759695E+00, 3.13747346E-01, 1.89878101E+00 };
	private static final double[] B_N_SF14 = new double[]{ 1.69022361E+00, 2.88870052E-01, 1.70451870E+00 };
	private static final double[] B_N_SF15 = new double[]{ 1.57055634E+00, 2.18987094E-01, 1.50824017E+00 };
	private static final double[] B_N_SF2 = new double[]{ 1.47343127E+00, 1.63681849E-01, 1.36920899E+00 };
	private static final double[] B_N_SF4 = new double[]{ 1.67780282E+00, 2.82849893E-01, 1.63539276E+00 };
	private static final double[] B_N_SF5 = new double[]{ 1.52481889E+00, 1.87085527E-01, 1.42729015E+00 };
	private static final double[] B_N_SF57 = new double[]{ 1.87543831E+00, 3.73757490E-01, 2.30001797E+00 };
	private static final double[] B_N_SF57HT = new double[]{ 1.87543831E+00, 3.73757490E-01, 2.30001797E+00 };
	private static final double[] B_N_SF6 = new double[]{ 1.77931763E+00, 3.38149866E-01, 2.08734474E+00 };
	private static final double[] B_N_SF66 = new double[]{ 2.02459760E+00, 4.70187196E-01, 2.59970433E+00 };
	private static final double[] B_N_SF6HT = new double[]{ 1.77931763E+00, 3.38149866E-01, 2.08734474E+00 };
	private static final double[] B_N_SF8 = new double[]{ 1.55075812E+00, 2.09816918E-01, 1.46205491E+00 };
	private static final double[] B_N_SK11 = new double[]{ 1.17963631E+00, 2.29817295E-01, 9.35789652E-01 };
	private static final double[] B_N_SK14 = new double[]{ 9.36155374E-01, 5.94052018E-01, 1.04374583E+00 };
	private static final double[] B_N_SK16 = new double[]{ 1.34317774E+00, 2.41144399E-01, 9.94317969E-01 };
	private static final double[] B_N_SK2 = new double[]{ 1.28189012E+00, 2.57738258E-01, 9.68186040E-01 };
	private static final double[] B_N_SK4 = new double[]{ 1.32993741E+00, 2.28542996E-01, 9.88465211E-01 };
	private static final double[] B_N_SK5 = new double[]{ 9.91463823E-01, 4.95982121E-01, 9.87393925E-01 };
	private static final double[] B_N_SSK2 = new double[]{ 1.43060270E+00, 1.53150554E-01, 1.01390904E+00 };
	private static final double[] B_N_SSK5 = new double[]{ 1.59222659E+00, 1.03520774E-01, 1.05174016E+00 };
	private static final double[] B_N_SSK8 = new double[]{ 1.44857867E+00, 1.17965926E-01, 1.06937528E+00 };
	private static final double[] B_N_ZK7 = new double[]{ 1.07715032E+00, 1.68079109E-01, 8.51889892E-01 };
	private static final double[] B_P_LASF47 = new double[]{ 1.85543101E+00, 3.15854649E-01, 1.28561839E+00 };
	private static final double[] B_P_PK53 = new double[]{ 9.60316767E-01, 3.40437227E-01, 7.77865595E-01 };
	private static final double[] B_P_SF67 = new double[]{ 1.97464225E+00, 4.67095921E-01, 2.43154209E+00 };
	private static final double[] B_P_SF8 = new double[]{ 1.55370411E+00, 2.06332561E-01, 1.39708831E+00 };
	private static final double[] B_P_SK57 = new double[]{ 1.31053414E+00, 1.69376189E-01, 1.10987714E+00 };
	private static final double[] B_SF1 = new double[]{ 1.55912923E+00, 2.84246288E-01, 9.68842926E-01 };
	private static final double[] B_SF10 = new double[]{ 1.61625977E+00, 2.59229334E-01, 1.07762317E+00 };
	private static final double[] B_SF2 = new double[]{ 1.40301821E+00, 2.31767504E-01, 9.39056586E-01 };
	private static final double[] B_SF4 = new double[]{ 1.61957826E+00, 3.39493189E-01, 1.02566931E+00 };
	private static final double[] B_SF5 = new double[]{ 1.46141885E+00, 2.47713019E-01, 9.49995832E-01 };
	private static final double[] B_SF56A = new double[]{ 1.70579259E+00, 3.44223052E-01, 1.09601828E+00 };
	private static final double[] B_SF57 = new double[]{ 1.81651371E+00, 4.28893641E-01, 1.07186278E+00 };
	private static final double[] B_SF57HHT = new double[]{ 1.81651371E+00, 4.28893641E-01, 1.07186278E+00 };
	private static final double[] B_SF6 = new double[]{ 1.72448482E+00, 3.90104889E-01, 1.04572858E+00 };
	private static final double[] B_SF6HT = new double[]{ 1.72448482E+00, 3.90104889E-01, 1.04572858E+00 };
	private static final double[] B_LITHOTEC_CAF2 = new double[]{ 6.17617011E-01, 4.21117656E-01, 3.79711183E+00 };
	private static final double[] B_LITHOSIL_Q = new double[]{ 6.70710810E-01, 4.33322857E-01, 8.77379057E-01 };

	private static final double[] C_F2 = new double[]{ 9.97743871E-15, 4.70450767E-14, 1.11886764E-10 };
	private static final double[] C_F2HT = new double[]{ 9.97743871E-15, 4.70450767E-14, 1.11886764E-10 };
	private static final double[] C_F5 = new double[]{ 9.58633048E-15, 4.57627627E-14, 1.15011883E-10 };
	private static final double[] C_K10 = new double[]{ 8.09424251E-15, 3.86051284E-14, 1.04747730E-10 };
	private static final double[] C_K7 = new double[]{ 7.20341707E-15, 2.69835916E-14, 1.00384588E-10 };
	private static final double[] C_KZFS12 = new double[]{ 1.02012744E-14, 4.69277969E-14, 6.98370722E-11 };
	private static final double[] C_KZFSN5 = new double[]{ 9.75488335E-15, 4.50495404E-14, 6.78786495E-11 };
	private static final double[] C_LAFN7 = new double[]{ 1.03159999E-14, 4.69216348E-14, 8.25078509E-11 };
	private static final double[] C_LF5 = new double[]{ 9.29854416E-15, 4.49135769E-14, 1.10493685E-10 };
	private static final double[] C_LLF1 = new double[]{ 8.57807248E-15, 4.20143003E-14, 1.07593060E-10 };
	private static final double[] C_N_BAF10 = new double[]{ 9.26681282E-15, 4.24489805E-14, 1.05613573E-10 };
	private static final double[] C_N_BAF4 = new double[]{ 9.42015382E-15, 5.31087291E-14, 1.10278856E-10 };
	private static final double[] C_N_BAF51 = new double[]{ 9.42734715E-15, 4.30826500E-14, 1.24889868E-10 };
	private static final double[] C_N_BAF52 = new double[]{ 9.07800128E-15, 5.08212080E-14, 1.05691856E-10 };
	private static final double[] C_N_BAK1 = new double[]{ 6.44742752E-15, 2.22284402E-14, 1.07297751E-10 };
	private static final double[] C_N_BAK2 = new double[]{ 5.92383763E-15, 2.03828415E-14, 1.13118417E-10 };
	private static final double[] C_N_BAK4 = new double[]{ 7.79980626E-15, 3.15631177E-14, 1.05965875E-10 };
	private static final double[] C_N_BALF4 = new double[]{ 7.96596450E-15, 3.30672072E-14, 1.09197320E-10 };
	private static final double[] C_N_BALF5 = new double[]{ 8.25815975E-15, 4.41920027E-14, 1.07097324E-10 };
	private static final double[] C_N_BASF2 = new double[]{ 1.08435729E-14, 5.62278762E-14, 1.31339700E-10 };
	private static final double[] C_N_BASF64 = new double[]{ 1.04485644E-14, 4.99394756E-14, 1.18961472E-10 };
	private static final double[] C_N_BK10 = new double[]{ 5.16900822E-15, 1.61190045E-14, 9.97575331E-11 };
	private static final double[] C_N_BK7 = new double[]{ 6.00069867E-15, 2.00179144E-14, 1.03560653E-10 };
	private static final double[] C_N_F2 = new double[]{ 9.95906143E-15, 5.46931752E-14, 1.19248346E-10 };
	private static final double[] C_N_FK5 = new double[]{ 4.75111955E-15, 1.49814849E-14, 9.78600293E-11 };
	private static final double[] C_N_FK51A = new double[]{ 4.72301995E-15, 1.53575612E-14, 1.68681330E-10 };
	private static final double[] C_N_K5 = new double[]{ 6.61099503E-15, 2.41108660E-14, 1.11982777E-10 };
	private static final double[] C_N_KF9 = new double[]{ 8.39154696E-15, 4.04010786E-14, 1.12572446E-10 };
	private static final double[] C_N_KZFS11 = new double[]{ 8.40298480E-15, 3.44239720E-14, 8.84310532E-11 };
	private static final double[] C_N_KZFS2 = new double[]{ 7.47170505E-15, 3.08053556E-14, 7.01731084E-11 };
	private static final double[] C_N_KZFS4 = new double[]{ 8.76282070E-15, 3.71767201E-14, 9.03866994E-11 };
	private static final double[] C_N_KZFS5 = new double[]{ 9.86143816E-15, 4.45477583E-14, 1.06436258E-10 };
	private static final double[] C_N_KZFS8 = new double[]{ 1.08808630E-14, 4.94207753E-14, 1.31009163E-10 };
	private static final double[] C_N_LAF2 = new double[]{ 1.01711622E-14, 4.42431765E-14, 1.00687748E-10 };
	private static final double[] C_N_LAF21 = new double[]{ 9.33322280E-15, 3.45637762E-14, 8.32404866E-11 };
	private static final double[] C_N_LAF33 = new double[]{ 9.27313493E-15, 3.58201181E-14, 8.73448712E-11 };
	private static final double[] C_N_LAF34 = new double[]{ 8.72810026E-15, 2.93020832E-14, 8.51780644E-11 };
	private static final double[] C_N_LAF35 = new double[]{ 7.50943203E-15, 2.60046715E-14, 8.05945159E-11 };
	private static final double[] C_N_LAF36 = new double[]{ 9.82397191E-15, 3.84309138E-14, 8.93984634E-11 };
	private static final double[] C_N_LAF7 = new double[]{ 1.07925580E-14, 5.38626639E-14, 1.06268665E-10 };
	private static final double[] C_N_LAK10 = new double[]{ 8.86014635E-15, 3.63416509E-14, 8.29009069E-11 };
	private static final double[] C_N_LAK12 = new double[]{ 5.77031797E-15, 2.00401678E-14, 9.54873482E-11 };
	private static final double[] C_N_LAK14 = new double[]{ 7.46098727E-15, 2.42024834E-14, 8.09565165E-11 };
	private static final double[] C_N_LAK21 = new double[]{ 6.02075682E-15, 1.96862889E-14, 8.84370099E-11 };
	private static final double[] C_N_LAK22 = new double[]{ 5.85778594E-15, 1.98546147E-14, 1.00834017E-10 };
	private static final double[] C_N_LAK33A = new double[]{ 6.80933877E-15, 2.22291824E-14, 8.09379555E-11 };
	private static final double[] C_N_LAK34 = new double[]{ 5.89278062E-15, 1.97509041E-14, 7.88894174E-11 };
	private static final double[] C_N_LAK7 = new double[]{ 6.10105538E-15, 2.01388334E-14, 9.06380380E-11 };
	private static final double[] C_N_LAK8 = new double[]{ 6.20023871E-15, 2.16465439E-14, 8.25827736E-11 };
	private static final double[] C_N_LAK9 = new double[]{ 7.24270156E-15, 2.43353131E-14, 8.54686868E-11 };
	private static final double[] C_N_LASF31A = new double[]{ 9.82060155E-15, 3.44713438E-14, 1.10739863E-10 };
	private static final double[] C_N_LASF40 = new double[]{ 1.09583310E-14, 4.74551603E-14, 9.69085286E-11 };
	private static final double[] C_N_LASF41 = new double[]{ 9.10368219E-15, 3.39247268E-14, 9.33580595E-11 };
	private static final double[] C_N_LASF43 = new double[]{ 1.04001413E-14, 4.47505292E-14, 8.74375690E-11 };
	private static final double[] C_N_LASF44 = new double[]{ 8.72506277E-15, 3.08085023E-14, 9.27743824E-11 };
	private static final double[] C_N_LASF45 = new double[]{ 1.12171920E-14, 5.05134972E-14, 1.47106505E-10 };
	private static final double[] C_N_LASF46A = new double[]{ 1.23595524E-14, 5.60610282E-14, 1.07047718E-10 };
	private static final double[] C_N_LASF9 = new double[]{ 1.21426017E-14, 5.38736236E-14, 1.56530829E-10 };
	private static final double[] C_N_PK51 = new double[]{ 5.85597402E-15, 1.94072416E-14, 1.40537046E-10 };
	private static final double[] C_N_PK52A = new double[]{ 5.16800155E-15, 1.66658798E-14, 1.38964129E-10 };
	private static final double[] C_N_PSK3 = new double[]{ 4.69824067E-15, 1.61818463E-14, 1.04374975E-10 };
	private static final double[] C_N_PSK53A = new double[]{ 7.06416337E-15, 2.33251345E-14, 9.74847345E-11 };
	private static final double[] C_N_SF1 = new double[]{ 1.19654879E-14, 5.90589722E-14, 1.35521676E-10 };
	private static final double[] C_N_SF10 = new double[]{ 1.22241457E-14, 5.95736775E-14, 1.47468793E-10 };
	private static final double[] C_N_SF11 = new double[]{ 1.31887070E-14, 6.23068142E-14, 1.55236290E-10 };
	private static final double[] C_N_SF14 = new double[]{ 1.30512113E-14, 6.13691880E-14, 1.49517689E-10 };
	private static final double[] C_N_SF15 = new double[]{ 1.16507014E-14, 5.97856897E-14, 1.32709339E-10 };
	private static final double[] C_N_SF2 = new double[]{ 1.09019098E-14, 5.85683687E-14, 1.27404933E-10 };
	private static final double[] C_N_SF4 = new double[]{ 1.26793450E-14, 6.02038419E-14, 1.45760496E-10 };
	private static final double[] C_N_SF5 = new double[]{ 1.12547560E-14, 5.88995392E-14, 1.29141675E-10 };
	private static final double[] C_N_SF57 = new double[]{ 1.41749518E-14, 6.40509927E-14, 1.77389795E-10 };
	private static final double[] C_N_SF57HT = new double[]{ 1.41749518E-14, 6.40509927E-14, 1.77389795E-10 };
	private static final double[] C_N_SF6 = new double[]{ 1.33714182E-14, 6.17533621E-14, 1.74017590E-10 };
	private static final double[] C_N_SF66 = new double[]{ 1.47053225E-14, 6.92998276E-14, 1.61817601E-10 };
	private static final double[] C_N_SF6HT = new double[]{ 1.33714182E-14, 6.17533621E-14, 1.74017590E-10 };
	private static final double[] C_N_SF8 = new double[]{ 1.14338344E-14, 5.82725652E-14, 1.33241650E-10 };
	private static final double[] C_N_SK11 = new double[]{ 6.80282081E-15, 2.19737205E-14, 1.01513232E-10 };
	private static final double[] C_N_SK14 = new double[]{ 4.61716525E-15, 1.68859270E-14, 1.03736265E-10 };
	private static final double[] C_N_SK16 = new double[]{ 7.04687339E-15, 2.29005000E-14, 9.27508526E-11 };
	private static final double[] C_N_SK2 = new double[]{ 7.27191640E-15, 2.42823527E-14, 1.10377773E-10 };
	private static final double[] C_N_SK4 = new double[]{ 7.16874107E-15, 2.46455892E-14, 1.00886364E-10 };
	private static final double[] C_N_SK5 = new double[]{ 5.22730467E-15, 1.72733646E-14, 9.83594579E-11 };
	private static final double[] C_N_SSK2 = new double[]{ 8.23982975E-15, 3.33736841E-14, 1.06870822E-10 };
	private static final double[] C_N_SSK5 = new double[]{ 9.20284626E-15, 4.23530072E-14, 1.06927374E-10 };
	private static final double[] C_N_SSK8 = new double[]{ 8.69310149E-15, 4.21566593E-14, 1.11300666E-10 };
	private static final double[] C_N_ZK7 = new double[]{ 6.76601657E-15, 2.30642817E-14, 8.90498778E-11 };
	private static final double[] C_P_LASF47 = new double[]{ 1.00328203E-14, 3.87095168E-14, 9.45421507E-11 };
	private static final double[] C_P_PK53 = new double[]{ 5.31032986E-15, 1.75073434E-14, 1.06875330E-10 };
	private static final double[] C_P_SF67 = new double[]{ 1.45772324E-14, 6.69790359E-14, 1.57444895E-10 };
	private static final double[] C_P_SF8 = new double[]{ 1.16582670E-14, 5.82087757E-14, 1.30748028E-10 };
	private static final double[] C_P_SK57 = new double[]{ 7.40877235E-15, 2.54563489E-14, 1.07751087E-10 };
	private static final double[] C_SF1 = new double[]{ 1.21481001E-14, 5.34549042E-14, 1.12174809E-10 };
	private static final double[] C_SF10 = new double[]{ 1.27534559E-14, 5.81983954E-14, 1.16607680E-10 };
	private static final double[] C_SF2 = new double[]{ 1.05795466E-14, 4.93226978E-14, 1.12405955E-10 };
	private static final double[] C_SF4 = new double[]{ 1.25502104E-14, 5.44559822E-14, 1.17652222E-10 };
	private static final double[] C_SF5 = new double[]{ 1.11826126E-14, 5.08594669E-14, 1.12041888E-10 };
	private static final double[] C_SF56A = new double[]{ 1.33874699E-14, 5.79561608E-14, 1.21616024E-10 };
	private static final double[] C_SF57 = new double[]{ 1.43704198E-14, 5.92801172E-14, 1.21419942E-10 };
	private static final double[] C_SF57HHT = new double[]{ 1.43704198E-14, 5.92801172E-14, 1.21419942E-10 };
	private static final double[] C_SF6 = new double[]{ 1.34871947E-14, 5.69318095E-14, 1.18557185E-10 };
	private static final double[] C_SF6HT = new double[]{ 1.34871947E-14, 5.69318095E-14, 1.18557185E-10 };
	private static final double[] C_LITHOTEC_CAF2 = new double[]{ 2.75381936E-15, 1.05900875E-14, 1.18267444E-09 };
	private static final double[] C_LITHOSIL_Q = new double[]{ 4.49192312E-15, 1.32812976E-14, 9.58899878E-11 };

	private static final Function1 N_F2 = new SellmeierFunction1(B_F2, C_F2, WAVELENGTH_RANGE);
	private static final Function1 N_F2HT = new SellmeierFunction1(B_F2HT, C_F2HT, WAVELENGTH_RANGE);
	private static final Function1 N_F5 = new SellmeierFunction1(B_F5, C_F5, WAVELENGTH_RANGE);
	private static final Function1 N_K10 = new SellmeierFunction1(B_K10, C_K10, WAVELENGTH_RANGE);
	private static final Function1 N_K7 = new SellmeierFunction1(B_K7, C_K7, WAVELENGTH_RANGE);
	private static final Function1 N_KZFS12 = new SellmeierFunction1(B_KZFS12, C_KZFS12, WAVELENGTH_RANGE);
	private static final Function1 N_KZFSN5 = new SellmeierFunction1(B_KZFSN5, C_KZFSN5, WAVELENGTH_RANGE);
	private static final Function1 N_LAFN7 = new SellmeierFunction1(B_LAFN7, C_LAFN7, WAVELENGTH_RANGE);
	private static final Function1 N_LF5 = new SellmeierFunction1(B_LF5, C_LF5, WAVELENGTH_RANGE);
	private static final Function1 N_LLF1 = new SellmeierFunction1(B_LLF1, C_LLF1, WAVELENGTH_RANGE);
	private static final Function1 N_N_BAF10 = new SellmeierFunction1(B_N_BAF10, C_N_BAF10, WAVELENGTH_RANGE);
	private static final Function1 N_N_BAF4 = new SellmeierFunction1(B_N_BAF4, C_N_BAF4, WAVELENGTH_RANGE);
	private static final Function1 N_N_BAF51 = new SellmeierFunction1(B_N_BAF51, C_N_BAF51, WAVELENGTH_RANGE);
	private static final Function1 N_N_BAF52 = new SellmeierFunction1(B_N_BAF52, C_N_BAF52, WAVELENGTH_RANGE);
	private static final Function1 N_N_BAK1 = new SellmeierFunction1(B_N_BAK1, C_N_BAK1, WAVELENGTH_RANGE);
	private static final Function1 N_N_BAK2 = new SellmeierFunction1(B_N_BAK2, C_N_BAK2, WAVELENGTH_RANGE);
	private static final Function1 N_N_BAK4 = new SellmeierFunction1(B_N_BAK4, C_N_BAK4, WAVELENGTH_RANGE);
	private static final Function1 N_N_BALF4 = new SellmeierFunction1(B_N_BALF4, C_N_BALF4, WAVELENGTH_RANGE);
	private static final Function1 N_N_BALF5 = new SellmeierFunction1(B_N_BALF5, C_N_BALF5, WAVELENGTH_RANGE);
	private static final Function1 N_N_BASF2 = new SellmeierFunction1(B_N_BASF2, C_N_BASF2, WAVELENGTH_RANGE);
	private static final Function1 N_N_BASF64 = new SellmeierFunction1(B_N_BASF64, C_N_BASF64, WAVELENGTH_RANGE);
	private static final Function1 N_N_BK10 = new SellmeierFunction1(B_N_BK10, C_N_BK10, WAVELENGTH_RANGE);
	private static final Function1 N_N_BK7 = new SellmeierFunction1(B_N_BK7, C_N_BK7, WAVELENGTH_RANGE);
	private static final Function1 N_N_F2 = new SellmeierFunction1(B_N_F2, C_N_F2, WAVELENGTH_RANGE);
	private static final Function1 N_N_FK5 = new SellmeierFunction1(B_N_FK5, C_N_FK5, WAVELENGTH_RANGE);
	private static final Function1 N_N_FK51A = new SellmeierFunction1(B_N_FK51A, C_N_FK51A, WAVELENGTH_RANGE);
	private static final Function1 N_N_K5 = new SellmeierFunction1(B_N_K5, C_N_K5, WAVELENGTH_RANGE);
	private static final Function1 N_N_KF9 = new SellmeierFunction1(B_N_KF9, C_N_KF9, WAVELENGTH_RANGE);
	private static final Function1 N_N_KZFS11 = new SellmeierFunction1(B_N_KZFS11, C_N_KZFS11, WAVELENGTH_RANGE);
	private static final Function1 N_N_KZFS2 = new SellmeierFunction1(B_N_KZFS2, C_N_KZFS2, WAVELENGTH_RANGE);
	private static final Function1 N_N_KZFS4 = new SellmeierFunction1(B_N_KZFS4, C_N_KZFS4, WAVELENGTH_RANGE);
	private static final Function1 N_N_KZFS5 = new SellmeierFunction1(B_N_KZFS5, C_N_KZFS5, WAVELENGTH_RANGE);
	private static final Function1 N_N_KZFS8 = new SellmeierFunction1(B_N_KZFS8, C_N_KZFS8, WAVELENGTH_RANGE);
	private static final Function1 N_N_LAF2 = new SellmeierFunction1(B_N_LAF2, C_N_LAF2, WAVELENGTH_RANGE);
	private static final Function1 N_N_LAF21 = new SellmeierFunction1(B_N_LAF21, C_N_LAF21, WAVELENGTH_RANGE);
	private static final Function1 N_N_LAF33 = new SellmeierFunction1(B_N_LAF33, C_N_LAF33, WAVELENGTH_RANGE);
	private static final Function1 N_N_LAF34 = new SellmeierFunction1(B_N_LAF34, C_N_LAF34, WAVELENGTH_RANGE);
	private static final Function1 N_N_LAF35 = new SellmeierFunction1(B_N_LAF35, C_N_LAF35, WAVELENGTH_RANGE);
	private static final Function1 N_N_LAF36 = new SellmeierFunction1(B_N_LAF36, C_N_LAF36, WAVELENGTH_RANGE);
	private static final Function1 N_N_LAF7 = new SellmeierFunction1(B_N_LAF7, C_N_LAF7, WAVELENGTH_RANGE);
	private static final Function1 N_N_LAK10 = new SellmeierFunction1(B_N_LAK10, C_N_LAK10, WAVELENGTH_RANGE);
	private static final Function1 N_N_LAK12 = new SellmeierFunction1(B_N_LAK12, C_N_LAK12, WAVELENGTH_RANGE);
	private static final Function1 N_N_LAK14 = new SellmeierFunction1(B_N_LAK14, C_N_LAK14, WAVELENGTH_RANGE);
	private static final Function1 N_N_LAK21 = new SellmeierFunction1(B_N_LAK21, C_N_LAK21, WAVELENGTH_RANGE);
	private static final Function1 N_N_LAK22 = new SellmeierFunction1(B_N_LAK22, C_N_LAK22, WAVELENGTH_RANGE);
	private static final Function1 N_N_LAK33A = new SellmeierFunction1(B_N_LAK33A, C_N_LAK33A, WAVELENGTH_RANGE);
	private static final Function1 N_N_LAK34 = new SellmeierFunction1(B_N_LAK34, C_N_LAK34, WAVELENGTH_RANGE);
	private static final Function1 N_N_LAK7 = new SellmeierFunction1(B_N_LAK7, C_N_LAK7, WAVELENGTH_RANGE);
	private static final Function1 N_N_LAK8 = new SellmeierFunction1(B_N_LAK8, C_N_LAK8, WAVELENGTH_RANGE);
	private static final Function1 N_N_LAK9 = new SellmeierFunction1(B_N_LAK9, C_N_LAK9, WAVELENGTH_RANGE);
	private static final Function1 N_N_LASF31A = new SellmeierFunction1(B_N_LASF31A, C_N_LASF31A, WAVELENGTH_RANGE);
	private static final Function1 N_N_LASF40 = new SellmeierFunction1(B_N_LASF40, C_N_LASF40, WAVELENGTH_RANGE);
	private static final Function1 N_N_LASF41 = new SellmeierFunction1(B_N_LASF41, C_N_LASF41, WAVELENGTH_RANGE);
	private static final Function1 N_N_LASF43 = new SellmeierFunction1(B_N_LASF43, C_N_LASF43, WAVELENGTH_RANGE);
	private static final Function1 N_N_LASF44 = new SellmeierFunction1(B_N_LASF44, C_N_LASF44, WAVELENGTH_RANGE);
	private static final Function1 N_N_LASF45 = new SellmeierFunction1(B_N_LASF45, C_N_LASF45, WAVELENGTH_RANGE);
	private static final Function1 N_N_LASF46A = new SellmeierFunction1(B_N_LASF46A, C_N_LASF46A, WAVELENGTH_RANGE);
	private static final Function1 N_N_LASF9 = new SellmeierFunction1(B_N_LASF9, C_N_LASF9, WAVELENGTH_RANGE);
	private static final Function1 N_N_PK51 = new SellmeierFunction1(B_N_PK51, C_N_PK51, WAVELENGTH_RANGE);
	private static final Function1 N_N_PK52A = new SellmeierFunction1(B_N_PK52A, C_N_PK52A, WAVELENGTH_RANGE);
	private static final Function1 N_N_PSK3 = new SellmeierFunction1(B_N_PSK3, C_N_PSK3, WAVELENGTH_RANGE);
	private static final Function1 N_N_PSK53A = new SellmeierFunction1(B_N_PSK53A, C_N_PSK53A, WAVELENGTH_RANGE);
	private static final Function1 N_N_SF1 = new SellmeierFunction1(B_N_SF1, C_N_SF1, WAVELENGTH_RANGE);
	private static final Function1 N_N_SF10 = new SellmeierFunction1(B_N_SF10, C_N_SF10, WAVELENGTH_RANGE);
	private static final Function1 N_N_SF11 = new SellmeierFunction1(B_N_SF11, C_N_SF11, WAVELENGTH_RANGE);
	private static final Function1 N_N_SF14 = new SellmeierFunction1(B_N_SF14, C_N_SF14, WAVELENGTH_RANGE);
	private static final Function1 N_N_SF15 = new SellmeierFunction1(B_N_SF15, C_N_SF15, WAVELENGTH_RANGE);
	private static final Function1 N_N_SF2 = new SellmeierFunction1(B_N_SF2, C_N_SF2, WAVELENGTH_RANGE);
	private static final Function1 N_N_SF4 = new SellmeierFunction1(B_N_SF4, C_N_SF4, WAVELENGTH_RANGE);
	private static final Function1 N_N_SF5 = new SellmeierFunction1(B_N_SF5, C_N_SF5, WAVELENGTH_RANGE);
	private static final Function1 N_N_SF57 = new SellmeierFunction1(B_N_SF57, C_N_SF57, WAVELENGTH_RANGE);
	private static final Function1 N_N_SF57HT = new SellmeierFunction1(B_N_SF57HT, C_N_SF57HT, WAVELENGTH_RANGE);
	private static final Function1 N_N_SF6 = new SellmeierFunction1(B_N_SF6, C_N_SF6, WAVELENGTH_RANGE);
	private static final Function1 N_N_SF66 = new SellmeierFunction1(B_N_SF66, C_N_SF66, WAVELENGTH_RANGE);
	private static final Function1 N_N_SF6HT = new SellmeierFunction1(B_N_SF6HT, C_N_SF6HT, WAVELENGTH_RANGE);
	private static final Function1 N_N_SF8 = new SellmeierFunction1(B_N_SF8, C_N_SF8, WAVELENGTH_RANGE);
	private static final Function1 N_N_SK11 = new SellmeierFunction1(B_N_SK11, C_N_SK11, WAVELENGTH_RANGE);
	private static final Function1 N_N_SK14 = new SellmeierFunction1(B_N_SK14, C_N_SK14, WAVELENGTH_RANGE);
	private static final Function1 N_N_SK16 = new SellmeierFunction1(B_N_SK16, C_N_SK16, WAVELENGTH_RANGE);
	private static final Function1 N_N_SK2 = new SellmeierFunction1(B_N_SK2, C_N_SK2, WAVELENGTH_RANGE);
	private static final Function1 N_N_SK4 = new SellmeierFunction1(B_N_SK4, C_N_SK4, WAVELENGTH_RANGE);
	private static final Function1 N_N_SK5 = new SellmeierFunction1(B_N_SK5, C_N_SK5, WAVELENGTH_RANGE);
	private static final Function1 N_N_SSK2 = new SellmeierFunction1(B_N_SSK2, C_N_SSK2, WAVELENGTH_RANGE);
	private static final Function1 N_N_SSK5 = new SellmeierFunction1(B_N_SSK5, C_N_SSK5, WAVELENGTH_RANGE);
	private static final Function1 N_N_SSK8 = new SellmeierFunction1(B_N_SSK8, C_N_SSK8, WAVELENGTH_RANGE);
	private static final Function1 N_N_ZK7 = new SellmeierFunction1(B_N_ZK7, C_N_ZK7, WAVELENGTH_RANGE);
	private static final Function1 N_P_LASF47 = new SellmeierFunction1(B_P_LASF47, C_P_LASF47, WAVELENGTH_RANGE);
	private static final Function1 N_P_PK53 = new SellmeierFunction1(B_P_PK53, C_P_PK53, WAVELENGTH_RANGE);
	private static final Function1 N_P_SF67 = new SellmeierFunction1(B_P_SF67, C_P_SF67, WAVELENGTH_RANGE);
	private static final Function1 N_P_SF8 = new SellmeierFunction1(B_P_SF8, C_P_SF8, WAVELENGTH_RANGE);
	private static final Function1 N_P_SK57 = new SellmeierFunction1(B_P_SK57, C_P_SK57, WAVELENGTH_RANGE);
	private static final Function1 N_SF1 = new SellmeierFunction1(B_SF1, C_SF1, WAVELENGTH_RANGE);
	private static final Function1 N_SF10 = new SellmeierFunction1(B_SF10, C_SF10, WAVELENGTH_RANGE);
	private static final Function1 N_SF2 = new SellmeierFunction1(B_SF2, C_SF2, WAVELENGTH_RANGE);
	private static final Function1 N_SF4 = new SellmeierFunction1(B_SF4, C_SF4, WAVELENGTH_RANGE);
	private static final Function1 N_SF5 = new SellmeierFunction1(B_SF5, C_SF5, WAVELENGTH_RANGE);
	private static final Function1 N_SF56A = new SellmeierFunction1(B_SF56A, C_SF56A, WAVELENGTH_RANGE);
	private static final Function1 N_SF57 = new SellmeierFunction1(B_SF57, C_SF57, WAVELENGTH_RANGE);
	private static final Function1 N_SF57HHT = new SellmeierFunction1(B_SF57HHT, C_SF57HHT, WAVELENGTH_RANGE);
	private static final Function1 N_SF6 = new SellmeierFunction1(B_SF6, C_SF6, WAVELENGTH_RANGE);
	private static final Function1 N_SF6HT = new SellmeierFunction1(B_SF6HT, C_SF6HT, WAVELENGTH_RANGE);
	private static final Function1 N_LITHOTEC_CAF2 = new SellmeierFunction1(B_LITHOTEC_CAF2, C_LITHOTEC_CAF2, WAVELENGTH_RANGE);
	private static final Function1 N_LITHOSIL_Q = new SellmeierFunction1(B_LITHOSIL_Q, C_LITHOSIL_Q, WAVELENGTH_RANGE);

	public static Material glassF2(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_F2)); }
	public static Material glassF2HT(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_F2HT)); }
	public static Material glassF5(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_F5)); }
	public static Material glassK10(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_K10)); }
	public static Material glassK7(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_K7)); }
	public static Material glassKZFS12(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_KZFS12)); }
	public static Material glassKZFSN5(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_KZFSN5)); }
	public static Material glassLAFN7(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_LAFN7)); }
	public static Material glassLF5(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_LF5)); }
	public static Material glassLLF1(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_LLF1)); }
	public static Material glassNBAF10(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_BAF10)); }
	public static Material glassNBAF4(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_BAF4)); }
	public static Material glassNBAF51(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_BAF51)); }
	public static Material glassNBAF52(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_BAF52)); }
	public static Material glassNBAK1(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_BAK1)); }
	public static Material glassNBAK2(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_BAK2)); }
	public static Material glassNBAK4(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_BAK4)); }
	public static Material glassNBALF4(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_BALF4)); }
	public static Material glassNBALF5(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_BALF5)); }
	public static Material glassNBASF2(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_BASF2)); }
	public static Material glassNBASF64(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_BASF64)); }
	public static Material glassNBK10(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_BK10)); }
	public static Material glassNBK7(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_BK7)); }
	public static Material glassNF2(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_F2)); }
	public static Material glassNFK5(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_FK5)); }
	public static Material glassNFK51A(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_FK51A)); }
	public static Material glassNK5(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_K5)); }
	public static Material glassNKF9(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_KF9)); }
	public static Material glassNKZFS11(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_KZFS11)); }
	public static Material glassNKZFS2(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_KZFS2)); }
	public static Material glassNKZFS4(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_KZFS4)); }
	public static Material glassNKZFS5(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_KZFS5)); }
	public static Material glassNKZFS8(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_KZFS8)); }
	public static Material glassNLAF2(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LAF2)); }
	public static Material glassNLAF21(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LAF21)); }
	public static Material glassNLAF33(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LAF33)); }
	public static Material glassNLAF34(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LAF34)); }
	public static Material glassNLAF35(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LAF35)); }
	public static Material glassNLAF36(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LAF36)); }
	public static Material glassNLAF7(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LAF7)); }
	public static Material glassNLAK10(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LAK10)); }
	public static Material glassNLAK12(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LAK12)); }
	public static Material glassNLAK14(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LAK14)); }
	public static Material glassNLAK21(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LAK21)); }
	public static Material glassNLAK22(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LAK22)); }
	public static Material glassNLAK33A(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LAK33A)); }
	public static Material glassNLAK34(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LAK34)); }
	public static Material glassNLAK7(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LAK7)); }
	public static Material glassNLAK8(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LAK8)); }
	public static Material glassNLAK9(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LAK9)); }
	public static Material glassNLASF31A(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LASF31A)); }
	public static Material glassNLASF40(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LASF40)); }
	public static Material glassNLASF41(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LASF41)); }
	public static Material glassNLASF43(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LASF43)); }
	public static Material glassNLASF44(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LASF44)); }
	public static Material glassNLASF45(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LASF45)); }
	public static Material glassNLASF46A(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LASF46A)); }
	public static Material glassNLASF9(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_LASF9)); }
	public static Material glassNPK51(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_PK51)); }
	public static Material glassNPK52A(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_PK52A)); }
	public static Material glassNPSK3(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_PSK3)); }
	public static Material glassNPSK53A(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_PSK53A)); }
	public static Material glassNSF1(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SF1)); }
	public static Material glassNSF10(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SF10)); }
	public static Material glassNSF11(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SF11)); }
	public static Material glassNSF14(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SF14)); }
	public static Material glassNSF15(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SF15)); }
	public static Material glassNSF2(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SF2)); }
	public static Material glassNSF4(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SF4)); }
	public static Material glassNSF5(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SF5)); }
	public static Material glassNSF57(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SF57)); }
	public static Material glassNSF57HT(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SF57HT)); }
	public static Material glassNSF6(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SF6)); }
	public static Material glassNSF66(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SF66)); }
	public static Material glassNSF6HT(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SF6HT)); }
	public static Material glassNSF8(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SF8)); }
	public static Material glassNSK11(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SK11)); }
	public static Material glassNSK14(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SK14)); }
	public static Material glassNSK16(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SK16)); }
	public static Material glassNSK2(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SK2)); }
	public static Material glassNSK4(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SK4)); }
	public static Material glassNSK5(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SK5)); }
	public static Material glassNSSK2(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SSK2)); }
	public static Material glassNSSK5(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SSK5)); }
	public static Material glassNSSK8(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_SSK8)); }
	public static Material glassNZK7(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_N_ZK7)); }
	public static Material glassPLASF47(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_P_LASF47)); }
	public static Material glassPPK53(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_P_PK53)); }
	public static Material glassPSF67(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_P_SF67)); }
	public static Material glassPSF8(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_P_SF8)); }
	public static Material glassPSK57(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_P_SK57)); }
	public static Material glassSF1(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_SF1)); }
	public static Material glassSF10(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_SF10)); }
	public static Material glassSF2(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_SF2)); }
	public static Material glassSF4(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_SF4)); }
	public static Material glassSF5(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_SF5)); }
	public static Material glassSF56A(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_SF56A)); }
	public static Material glassSF57(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_SF57)); }
	public static Material glassSF57HHT(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_SF57HHT)); }
	public static Material glassSF6(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_SF6)); }
	public static Material glassSF6HT(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_SF6HT)); }
	public static Material glassLITHOTECCAF2(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_LITHOTEC_CAF2)); }
	public static Material glassLITHOSILQ(ColorModel c) { return new DielectricMaterial(c.getContinuous(N_LITHOSIL_Q)); }

	public static Material glassF2(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_F2), includeDispersion); }
	public static Material glassF2HT(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_F2HT), includeDispersion); }
	public static Material glassF5(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_F5), includeDispersion); }
	public static Material glassK10(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_K10), includeDispersion); }
	public static Material glassK7(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_K7), includeDispersion); }
	public static Material glassKZFS12(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_KZFS12), includeDispersion); }
	public static Material glassKZFSN5(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_KZFSN5), includeDispersion); }
	public static Material glassLAFN7(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_LAFN7), includeDispersion); }
	public static Material glassLF5(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_LF5), includeDispersion); }
	public static Material glassLLF1(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_LLF1), includeDispersion); }
	public static Material glassNBAF10(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_BAF10), includeDispersion); }
	public static Material glassNBAF4(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_BAF4), includeDispersion); }
	public static Material glassNBAF51(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_BAF51), includeDispersion); }
	public static Material glassNBAF52(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_BAF52), includeDispersion); }
	public static Material glassNBAK1(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_BAK1), includeDispersion); }
	public static Material glassNBAK2(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_BAK2), includeDispersion); }
	public static Material glassNBAK4(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_BAK4), includeDispersion); }
	public static Material glassNBALF4(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_BALF4), includeDispersion); }
	public static Material glassNBALF5(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_BALF5), includeDispersion); }
	public static Material glassNBASF2(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_BASF2), includeDispersion); }
	public static Material glassNBASF64(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_BASF64), includeDispersion); }
	public static Material glassNBK10(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_BK10), includeDispersion); }
	public static Material glassNBK7(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_BK7), includeDispersion); }
	public static Material glassNF2(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_F2), includeDispersion); }
	public static Material glassNFK5(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_FK5), includeDispersion); }
	public static Material glassNFK51A(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_FK51A), includeDispersion); }
	public static Material glassNK5(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_K5), includeDispersion); }
	public static Material glassNKF9(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_KF9), includeDispersion); }
	public static Material glassNKZFS11(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_KZFS11), includeDispersion); }
	public static Material glassNKZFS2(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_KZFS2), includeDispersion); }
	public static Material glassNKZFS4(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_KZFS4), includeDispersion); }
	public static Material glassNKZFS5(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_KZFS5), includeDispersion); }
	public static Material glassNKZFS8(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_KZFS8), includeDispersion); }
	public static Material glassNLAF2(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LAF2), includeDispersion); }
	public static Material glassNLAF21(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LAF21), includeDispersion); }
	public static Material glassNLAF33(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LAF33), includeDispersion); }
	public static Material glassNLAF34(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LAF34), includeDispersion); }
	public static Material glassNLAF35(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LAF35), includeDispersion); }
	public static Material glassNLAF36(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LAF36), includeDispersion); }
	public static Material glassNLAF7(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LAF7), includeDispersion); }
	public static Material glassNLAK10(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LAK10), includeDispersion); }
	public static Material glassNLAK12(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LAK12), includeDispersion); }
	public static Material glassNLAK14(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LAK14), includeDispersion); }
	public static Material glassNLAK21(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LAK21), includeDispersion); }
	public static Material glassNLAK22(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LAK22), includeDispersion); }
	public static Material glassNLAK33A(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LAK33A), includeDispersion); }
	public static Material glassNLAK34(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LAK34), includeDispersion); }
	public static Material glassNLAK7(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LAK7), includeDispersion); }
	public static Material glassNLAK8(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LAK8), includeDispersion); }
	public static Material glassNLAK9(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LAK9), includeDispersion); }
	public static Material glassNLASF31A(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LASF31A), includeDispersion); }
	public static Material glassNLASF40(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LASF40), includeDispersion); }
	public static Material glassNLASF41(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LASF41), includeDispersion); }
	public static Material glassNLASF43(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LASF43), includeDispersion); }
	public static Material glassNLASF44(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LASF44), includeDispersion); }
	public static Material glassNLASF45(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LASF45), includeDispersion); }
	public static Material glassNLASF46A(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LASF46A), includeDispersion); }
	public static Material glassNLASF9(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_LASF9), includeDispersion); }
	public static Material glassNPK51(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_PK51), includeDispersion); }
	public static Material glassNPK52A(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_PK52A), includeDispersion); }
	public static Material glassNPSK3(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_PSK3), includeDispersion); }
	public static Material glassNPSK53A(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_PSK53A), includeDispersion); }
	public static Material glassNSF1(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SF1), includeDispersion); }
	public static Material glassNSF10(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SF10), includeDispersion); }
	public static Material glassNSF11(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SF11), includeDispersion); }
	public static Material glassNSF14(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SF14), includeDispersion); }
	public static Material glassNSF15(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SF15), includeDispersion); }
	public static Material glassNSF2(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SF2), includeDispersion); }
	public static Material glassNSF4(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SF4), includeDispersion); }
	public static Material glassNSF5(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SF5), includeDispersion); }
	public static Material glassNSF57(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SF57), includeDispersion); }
	public static Material glassNSF57HT(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SF57HT), includeDispersion); }
	public static Material glassNSF6(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SF6), includeDispersion); }
	public static Material glassNSF66(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SF66), includeDispersion); }
	public static Material glassNSF6HT(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SF6HT), includeDispersion); }
	public static Material glassNSF8(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SF8), includeDispersion); }
	public static Material glassNSK11(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SK11), includeDispersion); }
	public static Material glassNSK14(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SK14), includeDispersion); }
	public static Material glassNSK16(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SK16), includeDispersion); }
	public static Material glassNSK2(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SK2), includeDispersion); }
	public static Material glassNSK4(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SK4), includeDispersion); }
	public static Material glassNSK5(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SK5), includeDispersion); }
	public static Material glassNSSK2(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SSK2), includeDispersion); }
	public static Material glassNSSK5(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SSK5), includeDispersion); }
	public static Material glassNSSK8(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_SSK8), includeDispersion); }
	public static Material glassNZK7(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_N_ZK7), includeDispersion); }
	public static Material glassPLASF47(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_P_LASF47), includeDispersion); }
	public static Material glassPPK53(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_P_PK53), includeDispersion); }
	public static Material glassPSF67(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_P_SF67), includeDispersion); }
	public static Material glassPSF8(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_P_SF8), includeDispersion); }
	public static Material glassPSK57(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_P_SK57), includeDispersion); }
	public static Material glassSF1(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_SF1), includeDispersion); }
	public static Material glassSF10(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_SF10), includeDispersion); }
	public static Material glassSF2(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_SF2), includeDispersion); }
	public static Material glassSF4(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_SF4), includeDispersion); }
	public static Material glassSF5(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_SF5), includeDispersion); }
	public static Material glassSF56A(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_SF56A), includeDispersion); }
	public static Material glassSF57(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_SF57), includeDispersion); }
	public static Material glassSF57HHT(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_SF57HHT), includeDispersion); }
	public static Material glassSF6(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_SF6), includeDispersion); }
	public static Material glassSF6HT(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_SF6HT), includeDispersion); }
	public static Material glassLITHOTECCAF2(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_LITHOTEC_CAF2), includeDispersion); }
	public static Material glassLITHOSILQ(ColorModel c, boolean includeDispersion) { return new DielectricMaterial(c.getContinuous(N_LITHOSIL_Q), includeDispersion); }

}
