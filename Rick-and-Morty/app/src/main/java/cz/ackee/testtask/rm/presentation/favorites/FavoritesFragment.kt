package cz.ackee.testtask.rm.presentation.favorites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import cz.ackee.testtask.rm.R
import cz.ackee.testtask.rm.databinding.FavoritesBinding
import cz.ackee.testtask.rm.domain.Character
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var _binding: FavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteCharactersViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FavoritesBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.loadFavoriteCharacters()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.characters.observe(this, {
            if (it.isNotEmpty()) {
                showFavorites()
            } else {
                showNoFavoritesYetText()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        setToolbarTitle(getString(R.string.favorites))
    }

    private fun setToolbarTitle(title: String) {
        activity?.title = title
    }

    private fun showFavorites() = setupRecyclerView()

    /**
     * The noFavoritesTextContainer is GONE by default. But if there are no favorite characters
     * stored, this function will be called and the the container with the message will appear.
     */
    private fun showNoFavoritesYetText() {
        binding.noFavoritesTextContainer.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.favoriteList
        with(recyclerView) {
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.columns))
            adapter = FavoriteCharactersAdapter { onItemClicked(it) }
        }
    }

    private fun onItemClicked(it: Character) = navigateToCharacterDetails(it.id)

    private fun navigateToCharacterDetails(id: Long) {
        val action = FavoritesFragmentDirections
            .actionActionFavoritesToCharacterDetailsFragment(id)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}