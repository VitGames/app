package io.techmeskills.an02onl_plannerapp.screen.add_new

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentAddNewBinding
import io.techmeskills.an02onl_plannerapp.screen.main.MainFragment
import io.techmeskills.an02onl_plannerapp.screen.main.MainViewModel
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.setVerticalMargin
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class AddNewFragment : NavigationFragment<FragmentAddNewBinding>(R.layout.fragment_add_new) {
    override val viewBinding: FragmentAddNewBinding by viewBinding()

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setPadding(0, top, 0, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.btnAddNew.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
//            viewBinding.editData.setText(day + month + day)
//            print("LOL$year$month$day")
            if(viewBinding.editNewNote.text.isNotBlank()){
            val bundle = bundleOf(
                "txt_Note" to viewBinding.editNewNote.text.toString(),
                "txt_Data" to viewBinding.editData.text.toString()
            )
            setFragmentResult("requestKey", bundle)
            findNavController().navigate(R.id.action_addNewFragment_to_mainFragment, bundle)
        } else{
                val toast = Toast.makeText(context, "Поле ввода заметки пусто", Toast.LENGTH_SHORT)
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