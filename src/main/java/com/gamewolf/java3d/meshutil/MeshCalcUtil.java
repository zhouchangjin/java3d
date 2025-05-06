package com.gamewolf.java3d.meshutil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gamewolf.java3d.file.GltfUtil;
import com.gamewolf.java3d.model.J3DConstants;
import com.gamewolf.java3d.model.JMesh;
import com.gamewolf.java3d.model.JTriangle;
import com.gamewolf.java3d.model.JVertexSimple;

public class MeshCalcUtil {
	
//	public static List<MeshPart> SeparateMeshParts(JMesh mesh){
//		List<MeshPart> parts=new ArrayList<MeshPart>();
//		
//		List<JTriangle> triList=mesh.getTriangles();
//		
//		for(int i=0;i<triList.size();i++) {
//			JTriangle tri=triList.get(i);
//			int vId0=tri.getVertex(0);
//			int vId1=tri.getVertex(1);
//			int vId2=tri.getVertex(2);
//			
//			JVertexSimple v1=mesh.getVertex(vId0);
//			JVertexSimple v2=mesh.getVertex(vId1);
//			JVertexSimple v3=mesh.getVertex(vId2);
//			
//			
//		}
//		return parts;
//	}
	
	public static boolean ShareEdge(JTriangle triangle1, JTriangle triangle2, JMesh mesh) {
		
		int v1f=triangle1.getVertex(0);
		int v1m=triangle1.getVertex(1);
		int v1l=triangle1.getVertex(2);
		
		int v2f=triangle2.getVertex(0);
		int v2m=triangle2.getVertex(1);
		int v2l=triangle2.getVertex(2);
		
		JVertexSimple vv1f=mesh.getVertex(v1f);
		JVertexSimple vv1m=mesh.getVertex(v1m);
		JVertexSimple vv1l=mesh.getVertex(v1l);
		
		JVertexSimple vv2f=mesh.getVertex(v2f);
		JVertexSimple vv2m=mesh.getVertex(v2m);
		JVertexSimple vv2l=mesh.getVertex(v2l);
		
		if( vv1f.distance(vv2f)<J3DConstants.ignoreDistance ||
		    vv1f.distance(vv2m)<J3DConstants.ignoreDistance ||
		    vv1f.distance(vv2l)<J3DConstants.ignoreDistance ||
		    vv1m.distance(vv2f)<J3DConstants.ignoreDistance ||
		    vv1m.distance(vv2m)<J3DConstants.ignoreDistance ||
		    vv1m.distance(vv2l)<J3DConstants.ignoreDistance ||
		    vv1l.distance(vv2f)<J3DConstants.ignoreDistance ||
		    vv1l.distance(vv2m)<J3DConstants.ignoreDistance ||
		    vv1l.distance(vv2l)<J3DConstants.ignoreDistance) {
			return true;
		}else {
			return false;
		}
		
	}


}
