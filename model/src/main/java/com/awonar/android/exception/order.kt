package com.awonar.android.exception

class PositionExposureException constructor(val errorMessage: String, val value: Float) :
    Exception(errorMessage) {
}


class ValidateStopLossException constructor(val errorMessage: String) :
    Exception(errorMessage) {
}