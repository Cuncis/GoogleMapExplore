package com.gdc.googlemapexplore.model.v1;

import com.google.gson.annotations.SerializedName;

public class RuteData{

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

	public String getProvinsi() {
		return provinsi;
	}

	public void setProvinsi(String provinsi) {
		this.provinsi = provinsi;
	}

	public String getKota() {
		return kota;
	}

	public void setKota(String kota) {
		this.kota = kota;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getKecamatan() {
		return kecamatan;
	}

	public void setKecamatan(String kecamatan) {
		this.kecamatan = kecamatan;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	@Override
	public String toString() {
		return "RuteData{" +
				"provinsi='" + provinsi + '\'' +
				", kota='" + kota + '\'' +
				", lng=" + lng +
				", kecamatan='" + kecamatan + '\'' +
				", lat=" + lat +
				", alamat='" + alamat + '\'' +
				'}';
	}
}