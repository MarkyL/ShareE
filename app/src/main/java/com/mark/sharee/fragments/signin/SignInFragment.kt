package com.mark.sharee.fragments.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sharee.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.navigation.arguments.TransferInfo
import com.mark.sharee.screens.MainScreen
import kotlinx.android.synthetic.main.fragment_sign_in.*
import timber.log.Timber
import java.util.concurrent.TimeUnit


class SignInFragment : ShareeFragment() {

    companion object {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        generateOtpBtn.setOnClickListener {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumberET.text.toString(), // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                requireActivity(), // Activity (for callback binding)
                callbacks
            ) // OnVerificationStateChangedCallbacks
        }

        signInBtn.setOnClickListener {
            signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(storedVerificationId, otpET.text.toString()))
        }

    }

    val auth = FirebaseAuth.getInstance()
    var storedVerificationId = ""

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Timber.i("onVerificationCompleted:$credential")


            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Timber.w("onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

            Toast.makeText(context, "onVerificationFailed", Toast.LENGTH_LONG).show()
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
                    Timber.d(
                        "signInWithCredential:success user = $task.result?.user")

                    val user = task.result?.user
                    // ...
                    val transferInfo = TransferInfo()
                    transferInfo.phoneNumber = user?.phoneNumber.toString()
                    navigator.add(MainScreen(transferInfo))
                } else {
                    // Sign in failed, display a message and update the UI
                    Timber.w("signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }
}
