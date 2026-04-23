package com.ndejje.hostelfix

import com.ndejje.hostelfix.data.local.User
import com.ndejje.hostelfix.data.repository.UserRepository
import com.ndejje.hostelfix.viewmodel.AuthState
import com.ndejje.hostelfix.viewmodel.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: UserRepository
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(UserRepository::class.java)
        viewModel = AuthViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with correct credentials sets success state`() = runTest {
        val user = User(1, "Test User", "test@email.com", "password", "Student")
        `when`(repository.getUserByEmail("test@email.com")).thenReturn(user)

        viewModel.login("test@email.com", "password")
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.authState.value is AuthState.Success)
        assertEquals("Student", (viewModel.authState.value as AuthState.Success).role)
        assertEquals(user, viewModel.currentUser.value)
    }

    @Test
    fun `login with incorrect credentials sets error state`() = runTest {
        `when`(repository.getUserByEmail("wrong@email.com")).thenReturn(null)

        viewModel.login("wrong@email.com", "password")
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.authState.value is AuthState.Error)
    }
}
