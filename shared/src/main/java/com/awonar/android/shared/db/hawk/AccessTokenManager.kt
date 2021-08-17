package com.awonar.android.shared.db.hawk

import com.awonar.android.model.Auth
import com.awonar.android.shared.utils.HawkUtil
import javax.inject.Inject

class AccessTokenManager @Inject constructor(var hawk: HawkUtil) {

    companion object {
        const val ACCESS_TOKEN_DATABASE_EXPIRES_IN =
            "com.awonar.android.shared.db.hawk.accesstoken.expires.in"
        const val ACCESS_TOKEN_DATABASE_ACCESS_TOKEN =
            "com.awonar.android.shared.db.hawk.accesstoken.access"
        const val ACCESS_TOKEN_DATABASE_REFRESH_TOKEN =
            "com.awonar.android.shared.db.hawk.accesstoken.refresh"
    }

    fun save(auth: Auth) {
        hawk.put(ACCESS_TOKEN_DATABASE_EXPIRES_IN, auth.expiresIn)
        hawk.put(ACCESS_TOKEN_DATABASE_ACCESS_TOKEN, auth.accessToken)
        hawk.put(ACCESS_TOKEN_DATABASE_REFRESH_TOKEN, auth.refreshToken)
    }

    fun load() = Auth(
        accessToken = getAccessToken(),
        expiresIn = getExpiresIn(),
        refreshToken = getRefreshToken()
    )

    fun getExpiresIn(): Int = hawk.get(ACCESS_TOKEN_DATABASE_EXPIRES_IN, 0)
    fun getAccessToken(): String? = hawk.get(ACCESS_TOKEN_DATABASE_ACCESS_TOKEN)
    fun getRefreshToken(): String? = hawk.get(ACCESS_TOKEN_DATABASE_REFRESH_TOKEN)

    fun clearToken() {
        hawk.delete(ACCESS_TOKEN_DATABASE_ACCESS_TOKEN)
        hawk.delete(ACCESS_TOKEN_DATABASE_REFRESH_TOKEN)
        hawk.delete(ACCESS_TOKEN_DATABASE_EXPIRES_IN)
    }

    fun isAuthenticated(): Boolean = load().accessToken != null || !load().isExpireToken()
}
