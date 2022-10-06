package com.android.secondhand.login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.secondhand.R
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val EMAILID = "EmailId"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateUserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var emailId: String? = null
    lateinit var editTextPassword: EditText
    lateinit var clickListener: ClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            emailId = it.getString(EMAILID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_create_user, container, false)

        val editTextEmail = view.findViewById<EditText>(R.id.edit_text_email)

        editTextEmail.setText(emailId)

        editTextPassword = view.findViewById(R.id.edit_text_password)
        val createAccountButton: Button = view.findViewById(R.id.signUpButton)

        createAccountButton.setOnClickListener{ onClickCreateAccountButton() }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ClickListener) {
            clickListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement ClickListener")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            CreateUserFragment().apply {
                arguments = Bundle().apply {
                    putString(EMAILID, param1)
                }
            }
    }

    fun onClickCreateAccountButton() {
        //check password for strength
        val password = editTextPassword.text.toString()

        if(password.length < 6) {
            Toast.makeText(activity,"Password length must be more than 5.", Toast.LENGTH_SHORT).show()
            return
        }
        clickListener.onCreateAccount(password)
    }

    interface ClickListener {
        fun onCreateAccount(password: String)
    }
}