package cz.ackee.testtask.rm.presentation.characters

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import cz.ackee.testtask.rm.MainActivity
import cz.ackee.testtask.rm.R
import cz.ackee.testtask.rm.databinding.CharactersBinding
import cz.ackee.testtask.rm.domain.Character
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val SEARCH_KEY = "QUERY"

/**
 * This fragment displays the list of all characters and the list of filtered characters as defined
 * by the Rick and Morty API documentation: https://rickandmortyapi.com/documentation
 */
class CharactersFragment : Fragment() {

    private var _binding: CharactersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharactersViewModel by viewModel()

    private lateinit var charactersAdapter: CharactersAdapter
    private lateinit var filteredAdapter: FilteredCharactersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = CharactersBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setupRecyclerView()
        loadCharacters()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.characters_menu, menu)
        setToolbarTitle(getString(R.string.characters))
        setupSearchView(menu)
    }

    private fun setupAdapters() {
        charactersAdapter = CharactersAdapter { onItemClicked(it) }
        filteredAdapter = FilteredCharactersAdapter { onItemClicked(it) }
    }

    /**
     * The title is set on the toolbar which is owned by the activity.
     */
    private fun setToolbarTitle(title: String) {
        activity?.title = title
    }

    private fun setupSearchView(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        spanSearchViewFullWidth(searchView)
        setupOnQueryTextChange(searchView)
        setupOnSearchViewExpansion(searchItem)
        restoreSearchViewOnConfigurationChange(searchItem, searchView)
    }

    /**
     * Without this setting the search view doesn't span the full width of the toolbar when an
     * orientation change to landscape occurs.
     */
    private fun spanSearchViewFullWidth(searchView: SearchView) {
        searchView.maxWidth = Integer.MAX_VALUE
    }

    /**
     * Filters the characters as the user types his/her input query.
     */
    private fun setupOnQueryTextChange(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(query: String?): Boolean {
                loadFilteredCharacters(query)
                return false
            }
        })
    }

    /**
     * When the user clicks the search icon, the onMenuItemActionExpand listener is triggered.
     * When this happens:
     * 1. the app should not show any characters at the beginning.
     * 2. the bottom navigation should be hidden.
     * 3. a different recycler view should be set, as the UI for the filtered characters is a bit
     *    different.
     * 4. toggle the filteringOn which is later used to restore the search view after
     *    a configuration change.
     *
     * When the user closes the search view, the above operations are reversed within the
     * onMenuItemActionCollapse listener.
     */
    private fun setupOnSearchViewExpansion(item: MenuItem) {
        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                clearCharactersData()
                hideBottomNavigation()
                binding.characterList.adapter = filteredAdapter
                viewModel.filteringOn = true
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                loadCharacters()
                showBottomNavigation()
                binding.characterList.adapter = charactersAdapter
                viewModel.filteringOn = false
                return true
            }
        })
    }

    /**
     * Restores the search view only when it's expanded which is indicated by the filteringOn
     * boolean.
     */
    private fun restoreSearchViewOnConfigurationChange(
        searchItem: MenuItem,
        searchView: SearchView,
    ) {
        if (viewModel.filteringOn) {
            searchItem.expandActionView()
            searchView.setQuery(viewModel.lastQuery, true)
            searchView.clearFocus()
        }
    }

    /**
     * Clears the recycler view.
     */
    private fun clearCharactersData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadCharacters().collectLatest {
                charactersAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
            }
        }
    }

    /**
     * Loads all characters (using paging).
     */
    private fun loadCharacters() {
        viewLifecycleOwner.lifecycleScope.launch(IO) {
            viewModel.loadCharacters().collectLatest {
                charactersAdapter.submitData(it)
            }
        }
    }

    /**
     * Filters characters.
     */
    private fun loadFilteredCharacters(query: String?) {
        if (query.isNullOrBlank()) return
        viewLifecycleOwner.lifecycleScope.launch(IO) {
            viewModel.filterCharacters(query).collectLatest {
                filteredAdapter.submitData(it)
            }
        }
    }

    /**
     * Handles item clicks to navigate to the details of a character.
     */
    private fun onItemClicked(it: Character) {
        navigateToCharacterDetails(it.id)
    }

    /**
     * The bottom navigation is owned by the activity, and thus toggling its visibility is done
     * through the reference of that activity.
     */
    private fun showBottomNavigation() {
        (activity as MainActivity).binding.bottomNav.visibility = View.VISIBLE
    }

    private fun hideBottomNavigation() {
        (activity as MainActivity).binding.bottomNav.visibility = View.GONE
    }

    /**
     * Sets up the recycler view.
     * The default adapter used is the charactersAdapter which uses the UI for the non-filtered
     * characters.
     */
    private fun setupRecyclerView() {
        val recyclerView = binding.characterList
        with(recyclerView) {
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.columns))
            adapter = this@CharactersFragment.charactersAdapter
        }
    }

    private fun navigateToCharacterDetails(id: Long) {
        val action = CharactersFragmentDirections
            .actionActionCharactersToCharacterDetailsFragment(id)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}