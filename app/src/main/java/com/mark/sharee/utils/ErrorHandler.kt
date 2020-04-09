package com.mark.sharee.utils

import androidx.fragment.app.Fragment
import com.example.sharee.R
import com.google.gson.Gson
import com.mark.sharee.dialogs.ShareeDialog
import com.mark.sharee.navigation.ActivityNavigator
import com.mark.sharee.network.adapter.ServerException
import org.koin.core.KoinComponent
import retrofit2.HttpException
import timber.log.Timber

class ErrorHandler(val navigator: ActivityNavigator): KoinComponent {

    fun handleError(fragment: Fragment, throwable: Throwable) {
        val gson = Gson()
        Timber.i("throwable ")
        when (throwable) {
            is HttpException -> {
                throwable.response()?.errorBody()?.let {
                    val serverException = gson.fromJson(it.string(), ServerException::class.java)
                    handleHttpException(fragment, serverException)
                }
            }
            else -> {
                Timber.e("ErrorHandler - Unknown handled error of type : ${throwable.javaClass.canonicalName}")
                Toaster.show(fragment, "Error - ${throwable.javaClass.simpleName}")
            }
        }
    }

    private fun handleHttpException(fragment: Fragment, serverException: ServerException) {
        Timber.e("handleHttpException - $serverException")
        val dialog = ShareeDialog(
            title = fragment.resources.getString(R.string.dialog_error_default_title),
            subtitle = serverException.messageToClient,
            iconDrawable = R.drawable.ic_warning_black,
            positiveButtonText = R.string.dialog_positive_ok)

        dialog.show(fragment.parentFragmentManager, ShareeDialog.TAG)
    }

}