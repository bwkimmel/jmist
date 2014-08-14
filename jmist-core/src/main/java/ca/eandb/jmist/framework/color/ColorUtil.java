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
package ca.eandb.jmist.framework.color;

import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Tuple;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.jmist.util.ArrayUtil;

/**
 * Static utilty methods for colors.
 * @author Brad Kimmel
 */
public final class ColorUtil {

  public static final double LUMENS_PER_WATT = 683.0;  

  public static final Tuple X_BAR = new Tuple(
    0.000129900000, 0.000145847000, 0.000163802100, 0.000184003700, 0.000206690200,
    0.000232100000, 0.000260728000, 0.000293075000, 0.000329388000, 0.000369914000,
    0.000414900000, 0.000464158700, 0.000518986000, 0.000581854000, 0.000655234700,
    0.000741600000, 0.000845029600, 0.000964526800, 0.001094949000, 0.001231154000,
    0.001368000000, 0.001502050000, 0.001642328000, 0.001802382000, 0.001995757000,
    0.002236000000, 0.002535385000, 0.002892603000, 0.003300829000, 0.003753236000,
    0.004243000000, 0.004762389000, 0.005330048000, 0.005978712000, 0.006741117000,
    0.007650000000, 0.008751373000, 0.010028880000, 0.011421700000, 0.012869010000,
    0.014310000000, 0.015704430000, 0.017147440000, 0.018781220000, 0.020748010000,
    0.023190000000, 0.026207360000, 0.029782480000, 0.033880920000, 0.038468240000,
    0.043510000000, 0.048995600000, 0.055022600000, 0.061718800000, 0.069212000000,
    0.077630000000, 0.086958110000, 0.097176720000, 0.108406300000, 0.120767200000,
    0.134380000000, 0.149358200000, 0.165395700000, 0.181983100000, 0.198611000000,
    0.214770000000, 0.230186800000, 0.244879700000, 0.258777300000, 0.271807900000,
    0.283900000000, 0.294943800000, 0.304896500000, 0.313787300000, 0.321645400000,
    0.328500000000, 0.334351300000, 0.339210100000, 0.343121300000, 0.346129600000,
    0.348280000000, 0.349599900000, 0.350147400000, 0.350013000000, 0.349287000000,
    0.348060000000, 0.346373300000, 0.344262400000, 0.341808800000, 0.339094100000,
    0.336200000000, 0.333197700000, 0.330041100000, 0.326635700000, 0.322886800000,
    0.318700000000, 0.314025100000, 0.308884000000, 0.303290400000, 0.297257900000,
    0.290800000000, 0.283970100000, 0.276721400000, 0.268917800000, 0.260422700000,
    0.251100000000, 0.240847500000, 0.229851200000, 0.218407200000, 0.206811500000,
    0.195360000000, 0.184213600000, 0.173327300000, 0.162688100000, 0.152283300000,
    0.142100000000, 0.132178600000, 0.122569600000, 0.113275200000, 0.104297900000,
    0.095640000000, 0.087299550000, 0.079308040000, 0.071717760000, 0.064580990000,
    0.057950010000, 0.051862110000, 0.046281520000, 0.041150880000, 0.036412830000,
    0.032010000000, 0.027917200000, 0.024144400000, 0.020687000000, 0.017540400000,
    0.014700000000, 0.012161790000, 0.009919960000, 0.007967240000, 0.006296346000,
    0.004900000000, 0.003777173000, 0.002945320000, 0.002424880000, 0.002236293000,
    0.002400000000, 0.002925520000, 0.003836560000, 0.005174840000, 0.006982080000,
    0.009300000000, 0.012149490000, 0.015535880000, 0.019477520000, 0.023992770000,
    0.029100000000, 0.034814850000, 0.041120160000, 0.047985040000, 0.055378610000,
    0.063270000000, 0.071635010000, 0.080462240000, 0.089739960000, 0.099456450000,
    0.109600000000, 0.120167400000, 0.131114500000, 0.142367900000, 0.153854200000,
    0.165500000000, 0.177257100000, 0.189140000000, 0.201169400000, 0.213365800000,
    0.225749900000, 0.238320900000, 0.251066800000, 0.263992200000, 0.277101700000,
    0.290400000000, 0.303891200000, 0.317572600000, 0.331438400000, 0.345482800000,
    0.359700000000, 0.374083900000, 0.388639600000, 0.403378400000, 0.418311500000,
    0.433449900000, 0.448795300000, 0.464336000000, 0.480064000000, 0.495971300000,
    0.512050100000, 0.528295900000, 0.544691600000, 0.561209400000, 0.577821500000,
    0.594500000000, 0.611220900000, 0.627975800000, 0.644760200000, 0.661569700000,
    0.678400000000, 0.695239200000, 0.712058600000, 0.728828400000, 0.745518800000,
    0.762100000000, 0.778543200000, 0.794825600000, 0.810926400000, 0.826824800000,
    0.842500000000, 0.857932500000, 0.873081600000, 0.887894400000, 0.902318100000,
    0.916300000000, 0.929799500000, 0.942798400000, 0.955277600000, 0.967217900000,
    0.978600000000, 0.989385600000, 0.999548800000, 1.009089200000, 1.018006400000,
    1.026300000000, 1.033982700000, 1.040986000000, 1.047188000000, 1.052466700000,
    1.056700000000, 1.059794400000, 1.061799200000, 1.062806800000, 1.062909600000,
    1.062200000000, 1.060735200000, 1.058443600000, 1.055224400000, 1.050976800000,
    1.045600000000, 1.039036900000, 1.031360800000, 1.022666200000, 1.013047700000,
    1.002600000000, 0.991367500000, 0.979331400000, 0.966491600000, 0.952847900000,
    0.938400000000, 0.923194000000, 0.907244000000, 0.890502000000, 0.872920000000,
    0.854449900000, 0.835084000000, 0.814946000000, 0.794186000000, 0.772954000000,
    0.751400000000, 0.729583600000, 0.707588800000, 0.685602200000, 0.663810400000,
    0.642400000000, 0.621514900000, 0.601113800000, 0.581105200000, 0.561397700000,
    0.541900000000, 0.522599500000, 0.503546400000, 0.484743600000, 0.466193900000,
    0.447900000000, 0.429861300000, 0.412098000000, 0.394644000000, 0.377533300000,
    0.360800000000, 0.344456300000, 0.328516800000, 0.313019200000, 0.298001100000,
    0.283500000000, 0.269544800000, 0.256118400000, 0.243189600000, 0.230727200000,
    0.218700000000, 0.207097100000, 0.195923200000, 0.185170800000, 0.174832300000,
    0.164900000000, 0.155366700000, 0.146230000000, 0.137490000000, 0.129146700000,
    0.121200000000, 0.113639700000, 0.106465000000, 0.099690440000, 0.093330610000,
    0.087400000000, 0.081900960000, 0.076804280000, 0.072077120000, 0.067686640000,
    0.063600000000, 0.059806850000, 0.056282160000, 0.052971040000, 0.049818610000,
    0.046770000000, 0.043784050000, 0.040875360000, 0.038072640000, 0.035404610000,
    0.032900000000, 0.030564190000, 0.028380560000, 0.026344840000, 0.024452750000,
    0.022700000000, 0.021084290000, 0.019599880000, 0.018237320000, 0.016987170000,
    0.015840000000, 0.014790640000, 0.013831320000, 0.012948680000, 0.012129200000,
    0.011359160000, 0.010629350000, 0.009938846000, 0.009288422000, 0.008678854000,
    0.008110916000, 0.007582388000, 0.007088746000, 0.006627313000, 0.006195408000,
    0.005790346000, 0.005409826000, 0.005052583000, 0.004717512000, 0.004403507000,
    0.004109457000, 0.003833913000, 0.003575748000, 0.003334342000, 0.003109075000,
    0.002899327000, 0.002704348000, 0.002523020000, 0.002354168000, 0.002196616000,
    0.002049190000, 0.001910960000, 0.001781438000, 0.001660110000, 0.001546459000,
    0.001439971000, 0.001340042000, 0.001246275000, 0.001158471000, 0.001076430000,
    0.000999949300, 0.000928735800, 0.000862433200, 0.000800750300, 0.000743396000,
    0.000690078600, 0.000640515600, 0.000594502100, 0.000551864600, 0.000512429000,
    0.000476021300, 0.000442453600, 0.000411511700, 0.000382981400, 0.000356649100,
    0.000332301100, 0.000309758600, 0.000288887100, 0.000269539400, 0.000251568200,
    0.000234826100, 0.000219171000, 0.000204525800, 0.000190840500, 0.000178065400,
    0.000166150500, 0.000155023600, 0.000144621900, 0.000134909800, 0.000125852000,
    0.000117413000, 0.000109551500, 0.000102224500, 0.000095394450, 0.000089023900,
    0.000083075270, 0.000077512690, 0.000072313040, 0.000067457780, 0.000062928440,
    0.000058706520, 0.000054770280, 0.000051099180, 0.000047676540, 0.000044485670,
    0.000041509940, 0.000038733240, 0.000036142030, 0.000033723520, 0.000031464870,
    0.000029353260, 0.000027375730, 0.000025524330, 0.000023793760, 0.000022178700,
    0.000020673830, 0.000019272260, 0.000017966400, 0.000016749910, 0.000015616480,
    0.000014559770, 0.000013573870, 0.000012654360, 0.000011797230, 0.000010998440,
    0.000010253980, 0.000009559646, 0.000008912044, 0.000008308358, 0.000007745769,
    0.000007221456, 0.000006732475, 0.000006276423, 0.000005851304, 0.000005455118,
    0.000005085868, 0.000004741466, 0.000004420236, 0.000004120783, 0.000003841716,
    0.000003581652, 0.000003339127, 0.000003112949, 0.000002902121, 0.000002705645,
    0.000002522525, 0.000002351726, 0.000002192415, 0.000002043902, 0.000001905497,
    0.000001776509, 0.000001656215, 0.000001544022, 0.000001439440, 0.000001341977,
    0.000001251141);

  public static final Tuple Y_BAR = new Tuple(
    0.000003917000, 0.000004393581, 0.000004929604, 0.000005532136, 0.000006208245,
    0.000006965000, 0.000007813219, 0.000008767336, 0.000009839844, 0.000011043230,
    0.000012390000, 0.000013886410, 0.000015557280, 0.000017442960, 0.000019583750,
    0.000022020000, 0.000024839650, 0.000028041260, 0.000031531040, 0.000035215210,
    0.000039000000, 0.000042826400, 0.000046914600, 0.000051589600, 0.000057176400,
    0.000064000000, 0.000072344210, 0.000082212240, 0.000093508160, 0.000106136100,
    0.000120000000, 0.000134984000, 0.000151492000, 0.000170208000, 0.000191816000,
    0.000217000000, 0.000246906700, 0.000281240000, 0.000318520000, 0.000357266700,
    0.000396000000, 0.000433714700, 0.000473024000, 0.000517876000, 0.000572218700,
    0.000640000000, 0.000724560000, 0.000825500000, 0.000941160000, 0.001069880000,
    0.001210000000, 0.001362091000, 0.001530752000, 0.001720368000, 0.001935323000,
    0.002180000000, 0.002454800000, 0.002764000000, 0.003117800000, 0.003526400000,
    0.004000000000, 0.004546240000, 0.005159320000, 0.005829280000, 0.006546160000,
    0.007300000000, 0.008086507000, 0.008908720000, 0.009767680000, 0.010664430000,
    0.011600000000, 0.012573170000, 0.013582720000, 0.014629680000, 0.015715090000,
    0.016840000000, 0.018007360000, 0.019214480000, 0.020453920000, 0.021718240000,
    0.023000000000, 0.024294610000, 0.025610240000, 0.026958570000, 0.028351250000,
    0.029800000000, 0.031310830000, 0.032883680000, 0.034521120000, 0.036225710000,
    0.038000000000, 0.039846670000, 0.041768000000, 0.043766000000, 0.045842670000,
    0.048000000000, 0.050243680000, 0.052573040000, 0.054980560000, 0.057458720000,
    0.060000000000, 0.062601970000, 0.065277520000, 0.068042080000, 0.070911090000,
    0.073900000000, 0.077016000000, 0.080266400000, 0.083666800000, 0.087232800000,
    0.090980000000, 0.094917550000, 0.099045840000, 0.103367400000, 0.107884600000,
    0.112600000000, 0.117532000000, 0.122674400000, 0.127992800000, 0.133452800000,
    0.139020000000, 0.144676400000, 0.150469300000, 0.156461900000, 0.162717700000,
    0.169300000000, 0.176243100000, 0.183558100000, 0.191273500000, 0.199418000000,
    0.208020000000, 0.217119900000, 0.226734500000, 0.236857100000, 0.247481200000,
    0.258600000000, 0.270184900000, 0.282293900000, 0.295050500000, 0.308578000000,
    0.323000000000, 0.338402100000, 0.354685800000, 0.371698600000, 0.389287500000,
    0.407300000000, 0.425629900000, 0.444309600000, 0.463394400000, 0.482939500000,
    0.503000000000, 0.523569300000, 0.544512000000, 0.565690000000, 0.586965300000,
    0.608200000000, 0.629345600000, 0.650306800000, 0.670875200000, 0.690842400000,
    0.710000000000, 0.728185200000, 0.745463600000, 0.761969400000, 0.777836800000,
    0.793200000000, 0.808110400000, 0.822496200000, 0.836306800000, 0.849491600000,
    0.862000000000, 0.873810800000, 0.884962400000, 0.895493600000, 0.905443200000,
    0.914850100000, 0.923734800000, 0.932092400000, 0.939922600000, 0.947225200000,
    0.954000000000, 0.960256100000, 0.966007400000, 0.971260600000, 0.976022500000,
    0.980300000000, 0.984092400000, 0.987418200000, 0.990312800000, 0.992811600000,
    0.994950100000, 0.996710800000, 0.998098300000, 0.999112000000, 0.999748200000,
    1.000000000000, 0.999856700000, 0.999304600000, 0.998325500000, 0.996898700000,
    0.995000000000, 0.992600500000, 0.989742600000, 0.986444400000, 0.982724100000,
    0.978600000000, 0.974083700000, 0.969171200000, 0.963856800000, 0.958134900000,
    0.952000000000, 0.945450400000, 0.938499200000, 0.931162800000, 0.923457600000,
    0.915400000000, 0.907006400000, 0.898277200000, 0.889204800000, 0.879781600000,
    0.870000000000, 0.859861300000, 0.849392000000, 0.838622000000, 0.827581300000,
    0.816300000000, 0.804794700000, 0.793082000000, 0.781192000000, 0.769154700000,
    0.757000000000, 0.744754100000, 0.732422400000, 0.720003600000, 0.707496500000,
    0.694900000000, 0.682219200000, 0.669471600000, 0.656674400000, 0.643844800000,
    0.631000000000, 0.618155500000, 0.605314400000, 0.592475600000, 0.579637900000,
    0.566800000000, 0.553961100000, 0.541137200000, 0.528352800000, 0.515632300000,
    0.503000000000, 0.490468800000, 0.478030400000, 0.465677600000, 0.453403200000,
    0.441200000000, 0.429080000000, 0.417036000000, 0.405032000000, 0.393032000000,
    0.381000000000, 0.368918400000, 0.356827200000, 0.344776800000, 0.332817600000,
    0.321000000000, 0.309338100000, 0.297850400000, 0.286593600000, 0.275624500000,
    0.265000000000, 0.254763200000, 0.244889600000, 0.235334400000, 0.226052800000,
    0.217000000000, 0.208161600000, 0.199548800000, 0.191155200000, 0.182974400000,
    0.175000000000, 0.167223500000, 0.159646400000, 0.152277600000, 0.145125900000,
    0.138200000000, 0.131500300000, 0.125024800000, 0.118779200000, 0.112769100000,
    0.107000000000, 0.101476200000, 0.096188640000, 0.091122960000, 0.086264850000,
    0.081600000000, 0.077120640000, 0.072825520000, 0.068710080000, 0.064769760000,
    0.061000000000, 0.057396210000, 0.053955040000, 0.050673760000, 0.047549650000,
    0.044580000000, 0.041758720000, 0.039084960000, 0.036563840000, 0.034200480000,
    0.032000000000, 0.029962610000, 0.028076640000, 0.026329360000, 0.024708050000,
    0.023200000000, 0.021800770000, 0.020501120000, 0.019281080000, 0.018120690000,
    0.017000000000, 0.015903790000, 0.014837180000, 0.013810680000, 0.012834780000,
    0.011920000000, 0.011068310000, 0.010273390000, 0.009533311000, 0.008846157000,
    0.008210000000, 0.007623781000, 0.007085424000, 0.006591476000, 0.006138485000,
    0.005723000000, 0.005343059000, 0.004995796000, 0.004676404000, 0.004380075000,
    0.004102000000, 0.003838453000, 0.003589099000, 0.003354219000, 0.003134093000,
    0.002929000000, 0.002738139000, 0.002559876000, 0.002393244000, 0.002237275000,
    0.002091000000, 0.001953587000, 0.001824580000, 0.001703580000, 0.001590187000,
    0.001484000000, 0.001384496000, 0.001291268000, 0.001204092000, 0.001122744000,
    0.001047000000, 0.000976589600, 0.000911108800, 0.000850133200, 0.000793238400,
    0.000740000000, 0.000690082700, 0.000643310000, 0.000599496000, 0.000558454700,
    0.000520000000, 0.000483913600, 0.000450052800, 0.000418345200, 0.000388718400,
    0.000361100000, 0.000335383500, 0.000311440400, 0.000289165600, 0.000268453900,
    0.000249200000, 0.000231301900, 0.000214685600, 0.000199288400, 0.000185047500,
    0.000171900000, 0.000159778100, 0.000148604400, 0.000138301600, 0.000128792500,
    0.000120000000, 0.000111859500, 0.000104322400, 0.000097335600, 0.000090845870,
    0.000084800000, 0.000079146670, 0.000073858000, 0.000068916000, 0.000064302670,
    0.000060000000, 0.000055981870, 0.000052225600, 0.000048718400, 0.000045447470,
    0.000042400000, 0.000039561040, 0.000036915120, 0.000034448680, 0.000032148160,
    0.000030000000, 0.000027991250, 0.000026113560, 0.000024360240, 0.000022724610,
    0.000021200000, 0.000019778550, 0.000018452850, 0.000017216870, 0.000016064590,
    0.000014990000, 0.000013987280, 0.000013051550, 0.000012178180, 0.000011362540,
    0.000010600000, 0.000009885877, 0.000009217304, 0.000008592362, 0.000008009133,
    0.000007465700, 0.000006959567, 0.000006487995, 0.000006048699, 0.000005639396,
    0.000005257800, 0.000004901771, 0.000004569720, 0.000004260194, 0.000003971739,
    0.000003702900, 0.000003452163, 0.000003218302, 0.000003000300, 0.000002797139,
    0.000002607800, 0.000002431220, 0.000002266531, 0.000002113013, 0.000001969943,
    0.000001836600, 0.000001712230, 0.000001596228, 0.000001488090, 0.000001387314,
    0.000001293400, 0.000001205820, 0.000001124143, 0.000001048009, 0.000000977058,
    0.000000910930, 0.000000849251, 0.000000791721, 0.000000738090, 0.000000688110,
    0.000000641530, 0.000000598090, 0.000000557575, 0.000000519808, 0.000000484612,
    0.000000451810);

  public static final Tuple Z_BAR = new Tuple(
    0.000606100000, 0.000680879200, 0.000765145600, 0.000860012400, 0.000966592800,
    0.001086000000, 0.001220586000, 0.001372729000, 0.001543579000, 0.001734286000,
    0.001946000000, 0.002177777000, 0.002435809000, 0.002731953000, 0.003078064000,
    0.003486000000, 0.003975227000, 0.004540880000, 0.005158320000, 0.005802907000,
    0.006450001000, 0.007083216000, 0.007745488000, 0.008501152000, 0.009414544000,
    0.010549990000, 0.011965800000, 0.013655870000, 0.015588050000, 0.017730150000,
    0.020050010000, 0.022511360000, 0.025202880000, 0.028279720000, 0.031897040000,
    0.036210000000, 0.041437710000, 0.047503720000, 0.054119880000, 0.060998030000,
    0.067850010000, 0.074486320000, 0.081361560000, 0.089153640000, 0.098540480000,
    0.110200000000, 0.124613300000, 0.141701700000, 0.161303500000, 0.183256800000,
    0.207400000000, 0.233692100000, 0.262611400000, 0.294774600000, 0.330798500000,
    0.371300000000, 0.416209100000, 0.465464200000, 0.519694800000, 0.579530300000,
    0.645600000000, 0.718483800000, 0.796713300000, 0.877845900000, 0.959439000000,
    1.039050100000, 1.115367300000, 1.188497100000, 1.258123300000, 1.323929600000,
    1.385600000000, 1.442635200000, 1.494803500000, 1.542190300000, 1.584880700000,
    1.622960000000, 1.656404800000, 1.685295900000, 1.709874500000, 1.730382100000,
    1.747060000000, 1.760044600000, 1.769623300000, 1.776263700000, 1.780433400000,
    1.782600000000, 1.782968200000, 1.781699800000, 1.779198200000, 1.775867100000,
    1.772110000000, 1.768258900000, 1.764039000000, 1.758943800000, 1.752466300000,
    1.744100000000, 1.733559500000, 1.720858100000, 1.705936900000, 1.688737200000,
    1.669200000000, 1.647528700000, 1.623412700000, 1.596022300000, 1.564528000000,
    1.528100000000, 1.486111400000, 1.439521500000, 1.389879900000, 1.338736200000,
    1.287640000000, 1.237422300000, 1.187824300000, 1.138761100000, 1.090148000000,
    1.041900000000, 0.994197600000, 0.947347300000, 0.901453100000, 0.856619300000,
    0.812950100000, 0.770517300000, 0.729444800000, 0.689913600000, 0.652104900000,
    0.616200000000, 0.582328600000, 0.550416200000, 0.520337600000, 0.491967300000,
    0.465180000000, 0.439924600000, 0.416183600000, 0.393882200000, 0.372945900000,
    0.353300000000, 0.334857800000, 0.317552100000, 0.301337500000, 0.286168600000,
    0.272000000000, 0.258817100000, 0.246483800000, 0.234771800000, 0.223453300000,
    0.212300000000, 0.201169200000, 0.190119600000, 0.179225400000, 0.168560800000,
    0.158200000000, 0.148138300000, 0.138375800000, 0.128994200000, 0.120075100000,
    0.111700000000, 0.103904800000, 0.096667480000, 0.089982720000, 0.083845310000,
    0.078249990000, 0.073208990000, 0.068678160000, 0.064567840000, 0.060788350000,
    0.057250010000, 0.053904350000, 0.050746640000, 0.047752760000, 0.044898590000,
    0.042160000000, 0.039507280000, 0.036935640000, 0.034458360000, 0.032088720000,
    0.029840000000, 0.027711810000, 0.025694440000, 0.023787160000, 0.021989250000,
    0.020300000000, 0.018718050000, 0.017240360000, 0.015863640000, 0.014584610000,
    0.013400000000, 0.012307230000, 0.011301880000, 0.010377920000, 0.009529306000,
    0.008749999000, 0.008035200000, 0.007381600000, 0.006785400000, 0.006242800000,
    0.005749999000, 0.005303600000, 0.004899800000, 0.004534200000, 0.004202400000,
    0.003900000000, 0.003623200000, 0.003370600000, 0.003141400000, 0.002934800000,
    0.002749999000, 0.002585200000, 0.002438600000, 0.002309400000, 0.002196800000,
    0.002100000000, 0.002017733000, 0.001948200000, 0.001889800000, 0.001840933000,
    0.001800000000, 0.001766267000, 0.001737800000, 0.001711200000, 0.001683067000,
    0.001650001000, 0.001610133000, 0.001564400000, 0.001513600000, 0.001458533000,
    0.001400000000, 0.001336667000, 0.001270000000, 0.001205000000, 0.001146667000,
    0.001100000000, 0.001068800000, 0.001049400000, 0.001035600000, 0.001021200000,
    0.001000000000, 0.000968640000, 0.000929920000, 0.000886880000, 0.000842560000,
    0.000800000000, 0.000760960000, 0.000723680000, 0.000685920000, 0.000645440000,
    0.000600000000, 0.000547866700, 0.000491600000, 0.000435400000, 0.000383466700,
    0.000340000000, 0.000307253300, 0.000283160000, 0.000265440000, 0.000251813300,
    0.000240000000, 0.000229546700, 0.000220640000, 0.000211960000, 0.000202186700,
    0.000190000000, 0.000174213300, 0.000155640000, 0.000135960000, 0.000116853300,
    0.000100000000, 0.000086133330, 0.000074600000, 0.000065000000, 0.000056933330,
    0.000049999990, 0.000044160000, 0.000039480000, 0.000035720000, 0.000032640000,
    0.000030000000, 0.000027653330, 0.000025560000, 0.000023640000, 0.000021813330,
    0.000020000000, 0.000018133330, 0.000016200000, 0.000014200000, 0.000012133330,
    0.000010000000, 0.000007733333, 0.000005400000, 0.000003200000, 0.000001333333,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000, 0.000000000000,
    0.000000000000);

  public static final Tuple XYZ_WAVELENGTHS = new Tuple(ArrayUtil.range(360e-9, 830e-9, ColorUtil.X_BAR.size()));

  private static final LinearMatrix3 XYZ_TO_sRGBLin = new LinearMatrix3(
       3.2410, -1.5374, -0.4986,
      -0.9692,  1.8760,  0.0416,
       0.0556, -0.2040,  1.0570);

  private static final LinearMatrix3 sRGBLin_TO_XYZ = new LinearMatrix3(
      0.4124, 0.3576, 0.1805,
      0.2126, 0.7152, 0.0722,
      0.0193, 0.1192, 0.9505);

  private static final double EPSILON = 0.008856;
  private static final double CBRT_EPSILON = Math.cbrt(EPSILON);

  private static final double KAPPA = 903.3;

  public static CIEXYZ convertLuv2XYZ(double L, double u, double v, double Xr, double Yr, double Zr) {
    double wr = Xr + 15.0 * Yr + 3.0 * Zr;
    double u0 = 4.0 * Xr / wr;
    double v0 = 9.0 * Yr / wr;
    double Y;
    if (L > KAPPA * EPSILON) {
      Y = (L + 16.0) / 116.0;
      Y = Y * Y * Y;
    } else {
      Y = L / KAPPA;
    }

    double a = (1.0 / 3.0) * ((52.0 * L) / (u + 13.0 * L * u0) - 1.0);
    double b = -5.0 * Y;
    double c = -1.0 / 3.0;
    double d = Y * ((39.0 * L) / (v + 13.0 * L * v0) - 5.0);
    double X = (d - b) / (a - c);
    double Z = X * a + b;
    return new CIEXYZ(X, Y, Z);
  }

  public static CIEXYZ convertLuv2XYZ(double L, double u, double v, CIEXYZ ref) {
    return convertLuv2XYZ(L, u, v, ref.X(), ref.Y(), ref.Z());
  }

  public static CIEXYZ convertLuv2XYZ(CIELuv luv, CIEXYZ ref) {
    return convertLuv2XYZ(luv.L(), luv.u(), luv.v(), ref);
  }

  public static CIELuv convertXYZ2Luv(double X, double Y, double Z, double Xr, double Yr, double Zr) {
    double yr = Y / Yr;
    double w = X + 15.0 * Y + 3.0 * Z;
    double wr = Xr + 15.0 * Yr + 3.0 * Zr;
    double U = 4.0 * X / w;
    double V = 9.0 * Y / w;
    double Ur = 4.0 * Xr / wr;
    double Vr = 9.0 * Yr / wr;
    double L = yr > EPSILON ? 116.0 * Math.cbrt(yr) - 16.0 : KAPPA * yr;
    double u = 13.0 * L * (U - Ur);
    double v = 13.0 * L * (V - Vr);
    return new CIELuv(L, u, v);
  }

  public static CIELuv convertXYZ2Luv(double X, double Y, double Z, CIEXYZ ref) {
    return convertXYZ2Luv(X, Y, Z, ref.X(), ref.Y(), ref.Z());
  }

  public static CIELuv convertXYZ2Luv(CIEXYZ xyz, CIEXYZ ref) {
    return convertXYZ2Luv(xyz.X(), xyz.Y(), xyz.Z(), ref);
  }

  public static CIEXYZ convertLab2XYZ(double L, double a, double b, double Xr, double Yr, double Zr) {
    double fy = (L + 16.0) / 116.0;
    double fx = (a / 500.0) + fy;
    double fz = fy - (b / 200.0);
    double xr = fx > CBRT_EPSILON ? fx * fx * fx : (116.0 * fx - 16.0) / KAPPA;
    double zr = fz > CBRT_EPSILON ? fz * fz * fz : (116.0 * fz - 16.0) / KAPPA;
    double yr;
    if (L > KAPPA * EPSILON) {
      yr = (L + 16.0) / 116.0;
      yr = yr * yr * yr;
    } else {
      yr = L / KAPPA;
    }
    double X = xr * Xr;
    double Y = yr * Yr;
    double Z = zr * Zr;
    return new CIEXYZ(X, Y, Z);
  }

  public static CIEXYZ convertLab2XYZ(double L, double a, double b, CIEXYZ ref) {
    return convertLab2XYZ(L, a, b, ref.X(), ref.Y(), ref.Z());
  }

  public static CIEXYZ convertLab2XYZ(CIELab lab, CIEXYZ ref) {
    return convertLab2XYZ(lab.L(), lab.a(), lab.b(), ref);
  }

  public static CIELab convertXYZ2Lab(double X, double Y, double Z, double Xr, double Yr, double Zr) {
    double xr = X / Xr;
    double yr = Y / Yr;
    double zr = Z / Zr;
    double fx = xr > EPSILON ? Math.cbrt(xr) : (KAPPA * xr + 16.0) / 116.0;
    double fy = yr > EPSILON ? Math.cbrt(yr) : (KAPPA * yr + 16.0) / 116.0;
    double fz = zr > EPSILON ? Math.cbrt(zr) : (KAPPA * zr + 16.0) / 116.0;
    double L = 116.0 * fy - 16.0;
    double a = 500.0 * (fx - fy);
    double b = 200.0 * (fy - fz);
    return new CIELab(L, a, b);
  }

  public static CIELab convertXYZ2Lab(double X, double Y, double Z, CIEXYZ ref) {
    return convertXYZ2Lab(X, Y, Z, ref.X(), ref.Y(), ref.Z());
  }

  public static CIELab convertXYZ2Lab(CIEXYZ xyz, CIEXYZ ref) {
    return convertXYZ2Lab(xyz.X(), xyz.Y(), xyz.Z(), ref);
  }

  public static CIEYuv convertXYZ2Yuv(double X, double Y, double Z) {
    double w = X + 15.0 * Y + 3.0 * Z;
    return new CIEYuv(Y, 4.0 * X / w, 6.0 * Y / w);
  }

  public static CIEYuv convertXYZ2Yuv(CIEXYZ xyz) {
    return convertXYZ2Yuv(xyz.X(), xyz.Y(), xyz.Z());
  }

  public static CIEXYZ convertYuv2XYZ(double Y, double u, double v) {
    double w = 2.0 * u - 8.0 * v + 4.0;
    double x = 3.0 * u / w;
    double y = 2.0 * v / w;
    return convertxyY2XYZ(x, y, Y);
  }

  public static CIEXYZ convertYuv2XYZ(CIEYuv Yuv) {
    return convertYuv2XYZ(Yuv.Y(), Yuv.u(), Yuv.v());
  }

  public static CIExyY convertXYZ2xyY(double X, double Y, double Z) {
    double w = X + Y + Z;
    return MathUtil.isZero(w) ? CIExyY.ZERO : new CIExyY(X / w, Y / w, Y);
  }

  public static CIExyY convertXYZ2xyY(CIEXYZ xyz) {
    return convertXYZ2xyY(xyz.X(), xyz.Y(), xyz.Z());
  }

  public static CIEXYZ convertxyY2XYZ(double x, double y, double Y) {
    if (!MathUtil.isZero(y)) {
      double w = Y / y;
      return new CIEXYZ(x * w, Y, (1.0 - x - y) * w);
    } else {
      return CIEXYZ.ZERO;
    }
  }

  public static CIEXYZ convertxyY2XYZ(CIExyY xyY) {
    return convertxyY2XYZ(xyY.x(), xyY.y(), xyY.Y());
  }

  public static CIEXYZ convertRGB2XYZ(double r, double g, double b) {
    Vector3 rgb = new Vector3(linearize(r), linearize(g), linearize(b));
    Vector3 xyz = sRGBLin_TO_XYZ.times(rgb);
    return new CIEXYZ(xyz.x(), xyz.y(), xyz.z());
  }

  public static CIEXYZ convertRGB2XYZ(RGB rgb) {
    return convertRGB2XYZ(rgb.r(), rgb.g(), rgb.b());
  }

  private static double linearize(double c) {
    if (c <= 0.04045) {
      return c / 12.92;
    } else {
      return Math.pow((c + 0.055) / 1.055, 2.4);
    }
  }

  public static RGB convertXYZ2RGB(double x, double y, double z) {
    Vector3 xyz = new Vector3(x, y, z);
    Vector3 rgb = XYZ_TO_sRGBLin.times(xyz);
    return new RGB(
        delinearize(rgb.x()),
        delinearize(rgb.y()),
        delinearize(rgb.z()));
  }

  public static RGB convertXYZ2RGB(CIEXYZ xyz) {
    return convertXYZ2RGB(xyz.X(), xyz.Y(), xyz.Z());
  }

  private static double delinearize(double c) {
    if (c <= 0.0031308) {
      return 12.92 * c;
    } else {
      return 1.055 * Math.pow(c, 1.0 / 2.4) - 0.055;
    }
  }

  public static CIEXYZ convertSample2XYZ(double wavelength, double value) {
    return new CIEXYZ(
        value * MathUtil.interpolate(XYZ_WAVELENGTHS, X_BAR, wavelength),
        value * MathUtil.interpolate(XYZ_WAVELENGTHS, Y_BAR, wavelength),
        value * MathUtil.interpolate(XYZ_WAVELENGTHS, Z_BAR, wavelength));
  }

  public static RGB convertSample2RGB(double wavelength, double value) {
    return convertXYZ2RGB(convertSample2XYZ(wavelength, value));
  }

  public static CIEXYZ convertSpectrum2XYZ(double[] wavelengths, double[] values) {
    CIEXYZ xyz = CIEXYZ.ZERO;
    for (int i = 0, n = wavelengths.length; i < n; i++) {
      xyz = xyz.plus(convertSample2XYZ(wavelengths[i], values[i]
          / (double) n));
    }
    return xyz;
  }

  public static CIEXYZ convertSpectrum2XYZ(Tuple wavelengths, double[] values) {
    CIEXYZ xyz = CIEXYZ.ZERO;
    for (int i = 0, n = wavelengths.size(); i < n; i++) {
      xyz = xyz.plus(convertSample2XYZ(wavelengths.at(i), values[i]
          / (double) n));
    }
    return xyz;
  }

  public static RGB convertSpectrum2RGB(double[] wavelengths, double[] values) {
    return convertXYZ2RGB(convertSpectrum2XYZ(wavelengths, values));
  }

  public static RGB convertSpectrum2RGB(Tuple wavelengths, double[] values) {
    return convertXYZ2RGB(convertSpectrum2XYZ(wavelengths, values));
  }

  public static double convertSample2Luminance(double wavelength, double value) {
    return value * MathUtil.interpolate(XYZ_WAVELENGTHS, Y_BAR, wavelength);
  }

  public static double convertSpectrum2Luminance(double[] wavelengths, double[] values) {
    double Y = 0.0;
    int n = wavelengths.length;
    for (int i = 0; i < n; i++) {
      Y += convertSample2Luminance(wavelengths[i], values[i]);
    }
    return Y / (double) n;
  }

  public static double convertSpectrum2Luminance(Tuple wavelengths, double[] values) {
    double Y = 0.0;
    int n = wavelengths.size();
    for (int i = 0; i < n; i++) {
      Y += convertSample2Luminance(wavelengths.at(i), values[i]);
    }
    return Y / (double) n;
  }

  public static double convertRGB2Luminance(double r, double g, double b) {
    return 0.2126 * r + 0.7152 * g + 0.0722 * b;
  }

  public static double convertRGB2Luminance(RGB rgb) {
    return convertRGB2Luminance(rgb.r(), rgb.g(), rgb.b());
  }

  public static double convertXYZ2Luminance(double x, double y, double z) {
    return y;
  }

  public static double convertXYZ2Luminance(CIEXYZ xyz) {
    return xyz.Y();
  }

  public static double getMeanChannelValue(Color color) {
    int channels = color.getColorModel().getNumChannels();
    double sum = 0.0;
    for (int i = 0; i < channels; i++) {
      sum += color.getValue(i);
    }
    return sum / (double) channels;
  }

  public static double getTotalChannelValue(Color color) {
    int channels = color.getColorModel().getNumChannels();
    double sum = 0.0;
    for (int i = 0; i < channels; i++) {
      sum += color.getValue(i);
    }
    return sum;
  }
  
  public static double getMaxChannelValue(Color color) {
    int channels = color.getColorModel().getNumChannels();
    double max = Double.NEGATIVE_INFINITY;
    for (int i = 0; i < channels; i++) {
      double c = color.getValue(i);
      if (c > max) {
        max = c;
      }
    }
    return max;
  }

  /**
   * Adds two <code>Color</code>s, one or both of which may be
   * <code>null</code>.
   * @param a The first <code>Color</code>.
   * @param b The second <code>Color</code>.
   * @return If <code>a != null</code> and <code>b != null</code>, then the
   *     sum <code>a.plus(b)</code> is returned.  If one is non-null and the
   *     other is null, the non-null value is returned.  If both are null,
   *     <code>null</code> is returned.
   * @see Color#plus(Color)
   */
  public static Color add(Color a, Color b) {
    return a != null ? (b != null ? a.plus(b) : a) : b;
  }
  
  /**
   * Subtracts two <code>Color</code>s, one or both of which may be
   * <code>null</code>.
   * @param a The <code>Color</code> to subtract from.
   * @param b The <code>Color</code> to subtract.
   * @return If <code>a != null</code> and <code>b != null</code>, then the
   *     difference <code>a.minus(b)</code> is returned.  If <code>b ==
   *     null</code>, then <code>a</code> is returned.  If <code>a ==
   *     null</code>, then <code>b.negative()</code> is returned.  If both
   *     are null, then <code>null</code> is returned.
   * @see Color#minus(Color)
   * @see Color#negative() 
   */
  public static Color sub(Color a, Color b) {
    return b != null ? (a != null ? a.minus(b) : b.negative()) : a;
  }

  /**
   * Multiplies two <code>Color</code>, one or both of which may be
   * <code>null</code>.
   * @param a The first <code>Color</code>.
   * @param b The second <code>Color</code>.
   * @return If <code>a != null</code> and <code>b != null</code>, then the
   *     product <code>a.times(b)</code> is returned, otherwise
   *     <code>null</code> is returned.
   * @see Color#times(Color)
   */
  public static Color mul(Color a, Color b) {
    return a != null && b != null ? a.times(b) : null;
  }

  /**
   * Multiplies a (possibly <code>null</code>) <code>Color</code> by a
   * scalar.
   * @param a The <code>Color</code> to multiply.
   * @param c The scalar to multiply by.
   * @return If <code>a != null</code>, the product <code>a.times(c)</code>
   *     is returned, otherwise, <code>null</code> is returned.
   * @see Color#times(double)
   */
  public static Color mul(Color a, double c) {
    return a != null ? a.times(c) : null;
  }

  /**
   * Divides a (possibly <code>null</code>) <code>Color</code> by a scalar.
   * @param a The <code>Color</code> to divide.
   * @param c The scalar to divide by.
   * @return If <code>a != null</code>, the quotient <code>a.divide(c)</code>
   *     is returned, otherwise, <code>null</code> is returned.
   * @see Color#divide(double)
   */
  public static Color div(Color a, double c) {
    return a != null ? a.divide(c) : null;
  }

  public static Color getWhite(WavelengthPacket lambda) {
    return lambda.getColorModel().getWhite(lambda);
  }

  public static Color getBlack(WavelengthPacket lambda) {
    return lambda.getColorModel().getBlack(lambda);
  }

  public static Color getGray(double value, WavelengthPacket lambda) {
    return lambda.getColorModel().getGray(value, lambda);
  }
  
  public static int getDispersionChannel(Color color) {
    ColorModel cm = color.getColorModel();
    int channel = -1;
    for (int i = 0, n = cm.getNumChannels(); i < n; i++) {
      if (!MathUtil.isZero(color.getValue(i))) {
        if (channel < 0) {
          channel = i;
        } else {
          return -1;
        }
      }
    }
    return 0;
  }

  /** Instances of <code>ColorUtil</code>  cannot be created. */
  private ColorUtil() {}

}
