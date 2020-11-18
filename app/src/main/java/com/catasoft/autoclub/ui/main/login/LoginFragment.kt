package com.catasoft.autoclub.ui.main.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.LoginFragmentBinding
import com.catasoft.autoclub.model.User
import com.catasoft.autoclub.repository.State
import com.catasoft.autoclub.repository.remote.users.UsersRepository
import com.catasoft.autoclub.ui.BaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import timber.log.Timber


class LoginFragment : BaseFragment() {

    private lateinit var binding: LoginFragmentBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("MAIN2")

        firebaseAuth = Firebase.auth

        //TODO: Adaugare ViewModel pentru call-urile la repository
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = LoginFragmentBinding.inflate(layoutInflater)
        val rootView = binding.root

        //lifecycle setters
        binding.lifecycleOwner = this


        //Marire buton Sign In - WIDE
        binding.signInButton.setSize(SignInButton.SIZE_WIDE)


        // Varianta mai putin eleganta
        // Ar fi trebuit specificat in activity_login.xml sub forma de data binding
        // android:onClick="@{view -> viewModel.onSignInButtonClick(view)}"
        // insa nu merge (nu se genereaza clasa de binding)
        // Poate fi vazuta si ca o practica buna pentru ca listenerul de click face
        // parte tot din UI
        binding.signInButton.setOnClickListener {
            Timber.e("Click sign in button")
            launchSignInFlow()
        }

        return rootView
    }


    private val startSignInForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            Timber.e("%s %s ", result.data.toString(), result.resultCode.toString())
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    Timber.e("firebaseAuthWithGoogle:%s", account?.id)
                    val uiScope = CoroutineScope(Dispatchers.IO)
                    val credential = GoogleAuthProvider.getCredential(account?.idToken!!, null)
                    uiScope.launch {
                        kotlin.runCatching {
                            firebaseAuth.signInWithCredential(credential).await()
                        }.onSuccess {
                            // Sign in success, update UI with the signed-in user's information
                            Timber.e("signInWithCredential:success")
                            val user = firebaseAuth.currentUser
                            updateUI(user)
                        }.onFailure {
                            // If sign in fails, display a message to the user.
                            Timber.e("signInWithCredential:failure")
                            Snackbar.make(binding.root, R.string.login_fail, Snackbar.LENGTH_SHORT)
                                .show()
                            updateUI(null)
                        }
                    }
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Timber.e("Google sign in failed")
                    // ...
                }
            }
        }

    private fun loginSnackbarDismissed(){
        //TODO: Daca user-ul nu este inregistrat, porneste procesul de inregistrare
        if(firebaseAuth.currentUser != null){

            val usersRepository = UsersRepository()

            CoroutineScope(Dispatchers.IO).launch {
                usersRepository.getAllUsers().collect { state ->
                    when(state){
                        is State.Loading -> {
                            Timber.e("Loading...")
                        }
                        is State.Success -> {
                            Timber.e("Date: ")
                            Timber.e(state.data.toString())
                        }
                        is State.Failed -> {
                            Timber.e("Eroare GET1%s", state.message)
                        }
                    }
                }
            }

            //Verifica daca e cont nou

            //Altfel...
            //Revenire la activitatea de baza
            val returnIntent = Intent()
            Timber.e("Sent firebaseid: %s", firebaseAuth.uid)
            returnIntent.putExtra("firebase_account", firebaseAuth.uid)
            activity?.setResult(Activity.RESULT_OK, returnIntent)
            activity?.finish()
        }
        val navController = findNavController()
    }

    private fun updateUI(user: FirebaseUser?){
        val text: String =
            if(user != null)
                String.format(resources.getString(R.string.login_success), user.displayName)
            else
                resources.getString(R.string.login_fail)

        //Disable google login button to not be used again if user logged successfully
        // and while Snackbar is displayed
        if(user != null)
            binding.signInButton.isClickable = false

        val snackbar = Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT)
            .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    loginSnackbarDismissed()
                }
            })
        snackbar.show()
    }

    /*private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.e("signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.e("signInWithCredential:failure")
                    Snackbar.make(binding.root, R.string.login_fail, Snackbar.LENGTH_SHORT)
                        .show()
                    updateUI(null)
                }
            }
    }*/


    private fun launchSignInFlow() {
        Timber.e("Clientid: %s", resources.getString(R.string.default_web_client_id))
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resources.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        Timber.e(signInIntent.toString())
        startSignInForResult.launch(signInIntent)
    }

}
