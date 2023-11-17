package com.catnip.rizkyilmann_challange4.ui.cart

import com.catnip.egroceries.tools.getOrAwaitValue
import com.catnip.rizkyilmann_challange4.data.repository.CartRepository
import com.catnip.rizkyilmann_challange4.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.flow
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CartViewModelTest {

    @MockK
    private lateinit var repo: CartRepository

    private lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { repo.getUserCardData() } returns flow {
            emit(
                ResultWrapper.Success(
                    Pair(
                        listOf(
                            mockk(relaxed = true),
                            mockk(relaxed = true)
                        ),
                        127000.0
                    )
                )
            )
        }
        viewModel = spyk(CartViewModel(repo))
        val updateResulMock = flow {
            emit(ResultWrapper.Success(true))
        }
        coEvery { repo.decreaseCart(any()) } returns updateResulMock
        coEvery { repo.increaseCart(any()) } returns updateResulMock
        coEvery { repo.deleteCart(any()) } returns updateResulMock
        coEvery { repo.setCartNotes(any()) } returns updateResulMock
    }

    @Test
    fun`test cart list`() {
        val result = viewModel.cartList.getOrAwaitValue()
        assertEquals(result.payload?.first?.size, 2)
        assertEquals(result.payload?.second, 127000.0)
    }

    @Test
    fun`test decrease cart`() {
        viewModel.decreaseCart(mockk())
        coVerify { repo.decreaseCart(any()) }
    }

    @Test
    fun`test increase cart`() {
        viewModel.increaseCart(mockk())
        coVerify { repo.increaseCart(any()) }
    }

    @Test
    fun`test remove cart`() {
        viewModel.removeCart(mockk())
        coVerify { repo.deleteCart(any()) }
    }

    @Test
    fun`test set cart note`() {
        viewModel.setCartNotes(mockk())
        coVerify { repo.setCartNotes(any()) }
    }
}
