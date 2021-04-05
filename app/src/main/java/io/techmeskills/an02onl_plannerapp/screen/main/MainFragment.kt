package io.techmeskills.an02onl_plannerapp.screen.main

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentMainBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.text.Layout
import androidx.fragment.app.setFragmentResultListener
import io.techmeskills.an02onl_plannerapp.support.setVerticalMargin


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
        viewBinding.btnNavToNew.setOnClickListener {
            view.findNavController().navigate(R.id.action_mainFragment_to_addNewFragment)
        }
        setFragmentResultListener("requestKey") { key, bundle ->
            val txtNote = bundle.getString("txt_Note")
            val txtData = bundle.getString("txt_Data")
            txtNote?.let {
                viewModel.addNewNote(it,txtData)
            }
        }
    }
}






