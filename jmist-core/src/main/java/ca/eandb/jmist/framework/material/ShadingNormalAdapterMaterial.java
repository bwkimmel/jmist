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

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Medium;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class ShadingNormalAdapterMaterial implements Material {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 6083972357040808677L;
	
	private final Material inner;
	
	private static final class SurfacePointAdapter implements SurfacePoint {

		private final SurfacePoint inner;
		
		public SurfacePointAdapter(SurfacePoint inner) {
			this.inner = inner;
		}
		
		@Override
		public Point3 getPosition() {
			return inner.getPosition();
		}

		@Override
		public Vector3 getNormal() {
			return inner.getShadingNormal();
		}

		@Override
		public Basis3 getBasis() {
			return inner.getShadingBasis();
		}

		@Override
		public Vector3 getShadingNormal() {
			return inner.getShadingNormal();
		}

		@Override
		public Basis3 getShadingBasis() {
			return inner.getShadingBasis();
		}

		@Override
		public Vector3 getTangent() {
			return inner.getShadingBasis().u();
		}

		@Override
		public Point2 getUV() {
			return inner.getUV();
		}

		@Override
		public int getPrimitiveIndex() {
			return inner.getPrimitiveIndex();
		}

		@Override
		public Material getMaterial() {
			return inner.getMaterial();
		}

		@Override
		public Medium getAmbientMedium() {
			return inner.getAmbientMedium();
		}
		
	}
	
	public ShadingNormalAdapterMaterial(Material inner) {
		this.inner = inner;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#transmittance(ca.eandb.jmist.math.Ray3, double, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color transmittance(Ray3 ray, double distance,
			WavelengthPacket lambda) {
		return inner.transmittance(ray, distance, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#refractiveIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color refractiveIndex(Point3 p, WavelengthPacket lambda) {
		return inner.refractiveIndex(p, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color extinctionIndex(Point3 p, WavelengthPacket lambda) {
		return inner.extinctionIndex(p, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#isEmissive()
	 */
	@Override
	public boolean isEmissive() {
		return inner.isEmissive();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
	 */
	@Override
	public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
			WavelengthPacket lambda, double ru, double rv, double rj) {
		
		SurfacePoint adapter = new SurfacePointAdapter(x);
		ScatteredRay sr = inner.scatter(adapter, v, adjoint, lambda, ru, rv, rj);
		
		if (sr != null) {
			double idotn = -v.dot(x.getNormal());
			double odotn = sr.getRay().direction().dot(x.getNormal());
			
			boolean transmitted = sr.isTransmitted();
			boolean sameSide = (idotn > 0 && odotn > 0) || (idotn < 0 && odotn < 0);
			
			if ((transmitted && !sameSide) || (!transmitted && sameSide)) {
				return sr;
			}
		}
		
		return null;
		
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#emit(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
	 */
	@Override
	public ScatteredRay emit(SurfacePoint x, WavelengthPacket lambda,
			double ru, double rv, double rj) {
		return inner.emit(x, lambda, ru, rv, rj);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#getScatteringPDF(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public double getScatteringPDF(SurfacePoint x, Vector3 in, Vector3 out,
			boolean adjoint, WavelengthPacket lambda) {
		
		double idotn = -in.dot(x.getNormal());
		double idots = -in.dot(x.getShadingNormal());
		double odotn = out.dot(x.getNormal());
		double odots = out.dot(x.getShadingNormal());
		
		if ((idotn > 0) == (idots > 0) && (odotn > 0) == (odots > 0)) {
			SurfacePoint adapter = new SurfacePointAdapter(x);
			double pdf = inner.getScatteringPDF(adapter, in, out, adjoint, lambda);
			if (adjoint) {
				return pdf * odots / odotn;
			} else {
				return pdf;
			}
		} else {
			return 0.0;
		}

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#getEmissionPDF(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public double getEmissionPDF(SurfacePoint x, Vector3 out,
			WavelengthPacket lambda) {
		return inner.getEmissionPDF(x, out, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#bsdf(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color bsdf(SurfacePoint x, Vector3 in, Vector3 out,
			WavelengthPacket lambda) {
		
		double idotn = -in.dot(x.getNormal());
		double idots = -in.dot(x.getShadingNormal());
		double odotn = out.dot(x.getNormal());
		double odots = out.dot(x.getShadingNormal());
		
		if ((idotn > 0) == (idots > 0) && (odotn > 0) == (odots > 0)) {
			SurfacePoint adapter = new SurfacePointAdapter(x);
			return inner.bsdf(adapter, in, out, lambda);
		} else {
			return ColorUtil.getBlack(lambda);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#emission(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color emission(SurfacePoint x, Vector3 out, WavelengthPacket lambda) {
		return inner.emission(x, out, lambda);
	}

}
