package com.awonar.android.exception

class PositionExposureException constructor(val errorMessage: String, val value: Float) :
    Exception(errorMessage) {
}


class ValidationException constructor(val errorMessage: String, val value: Float) :
    Exception(errorMessage) {
}

class AvailableNotEnoughException(val errorMessage: String) : Exception(errorMessage)

class AddAmountException(val errorMessage: String,val value: Float) : Exception(errorMessage)

class RefundException(val errorMessage: String,val value: Float) : Exception(errorMessage)