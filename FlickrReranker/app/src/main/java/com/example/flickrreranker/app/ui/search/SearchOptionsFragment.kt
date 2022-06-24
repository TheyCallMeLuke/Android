package com.example.flickrreranker.app.ui.search

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.flickrreranker.R
import com.example.flickrreranker.app.ui.helper.*
import com.example.flickrreranker.app.ui.map.CustomMapFragment
import com.example.flickrreranker.databinding.FragmentSearchOptionsBinding
import com.example.flickrreranker.model.PhotoViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*


/**
 * The fragment for the search options.
 */
class SearchOptionsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentSearchOptionsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoViewModel by sharedViewModel()

    private var marker: Marker? = null
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchOptionsBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    /**
     * Set up options menu, listeners, map and reset the view model state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setOnSubmitButtonClick()
        setOnEditTextClick()
        setEditTextChange()
        setSwitchCheckedChange()
        setKeyboardHidingOnFocusChange(view)

        setupMap()
        viewModel.reset()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_options, menu)
    }

    /**
     * The target upload date of a photo is chosen by using a date picker dialog.
     */
    private fun setOnEditTextClick() {
        binding.dateEditText.setOnClickListener { showDatePickerDialog() }
    }

    /**
     * Submit the query with all from inputs after clicking the submit button.
     */
    private fun setOnSubmitButtonClick() {
        binding.submitButton.setOnClickListener { submitQuery() }
    }

    /**
     * This class will be used to implement the onMapReady callback.
     */
    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Here we check if the data is valid before actually submitting the query and loading the photos.
     * This is important because it enables us to assume this validity even in subsequent code
     * which makes it more readable (e.g. we can use safely us !! to cast a nullable to a non-nullable).
     */
    private fun submitQuery() {
        if (viewModel.isDataValid()) {
            viewModel.loadPhotos()
            NavigationUI.navigateUp(findNavController(), null)
        }
    }

    /**
     * The view model data is updated on-the-fly after every text change. The overhead is negligible
     * and it makes the submitQuery() function more readable, as we read the data here and not
     * there.
     */
    private fun setEditTextChange() {
        binding.keywordsEditText.doAfterTextChanged { viewModel.keywords = it.toString() }
        binding.photoCountEditText.doAfterTextChanged { viewModel.photoCount = it.toInt() }
        binding.usernameEditText.doAfterTextChanged { viewModel.author = it.toString() }
        binding.dateEditText.doAfterTextChanged {
            viewModel.date = it.toString().toUnix()
        }
        binding.viewCountEditText.doAfterTextChanged { viewModel.viewCount = it.toInt() }
        binding.latitudeEditText.doAfterTextChanged { viewModel.latitude = it.toDouble() }
        binding.longitudeEditText.doAfterTextChanged { viewModel.longitude = it.toDouble() }
    }

    /**
     * Every optional input has a corresponding switch which tells us if its corresponding input
     * is enabled or disabled. If it is in disabled state, we prohibit any text change on the input.
     *
     */
    private fun setSwitchCheckedChange() {
        binding.switchAuthor.setOnCheckedChangeListener { _, isChecked ->
            val authorContainer = binding.authorContainer
            if (!isChecked) {
                authorContainer.disable()
                viewModel.authorEnabled = false
            } else {
                authorContainer.enable()
                viewModel.authorEnabled = true
            }
        }
        binding.switchDate.setOnCheckedChangeListener { _, isChecked ->
            val dateContainer = binding.dateContainer
            if (!isChecked) {
                viewModel.dateEnabled = false
                dateContainer.disable()
            } else {
                dateContainer.enable()
                viewModel.dateEnabled = true
            }
        }
        binding.switchGeo.setOnCheckedChangeListener { _, isChecked ->
            val latitudeContainer = binding.latitudeContainer
            val longitudeContainer = binding.longitudeContainer
            val mapView = binding.map
            if (!isChecked) {
                viewModel.geoEnabled = false
                latitudeContainer.disable()
                longitudeContainer.disable()
            } else {
                latitudeContainer.enable()
                longitudeContainer.enable()
                viewModel.geoEnabled = true
            }
        }
        binding.switchViews.setOnCheckedChangeListener { _, isChecked ->
            val viewContainer = binding.viewCountContainer
            if (!isChecked) {
                viewModel.viewCountEnabled = false
                viewContainer.disable()
            } else {
                viewModel.viewCountEnabled = true
                viewContainer.enable()
            }
        }
    }

    private fun setKeyboardHidingOnFocusChange(view: View) {
        when (view) {
            is ViewGroup -> view.children.forEach { childView ->
                setKeyboardHidingOnFocusChange(
                    childView
                )
            }
            is TextInputEditText -> setTextEditFocusChange(view)
        }
    }

    private fun showDatePickerDialog() {
        DatePickerFacade.show(this)
        DatePickerFacade.addOnPositiveButtonClickListener { unixMillis ->
            val editText = binding.dateEditText
            editText.setText(unixMillis.toDateString())
        }
    }

    private fun setTextEditFocusChange(editText: TextInputEditText) {
        editText.setOnFocusChangeListener { view, hasFocus -> if (!hasFocus) hideKeyboard(view) }
    }

    private fun hideKeyboard(view: View) {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Sets up google maps.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val latitude = 37.422160
        val longitude = -122.084270
        val zoomLevel = 15f
        val startingLatLng = LatLng(latitude, longitude)

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startingLatLng, zoomLevel))

        enableMapNavigationInScrollView()
        setMapClick(map)
        setPoiClick(map)
    }

    /**
     * Google Maps in a scroll view aren't scrollable by default,
     * so we we have to hack it ourselves.
     */
    private fun enableMapNavigationInScrollView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? CustomMapFragment
        mapFragment?.listener = object : CustomMapFragment.OnTouchListener {
            override fun onTouch() {
                binding.scrollView.requestDisallowInterceptTouchEvent(true)
            }
        }
    }

    /**
     * Clicking on a position on the map will delete the previous marker and create a new marker.
     * This marker will contain additional information about the geo coordinates.
     */
    private fun setMapClick(map: GoogleMap) {
        map.setOnMapClickListener { latLng ->
            val snippet = latLng.toPrettifiedString()
            marker?.remove()
            marker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.dropped_pin))
                    .snippet(snippet)

            )
            updateGeoEditText(latLng)
        }
    }

    /**
     * Update the UI based on the geo location.
     */
    private fun updateGeoEditText(latLng: LatLng) {
        binding.latitudeEditText.setText(latLng.latitude.toString())
        binding.longitudeEditText.setText(latLng.longitude.toString())
    }

    /**
     * Show info window on marker click.
     */
    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            poiMarker?.showInfoWindow()
        }
    }

    /**
     * Change the map type based on the user's selection.
     */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun View.disable() {
        isEnabled = false
    }

    private fun View.enable() {
        isEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun LatLng.toPrettifiedString(): String? {
    return String.format(
        Locale.getDefault(),
        "Lat: %1$.5f, Long: %2$.5f",
        latitude,
        longitude
    )
}
