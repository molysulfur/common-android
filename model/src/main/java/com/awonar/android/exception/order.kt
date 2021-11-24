package com.awonar.android.exception

class PositionExposureException constructor(val errorMessage: String, val value: Float) :
    Exception(errorMessage) {
}


class ValidationException constructor(val errorMessage: String, val value: Float) :
    Exception(errorMessage) {
}