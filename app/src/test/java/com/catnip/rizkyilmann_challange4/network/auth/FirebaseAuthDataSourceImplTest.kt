package com.catnip.rizkyilmann_challange4.network.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import io.mockk.mockkStatic
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FirebaseAuthDataSourceImplTest {

    @MockK
    lateinit var mockFirebaseAuth: FirebaseAuth
    private lateinit var mockFirebaseUser: FirebaseUser
    private lateinit var authDataSource: FirebaseAuthDataSourceImpl

    @Before
    fun setUp() {
        mockkObject(Firebase)
        every { Firebase.auth } returns mockFirebaseAuth

        authDataSource = FirebaseAuthDataSourceImpl(mockFirebaseAuth)
    }


}
