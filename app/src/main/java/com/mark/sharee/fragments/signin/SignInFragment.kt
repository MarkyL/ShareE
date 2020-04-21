package com.mark.sharee.fragments.signin

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.example.sharee.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.gson.Gson
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.model.User
import com.mark.sharee.navigation.arguments.TransferInfo
import com.mark.sharee.network.adapter.ServerException
import com.mark.sharee.screens.MainScreen
import com.mark.sharee.utils.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import retrofit2.HttpException
import timber.log.Timber
import java.util.concurrent.TimeUnit


class SignInFragment : ShareeFragment() {

    private val viewModel: SignInViewModel by sharedViewModel()

    private val auth = FirebaseAuth.getInstance()
    var credential: PhoneAuthCredential? = null

    var storedVerificationId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.i("SignInFragment onViewCreated")

        phoneNumberET.setOnEditorActionListener(onKeyboardDoneListener)
        phoneNumberET.addTextChangedListener(object : BaseTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                isPhoneNumberValid(phoneNumberET.text.toString())
            }
        })

        //TEST DATA for work phone//
        phoneNumberET.setText("0529426921") // test phone
//        phoneNumberET.setText("0549409575") // mark's phone

        //TEST DATA for work phone//

        registerViewModel()

        generateOtpBtn.setOnClickListener { onRequestVerifyPhoneNumber(phoneNumberET.text.toString()) }

        signInBtn.setOnClickListener { onSignInBtnClick() }

        attemptAutoLogin()
    }

    private fun attemptAutoLogin() {
//        val phone = User.me()?.phoneNumber
        var isSplashNeeded = false
        User.me()?.getPhone()?.let {
            if (it.isNotEmpty()) {
                isSplashNeeded = true
                onRequestVerifyPhoneNumber(it)
            }
        }
        if (!isSplashNeeded) {
            splashView.visibility = View.GONE
        }

    }

    private fun onSignInBtnClick() {
        Timber.i("mark - signInBtn click")
        credential?.let { signInWithPhoneAuthCredential(it) } ?: run {
            if (storedVerificationId != StringUtils.EMPTY_STRING && otpET.text != null) {
                signInWithPhoneAuthCredential(
                    PhoneAuthProvider.getCredential(
                        storedVerificationId,
                        otpET.text.toString()
                    )
                )
            }
        }
    }

    private fun onRequestVerifyPhoneNumber(number: String) {
        progressBar.visibility = View.VISIBLE
        val e164PhoneNumber = PhoneNumber.create(number)?.e164()
        if (e164PhoneNumber.isNullOrEmpty()) {
            Timber.e("onRequestVerifyPhoneNumber() - invalid phone number - $number")
            //TODO("onRequestVerifyPhoneNumber invalid phone number flow handling.")
            return
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            e164PhoneNumber, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            requireActivity(), // Activity (for callback binding)
            verificationStateCallback
        ) // OnVerificationStateChangedCallbacks
    }

    fun toggleBtn(isEnabled: Boolean) {
        signInBtn.isEnabled = isEnabled
    }

    private fun registerViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            val dataState = it ?: return@Observer
            progressBar.visibility = if (dataState.showProgress) View.VISIBLE else View.GONE
            if (dataState.response != null && !dataState.response.consumed) {
                dataState.response.consume()?.let { response ->
                    Toast.makeText(context, response.verificationToken, Toast.LENGTH_LONG).show()
                    Timber.i("login success, verificationToken = ${response.verificationToken}")

                    val transferInfo = TransferInfo()
                    transferInfo.phoneNumber = phoneNumberET.text.toString()
                    navigator.replace(MainScreen(transferInfo), false)
                }
            }
            if (dataState.error != null && !dataState.error.consumed) {
                dataState.error.consume()?.let { error ->
                    // handle error state
                    splashView.visibility = View.GONE
                    errorHandler.handleError(this, error)
                }
            }

        })
    }

    private val verificationStateCallback =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Timber.i("onVerificationCompleted:$credential")
                progressBar.visibility = View.GONE

                this@SignInFragment.credential = credential
                toggleBtn(true)
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Timber.w("onVerificationFailed", e)
                progressBar.visibility = View.GONE

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                Toast.makeText(context, "onVerificationFailed", Toast.LENGTH_LONG).show()
                splashView.visibility = View.GONE
                // Show a message and update the UI
                // ...
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Timber.i("onCodeSent:$verificationId, token:$token")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
//            resendToken = token

                // ...
            }


        }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInWithCredential:success user = $task.result?.user")

                    val user = task.result?.user
                    // ...
                    login(user)
                } else {
                    // Sign in failed, display a message and update the UI
                    Timber.w("signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }

    private fun login(user: FirebaseUser?) {
        user?.let {
            it.phoneNumber?.let { number ->
                val formattedNumber = PhoneNumber.create(number).toString().replace("-", "")
                viewModel.dispatchInputEvent(Login(formattedNumber, it.uid))
            }
        }
    }

    private val onKeyboardDoneListener = object : OnKeyboardActionListener(KeyboardAction.Done) {
        override fun onEditorAction(): Boolean {
            Timber.i("onEditorAction - ")
            val phoneNumber = phoneNumberET.text.toString()
            if (isPhoneNumberValid(phoneNumber)) {
                onRequestVerifyPhoneNumber(phoneNumber)
            }
            return false
        }
    }

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        val isEmpty = phoneNumber.isEmpty()
        val isValidPrefix = isValidPrefix(phoneNumber)
        val isValid = isValidPrefix && phoneNumber.length == PHONE_NUMBER_LENGTH
        val showPrefixError = !isEmpty && !isValidPrefix
        generateOtpBtn.isEnabled = isValid
        phoneNumberLayout.error = if (showPrefixError) getString(R.string.enter_phone_number_check_prefix_error) else ""
        phoneNumberLayout.isErrorEnabled = showPrefixError
        return isValid
    }

    private fun isValidPrefix(phoneNumber: String): Boolean {
        return phoneNumber == "0" || phoneNumber.startsWith("05")
    }

    companion object {
        private val PHONE_NUMBER_LENGTH = 10
    }
}
