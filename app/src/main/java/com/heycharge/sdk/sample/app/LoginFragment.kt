package com.heycharge.sdk.sample.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment() {

    private lateinit var loginButton: Button
    private lateinit var loginUserIdEditText: AppCompatEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        loginButton = view.findViewById(R.id.loginButton)
        loginUserIdEditText = view.findViewById(R.id.loginUserIdEditText)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton.setOnClickListener {
            val text = loginUserIdEditText.text.toString().trim()
            if (text.isNotEmpty()) {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToHomeFragment(
                        text
                    )
                )
            }
        }
    }
}