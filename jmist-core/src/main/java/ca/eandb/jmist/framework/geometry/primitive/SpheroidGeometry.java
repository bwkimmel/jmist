package ca.eandb.jmist.framework.geometry.primitive;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.framework.geometry.AbstractGeometry;
import ca.eandb.jmist.math.*;

public final class SpheroidGeometry extends PrimitiveGeometry {

    /** Serialization version ID. */
    private static final long serialVersionUID = -3863465919049151683L;

    /** The <code>Spheroid</code> describing this <code>SceneElement</code>. */
    private final Spheroid spheroid;

    private final Sphere boundingSphere;

    /**
     * Creates a new <code>SpheroidGeometry</code>.
     * @param spheroid The <code>Spheroid</code> describing to be rendered.
     */
    public SpheroidGeometry(Spheroid spheroid) {
        this.spheroid = spheroid;
        this.boundingSphere = new Sphere(spheroid.center(), Math.max(spheroid.a(), spheroid.c()));
    }

    /**
     * Creates a new <code>SpheroidGeometry</code>.
     * @param center The <code>Point3</code> at the center of the spheroid.
     * @param a The length of the first and second semi-principal axes.
     * @param c The length of the third semi-principal axis.
     */
    public SpheroidGeometry(Point3 center, double a, double c) {
        this(new Spheroid(a,c,center, Vector3.K));
    }

    @Override
    public void intersect(Ray3 ray, IntersectionRecorder recorder) {
        Interval I =  spheroid.intersect(ray);
        if (!I.isEmpty()) {
            Point3 p1 = ray.pointAt(I.minimum());
            Intersection iFront = super.newIntersection(ray,I.minimum(),true).setLocation(p1).setNormal(spheroid.gradient(p1));
            recorder.record(iFront);
            Point3 p2 = ray.pointAt(I.maximum());
            Intersection iBack = super.newIntersection(ray,I.maximum(),false).setLocation(p2).setNormal(spheroid.gradient(p2));
            recorder.record(iBack);
        }
    }

    @Override
    protected Basis3 getBasis(AbstractGeometry.GeometryIntersection x) {
        return  Basis3.fromW(x.getNormal(), Basis3.Orientation.RIGHT_HANDED);
    }

    @Override
    protected Vector3 getNormal(AbstractGeometry.GeometryIntersection x) {
        return this.spheroid.gradient(x.getPosition());
    }

    @Override
    public Box3 boundingBox() {
        return this.boundingSphere.boundingBox();
    }

    @Override
    public Sphere boundingSphere() {
        return this.boundingSphere;
    }


    @Override
    public double getSurfaceArea() {
        return spheroid.surfaceArea();
    }

    @Override
    protected Basis3 getShadingBasis(GeometryIntersection x) {
        return Basis3.fromW(x.getNormal(), Basis3.Orientation.RIGHT_HANDED);
    }

    @Override
    protected Vector3 getShadingNormal(GeometryIntersection x) {
        return x.getNormal();
    }

}
