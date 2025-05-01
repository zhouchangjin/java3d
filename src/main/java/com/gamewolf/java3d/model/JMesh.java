package com.gamewolf.java3d.model;

import java.util.ArrayList;
import java.util.List;

import glm.vec._2.Vec2;
import glm.vec._3.Vec3;

public class JMesh {
	
	 List<JVertexSimple> vertices;
	 
	 List<JTriangle> triangles;
	 
	 List<Vec2> uvMaps;
	 
	 List<Vec3> vertexNormals;
	 
	 public JMesh(){
		 vertices=new ArrayList<JVertexSimple>();
		 triangles=new ArrayList<JTriangle>();
		 uvMaps=new ArrayList<Vec2>();
		 vertexNormals=new ArrayList<Vec3>();
	 }
	 
	public void addVetex(JVertexSimple p) {
		vertices.add(p);
	}
	
	public void addNormal(Vec3 normal) {
		vertexNormals.add(normal);
	}
	
	public void addUv(Vec2 uv) {
		uvMaps.add(uv);
	}
	
	public void addTriangle(JTriangle t){
		triangles.add(t);
	}

	public List<JVertexSimple> getVertices() {
		return vertices;
	}
	
	public JVertexSimple getVertex(int id) {
		return vertices.get(id);
	}

	public void setVertices(List<JVertexSimple> vertices) {
		this.vertices = vertices;
	}

	public List<JTriangle> getTriangles() {
		return triangles;
	}

	public void setTriangles(List<JTriangle> triangles) {
		this.triangles = triangles;
	}

	public List<Vec2> getUvMaps() {
		return uvMaps;
	}

	public void setUvMaps(List<Vec2> uvMaps) {
		this.uvMaps = uvMaps;
	}

	public List<Vec3> getVertexNormals() {
		return vertexNormals;
	}

	public void setVertexNormals(List<Vec3> vertexNormals) {
		this.vertexNormals = vertexNormals;
	}
	 
	 

}
