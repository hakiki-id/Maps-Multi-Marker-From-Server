package com.hakiki95.petamadu;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private  GoogleMap mGoogleMap;
    private  double lat, lng ;
    RequestQueue mRequestQueue ;

    private static  final  String url = "http://hakiki.hol.es/android/kosan/getall.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mFragmentMap = MapFragment.newInstance();
        FragmentTransaction mTranscation = getFragmentManager().beginTransaction();
        mTranscation.add(R.id.maps_place,mFragmentMap);
        mTranscation.commit();
        mFragmentMap.getMapAsync(this);

        mRequestQueue = Volley.newRequestQueue(this);

        getData();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        LatLng Pusat = new LatLng(-8.0067358,112.6195679);
        mGoogleMap.addMarker(new MarkerOptions().position(Pusat).title("this places"));
        getData();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Pusat,16));

    }


    public void getData() {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("Data_Kosan");
                            for (int i=0 ; i < array.length(); i++){
                                JSONObject obj = array.getJSONObject(i);
                                String ptitle = obj.getString("nama");
                                String pketerangan = obj.getString("keterangan");
                                String plat = String.valueOf(obj.getString("lat"));
                                String plng = String.valueOf(obj.getString("lng"));
                                mGoogleMap.addMarker(new MarkerOptions().position(
                                        new LatLng(Double.parseDouble(plat),Double.parseDouble(plng))).title(ptitle));
                            }

                        } catch (JSONException e) {
                                e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR",error.toString());
                    }
                });

            mRequestQueue.add(jsonRequest);
    }
}
