package io.techmeskills.an02onl_plannerapp.screen.main

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.database.User
import io.techmeskills.an02onl_plannerapp.databinding.FragmentLoginBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import kotlinx.coroutines.flow.map
import org.koin.android.viewmodel.ext.android.viewModel


class LoginFragment() : NavigationFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    override val viewBinding: FragmentLoginBinding by viewBinding()
    private val viewModel: UsersViewModel by viewModel()
    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedPref = SharedPref(requireContext())
        //TODO add autologin
//        if(sharedPref.userNameFlow.map { it.isNotEmpty() }){
//
//        }
        super.onViewCreated(view, savedInstanceState)

        viewBinding.confirmButton.setOnClickListener {
            val userName = viewBinding.editTxtLogin.text.trim().toString()
            if (userName.isNotBlank()) {
                viewModel.login(userName)
                view.findNavController().navigate(R.id.action_loginFragment_to_splashFragment)
            } else {
                val toast = Toast.makeText(context, "Поле ввода логина пусто", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
}