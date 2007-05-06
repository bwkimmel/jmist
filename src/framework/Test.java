package framework;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int N = 10000000;

		Point3 p = Point3.ORIGIN;
		Vector3 v = new Vector3(1.0, 1.0, 1.0);

		Timer timer = new Timer();
		for (int i = 0; i < N; i++) {
			p.add(v);
		}
		timer.print("Reusing point");

		p = Point3.ORIGIN;

		timer = new Timer();
		for (int i = 0; i < N; i++) {
			p = p.plus(v);
		}
		timer.print("Using new");

	}

}
