package com.example.mycarinventory

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycarinventory.dto.Part
import com.example.mycarinventory.dto.SpecificCarPart
import com.example.mycarinventory.dto.User
import com.example.mycarinventory.service.IPartService
import com.example.mycarinventory.service.PartService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.launch

class MainViewModel(var partService : IPartService = PartService()) : ViewModel() {

    internal val NEWLY_CREATED_PART = "Newly Created Part"
    var parts : MutableLiveData<List<Part>> = MutableLiveData<List<Part>>()
    var specifiedCarPart: MutableLiveData<List<SpecificCarPart>> = MutableLiveData<List<SpecificCarPart>>()
    var userPickedPart: SpecificCarPart by mutableStateOf(SpecificCarPart())
    var user : User? = null

    private lateinit var firestore : FirebaseFirestore

    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

    }

    fun listenToParts() {
        user?.let { user ->
            firestore.collection("users").document(user.uid).collection("specifiedCarParts").addSnapshotListener {
                    snapshot, e ->
                    //handle existing errors
                    if (e != null) {
                        Log.w("Listen Failed", e)
                        return@addSnapshotListener
                    }
                    //No error if reached here
                    snapshot?.let {
                        val allParts = ArrayList<SpecificCarPart>()
                        allParts.add(SpecificCarPart(thisPartName = "Newly Created Part"))
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
    }

    fun fetchParts() {
        viewModelScope.launch{
            var innerParts = partService.fetchParts()
            parts.postValue(innerParts)
        }
    }

    fun saveNewPart() {
        user?.let {
            user ->
            val document = if (userPickedPart.partId == null || userPickedPart.partId.isEmpty()) {
                //create new
                firestore.collection("users").document(user.uid).collection("specifiedCarParts").document()
            } else {
                //update
                firestore.collection("users").document(user.uid).collection("specifiedCarParts").document(userPickedPart.partId.toString())
            }
            //specificCarPart.partId = document.id
            val handle = document.set(userPickedPart)
            handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
            handle.addOnFailureListener { Log.d("Firebase", "Document Save Failed $it ") }
        }
    }

    fun saveUser() {
        user?.let {
            user ->
            val handle = firestore.collection("users").document(user.uid).set(user)
            handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
            handle.addOnFailureListener { Log.d("Firebase", "Document Save Failed $it ") }
        }
    }
}

private fun Int.isEmpty(): Boolean {

    return false
}
