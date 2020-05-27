package com.gdc.googlemapexplore.model.v1;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RuteTagih {

	@SerializedName("origin")
	private Origin origin;

	@SerializedName("destination")
	private List<DestinationItem> destination;

	public Origin getOrigin(){
		return origin;
	}

	public List<DestinationItem> getDestination(){
		return destination;
	}

	@Override
	public String toString() {
		return "RuteTagih{" +
				"origin=" + origin +
				", destination=" + destination +
				'}';
	}
}