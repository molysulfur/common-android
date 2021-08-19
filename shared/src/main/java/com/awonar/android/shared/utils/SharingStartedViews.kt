package com.awonar.android.shared.utils

import kotlinx.coroutines.flow.SharingStarted

val WhileViewSubscribed: SharingStarted = SharingStarted.WhileSubscribed(5000)
