package com.catasoft.autoclub.ui.main.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.catasoft.autoclub.StartActivity
import com.catasoft.autoclub.databinding.FragmentRegisterNewAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class RegisterNewAccountFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterNewAccountFragment()
    }

    private lateinit var binding: FragmentRegisterNewAccountBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterNewAccountBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun validateEmail(email: String?): Boolean {
        if(email == null)
            return false

        if(email.isEmpty())
            return false

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(passWord: String?): Boolean{
        return passWord?.let { it.length >= 6} == true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val email = binding.emailInputLayout.editText?.text.toString()
            val password = binding.passwordInputLayout.editText?.text.toString()

            var ok = 0

            if(validateEmail(email)){
                ok ++
                binding.emailInputLayout.error = null
            }
            else{
                binding.emailInputLayout.error = "Email-ul nu este valid!"
            }

            if(validatePassword(password)){
                ok ++
                binding.passwordInputLayout.error = null
            }
            else{
                binding.passwordInputLayout.error = "Parola trebuie să aibă minim 6 caractere!"
            }

            if(ok == 2){
                registerWithFirebase(email, password)
            }
        }
    }

    private val startProfileActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            when(it.resultCode){
                RegisterProfileActivity.RESULT_REGISTERED -> {
                    activity?.setResult(StartActivity.RESULT_REGISTER_OK)
                    activity?.finish()
                }
                RegisterProfileActivity.RESULT_NOT_REGISTERED -> {
                    activity?.setResult(StartActivity.RESULT_REGISTER_BAD)
                    activity?.finish()
                }
            }
        }

    private fun registerWithFirebase(email: String, password: String) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.e("createUserWithEmail:success")
                    val user = auth.currentUser

                    val intent = Intent(requireContext(), RegisterProfileActivity::class.java)
                    startProfileActivityForResult.launch(intent)

//                    val action = RegisterNewAccountFragmentDirections.actionRegisterNewAccountFragmentToRegisterProfileActivity()
//                    findNavController().navigate(action)
//                    findNavController().popBackStack()

                } else {
                    // If sign in fails, display a message to the user.
                    Timber.e("createUserWithEmail:failure ${task.exception}")
                    when(task.exception){
                        is FirebaseAuthUserCollisionException -> {
                            Toast.makeText(requireContext(), "Email-ul folosit este deja asociat unui cont existent!",
                                Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(requireContext(), "Înregistrarea a eșuat! Vă rugăm să încercați din nou",
                                Toast.LENGTH_LONG).show()
                        }
                    }

                }
            }
    }

}