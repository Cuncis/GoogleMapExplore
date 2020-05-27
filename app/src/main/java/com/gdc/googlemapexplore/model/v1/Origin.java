package com.gdc.googlemapexplore.model.v1;

import com.google.gson.annotations.SerializedName;

public class Origin{

	@SerializedName("provinsi")
	private String provinsi;

	@SerializedName("kota")
	private String kota;

	@SerializedName("lng")
	private double lng;

	@SerializedName("kecamatan")
	private String kecamatan;

	@SerializedName("lat")
	private double lat;

	@SerializedName("alamat")
	private String alamat;

	public String getProvinsi(){
		return provinsi;
	}

	public String getKota(){
		return kota;
	}

	public double getLng(){
		return lng;
	}

	public String getKecamatan(){
		return kecamatan;
	}

	public double getLat(){
		return lat;
	}

	public String getAlamat(){
		return alamat;
	}
}