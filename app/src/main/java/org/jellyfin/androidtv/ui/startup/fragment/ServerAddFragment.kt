package org.jellyfin.androidtv.ui.startup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.auth.model.ConnectedState
import org.jellyfin.androidtv.auth.model.ConnectingState
import org.jellyfin.androidtv.auth.model.UnableToConnectState
import org.jellyfin.androidtv.constant.ServerConstants
import org.jellyfin.androidtv.databinding.FragmentServerAddBinding
import org.jellyfin.androidtv.ui.startup.ServerAddViewModel
import org.jellyfin.androidtv.util.createBundle
import org.jellyfin.androidtv.util.getSummary
import org.koin.androidx.viewmodel.ext.android.viewModel

class ServerAddFragment : Fragment() {
	companion object {
		const val ARG_SERVER_ADDRESS = "server_address"
	}

	private val startupViewModel: ServerAddViewModel by viewModel()
	private var _binding: FragmentServerAddBinding? = null
	private val binding get() = _binding!!

	private val serverAddressArgument get() = arguments?.getString(ARG_SERVER_ADDRESS)?.ifBlank { null }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentServerAddBinding.inflate(inflater, container, false)

		with(binding.address) {
			setOnEditorActionListener { _, actionId, _ ->
				when (actionId) {
					EditorInfo.IME_ACTION_DONE -> {
						submitAddress()
						true
					}

					else -> false
				}
			}
		}

		with(binding.confirm) {
			setOnClickListener { submitAddress() }
		}

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val address = serverAddressArgument
		if (address != null) {
			// Explicit address supplied (e.g. deep link): pre-fill and connect.
			binding.address.setText(address)
			binding.address.isEnabled = false
			submitAddress()
		} else {
			// No address supplied: use the configured default (if any).
			val defaultAddress = ServerConstants.DEFAULT_SERVER_ADDRESS
			binding.address.setText(defaultAddress)
			if (defaultAddress.isNotBlank() && ServerConstants.AUTO_CONNECT_DEFAULT_SERVER) {
				binding.address.isEnabled = false
				submitAddress()
			} else {
				binding.address.requestFocus()
			}
		}

		startupViewModel.state.onEach { state ->
			when (state) {
				is ConnectingState -> {
					// Disable form
					binding.address.isEnabled = false
					binding.confirm.isEnabled = false
					// Update state text
					binding.error.text = getString(R.string.server_connecting, state.address)
				}

				is UnableToConnectState -> {
					// Enable form
					binding.address.isEnabled = true
					binding.confirm.isEnabled = true
					// Update state text
					binding.error.text = getString(
						R.string.server_connection_failed_candidates,
						state.addressCandidates
							.map { "${it.key} - ${it.value.getSummary(requireContext())}" }
							.joinToString(prefix = "\n", separator = "\n")
					)
				}

				is ConnectedState -> parentFragmentManager.commit {
					// Open server view
					replace<StartupToolbarFragment>(R.id.content_view)
					add<ServerFragment>(
						R.id.content_view,
						null,
						createBundle {
							putString(ServerFragment.ARG_SERVER_ID, state.id.toString())
						}
					)
				}

				null -> Unit
			}
		}.launchIn(lifecycleScope)
	}

	override fun onDestroyView() {
		super.onDestroyView()

		_binding = null
	}

	private fun submitAddress() = when {
		binding.address.text.isNotBlank() -> startupViewModel.addServer(binding.address.text.toString())
		else -> binding.error.setText(R.string.server_field_empty)
	}
}
