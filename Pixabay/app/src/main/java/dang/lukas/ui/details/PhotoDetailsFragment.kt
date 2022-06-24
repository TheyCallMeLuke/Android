package dang.lukas.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dang.lukas.MainActivity
import dang.lukas.databinding.PhotoDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * The fragment for the photo detail.
 */
class PhotoDetailsFragment : Fragment() {

    private var _binding: PhotoDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoDetailsViewModel by viewModel()

    private val args: PhotoDetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PhotoDetailsBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.loadPhoto(args.photoId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupOnPhotoClick()
        setupOnUserClick()
    }

    private fun setupOnUserClick() {
        binding.userContainer.setOnClickListener { navigateToUserProfilePage() }
    }

    private fun navigateToUserProfilePage() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.photo.value?.profileUrl))
        startActivity(intent)
    }

    private fun setupOnPhotoClick() {
        binding.photoView.setOnClickListener { navigateToPhotoPage() }
    }

    private fun navigateToPhotoPage() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.photo.value?.pageUrl))
        startActivity(intent)
    }

    private fun setupToolbar() {
        with(requireActivity() as MainActivity) { setupActionBar(binding.toolbar) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}