package com.awonar.android.exception

class ValidationAmountCopyException constructor(val errorMessage: String, val value: Float) :
    Exception(errorMessage) {
}

class ValidationStopLossCopyException constructor(val errorMessage: String, val value: Float) :
    Exception(errorMessage) {
}

class UpdateFundException constructor(val errorMessage: String, val value: Float) :
    Exception(errorMessage) {
}
