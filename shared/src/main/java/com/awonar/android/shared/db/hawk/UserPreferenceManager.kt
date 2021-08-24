package com.awonar.android.shared.db.hawk

import com.awonar.android.model.user.User
import com.awonar.android.shared.utils.HawkUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferenceManager @Inject constructor(
    private val hawk: HawkUtil
) {
    companion object {
        const val USER_INFO_PREFERENCE = "com.awonar.android.shared.db.hawk.hawk.user_info"
    }

    fun save(user: User) {
        hawk.put(USER_INFO_PREFERENCE, user)
    }

    fun get(): User? = hawk.get<User>(USER_INFO_PREFERENCE)

    fun clear() {
        hawk.delete(USER_INFO_PREFERENCE)
    }

}
