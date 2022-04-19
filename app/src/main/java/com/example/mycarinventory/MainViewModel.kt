package com.example.mycarinventory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycarinventory.dto.Part
import com.example.mycarinventory.dto.SpecificCarPart
import com.example.mycarinventory.service.IPartService
import com.example.mycarinventory.service.PartService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.launch

class MainViewModel(var partService : IPartService = PartService()) : ViewModel() {

    var parts : MutableLiveData<List<Part>> = MutableLiveData<List<Part>>()
    var specifiedCarPart: MutableLiveData<List<SpecificCarPart>> = MutableLiveData<List<SpecificCarPart>>()

    private lateinit var firestore : FirebaseFirestore

    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToParts()
    }

    private fun listenToParts() {
        firestore.collection("specifiedCarParts").addSnapshotListener{
            snapshot, e->
            //handle existing errors
            if(e != null){
                Log.w("Listen Failed", e)
                return@addSnapshotListener
            }
            //No error if reached here
            snapshot?.let{
                val allParts = ArrayList<SpecificCarPart>()
                val documents = snapshot.documents
                documents.forEach {
                    val specificCarPart = it.toObject(SpecificCarPart::class.java)
                    specificCarPart?.let {
                        allParts.add(it)
                    }
                }
                specifiedCarPart.value = allParts
            }
        }
    }

    fun fetchParts() {
        viewModelScope.launch{
            var innerParts = partService.fetchParts()
            parts.postValue(innerParts)
        }
    }

    fun save(specificCarPart: SpecificCarPart) {
        val document = if(specificCarPart.partId == null || specificCarPart.partId.isEmpty()){
            //create new
            firestore.collection("specifiedCarParts").document()}
        else {
            //update
            firestore.collection("specifiedCarParts").document(specificCarPart.partId.toString())
        }
        //specificCarPart.partId = document.id
        val handle = document.set(specificCarPart)
        handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
        handle.addOnFailureListener { Log.d("Firebase", "Document Save Failed $it ") }
    }
}

private fun Int.isEmpty(): Boolean {

    return false
}
