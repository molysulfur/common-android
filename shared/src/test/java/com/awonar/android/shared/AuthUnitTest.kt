package com.awonar.android.shared

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.awonar.android.model.Auth
import com.awonar.android.model.SignInRequest
import com.awonar.android.shared.domain.auth.SignInWithPasswordUseCase
import com.awonar.android.shared.repos.AuthRepository
import com.molysulfur.library.result.Result
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class AuthUnitTest {

//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @get:Rule
//    var coroutineRule = MainCoroutineRule()
//
//    private val testDispatcher = coroutineRule.testDispatcher
//
//    @Before
//    fun setUp() {
//        MockitoAnnotations.openMocks(this)
//    }
//
//    @Test
//    fun testUpcomingState() = testDispatcher.runBlockingTest {
//        val useCase = SignInWithPasswordUseCase(FakeRepository(), testDispatcher)
//        val data = SignInRequest("", "")
//        val expect = Auth("", 0, "")
//        val auth = useCase.invoke(data)
//        println(auth)
//        assertEquals(
//            Result.Success(expect),
//            auth
//        )
//    }
//
//    internal class FakeRepository : AuthRepository {
//
//        override fun signInWithPassword(request: SignInRequest): Auth = Auth("", 0, "")
//
//    }

}