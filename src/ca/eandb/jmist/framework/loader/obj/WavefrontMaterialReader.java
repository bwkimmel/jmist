/*
 * Copyright (c) 2008 Bradley W. Kimmel
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

package ca.eandb.jmist.framework.loader.obj;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.material.LambertianMaterial;
import ca.eandb.jmist.framework.painter.ProductPainter;
import ca.eandb.jmist.framework.painter.Texture2Painter;
import ca.eandb.jmist.framework.painter.UniformPainter;
import ca.eandb.jmist.framework.shader.ConstantShader;
import ca.eandb.jmist.framework.shader.DirectLightingShader;
import ca.eandb.jmist.framework.shader.PhongShader;
import ca.eandb.jmist.framework.texture.RasterTexture2;
import ca.eandb.util.UnimplementedException;

/**
 * @author brad
 *
 */
final class WavefrontMaterialReader {

	private static Map<String, LineInterpreter> lineInterpreters;

	private static LineInterpreter LI_DEFAULT = new LineInterpreter() {
		public void process(String[] args, State state) {
			state.addWarningMessage(String.format("Unrecognized command: `%s'", args[0]));
		}
	};

	private static LineInterpreter LI_NEWMTL = new LineInterpreter() {
		public void process(String[] args, State state) throws IOException {
			state.newMaterial(args[1]);
		}
	};

	private static LineInterpreter LI_MAP = new LineInterpreter() {
		public void process(String[] args, State state) {
			String name = args[0].replaceFirst("^map_", "");
			state.setMap(name, args[1]);
		}
	};

	private static LineInterpreter LI_COLOR = new LineInterpreter() {
		public void process(String[] args, State state) {

			String name = args[0];
			Spectrum s;

			if (args[1].equals("xyz")) {
				double x = Double.parseDouble(args[2]);
				double y = (args.length > 3) ? Double.parseDouble(args[3]) : x;
				double z = (args.length > 3) ? Double.parseDouble(args[4]) : x;
				s = state.colorModel.fromXYZ(x, y, z);
			} else if (args[1].equals("spectral")) {
				state.addWarningMessage("spectral is unsupported");
				s = state.colorModel.getWhite();
			} else {
				double r = Double.parseDouble(args[1]);
				double g = (args.length > 2) ? Double.parseDouble(args[2]) : r;
				double b = (args.length > 2) ? Double.parseDouble(args[3]) : r;
				s = state.colorModel.fromRGB(r, g, b);
			}

			state.setColor(name, s);

		}
	};

	private static LineInterpreter LI_NS = new LineInterpreter() {
		public void process(String[] args, State state) {
			state.setExponent(Double.parseDouble(args[1]));
		}
	};

	private static LineInterpreter LI_SHARPNESS = new LineInterpreter() {
		public void process(String[] args, State state) {
			state.setSharpness(Double.parseDouble(args[1]));
		}
	};

	private static LineInterpreter LI_OPTICAL_DENSITY = new LineInterpreter() {
		public void process(String[] args, State state) {
			state.setRefractiveIndex(Double.parseDouble(args[1]));
		}
	};

	private static LineInterpreter LI_ILLUM = new LineInterpreter() {
		public void process(String[] args, State state) {
			state.setIlluminationModel(Integer.parseInt(args[1]));
		}
	};

	public void read(File file, ColorModel cm, AppearanceVisitor visitor) throws IOException {
		FileInputStream stream = new FileInputStream(file);
		LineNumberReader reader = new LineNumberReader(new InputStreamReader(stream));
		State state = new State(file.getParentFile(), cm, visitor);

		while (true) {

			String line = reader.readLine();
			if (line == null) {
				break;
			}
			while (line.endsWith("\\")) {
				line = line.substring(0, line.length() - 1) + " " + reader.readLine();
			}

			line = line.replaceAll("#.*$", "");

			String[] args = line.split("\\s+");
			if (args.length > 0) {
				LineInterpreter interp = getLineInterpreter(args[0]);
				interp.process(args, state);
			}

		}

	}

	private LineInterpreter getLineInterpreter(String key) {
		if (lineInterpreters == null) {
			initialize();
		}
		return lineInterpreters.containsKey(key) ? lineInterpreters.get(key) : LI_DEFAULT;
	}

	private static void initialize() {
		lineInterpreters = new HashMap<String, LineInterpreter>();

		lineInterpreters.put("newmtl", LI_NEWMTL);
		lineInterpreters.put("Ka", LI_COLOR);
		lineInterpreters.put("Kd", LI_COLOR);
		lineInterpreters.put("Ks", LI_COLOR);
		lineInterpreters.put("Tf", LI_COLOR);
		lineInterpreters.put("map_Ka", LI_MAP);
		lineInterpreters.put("map_Kd", LI_MAP);
		lineInterpreters.put("map_Ks", LI_MAP);
		lineInterpreters.put("map_Tf", LI_MAP);
		lineInterpreters.put("Ns", LI_SHARPNESS);
		lineInterpreters.put("sharpness", LI_SHARPNESS);
		lineInterpreters.put("optical_density", LI_OPTICAL_DENSITY);
		lineInterpreters.put("illum", LI_ILLUM);
	}

	private class State {

		public final ColorModel colorModel;
		private final AppearanceVisitor visitor;
		private String currentMaterialName = null;
		private Map<String, Spectrum> colors = new HashMap<String, Spectrum>();
		private Map<String, String> maps = new HashMap<String, String>();
		private double exponent;
		private double sharpness;
		private double refractiveIndex;
		private int illum;
		private final File directory;

		public State(File directory, ColorModel colorModel, AppearanceVisitor visitor) {
			this.directory = directory;
			this.colorModel = colorModel;
			this.visitor = visitor;
			reset();
		}

		public void newMaterial(String name) throws IOException {
			visitMaterial();
			currentMaterialName = name;
		}

		private Painter getPainter(String name) throws IOException {
			Spectrum color = getColor(name);
			if (maps.containsKey(name)) {
				File file = new File(directory, maps.get(name));
				return new ProductPainter(color, new Texture2Painter(new RasterTexture2(file)));
			}
			return new UniformPainter(color);
		}

		private Spectrum getColor(String name) {
			if (colors.containsKey(name)) {
				return colors.get(name);
			} else {
				return colorModel.getWhite();
			}
		}

		private void visitMaterial() throws IOException {
			if (currentMaterialName != null) {
				Material material = Material.BLACK; // FIXME
				Shader shader = null;
				Painter ka, kd, ks, tf;

				switch (illum) {
				case 0:
					kd = getPainter("Kd");
					material = new LambertianMaterial(kd);
					shader = new ConstantShader(kd);
					break;

				case 1:
					ka = getPainter("Ka");
					kd = getPainter("Kd");
					// TODO add ambient term
					material = new LambertianMaterial(kd);
					shader = new DirectLightingShader();
					break;

				case 2:
					ka = getPainter("Ka");
					kd = getPainter("Kd");
					ks = getPainter("Ks");
					material = new LambertianMaterial(kd); // FIXME should be phong material
					shader = new PhongShader(kd, ks, ka, exponent);
					break;

				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
					throw new UnimplementedException();

				default:
					throw new IllegalStateException("Unrecognized illumination model: " + Integer.toString(illum));
				}

				visitor.visit(currentMaterialName, material, shader);
				reset();
			}
		}

		private void reset() {
			currentMaterialName = null;
			colors.clear();
			exponent = Double.NaN;
			sharpness = Double.POSITIVE_INFINITY;
			refractiveIndex = Double.NaN;
		}

		private void setColor(String name, Spectrum spectrum) {
			colors.put(name, spectrum);
		}

		private void setMap(String name, String file) {
			maps.put(name, file);
		}

		private void setExponent(double exponent) {
			this.exponent = exponent;
		}

		private void setSharpness(double sharpness) {
			this.sharpness = sharpness;
		}

		private void setRefractiveIndex(double refractiveIndex) {
			this.refractiveIndex = refractiveIndex;
		}

		private void setIlluminationModel(int illum) {
			this.illum = illum;
		}

		public void addWarningMessage(String format) {
			// TODO Auto-generated method stub

		}

		public void finish() throws IOException {
			visitMaterial();
		}

	}

	private interface LineInterpreter {

		void process(String[] args, State state) throws IOException;

	}

}
