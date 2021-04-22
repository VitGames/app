package io.techmeskills.an02onl_plannerapp.screen.main

import RecyclerItemClickListener
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
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
       // viewModel.invalidateList()
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
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
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
    }
    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
            }
        }
}