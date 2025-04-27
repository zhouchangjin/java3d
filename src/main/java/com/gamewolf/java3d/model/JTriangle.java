package com.gamewolf.java3d.model;

import glm.vec._3.Vec3;

public class JTriangle {
	
	int vtxId[];
	int uvId[];
	int normalId[];
	Vec3 normal;
	

	public JTriangle(){
		vtxId=new int[3];
		uvId=new int[3];
		normalId=new int[3];
	}
	
	public void setVertex(int id0,int id1,int id2) {
		vtxId[0]=id0;
		vtxId[1]=id1;
		vtxId[2]=id2;
	}
	
	public void setUv(int uv0,int uv1,int uv2) {
		uvId[0]=uv0;
		uvId[1]=uv1;
		uvId[2]=uv2;
	}
	
	public void setNormal(int n0,int n1,int n2) {
		normalId[0]=n0;
		normalId[1]=n1;
		normalId[2]=n2;
	}
	
	public int getVertex(int seq) {
		return vtxId[seq];
	}
	
	public int getUv(int seq) {
		return uvId[seq];
	}
	
	public int getNormal(int seq) {
		return normalId[seq];
	}

}
