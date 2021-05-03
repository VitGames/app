package io.techmeskills.an02onl_plannerapp.screen.main

import RecyclerItemClickListener
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.database.Note
import io.techmeskills.an02onl_plannerapp.databinding.FragmentMainBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.setVerticalMargin
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : NavigationFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override val viewBinding: FragmentMainBinding by viewBinding()

    private val viewModel: MainViewModel by viewModel()


    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setPadding(0, top, 0, 0)
        viewBinding.recyclerView.setVerticalMargin(marginBottom = bottom)
        viewBinding.btnNavToNew.setVerticalMargin(marginBottom = bottom)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveData.observe(this.viewLifecycleOwner, Observer {
            viewBinding.recyclerView.adapter = NotesRecyclerViewAdapter(it)
        })
        viewBinding.btnCloud.setOnClickListener {
            if (viewModel.checkInternetConnection()) {
                showCloudDialog()
            } else {
                Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_LONG)
                    .show()
            }
        }
        viewBinding.btnDeleteUser.setOnClickListener {
            viewModel.checkInternetConnection()
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete user")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes") { dialog, _ ->
                    viewModel.delete()
                    viewModel.logout()
                    view.findNavController().navigate(R.id.loginFragment)
                }.setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }.show()
        }
        viewBinding.btnNavToNew.setOnClickListener {
            view.findNavController().navigate(R.id.action_mainFragment_to_addNewFragment)
        }
        viewBinding.recyclerView.addOnItemTouchListener(RecyclerItemClickListener(context,
            viewBinding.recyclerView,
            object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View?, position: Int) {
                    val note: Note = viewModel.getItemById(position)
                    val bundle = bundleOf(
                        "note" to note,
                        "txt_NoteEdit" to note.text,
                        "txt_DataEdit" to note.date
                    )
                    setFragmentResult("EditKey", bundle)
                    this@MainFragment.findNavController()
                        .navigate(R.id.action_mainFragment_to_editNoteFragment, bundle)
                }

                override fun onLongItemClick(view: View?, position: Int) {
                    val note: Note = viewModel.getItemById(position)
                    viewModel.removeItemByDao(note)
                }
            })
        )
        viewBinding.btnLogout.setOnClickListener {
            viewModel.logout()
            this@MainFragment.findNavController().navigate(R.id.loginFragment)
        }
        setFragmentResultListener("EditKeyUpdate") { key, bundle ->
            val id: Int = bundle.getInt("note_id")
            val txtNote = bundle.getString("txt_NoteEditNew")
            val txtDate = bundle.getString("txt_DataEditNew")
            id.let {
                val note: Note = viewModel.getItemById(id)
                note.text = txtNote.toString()
                note.date = txtDate.toString()
            }
        }
        viewModel.progressLiveData.observe(this.viewLifecycleOwner)
        { success ->
            if (success.not()) {
                viewBinding.progressBar.isVisible = false
            } else {
                viewBinding.progressBar.isVisible = false
            }
        }
        viewModel.internetConnectionLiveData.observe(this.viewLifecycleOwner)
        { connection ->
            if (connection.not()) {
                Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun showCloudDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cloud storage")
            .setMessage("Please, pick cloud action")
            .setPositiveButton("Import") { dialog, _ ->
                viewBinding.progressBar.isVisible = true
                viewModel.checkInternetConnection()
                if (viewModel.checkInternetConnection()) {
                    viewModel.importNotes()
                    dialog.cancel()
                } else {
                    viewBinding.progressBar.isVisible = false
                    Toast.makeText(requireContext(),
                        "No internet connection",
                        Toast.LENGTH_LONG)
                        .show()
                }
            }.setNegativeButton("Export") { dialog, _ ->
                viewModel.checkInternetConnection()
                viewBinding.progressBar.isVisible = true
                if (viewModel.checkInternetConnection()) {
                    viewModel.exportNotes()
                    dialog.cancel()
                } else {
                    viewBinding.progressBar.isVisible = false
                    Toast.makeText(requireContext(),
                        "No internet connection",
                        Toast.LENGTH_LONG)
                        .show()
                }
            }.show()
    }


    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
            }
        }
}


