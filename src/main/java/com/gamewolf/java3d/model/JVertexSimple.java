package com.gamewolf.java3d.model;

import glm.vec._3.Vec3;

public class JVertexSimple {
	
	public JVertexSimple(Vec3 p) {
		this.position=p;
	}
	
	Vec3 position;
	
	public Vec3 getPosition() {
		return position;
	}
	
	float distance(JVertexSimple another) {
		float dx=position.x-another.getPosition().x;
		float dy=position.y-another.getPosition().y;
		float dz=position.z-another.getPosition().z;
		float distance=(float)Math.sqrt(dx*dx+dy*dy+dz*dz);
		return distance;
	}

}
