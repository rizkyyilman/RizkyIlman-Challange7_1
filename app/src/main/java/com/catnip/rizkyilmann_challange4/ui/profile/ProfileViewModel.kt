package com.catnip.rizkyilmann_challange4.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.rizkyilmann_challange4.data.repository.UserRepository
import com.catnip.rizkyilmann_challange4.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    /* private val _loginResult = MutableLiveData<ResultWrapper<Boolean>>()
    val loginResult: LiveData<ResultWrapper<Boolean>>
        get() = _loginResult */

 /* fun doLogin(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.doLogin(email, password).collect {
                _loginResult.postValue(it)
            }
        }
    }*/

    fun isUserLoggedIn() = repository.isLoggedIn()
    fun getCurrentUser() = repository.getCurrentUser()

    private val _changeProfileResult = MutableLiveData<ResultWrapper<Boolean>>()
    val changeProfileResult: LiveData<ResultWrapper<Boolean>>
        get() = _changeProfileResult

    fun updateFullName(fullName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProfile(fullName = fullName).collect {
                _changeProfileResult.postValue(it)
            }
        }
    }

    fun createChangePwdRequest() {
        repository.sendChangePasswordRequestByEmail()
    }

    fun doLogout() {
        repository.doLogout()
    }
}
