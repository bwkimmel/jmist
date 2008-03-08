/**
 *
 */
package org.jmist.packages;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmist.framework.Material;
import org.jmist.packages.geometry.primitive.PolygonGeometry;
import org.jmist.packages.light.CompositeLight;
import org.jmist.toolkit.Point3;

/**
 * @author bkimmel
 *
 */
public final class WavefrontObjectReader {

	public synchronized void read(InputStream in, CompositeGeometry geometry, CompositeLight light) throws IOException {
		this.read(in, geometry, light, Material.BLACK);
	}

	public synchronized void read(InputStream in, CompositeGeometry geometry, CompositeLight light, double scale) throws IOException {
		this.read(in, geometry, light, Material.BLACK, scale);
	}

	public synchronized void read(InputStream in, CompositeGeometry geometry, CompositeLight light, Material defaultMaterial) throws IOException {
		this.read(in, geometry, light, defaultMaterial, 1.0);
	}

	public synchronized void read(InputStream in, CompositeGeometry geometry, CompositeLight light, Material defaultMaterial, double scale) throws IOException {

		LineNumberReader reader = new LineNumberReader(new InputStreamReader(in));
		State state = new State(geometry, light, defaultMaterial, scale);

		for (Map.Entry<String, Material> entry : this.materials.entrySet()) {
			state.addMaterial(entry.getKey(), entry.getValue());
		}

		while (true) {

			String line = reader.readLine();
			if (line == null) {
				break;
			}

			line = line.replace("#.*$", "");

			String[] args = line.split("\\s+");
			if (args.length > 0) {
				LineInterpreter interp = getLineInterpreter(args[0]);
				interp.process(args, state);
			}

		}

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
		public State(CompositeGeometry geometry, CompositeLight light, Material defaultMaterial, double scale) {
			this.geometry = geometry;
			this.light = light;
			this.activeMaterial = defaultMaterial;
			this.scale = scale;
		}

		public void addErrorMessage(String format) {
			// TODO Auto-generated method stub

		}

		public void addWarningMessage(String format) {
			// TODO Auto-generated method stub

		}

		public void addFace(int[] vertexIndices, int[] textureIndices, int[] normalIndices) {
			Point3 p[] = new Point3[vertexIndices.length];
			for (int i = 0; i < vertexIndices.length; i++) {
				int index = vertexIndices[i] >= 0 ? vertexIndices[i] - 1 : vertexIndices.length + vertexIndices[i];
				p[i] = this.vertices.get(index);
			}

			PolygonGeometry face = new PolygonGeometry(p, this.activeMaterial);
			this.geometry.addChild(face);

			if (this.activeMaterial.isEmissive()) {
				this.light.addChild(face);
			}
		}

		public void addMaterial(String name, Material material) {
			this.materials.put(name, material);
		}

		public void addVertex(Point3 p, double w) {
			this.vertices.add(new Point3(p.x() * scale, p.y() * scale, p.z() * scale));
			this.weights.add(w);
		}

		public void setActiveMaterial(String name) {
			this.activeMaterial = materials.containsKey(name) ? materials.get(name) : Material.BLACK;
		}

		private final Map<String, Material> materials = new HashMap<String, Material>();
		private final List<Point3> vertices = new ArrayList<Point3>();
		private final List<Double> weights = new ArrayList<Double>();
		private Material activeMaterial;
		private final CompositeGeometry geometry;
		private final CompositeLight light;
		private final double scale;

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
		lineInterpreters.put("f", LI_F);
		lineInterpreters.put("usemtl", LI_USEMTL);
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

	private static LineInterpreter LI_F = new LineInterpreter() {

		public void process(String[] args, State state) {
			checkArgs(args, state, 3, Integer.MAX_VALUE);

			List<Integer> vertexIndexList = new ArrayList<Integer>();
			List<Integer> textureIndexList = new ArrayList<Integer>();
			List<Integer> normalIndexList = new ArrayList<Integer>();

			for (int i = 1; i < args.length; i++) {
				String[] indices = args[i].split("/", 3);

				vertexIndexList.add(Integer.parseInt(indices[0]));
				if (indices.length > 1 && indices[1] != "") {
					textureIndexList.add(Integer.parseInt(indices[1]));
				}
				if (indices.length > 2 && indices[2] != "") {
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



}
