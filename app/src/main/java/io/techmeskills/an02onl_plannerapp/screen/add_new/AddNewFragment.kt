package io.techmeskills.an02onl_plannerapp.screen.add_new

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.alarmservice.AlarmReceiver
import io.techmeskills.an02onl_plannerapp.database.Note
import io.techmeskills.an02onl_plannerapp.databinding.FragmentAddNewBinding
import io.techmeskills.an02onl_plannerapp.screen.main.NoteDetailsViewModel
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AddNewFragment : NavigationFragment<FragmentAddNewBinding>(R.layout.fragment_add_new) {
    override val viewBinding: FragmentAddNewBinding by viewBinding()
    private val viewModel: NoteDetailsViewModel by viewModel()
    private lateinit var alarmManager: AlarmManager
    private lateinit var alarmIntent: PendingIntent
    private var selectedDate: Date = Date()
    private val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setPadding(0, top, 0, 0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent =
            PendingIntent.getBroadcast(context, 0, Intent(context, AlarmReceiver::class.java), 0)
        viewBinding.calendarView.addOnDateChangedListener { displayed, date ->
            selectedDate = date
        }
        viewBinding.btnAddNew.setOnClickListener {
            if (viewBinding.editNewNote.text.isNotBlank()) {
                viewModel.addNewNote(
                    Note(
                    text = viewBinding.editNewNote.text.toString(),
                    date = dateFormat.format(selectedDate),
                    userName = "",
                    alarmEnabled = viewBinding.checkBox.isChecked
                ))
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, "Поле ввода заметки пусто", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
}