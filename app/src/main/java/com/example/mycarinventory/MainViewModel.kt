package com.example.mycarinventory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycarinventory.dto.Part
import com.example.mycarinventory.service.IPartService
import com.example.mycarinventory.service.PartService
import kotlinx.coroutines.launch

class MainViewModel(var partService : IPartService = PartService()) : ViewModel() {

    var parts : MutableLiveData<List<Part>> = MutableLiveData<List<Part>>()

    fun fetchParts() {
        viewModelScope.launch{
            var innerParts = partService.fetchParts()
            parts.postValue(innerParts)
        }
    }
}