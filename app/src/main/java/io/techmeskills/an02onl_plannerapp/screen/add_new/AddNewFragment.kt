package io.techmeskills.an02onl_plannerapp.screen.add_new

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
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
    private var calendar = Calendar.getInstance()
    private val alarmReceiver: AlarmReceiver = AlarmReceiver()


    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setPadding(0, top, 0, 0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = PendingIntent.getBroadcast(context, 0, Intent(context, AlarmReceiver::class.java), 0)
        val currentDate = Date()
        val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val dateText: String = dateFormat.format(currentDate)
        viewBinding.editData.setText(dateText)
        viewBinding.checkBoxNatification.setOnCheckedChangeListener { buttonView, isChecked ->
            viewBinding.pickTimeBtn.isVisible = isChecked
        }
        viewBinding.pickTimeBtn.setOnClickListener {
            calendar = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                viewBinding.pickTimeBtn.text = SimpleDateFormat("HH:mm").format(calendar.time)
            }
            TimePickerDialog(context,
                timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true).show()
        }
        viewBinding.btnAddNew.setOnClickListener {
            if (viewBinding.editNewNote.text.isNotBlank()) {
                viewModel.addNewNote(Note(
                    text = viewBinding.editNewNote.text.toString(),
                    date = viewBinding.editData.text.toString(),
                    userName = ""
                ))
                if (viewBinding.checkBoxNatification.isChecked) {
                   //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
                    alarmReceiver.setAlarm(requireContext(),calendar.timeInMillis,viewBinding.editData.text.toString(),
                        viewBinding.editNewNote.text.toString(),)

                }
                findNavController().popBackStack()
            } else {
                val toast =
                    Toast.makeText(context, "Поле ввода заметки пусто", Toast.LENGTH_SHORT)
                toast.show()
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