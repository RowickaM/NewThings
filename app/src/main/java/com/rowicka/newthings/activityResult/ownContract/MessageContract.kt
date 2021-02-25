package com.rowicka.newthings.activityResult.ownContract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.rowicka.newthings.activityResult.GET_MESSAGE

class MessageContract : ActivityResultContract<Intent, Either<Failure, String>>() {
    override fun createIntent(context: Context, input: Intent): Intent {
        return input
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Either<Failure, String> {
        if (resultCode != Activity.RESULT_OK) {
            return Either.Left(Failure.ResultNoOK)
        }

        return intent?.getStringExtra(GET_MESSAGE)?.let {
            Either.Right(it)
        } ?: let { Either.Left(Failure.NoPassData) }
    }

}