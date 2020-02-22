package com.aaqibkhan.dottchallengeaaqib.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.aaqibkhan.dottchallengeaaqib.R
import com.aaqibkhan.dottchallengeaaqib.fragment.RestaurantBottomDialog
import com.aaqibkhan.dottchallengeaaqib.model.MapClusterItem
import com.aaqibkhan.dottchallengeaaqib.model.RestaurantSimple
import com.aaqibkhan.dottchallengeaaqib.model.Status
import com.aaqibkhan.dottchallengeaaqib.util.LocationProvider
import com.aaqibkhan.dottchallengeaaqib.util.UIUtil
import com.aaqibkhan.dottchallengeaaqib.util.getDisplayString
import com.aaqibkhan.dottchallengeaaqib.viewmodel.MapResultViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity(),
    OnMapReadyCallback,
    ClusterManager.OnClusterItemClickListener<MapClusterItem>,
    ClusterManager.OnClusterClickListener<MapClusterItem>,
    GoogleMap.OnCameraIdleListener,
    GoogleMap.OnCameraMoveStartedListener {

    companion object {
        private const val PERMISSION_REQUEST_CODE_LOCATION = 101
        private const val MAP_DEFAULT_ZOOM_LEVEL = 16f
        private const val MAP_CLUSTER_CLICK_ANIMATION_DURATION_MS = 500
        private const val MAP_CLUSTER_ITEM_CLICK_ANIMATION_DURATION_MS = 500
    }

    private lateinit var mMap: GoogleMap
    private lateinit var mClusterManager: ClusterManager<MapClusterItem>
    private val mapResultViewModel: MapResultViewModel by viewModels()
    private var cameraMovedByUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkLocationPermission()
    }

    /**
     * Check for location permission. If not granted then request it.
     * It uses [EasyPermissions] library.
     */
    private fun checkLocationPermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            onLocationPermissionGranted()
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.location_permission_rationale),
                PERMISSION_REQUEST_CODE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    /**
     * After getting location permission, or if already present, get the [GoogleMap]
     */
    @AfterPermissionGranted(PERMISSION_REQUEST_CODE_LOCATION)
    private fun onLocationPermissionGranted() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * When map is ready and location permission is enabled:
     * 1. Setup map UI settings and listeners
     * 2. Get user's current location
     * 3. Animate camera to user's current location
     * 4. Load restaurants around this area
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mClusterManager = ClusterManager(this, googleMap)
        setupListeners()

        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
            googleMap.setPadding(
                0,
                resources.getDimensionPixelSize(R.dimen.google_map_top_padding),
                0,
                resources.getDimensionPixelSize(R.dimen.google_map_bottom_padding)
            )
            LocationProvider.getCurrentLocation {
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(it, MAP_DEFAULT_ZOOM_LEVEL),
                    object : GoogleMap.CancelableCallback {
                        override fun onFinish() {
                            loadRestaurants()
                        }

                        override fun onCancel() {
                        }
                    })
            }
        }
    }

    private fun setupListeners() {
        mMap.setOnCameraMoveStartedListener(this)
        mMap.setOnCameraIdleListener(this)
        mMap.setOnMarkerClickListener(mClusterManager)
        mMap.setOnInfoWindowClickListener(mClusterManager)

        mClusterManager.setOnClusterItemClickListener(this)
        mClusterManager.setOnClusterClickListener(this)

        mapResultViewModel.restaurants.observe(this, Observer {
            if (it.status == Status.ERROR) {
                UIUtil.showToast(it.message)
            } else if (it.status == Status.SUCCESS) {
                setupMapMarkers(it.data)
            }
        })
    }

    /**
     * Start loading restaurants based on [GoogleMap] current viewport
     */
    private fun loadRestaurants() {
        val visibleRegionBounds = mMap.projection.visibleRegion.latLngBounds
        mapResultViewModel.loadRestaurants(
            visibleRegionBounds.southwest,
            visibleRegionBounds.northeast
        )
    }

    /**
     * Add cluster item [MapClusterItem] to the map using [ClusterManager]
     * Since [ClusterManager] does not handle duplicate cluster items as compared to regular marker,
     * we only add the [MapClusterItem] if not already present with [ClusterManager]
     */
    private fun setupMapMarkers(restaurantList: List<RestaurantSimple>?) {
        if (restaurantList.isNullOrEmpty()) {
            return
        }
        for (restaurant in restaurantList) {
            if (restaurant.location == null) {
                continue
            }
            val existingItem =
                mClusterManager.algorithm.items.find { it.heading == restaurant.name }
            if (existingItem == null) {
                mClusterManager.addItem(
                    MapClusterItem(
                        restaurant.id,
                        LatLng(restaurant.location.lat, restaurant.location.lng),
                        restaurant.name,
                        restaurant.categories?.getDisplayString()
                    )
                )
            }
        }
        mClusterManager.cluster()
    }

    /**
     * Let [EasyPermissions] library handle the permissions
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * Show [RestaurantBottomDialog] on cluster item click after animating to location of the cluster item
     */
    override fun onClusterItemClick(mapClusterItem: MapClusterItem): Boolean {
        (mClusterManager.renderer as DefaultClusterRenderer).getMarker(mapClusterItem)
            .showInfoWindow()
        mMap.animateCamera(CameraUpdateFactory.newLatLng(mapClusterItem.location),
            MAP_CLUSTER_ITEM_CLICK_ANIMATION_DURATION_MS,
            object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    RestaurantBottomDialog.newInstance(mapClusterItem.restaurantId)
                        .show(supportFragmentManager, RestaurantBottomDialog.TAG)
                }

                override fun onCancel() {
                }
            }
        )
        return true
    }

    /**
     * When a cluster on map is clicked, zoom in to the bounds derived from all the items in the cluster.
     */
    override fun onClusterClick(cluster: Cluster<MapClusterItem>): Boolean {
        val bounds = LatLngBounds.Builder()
        for (clusterItem in cluster.items) {
            bounds.include(clusterItem.location)
        }
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                resources.getDimensionPixelSize(R.dimen.google_map_cluster_padding)
            ),
            MAP_CLUSTER_CLICK_ANIMATION_DURATION_MS,
            object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    mClusterManager.cluster()
                }

                override fun onCancel() {
                }

            }
        )
        return true
    }

    /**
     * Remember [cameraMovedByUser] if the map movement is initiated by the user's gesture and not by animation
     */
    override fun onCameraMoveStarted(reason: Int) {
        cameraMovedByUser = reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
    }

    /**
     * When map comes to idle state after movement,
     * check if movement was initiated by user [cameraMovedByUser]
     * If yes, then start loading restaurants in that area [loadRestaurants]
     */
    override fun onCameraIdle() {
        if (cameraMovedByUser) {
            cameraMovedByUser = false
            loadRestaurants()
        }
    }

}
