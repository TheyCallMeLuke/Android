package dang.lukas.ui.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dang.lukas.MainActivity
import dang.lukas.R
import dang.lukas.databinding.PhotosBinding
import dang.lukas.domain.Photo
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * The main fragment for the list of photos and for searching
 */
class PhotosFragment : Fragment() {

    private var _binding: PhotosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotosViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadRecentPhotos()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PhotosBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
        setupToolbar()
        setupSwipeRefresh()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.reloadPhotos() }
        viewModel.isLoading.observe(viewLifecycleOwner, { isRefreshing ->
            if (isRefreshing == false) binding.swipeRefreshLayout.isRefreshing = false
        })
    }

    private fun setupToolbar() {
        (requireActivity() as MainActivity).setupActionBar(binding.toolbar)
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.loadPhotos(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.photosView
        with(recyclerView) {
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.columns))
            adapter = PhotosAdapter { navigateToPhotoDetails(it) }
        }
    }

    private fun RecyclerView.navigateToPhotoDetails(it: Photo) {
        val action =
            PhotosFragmentDirections.actionRecentPhotosFragmentToPhotoDetailsFragment(it.id)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}