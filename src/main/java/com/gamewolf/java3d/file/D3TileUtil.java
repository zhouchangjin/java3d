package com.gamewolf.java3d.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gamewolf.java3d.coordinates.EarthModel;
import com.gamewolf.java3d.coordinates.TransformUtil;
import com.gamewolf.java3d.model.B3DM;
import com.gamewolf.java3d.model.JMesh;
import com.gamewolf.java3d.model.JTriangle;
import com.gamewolf.java3d.model.JVertexSimple;
import com.gamewolf.java3d.model.MeshGraph;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

public class D3TileUtil {
	

	public static JSONObject convertB3DM2GeoJSON(String path,Mat4 mat4) {
		if(mat4==null) {
			mat4=new Mat4(1.0f);
		}
		B3DM b3dm=B3DMUtil.readB3DMModelFromB3DMFile(path);
		Mat4 mat=b3dm.getTransformation();
		Mat4 transformMat=mat4.mul(mat);
		List<JMesh> meshlist=b3dm.getMeshList();
		JSONObject featureCollection=new JSONObject();
		featureCollection.put("type", "FeatureCollection");
		JSONArray features=new JSONArray();
		featureCollection.put("features", features);
		for(int mi=0;mi<meshlist.size();mi++) {
			JMesh mesh=meshlist.get(mi);
			List<JTriangle> tList=mesh.getTriangles();
			for(int ti=0;ti<tList.size();ti++) {
				JVertexSimple[] vlist=getVertexList(mesh, ti);
				List<double[]> points=transform(vlist, transformMat, EarthModel.WGS_84);
				//JSONArray arrayJsonObject=JSONArray.from(points);
				
				JSONObject feature=generatePolygonFeatureFromPoints(points);
				if(feature!=null) {
					features.add(feature);
					//System.out.println(arrayJsonObject);				
				}
			}
		}
		return featureCollection;
	}
	
	public static JSONObject generatePolygonFeatureFromPoints(List<double[]> points) {
		JSONObject feature=new JSONObject();
		feature.put("type", "Feature");
		feature.put("properties", new JSONObject());
		JSONObject geometry=generatePolygonGeometryFromPoints(points);
		if(geometry!=null) {
			feature.put("geometry", geometry);
			return feature;
		}else {
			return null;
		}
		
	}
	
	private static double PointDistance(double[] p1,double[] p2,int dimension) {
		double lng1=p1[0];
		double lat1=p1[1];
		double alt1=p1[2];
		double lng2=p2[0];
		double lat2=p2[1];
		double alt2=p2[2];
		double dlng=lng2-lng1;
		double dlng2=dlng*dlng;
		double dlat=lat2-lat1;
		double dlat2=dlat*dlat;
		double dalt=alt2-alt1;
		double dalt2=dalt*dalt;
		if(dimension==2) {
			return Math.sqrt(dlng2+dlat2);
		}else {
			return Math.sqrt(dlng2+dlat2+dalt2);
		}
	}
	
	private static JSONObject generatePolygonGeometryFromPoints(List<double[]> points) {
		
		JSONObject geometry=new JSONObject();
		double[] firstPoint=points.get(0);
		double[] midPoint=points.get(1);
		double[] lastPoint=points.get(points.size()-1);
		double distance=PointDistance(firstPoint, lastPoint,3);
		if(points.size()==3) {
			double lng1=firstPoint[0];
			double lat1=firstPoint[1];
			double lng2=midPoint[0];
			double lat2=midPoint[1];
			double lng3=lastPoint[0];
			double lat3=lastPoint[1];
			double dlng2=(lng1-lng2)*(lng1-lng2)+(lng2-lng3)*(lng2-lng3);
			double dlng=Math.sqrt(dlng2);
			if(dlng<0.00000001) {
				//System.out.println("垂直面，无法转geojson");
				return null;
			}
			double dlat2=(lat1-lat2)*(lat1-lat2)+(lat2-lat3)*(lat2-lat3);
			double dlat=Math.sqrt(dlat2);
			if(dlat<0.00000001) {
				//System.out.println("垂直面，无法转geojson");
				return null;
			}
			
		}
		
		if(distance>0.0001) {
			double lastPointAdd[]=new double[] {firstPoint[0],firstPoint[1],firstPoint[2]};
			points.add(lastPointAdd);
		}
		geometry.put("type", "Polygon");
		JSONArray rings=new JSONArray();
		rings.add(points);
		geometry.put("coordinates", rings);
		return geometry;
	}

	public static void main(String args[]) {
		
		String path="d:/gltf/ll.b3dm";
		JSONObject featureCollection=convertB3DM2GeoJSON(path,null);
		System.out.println(featureCollection);
		
		String path2="d:/gltf/building.b3dm";
		Mat4 mat2=new Mat4(4.843178171884396,
		          1.2424271388626869,
		          0,
		          0,
		          -0.7993230372483163,
		          3.115888059101095,
		          3.827835456922795,
		          0,
		          0.9511613309563466,
		          -3.7077778261067222,
		          3.2167803336138237,
		          0,
		          1215011.9317263428,
		          -4736309.3434217675,
		          4081602.0044800863,
		          1);
		JSONObject featureCollection2=convertB3DM2GeoJSON(path2, mat2);
		
		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter("d:/building3.geojson"));
			bw.append(featureCollection2.toString());
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(featureCollection2);
		
	}

	
	public static JVertexSimple[] getVertexList(JMesh mesh,int ti) {
		    JTriangle t=mesh.getTriangles().get(ti);
		    int v0=t.getVertex(0);
			int v1=t.getVertex(1);
			int v2=t.getVertex(2);
			JVertexSimple va=mesh.getVertex(v0);
			JVertexSimple vb=mesh.getVertex(v1);
			JVertexSimple vc=mesh.getVertex(v2);
			return new JVertexSimple[] {va,vb,vc};
	}
	
	
	public static List<double[]> transform(JVertexSimple[] vertices,Mat4 mat4,String type) {
		EarthModel.EarthModelFactory facotory=new EarthModel.EarthModelFactory();
		EarthModel em=facotory.createEarthMode(type);
		List<double[]> pointList=new ArrayList<>();
		for(JVertexSimple v:vertices) {
			Vec4 vec4=new Vec4(v.getPosition().x,-1*v.getPosition().z,v.getPosition().y, 1);
			Vec4 out=mat4.mul(vec4);
			Vec3 res=out.toVec3_();
			double geo[]=TransformUtil.ECEF2GeoDegree(res.x, res.y, res.z, em);
			//System.out.println(geo[0]+", "+geo[1]+", "+geo[2]);
			//System.out.println(res);
			pointList.add(geo);
		}
		return pointList;
	}
	

}
