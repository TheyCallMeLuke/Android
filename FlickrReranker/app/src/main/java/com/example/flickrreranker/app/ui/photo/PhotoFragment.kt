package com.example.flickrreranker.app.ui.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flickrreranker.databinding.FragmentPhotoListBinding
import com.example.flickrreranker.model.PhotoViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * The fragment representing a list of photos.
 */
class PhotoFragment : Fragment() {

    private var _binding: FragmentPhotoListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoViewModel by sharedViewModel()

    /**
     * Enable data binding.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPhotoListBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    /**
     * We use a linear layout for the recycler view here.
     * Then we set the onClickListener of the search button that navigates the user
     * to the search options fragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.photoList
        with(recyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = PhotoRecyclerViewAdapter(childFragmentManager)
        }
        setSearchOptionsButtonClick(binding.search)

    }

    private fun setSearchOptionsButtonClick(btn: Button) {
        btn.setOnClickListener {
            val action = PhotoFragmentDirections.actionPhotoFragmentToSearchOptionsFragment()
            findNavController().navigate(action)
        }
    }

}