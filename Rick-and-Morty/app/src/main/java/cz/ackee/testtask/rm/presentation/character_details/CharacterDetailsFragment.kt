package cz.ackee.testtask.rm.presentation.character_details

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import cz.ackee.testtask.rm.MainActivity
import cz.ackee.testtask.rm.R
import cz.ackee.testtask.rm.databinding.CharacterDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterDetailsFragment : Fragment() {

    private var _binding: CharacterDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterDetailsViewModel by viewModel()

    private val args: CharacterDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = CharacterDetailsBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        loadCharacter()
        setHasOptionsMenu(true)
        hideBottomNav()
        return binding.root
    }

    private fun loadCharacter() = viewModel.loadCharacter(args.id)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.character_details_menu, menu)
        setTitle()
        setFavoriteIcon(menu)
        setBackArrowIcon()
    }

    private fun setTitle() =
        viewModel.character.observe(viewLifecycleOwner, {
            (activity as MainActivity).binding.toolbar.title = it.name
        })

    private fun setBackArrowIcon() =
        (activity as MainActivity).binding.toolbar.setNavigationIcon(R.drawable.ic_24_arrow_left)

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.action_save -> {
                val toggledIcon = viewModel.handleSaveAndReturnToggledSaveIcon()
                item.icon = toggledIcon
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun setFavoriteIcon(menu: Menu) =
        viewModel.character.observe(viewLifecycleOwner, {
            val favoriteIcon = viewModel.getFavoriteIcon()
            menu.getItem(0).icon = favoriteIcon
        })

    override fun onDestroy() {
        super.onDestroy()
        showBottomNav()
        _binding = null
    }

    private fun showBottomNav() {
        (activity as MainActivity).binding.bottomNav.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        (activity as MainActivity).binding.bottomNav.visibility = View.GONE
    }
}