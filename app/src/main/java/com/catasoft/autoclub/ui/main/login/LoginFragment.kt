package com.catasoft.autoclub.ui.main.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catasoft.autoclub.R
import com.catasoft.autoclub.StartActivity
import com.catasoft.autoclub.databinding.FragmentLoginBinding
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.ui.BaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        val rootView = binding.root

        Timber.e("Login fragment!")

        //lifecycle setters
        binding.lifecycleOwner = this


        //Marire buton Sign In - WIDE
        binding.googleSignInButton.setSize(SignInButton.SIZE_WIDE)


        // Varianta mai putin eleganta
        // Ar fi trebuit specificat in activity_login.xml sub forma de data binding
        // android:onClick="@{view -> viewModel.onSignInButtonClick(view)}"
        // insa nu merge (nu se genereaza clasa de binding)
        // Poate fi vazuta si ca o practica buna pentru ca listenerul de click face
        // parte tot din UI
        binding.googleSignInButton.setOnClickListener {
            Timber.e("Click sign in button")
            //Disable google login button to not be used again if user logged successfully
            // and while Snackbar is displayed
            binding.googleSignInButton.isClickable = false
            launchGoogleSignInFlow()
        }

        binding.btnLocalLogin.setOnClickListener {
            val email = binding.emailInputLayout.editText?.text?.toString()
            val password = binding.passwordInputLayout.editText?.text?.toString()

            if(email?.isEmpty() != false || password?.isEmpty() != false){
                Snackbar.make(binding.root, "Introduceți email-ul și parola mai întâi!", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val auth = Firebase.auth
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.e("signInWithEmail:success")
                        activity?.setResult(StartActivity.RESULT_LOGIN_OK)
                        activity?.finish()
                    } else {
                        Timber.e("signInWithEmail:failed")
                        Snackbar.make(binding.root, "Email-ul și/sau parola greșite!", Snackbar.LENGTH_SHORT).show()
                    }
                }
        }

        binding.btnRegister.setOnClickListener{
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterNewAccountFragment()
            findNavController().navigate(action)
        }

        viewModel.loginState.observe(viewLifecycleOwner, {
            when(it){
                is LoginState.Registered -> {
                    Timber.e("firebaseid: %s", it.user?.uid)
                    activity?.setResult(StartActivity.RESULT_LOGIN_OK)
                    activity?.finish()
                }
                is LoginState.FetchError -> {
                    showFailedLogin()
                    Timber.e("Login failed")
                }
                is LoginState.NotRegistered -> {
                    Timber.e("INCEPE PROCESUL DE AUTENTIFICARE")
                    val navController = findNavController()
                    navController.navigate(R.id.action_loginFragment_to_registerProfileActivity)
                }
            }
        })

        return rootView
    }

    private val startGoogleSignInForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            Timber.e("%s %s ", result.data.toString(), result.resultCode.toString())
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.signInWithGoogle(result.data)
            }
        }

    private fun showFailedLogin() =
        Snackbar.make(binding.root, resources.getString(R.string.login_fail), Snackbar.LENGTH_SHORT).show()


    private fun launchGoogleSignInFlow() {
        Timber.e("Clientid: %s", resources.getString(R.string.default_web_client_id))
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resources.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        Timber.e(signInIntent.toString())
        startGoogleSignInForResult.launch(signInIntent)
    }
}
