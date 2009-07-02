/**
 *
 */
package ca.eandb.jmist.framework.loader.obj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.rgb.RGBColorModel;
import ca.eandb.jmist.framework.geometry.primitive.PolyhedronGeometry;
import ca.eandb.jmist.framework.scene.AppearanceMapSceneElement;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad Kimmel
 *
 */
public final class WavefrontObjectReader {

	public synchronized SceneElement read(File in, ColorModel cm) throws IOException {
		return read(in, 1.0, cm);
	}

	public synchronized SceneElement read(File in, double scale, ColorModel cm) throws IOException {
		return read(in, new HashMap<String, Material>(), scale, cm);
	}

	public synchronized SceneElement read(File in, Map<String, Material> materials, double scale, ColorModel cm) throws IOException {

		FileInputStream stream = new FileInputStream(in);
		LineNumberReader reader = new LineNumberReader(new InputStreamReader(stream));
		State state = new State(in.getParentFile(), scale, cm);

		for (Map.Entry<String, Material> entry : this.materials.entrySet()) {
			state.addAppearance(entry.getKey(), entry.getValue(), null);
		}

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
				try {
					interp.process(args, state);
				} catch (Exception e) {
					System.err.println("Error occurred on line " + Integer.toString(reader.getLineNumber()));
					e.printStackTrace();
					return null;
				}
			}

		}

		return state.result;

	}

	public void addMaterial(String name, Material material) {
		this.materials.put(name, material);
	}

	private final Map<String, Material> materials = new HashMap<String, Material>();

	private static void checkArgs(String[] args, State state, int min, int max) {
		int count = args.length - 1;
		if (!(min <= count && count <= max)) {
			if (min == max) {
				state.addErrorMessage(String.format("Expected %d arguments, but got %d.", min, count));
			} else if (max == Integer.MAX_VALUE) {
				state.addErrorMessage(String.format("Expected at least %d arguments, but got %d.", min, count));
			} else {
				state.addErrorMessage(String.format("Expected between %d and %d arguments, but got %d.", min, max, count));
			}
		}
	}

	private static void checkArgs(String[] args, State state, int count) {
		checkArgs(args, state, count, count);
	}


	private static class State {

		/**
		 * @param geometry
		 */
		public State(File directory, double scale, ColorModel colorModel) {
			this.directory = directory;
			this.scale = scale;
			this.colorModel = colorModel;
		}

		public void addErrorMessage(String format) {
			// TODO Auto-generated method stub

		}

		public void addWarningMessage(String format) {
			// TODO Auto-generated method stub

		}

		public void addFace(int[] vi, int[] vti, int[] vni) {
			canonicalize(vi, vs.size());
			canonicalize(vti, vts.size());
			canonicalize(vni, vns.size());

			int fi = geometry.getNumPrimitives();
			geometry.addFace(vi, vti, vni);

			if (activeMaterialName != null) {
				appearance.setAppearance(fi, activeMaterialName);
			}
		}

		private void canonicalize(int[] indices, int size) {
			if (indices != null) {
				for (int i = 0; i < indices.length; i++) {
					indices[i] = (indices[i] >= 0) ? indices[i] - 1 : indices[i] + size;
					if (indices[i] < 0 || indices[i] >= size) {
						throw new IndexOutOfBoundsException();
					}
				}
			}
		}

		public void addAppearance(String name, Material material, Shader shader) {
			ensureAppearanceMap();
			appearance.addAppearance(name, material, shader);
		}

		public void addVertex(Point3 p, double w) {
			this.vs.add(new Point3(p.x() * scale, p.y() * scale, p.z() * scale));
			this.weights.add(w);
		}

		public void addTexCoord(Point2 p) {
			this.vts.add(p);
		}

		public void addNormal(Vector3 v) {
			this.vns.add(v);
		}

		public void setActiveMaterial(String name) {
			activeMaterialName = name;
			ensureAppearanceMap();
		}

		private void ensureAppearanceMap() {
			if (appearance == null) {
				appearance = new AppearanceMapSceneElement(geometry);
				result = appearance;
			}
		}

		//private final Map<String, Material> materials = new HashMap<String, Material>();
		private final List<Point3> vs = new ArrayList<Point3>();
		private final List<Point2> vts = new ArrayList<Point2>();
		private final List<Vector3> vns = new ArrayList<Vector3>();
		private final List<Double> weights = new ArrayList<Double>();

		private String activeMaterialName = null;

		private final PolyhedronGeometry geometry = new PolyhedronGeometry(vs, vts, vns);
		private AppearanceMapSceneElement appearance = null;

		private SceneElement result = geometry;

		private final double scale;

		private final File directory;

		private final ColorModel colorModel;

	}

	private static interface LineInterpreter {

		void process(String[] args, State state);

	}

	private LineInterpreter getLineInterpreter(String key) {
		if (lineInterpreters == null) {
			initialize();
		}
		return lineInterpreters.containsKey(key) ? lineInterpreters.get(key) : LI_DEFAULT;
	}

	private static void initialize() {
		lineInterpreters = new HashMap<String, LineInterpreter>();

		lineInterpreters.put("v", LI_V);
		lineInterpreters.put("vt", LI_VT);
		lineInterpreters.put("vn", LI_VN);
		lineInterpreters.put("f", LI_F);
		lineInterpreters.put("usemtl", LI_USEMTL);
		lineInterpreters.put("mtllib", LI_MTLLIB);
	}

	private static Map<String, LineInterpreter> lineInterpreters = null;

	private static LineInterpreter LI_DEFAULT = new LineInterpreter() {

		public void process(String[] args, State state) {
			state.addWarningMessage(String.format("Unrecognized command: `%s'", args[0]));
		}

	};

	private static LineInterpreter LI_V = new LineInterpreter() {

		public void process(String[] args, State state) {
			checkArgs(args, state, 3, 4);

			state.addVertex(
					new Point3(
							Double.parseDouble(args[1]),
							Double.parseDouble(args[2]),
							Double.parseDouble(args[3])
					),
					args.length > 4 ? Double.parseDouble(args[4]) : 1.0
			);

		}

	};

	private static LineInterpreter LI_VT = new LineInterpreter() {

		public void process(String[] args, State state) {
			checkArgs(args, state, 2, 3);

			state.addTexCoord(
					new Point2(
							Double.parseDouble(args[1]),
							Double.parseDouble(args[2])));
		}

	};

	private static LineInterpreter LI_VN = new LineInterpreter() {

		public void process(String[] args, State state) {
			checkArgs(args, state, 3, 3);

			state.addNormal(
					new Vector3(
							Double.parseDouble(args[1]),
							Double.parseDouble(args[2]),
							Double.parseDouble(args[3])));
		}

	};

	private static LineInterpreter LI_F = new LineInterpreter() {

		public void process(String[] args, State state) {
			checkArgs(args, state, 3, Integer.MAX_VALUE);

			List<Integer> vertexIndexList = new ArrayList<Integer>();
			List<Integer> textureIndexList = new ArrayList<Integer>();
			List<Integer> normalIndexList = new ArrayList<Integer>();

			for (int i = 1; i < args.length; i++) {
				String[] indices = args[i].split("/", 3);

				vertexIndexList.add(Integer.parseInt(indices[0]));
				if (indices.length > 1 && !indices[1].equals("")) {
					textureIndexList.add(Integer.parseInt(indices[1]));
				}
				if (indices.length > 2 && !indices[2].equals("")) {
					normalIndexList.add(Integer.parseInt(indices[2]));
				}
			}

			int[] vertexIndices = !vertexIndexList.isEmpty() ? new int[vertexIndexList.size()] : null;
			for (int i = 0; i < vertexIndexList.size(); i++) {
				vertexIndices[i] = vertexIndexList.get(i);
			}

			int[] textureIndices = !textureIndexList.isEmpty() ? new int[textureIndexList.size()] : null;
			for (int i = 0; i < textureIndexList.size(); i++) {
				textureIndices[i] = textureIndexList.get(i);
			}

			int[] normalIndices = !normalIndexList.isEmpty() ? new int[normalIndexList.size()] : null;
			for (int i = 0; i < normalIndexList.size(); i++) {
				normalIndices[i] = normalIndexList.get(i);
			}

			state.addFace(vertexIndices, textureIndices, normalIndices);
		}

	};

	private static LineInterpreter LI_USEMTL = new LineInterpreter() {

		public void process(String[] args, State state) {
			checkArgs(args, state, 1);
			state.setActiveMaterial(args[1]);
		}

	};

	private static LineInterpreter LI_MTLLIB = new LineInterpreter() {

		public void process(String[] args, final State state) {

			WavefrontMaterialReader reader = new WavefrontMaterialReader();
			for (int i = 1; i < args.length; i++) {
				File file = new File(state.directory, args[i]);
				try {
					reader.read(file, state.colorModel, new AppearanceVisitor() {
						public void visit(String name, Material material,
								Shader shader) {
							state.addAppearance(name, material, shader);
						}
					});
				} catch (FileNotFoundException e) {
					state.addErrorMessage("File not found: " + args[i]);
					e.printStackTrace();
				} catch (IOException e) {
					state.addErrorMessage("Could not read file: " + args[i]);
					e.printStackTrace();
				}

			}

		}

	};



}
