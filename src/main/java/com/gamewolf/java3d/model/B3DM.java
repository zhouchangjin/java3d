package com.gamewolf.java3d.model;

import java.util.List;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import glm.mat._4.Mat4;

public class B3DM {
	
	public B3DM(){
	}
	
	String magicHead;
	
	int version;
	
	long byteLength;

	List<JMesh> meshList;
	
	int featureJsonLength;
	
	int featureTableBinaryLength;
	
	int batchTableJSONLength;
	
	int batchTableBinaryLegnth;
	
	JSONObject featureJson;
	
	JSONObject batchJson;
	
	long glbOffset;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<JMesh> getMeshList() {
		return meshList;
	}

	public void setMeshList(List<JMesh> meshList) {
		this.meshList = meshList;
	}

	public long getByteLength() {
		return byteLength;
	}

	public void setByteLength(long byteLength) {
		this.byteLength = byteLength;
	}

	public int getFeatureJsonLength() {
		return featureJsonLength;
	}

	public void setFeatureJsonLength(int featureJsonLength) {
		this.featureJsonLength = featureJsonLength;
	}

	public int getFeatureTableBinaryLength() {
		return featureTableBinaryLength;
	}

	public void setFeatureTableBinaryLength(int featureTableBinaryLength) {
		this.featureTableBinaryLength = featureTableBinaryLength;
	}

	public String getMagicHead() {
		return magicHead;
	}

	public void setMagicHead(String magicHead) {
		this.magicHead = magicHead;
	}

	public int getBatchTableJSONLength() {
		return batchTableJSONLength;
	}

	public void setBatchTableJSONLength(int batchTableJSONLength) {
		this.batchTableJSONLength = batchTableJSONLength;
	}

	public int getBatchTableBinaryLegnth() {
		return batchTableBinaryLegnth;
	}

	public void setBatchTableBinaryLegnth(int batchTableBinaryLegnth) {
		this.batchTableBinaryLegnth = batchTableBinaryLegnth;
	}

	public JSONObject getFeatureJson() {
		return featureJson;
	}

	public void setFeatureJson(JSONObject featureJson) {
		this.featureJson = featureJson;
	}

	public JSONObject getBatchJson() {
		return batchJson;
	}

	public void setBatchJson(JSONObject batchJson) {
		this.batchJson = batchJson;
	}

	public long getGlbOffset() {
		return glbOffset;
	}

	public void setGlbOffset(long glbOffset) {
		this.glbOffset = glbOffset;
	}
	
	public Mat4 getTransformation() {
		Mat4 mat4=new Mat4(1.0f);
		if(featureJson.containsKey("RTC_CENTER")) {
			JSONArray coordinates=featureJson.getJSONArray("RTC_CENTER");
			float tx=coordinates.getFloatValue(0);
			float ty=coordinates.getFloatValue(1);
			float tz=coordinates.getFloatValue(2);
			mat4.translation(tx, ty, tz);
		}
		return mat4;
	}
	
	
}
