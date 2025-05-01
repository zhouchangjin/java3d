package com.gamewolf.java3d.coordinates;
/**
 * 
 * @author zcj
 * Reference 
 * https://danceswithcode.net/engineeringnotes/geodetic_to_ecef/geodetic_to_ecef.html
 */
public class TransformUtil {
	
	public static double[] ECEF2GeoDegree(double x,double y,double z,EarthModel em){
		double geo[]=ECEF2Geo(x, y, z, em);
		return new double[] {Math.toDegrees(geo[0]),Math.toDegrees(geo[1]),geo[2]};
	}

	public static double[] ECEF2Geo(double x, double y, double z, EarthModel earthModel) {

		double a = earthModel.getSemiMajorAxis();
		double e = earthModel.getEccentricity();
		double e2 = e * e;
		double a1 = a * e2;
		double a2 = a1 * a1;
		double a3 = a1 * e2 / 2;
		double a4 = 2.5 * a2;
		double a5 = a1 + a3;
		double a6 = 1 - e2;
		double zp = Math.abs(z);
		double w2 = x * x + y * y;
		double w = Math.sqrt(w2);
		double r2 = w2 + z * z;
		double r = Math.sqrt(r2);
		double lng = Math.atan2(y, x);
		double s2 = z * z / r2;
		double c2 = w2 / r2;
		double u = a2 / r;
		double v = a3 - a4 / r;
		double lat = 0;
		double c = 0;
		double ss = 0;
		double s=0;
		if (c2 > 0.3) {
			s = (zp / r) * (1.0 + c2 * (a1 + u + s2 * v) / r);
			lat = Math.asin(s); 
			ss = s * s;
			c = Math.sqrt(1.0 - ss);
		} else {
			c = (w / r) * (1.0 - s2 * (a5 - u - c2 * v) / r);
			lat = Math.acos(c); 
			ss = 1.0 - c * c;
		}
		s = Math.sqrt(ss);
		double g = 1.0 - e2*ss;
        double rg = a/Math.sqrt( g );
        double rf = a6*rg;
        u = w - rg*c;
        v = zp - rf*s;
        double f = c*u + s*v;
        double m = c*v - s*u;
        double p = m/( rf/g + f );
        lat = lat + p;      //Lat
        double alt = f + m*p/2.0;     //Altitude
        if( z < 0.0 ){
            lat *= -1.0;     //Lat
        }
		return new double[] {lng,lat,alt};

	}

	public static double[] Geo2ECEF(double longitude, double latitude, double alt, EarthModel earthModel) {

		double[] ecef = new double[3];
		double a = earthModel.getSemiMajorAxis();
		double e = earthModel.getEccentricity();

		double latRadians = Math.toRadians(latitude);
		double lngRadians = Math.toRadians(longitude);

		double n = a / Math.sqrt(1 - e * e * Math.sin(latRadians) * Math.sin(latRadians));

		ecef[0] = (n + alt) * Math.cos(latRadians) * Math.cos(lngRadians); // ECEF x
		ecef[1] = (n + alt) * Math.cos(latRadians) * Math.sin(lngRadians); // ECEF y
		ecef[2] = (n * (1 - e * e) + alt) * Math.sin(latRadians); // ECEF z
		return ecef;
	}

	public static void main(String args[]) {
		EarthModel.EarthModelFactory factory = new EarthModel.EarthModelFactory();
		EarthModel em = factory.createEarthMode(EarthModel.WGS_84);
		double coor[] = Geo2ECEF(-75.612123, 40.042508, 10, em);
		System.out.println(coor[0] + "," + coor[1] + "," + coor[2]);
		double geo[]=ECEF2GeoDegree(coor[0], coor[1], coor[2], em);
		System.out.println(geo[0]+"=="+geo[1]+"=="+geo[2]);
	}

}
