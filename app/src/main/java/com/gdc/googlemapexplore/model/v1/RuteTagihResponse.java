package com.gdc.googlemapexplore.model.v1;

import com.google.gson.annotations.SerializedName;

public class RuteTagihResponse{

	@SerializedName("rc_desc")
	private String rcDesc;

	@SerializedName("rc")
	private String rc;

	@SerializedName("data")
	private RuteTagih ruteTagih;

	public String getRcDesc() {
		return rcDesc;
	}

	public void setRcDesc(String rcDesc) {
		this.rcDesc = rcDesc;
	}

	public String getRc() {
		return rc;
	}

	public void setRc(String rc) {
		this.rc = rc;
	}

	public RuteTagih getRuteTagih() {
		return ruteTagih;
	}

	public void setRuteTagih(RuteTagih ruteTagih) {
		this.ruteTagih = ruteTagih;
	}

	@Override
	public String toString() {
		return "RuteTagihResponse{" +
				"rcDesc='" + rcDesc + '\'' +
				", rc='" + rc + '\'' +
				", ruteTagih=" + ruteTagih +
				'}';
	}
}