package com.gamewolf.java3d.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gamewolf.java3d.file.GltfUtil;
import com.gamewolf.java3d.file.WaveFrontUtil;

public class MeshGraph {
	
	JMesh mesh;
	
	Map<String,Set<Integer>> edgeTriangleMap;
	
	Map<Integer,Set<String>> triangleEdgeMap;
	

	
	public MeshGraph(JMesh mesh) {
		this.mesh=mesh;
		edgeTriangleMap=new HashMap<String, Set<Integer>>();
		triangleEdgeMap=new HashMap<Integer, Set<String>>();
		
	}
	
	public void loadEdgeTriangleMap() {
	    List<JTriangle> triList=mesh.getTriangles();
		
		for(int i=0;i<triList.size();i++) {
			JTriangle tri=triList.get(i);
			int vId0=tri.getVertex(0);
			int vId1=tri.getVertex(1);
			int vId2=tri.getVertex(2);
			
			JVertexSimple v1=mesh.getVertex(vId0);
			JVertexSimple v2=mesh.getVertex(vId1);
			JVertexSimple v3=mesh.getVertex(vId2);
			addEdgeTriangle(v1, v2, i);
			addEdgeTriangle(v2, v3, i);
			addEdgeTriangle(v3, v1, i);
		}
	}
	
	public void addEdgeTriangle(JVertexSimple v1,JVertexSimple v2,int triangleId) {
		String edgeStr=getEdgeString(v1, v2);
		if(edgeTriangleMap.containsKey(edgeStr)) {
			edgeTriangleMap.get(edgeStr).add(triangleId);
		}else {
			Set<Integer> tSet=new TreeSet<Integer>();
			tSet.add(triangleId);
			edgeTriangleMap.put(edgeStr, tSet);
		}
		
		if(triangleEdgeMap.containsKey(triangleId)) {
			triangleEdgeMap.get(triangleId).add(edgeStr);
		}else {
			Set<String> eSet=new TreeSet<String>();
			eSet.add(edgeStr);
			triangleEdgeMap.put(triangleId, eSet);
		}
	}
	
	public String getEdgeString(JVertexSimple v1,JVertexSimple v2) {
		  float x1=v1.getPosition().x;
	 	  float y1=v1.getPosition().y;
		  float z1=v1.getPosition().z;
		  
		  float x2=v2.getPosition().x;
		  float y2=v2.getPosition().y;
		  float z2=v2.getPosition().z;
		  
		  String p1=x1+","+y1+","+z1;
		  String p2=x2+","+y2+","+z2;
		  if(p1.compareTo(p2)<0) {
			  return p1+"-"+p2;
		  }else {
			  return p2+"-"+p1;
		  }
		
	}
	
	public List<Set<Integer>> parseMeshGroup() {
		Set<String> traversed=new HashSet<String>();
		List<Set<Integer>> triangelGroups=new ArrayList<Set<Integer>>();
		int start=0;
		Set<Integer> triangles=new HashSet<Integer>();
		for(int i=1;i<mesh.getTriangles().size();i++) {
			triangles.add(i);
		}
		
		do {
			Set<Integer> triangelGroup=new HashSet<>();
		    Stack<String> edges=new Stack<String>();
			JTriangle first=mesh.getTriangles().get(start);
			int vId0=first.getVertex(0);
			int vId1=first.getVertex(1);
			JVertexSimple v1=mesh.getVertex(vId0);
			JVertexSimple v2=mesh.getVertex(vId1);
			String edgesTR=getEdgeString(v1, v2);
			edges.add(edgesTR);
			while(!edges.empty()) {
				String edge=edges.pop();
				traversed.add(edge);
				Set<Integer> triSet=edgeTriangleMap.get(edge);
				triangelGroup.addAll(triSet);
				List<String> edgeLinks=getEdgeStrings(triSet);
				for(String edgeLink:edgeLinks) {
					if(!traversed.contains(edgeLink)) {
						edges.push(edgeLink);
					}
				}
			}
			triangles.removeAll(triangelGroup);

			triangelGroups.add(triangelGroup);
			if(triangles.size()>0) {
				start=triangles.iterator().next();
			}
			
		}while(triangles.size()>0);
		
		return triangelGroups;
	}

	private List<String> getEdgeStrings(Set<Integer> triSet) {
		List<String> edgeLinks=new ArrayList<String>();
		for(Integer i:triSet) {
			JTriangle t=mesh.getTriangles().get(i);
			List<String> edgeLinksTri=getEdgeStrings(t);
			edgeLinks.addAll(edgeLinksTri);
		}
		
		return edgeLinks;
	}

	private List<String> getEdgeStrings(JTriangle t) {
		int vId0=t.getVertex(0);
		int vId1=t.getVertex(1);
		int vId2=t.getVertex(2);
		JVertexSimple v1=mesh.getVertex(vId0);
		JVertexSimple v2=mesh.getVertex(vId1);
		JVertexSimple v3=mesh.getVertex(vId2);
		String edgesTR1=getEdgeString(v1, v2);
		String edgesTR2=getEdgeString(v2, v3);
		String edgesTR3=getEdgeString(v1, v3);
		List<String> edges=new ArrayList<String>();
		edges.add(edgesTR1);
		edges.add(edgesTR2);
		edges.add(edgesTR3);
		return edges;
	}
	
	public static void main(String args[]) {
		List<JMesh> meshlist=GltfUtil.readMeshFromGlb("d:/Tile_+246_+126_L19_000013.b3dm.glb");
		String outpath="d:/temp/test";
		int graphC=0;
		for(JMesh m:meshlist) {
			graphC++;
			MeshGraph meshGraph=new MeshGraph(m);
			meshGraph.loadEdgeTriangleMap();
			List<Set<Integer>> tList=meshGraph.parseMeshGroup();
			//JSONArray array=JSONArray.from(tList);
			//System.out.println(array);
			int counter=0;
			for(Set<Integer> group:tList) {
				counter++;
				JMesh mout=meshGraph.generateNewMesh(group);
				WaveFrontUtil.SaveMeshToWaveFont(mout, "group"+counter, outpath+"out"+graphC+"_"+counter+".obj");
			}
		}
	}

	private JMesh generateNewMesh(Set<Integer> group) {
		JMesh out=new JMesh();
		
		for(Integer i:group) {
			
			JTriangle t=mesh.getTriangles().get(i);
			int tid0=t.getVertex(0);
			int tid1=t.getVertex(1);
			int tid2=t.getVertex(2);
			JVertexSimple v0=mesh.getVertex(tid0);
			JVertexSimple v1=mesh.getVertex(tid1);
			JVertexSimple v2=mesh.getVertex(tid2);
			JTriangle tout=new JTriangle();
			int curPos=out.getVertices().size();
			tout.setVertex(curPos, curPos+1, curPos+2);
			out.addVetex(v0);
			out.addVetex(v1);
			out.addVetex(v2);
			out.addTriangle(tout);
		}
		return out;
	}



}
