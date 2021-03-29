package com.molysulfur.library.exception

import android.accounts.NetworkErrorException

class NetworkBoundResourceException(errorMessage: String?) : NetworkErrorException(errorMessage)