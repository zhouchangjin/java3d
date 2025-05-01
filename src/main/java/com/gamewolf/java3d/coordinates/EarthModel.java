package com.gamewolf.java3d.coordinates;

public class EarthModel {
	
	public EarthModel() {
		
	}
	
	public double getSemiMajorAxis() {
		return semiMajorAxis;
	}

	public void setSemiMajorAxis(double semiMajorAxis) {
		this.semiMajorAxis = semiMajorAxis;
	}

	public double getMinorAxis() {
		return minorAxis;
	}

	public void setMinorAxis(double minorAxis) {
		this.minorAxis = minorAxis;
	}

	public double getEccentricity() {
		return eccentricity;
	}

	public void setEccentricity(double eccentricity) {
		this.eccentricity = eccentricity;
	}

	public double getFlattenCoefficient() {
		return flattenCoefficient;
	}

	public void setFlattenCoefficient(double flattenCoefficient) {
		this.flattenCoefficient = flattenCoefficient;
	}

	//长半轴 a
	double semiMajorAxis;
	
	//b
	double minorAxis;
	
	//偏心率 e
	double eccentricity;
	
	//扁率 f
	double flattenCoefficient;
	
	public static String WGS_84="WGS84";
	
	public static String CSC2000="CSC2000";
	
	public static class EarthModelFactory{
		
		EarthModel createEarthMode(String crsStr) {
			EarthModel em=new EarthModel();
			double a=6378137;
			double f=1/298.257223565;
			if(crsStr.equals(WGS_84)) {
				f=1/298.257223565;
				
			}else if(crsStr.equals(CSC2000)) {
				f=1/298.257222101;
			}
			double b=a*(1-f);
			double e=Math.sqrt(a*a-b*b)/a;
			em.setSemiMajorAxis(a);
			em.setFlattenCoefficient(f);
			em.setMinorAxis(b);
			em.setEccentricity(e);
			return em;
		}
	
	}

}
