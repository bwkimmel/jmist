package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Vector3;

public interface ScatteringStrategy {

	ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
			WavelengthPacket lambda, double ru, double rv, double rj);

	ScatteredRay emit(SurfacePoint x, WavelengthPacket lambda, double ru,
			double rv, double rj);

	double getScatteringPDF(SurfacePoint x, Vector3 in, Vector3 out,
			boolean adjoint, WavelengthPacket lambda);

	double getEmissionPDF(SurfacePoint x, Vector3 out, WavelengthPacket lambda);
	
	double getWeight(SurfacePoint x, WavelengthPacket lambda);

}