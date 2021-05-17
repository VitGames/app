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
import io.techmeskills.an02onl_plannerapp.database.Note
import io.techmeskills.an02onl_plannerapp.database.NotesDao
import io.techmeskills.an02onl_plannerapp.databinding.FragmentEditNoteBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class EditNoteFragment() :
    NavigationFragment<FragmentEditNoteBinding>(R.layout.fragment_edit_note) {

    override val viewBinding: FragmentEditNoteBinding by viewBinding()
    private val viewModel: NoteDetailsViewModel by viewModel()
    private var selectedDate: Date = Date()
    private val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setPadding(0, top, 0, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var note: Note? = null
        setFragmentResultListener("EditKey") { key, bundle ->
            note = bundle.getParcelable("note")
            viewBinding.editNote.setText(note!!.text)
        }
        viewBinding.editCalendar.addOnDateChangedListener { displayed, date ->
            selectedDate = date
        }
        viewBinding.btnEdit.setOnClickListener {
            if (viewBinding.editNote.text.isNotBlank()) {
                viewModel.updateNote(Note(
                    id = note!!.id,
                    text = viewBinding.editNote.text.toString(),
                    date = dateFormat.format(selectedDate),
                    userName = note!!.userName,
                    alarmEnabled = viewBinding.checkBoxNatification.isChecked
                ))
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, "Поле ввода заметки пусто", Toast.LENGTH_SHORT).show()
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