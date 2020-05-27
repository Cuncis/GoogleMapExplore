package com.gdc.googlemapexplore.model.v1;

import com.google.gson.annotations.SerializedName;

public class DestinationItem{

	@SerializedName("lng")
	private double lng;

	@SerializedName("no_kredit")
	private String noKredit;

	@SerializedName("idx")
	private int idx;

	@SerializedName("lat")
	private double lat;

	public double getLng(){
		return lng;
	}

	public String getNoKredit(){
		return noKredit;
	}

	public int getIdx(){
		return idx;
	}

	public double getLat(){
		return lat;
	}

	@Override
	public String toString() {
		return "DestinationItem{" +
				"lng=" + lng +
				", noKredit='" + noKredit + '\'' +
				", idx=" + idx +
				", lat=" + lat +
				'}';
	}
}