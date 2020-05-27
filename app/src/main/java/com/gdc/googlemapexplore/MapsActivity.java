package com.gdc.googlemapexplore;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gdc.googlemapexplore.api.ApiClient;
import com.gdc.googlemapexplore.api.ApiInterface;
import com.gdc.googlemapexplore.model.GMapResponse;
import com.gdc.googlemapexplore.model.LegsItem;
import com.gdc.googlemapexplore.model.RoutesItem;
import com.gdc.googlemapexplore.model.v1.DestinationItem;
import com.gdc.googlemapexplore.model.v1.RuteData;
import com.gdc.googlemapexplore.model.v1.RuteTagihResponse;
import com.gdc.googlemapexplore.util.Constant;
import com.gdc.googlemapexplore.ver1.FetchURL;
import com.gdc.googlemapexplore.ver1.TaskLoadedCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private static final String TAG = "_MapsActivity";

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean mLocationPermissionGranted = false;
    private Location lastLocation;
    private Button btn_getRoute;
    private MarkerOptions marker = new MarkerOptions();
//    private MarkerOptions place1, place2, place3, place4;
    private MarkerOptions mark1, mark2, mark3, mark4, mark5;
    Polyline currentPolyline;

    boolean isMapReady = false;

    private static final LatLng HOME = new LatLng(-7.2374363, 112.6274348);
    private static final LatLng T_JUNCTION = new LatLng(-7.2411412, 112.6288518);
    private static final LatLng GAS_STATION = new LatLng(-7.2369845, 112.61866);

    private static final LatLng LOCATION1 = new LatLng(-7.272281,112.7443577);      // -7.273907299999999, 112.7435858
    private static final LatLng LOCATION2 = new LatLng(-7.2740262, 112.7434609);
    private static final LatLng LOCATION3 = new LatLng(-7.273349199999999, 112.7423484);
    private static final LatLng LOCATION_DEST = new LatLng(-7.277328100000001, 112.7413782);

    private double currentlat = 0.0, currentLng = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmap);
        initView();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        place1 = new MarkerOptions().position(new LatLng(-7.2768976,112.7433008)).title("Location 1");
//        place2 = new MarkerOptions().position(new LatLng(-7.2737008,112.7452361)).title("Location 2");
//        place3 = new MarkerOptions().position(new LatLng(-7.2843912,112.7478597)).title("Location 3");
//        place4 = new MarkerOptions().position(new LatLng(-7.2900422,112.7480099)).title("Location 4");

        if (!checkGps()) {
            Toast.makeText(this, "GPS not activated", Toast.LENGTH_SHORT).show();
        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }


        initListener();

//        getDirectionJson();

    }

    private String getUrl2(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        //waypoint
//        String waypoints = "waypoints=optimize:true|-7.2411412, 112.6288518|";
        String waypoints = "waypoints=optimize:true|"
                + LOCATION1.latitude + "," + LOCATION2.longitude
                + "|" + "|" + LOCATION2.latitude + "," + LOCATION2.longitude
                + "|" + LOCATION3.latitude + "," + LOCATION3.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + waypoints + "&" + str_dest + "&" + mode + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    private String getUrl3(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // waypoints
        StringBuilder wayPoints = new StringBuilder();

        for (int i = 0; i < destinationItems.size(); i++) {
            wayPoints.append("|").append(destinationItems.get(i).getLat()).append(",").append(destinationItems.get(i).getLng()).append("|");
        }
        wayPoints.insert(0, "waypoints=optimize:true");
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + wayPoints + "&" + str_dest + "&" + mode + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }


    private boolean checkGps() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;

                getDeviceLocation();
                Log.d(TAG, "onRequestPermissionsResult: Permission Running...");
            }
        }

        updateLocationUI();
    }

    private void updateLocationUI() {
        try {
            if (mLocationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                lastLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.d(TAG, "updateLocationUI: Error: " + e.getMessage());
        }
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = fusedLocationClient.getLastLocation();
                locationResult.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            lastLocation = location;
                            Log.d(TAG, "onSuccess: " + lastLocation);


                            LatLng current = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
//                            LatLng current = new LatLng(currentlat ,currentLng);

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 18f));
                            marker.position(current);
                            googleMap.setMyLocationEnabled(true);

//                            googleMap.addMarker(marker);
//                            googleMap.addMarker(mark1);
//                            googleMap.addMarker(mark2);
//                            googleMap.addMarker(mark3);
//                            googleMap.addMarker(mark4);
//                            googleMap.addMarker(mark5);

                            getRuteTagih();

//                            googleMap.addMarker(place1
//                                    .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_person_pin_circle_black_24dp))
//                                    .snippet("We are here!")).setTag("place1");
//                            googleMap.addMarker(place2).setTag("place2");
//                            googleMap.addMarker(place3).setTag("place3");
//                            googleMap.addMarker(place4).setTag("place4");
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: Error: " + e.getMessage());
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void getRouteReal() {
        String srcAdd = "&origin=" + destinationItems.get(0).getLat() + "," + destinationItems.get(0).getLng();
        String desAdd = "&destination=" + destinationItems.get(destinationItems.size() - 1).getLat() + "," + destinationItems.get(destinationItems.size() - 1).getLng();
        String wayPoints = "";

        for (int j = 1; j < destinationItems.size() - 1; j++) {
            wayPoints = wayPoints + (wayPoints.equals("") ? "" : "%7C") + destinationItems.get(j).getLat() + "," + destinationItems.get(j).getLng();
        }
        wayPoints = "&waypoints=" + wayPoints;

        String link="https://www.google.com/maps/dir/?api=1&travelmode=driving"+srcAdd+desAdd+wayPoints;

        final Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(link));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    private void initListener() {
        btn_getRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMapReady) {
                    String url = getUrl3(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()),
                            new LatLng(destinationItems.get(destinationItems.size()-1).getLat(), destinationItems.get(destinationItems.size()-1).getLng()), "Driving");
                    new FetchURL(MapsActivity.this).execute(url);
//                    getRouteReal();
                } else {
                    Toast.makeText(MapsActivity.this, "The map is not ready yet", Toast.LENGTH_SHORT).show();
                }
//                String url = getUrl3(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()),
//                        new LatLng(destinationItems.get(0).getLat(), destinationItems.get(0).getLng()), "Driving");
//                new FetchURL(MapsActivity.this).execute(url);
            }
        });
    }

    private void initView() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        btn_getRoute = findViewById(R.id.btn_getRoute);
    }

    public void drawRoute(String result) {

        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            Polyline line = googleMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(12)
                    .color(Color.parseColor("#05b1fb"))//Google maps blue color
                    .geodesic(true)
            );


//            googleMap.addPolyline(line);

        } catch (JSONException e) {

        }
    }

    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }

    private void getDirectionJson() {
        GMapResponse routesItem = new Gson().fromJson(Constant.directionsResponse, GMapResponse.class);
        for (int i = 0; i < routesItem.getRoutes().size(); i++) {
            List<LegsItem> legsItems = routesItem.getRoutes().get(i).getLegs();
            for (int j = 0; j < legsItems.size(); j++) {
                Log.d(TAG, "getDirectionJson: LEGSITEM: " + legsItems.get(j).getDistance());
                currentlat = legsItems.get(j).getStartLocation().getLat();
                currentLng = legsItems.get(j).getStartLocation().getLng();

//                mark1 = new MarkerOptions().position(new LatLng(currentlat, currentLng)).title("Start Location");
//                mark2 = new MarkerOptions().position(new LatLng(LOCATION1.latitude, LOCATION1.longitude)).title("Location 1");
//                mark3 = new MarkerOptions().position(new LatLng(LOCATION2.latitude, LOCATION2.longitude)).title("Location 2");
//                mark4 = new MarkerOptions().position(new LatLng(LOCATION3.latitude, LOCATION3.longitude)).title("Location 3");
//                mark5 = new MarkerOptions().position(new LatLng(LOCATION_DEST.latitude, LOCATION_DEST.longitude)).title("Destination");
//                mark2 = new MarkerOptions().position(new LatLng(legsItems.get(j).getEndLocation().getLat(), legsItems.get(j).getEndLocation().getLng()));
            }
        }

    }


    List<DestinationItem> destinationItems = new ArrayList<>();
    private void getRuteTagih() {
        RuteData data = new RuteData();
        data.setAlamat("string");
        data.setKota("string");
        data.setKecamatan("string");
        data.setProvinsi("string");
        data.setLat(lastLocation.getLatitude());
        data.setLng(lastLocation.getLongitude());

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<RuteTagihResponse> call = apiInterface.getRuteTagih(Constant.GDC_TOKEN, data);
        call.enqueue(new Callback<RuteTagihResponse>() {
            @Override
            public void onResponse(Call<RuteTagihResponse> call, Response<RuteTagihResponse> response) {
                if (response.isSuccessful()) {
                    destinationItems = response.body().getRuteTagih().getDestination();
                    Log.d(TAG, "onResponse: Before Sorting" + destinationItems);
                    Collections.sort(destinationItems, new Comparator<DestinationItem>() {
                        @Override
                        public int compare(DestinationItem dest1, DestinationItem dest2) {
                            return dest1.getIdx() - dest2.getIdx();
                        }
                    });
                    Log.d(TAG, "onResponse: After Sorting; " + destinationItems);

                    for (int i = 0; i < destinationItems.size(); i++) {
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(destinationItems.get(i).getLat(),
                                destinationItems.get(i).getLng())).title("Nasabah #" + (i+1)));
                    }
                } else {
                    Toast.makeText(MapsActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RuteTagihResponse> call, Throwable t) {
                Toast.makeText(MapsActivity.this, "Error2: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        isMapReady = true;

        updateLocationUI();

        getDeviceLocation();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null) {
            currentPolyline.remove();
        }
        currentPolyline = googleMap.addPolyline((PolylineOptions) values[0]);
    }
}
