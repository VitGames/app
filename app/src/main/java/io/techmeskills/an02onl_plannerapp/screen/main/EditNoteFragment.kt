package io.techmeskills.an02onl_plannerapp.screen.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentEditNoteBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment

class EditNoteFragment() :
    NavigationFragment<FragmentEditNoteBinding>(R.layout.fragment_edit_note) {

    override val viewBinding: FragmentEditNoteBinding by viewBinding()

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setPadding(0, top, 0, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var id: Int? = null
        setFragmentResultListener("EditKey") { key, bundle ->
            id = bundle.getInt("note_id")
            val txtNote = bundle.getString("txt_NoteEdit")
            val txtData = bundle.getString("txt_DataEdit")
            viewBinding.editNote.setText(txtNote)
            viewBinding.editData.setText(txtData)
        }
        viewBinding.btnEdit.setOnClickListener {
            if (viewBinding.editNote.text.isNotBlank()) {
                val bundle = bundleOf(
                    "note_id" to id,
                    "txt_NoteEditNew" to viewBinding.editNote.text.toString(),
                    "txt_DataEditNew" to viewBinding.editData.text.toString()
                )
                setFragmentResult("EditKeyUpdate", bundle)
                findNavController().popBackStack()
            } else {
                val toast = Toast.makeText(context, "Поле ввода заметки пусто", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

}


