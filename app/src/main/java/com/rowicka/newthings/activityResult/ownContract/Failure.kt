package com.rowicka.newthings.activityResult.ownContract

sealed class Failure {
    object ResultNoOK : Failure()
    object NoPassData : Failure()
}
