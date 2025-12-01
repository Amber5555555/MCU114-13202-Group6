package com.example.homework7

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log // <---【新增】為了更詳細的除錯
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// ... (資料類別和介面保持不變) ...
data class DirectionsResponse(@SerializedName("routes") val routes: List<Route>)
data class Route(@SerializedName("overview_polyline") val overviewPolyline: OverviewPolyline)
data class OverviewPolyline(@SerializedName("points") val points: String)

interface DirectionsApiService {
    @GET("maps/api/directions/json")
    fun getDirections(@Query("origin") origin: String, @Query("destination") destination: String, @Query("mode") mode: String, @Query("key") apiKey: String): Call<DirectionsResponse>
}


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    // ... (onRequestPermissionsResult, loadMap, onCreate 保持不變) ...
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && requestCode == 0) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) loadMap()
            else finish()
        }
    }
    private fun loadMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMap()
    }


    override fun onMapReady(map: GoogleMap) {
        val isAccessFineLocationGranted = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val isAccessCoarseLocationGranted = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (isAccessFineLocationGranted && isAccessCoarseLocationGranted) {
            val taipei101 = LatLng(25.033611, 121.565000)
            val taipeiStation = LatLng(25.047924, 121.517081)

            map.addMarker(MarkerOptions().position(taipei101).title("台北101"))
            map.addMarker(MarkerOptions().position(taipeiStation).title("台北車站"))

            val bounds = LatLngBounds.Builder().include(taipei101).include(taipeiStation).build()
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150))

            // --- ★★★★★【最終修正】★★★★★ ---
            // 1. 請務必將下方的金鑰替換成您自己的。
            // 2. 我已經刪除所有 if 檢查，直接呼叫 API。
            val apiKey = "AIzaSyAcC9D258co2DhrCCsr1mfZW_afrWElFVM" // <-- ★★★ 確認這是你自己的有效金鑰 ★★★

            fetchDirections(taipei101, taipeiStation, apiKey, map)

        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }
    }

    private fun fetchDirections(origin: LatLng, destination: LatLng, apiKey: String, map: GoogleMap) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DirectionsApiService::class.java)
        val originStr = "${origin.latitude},${origin.longitude}"
        val destinationStr = "${destination.latitude},${destination.longitude}"

        service.getDirections(originStr, destinationStr, "walking", apiKey).enqueue(object : Callback<DirectionsResponse> {
            override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                // 【除錯強化】無論成功或失敗，都印出詳細資訊
                Log.d("DirectionsAPI", "Response Code: ${response.code()}")
                Log.d("DirectionsAPI", "Response Body: ${response.body()?.toString()}")
                Log.d("DirectionsAPI", "Error Body: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    val routes = response.body()?.routes
                    if (!routes.isNullOrEmpty() && routes[0].overviewPolyline.points.isNotEmpty()) {
                        val points = routes[0].overviewPolyline.points
                        val decodedPath = decodePolyline(points)
                        map.addPolyline(PolylineOptions().addAll(decodedPath).color(Color.RED).width(12f))
                    } else {
                        Toast.makeText(this@MainActivity, "成功連線但找不到路線，請檢查金鑰權限", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "取得路線失敗，錯誤碼: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                // 【除錯強化】印出網路錯誤
                Log.e("DirectionsAPI", "Network Failure: ${t.message}", t)
                Toast.makeText(this@MainActivity, "網路錯誤，請檢查網路連線和權限", Toast.LENGTH_LONG).show()
            }
        })
    }

    // ... (decodePolyline 函式保持不變) ...
    private fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
            poly.add(p)
        }
        return poly
    }
}
