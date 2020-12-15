package sheridan.capstone.findmyfarmer.Customer.Model

/**
@author Andrei Constantinescu
 * Sets up the shared view model class
 **/

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sheridan.capstone.findmyfarmer.Entities.Farm
import sheridan.capstone.findmyfarmer.ImageHandler.FirebaseImagehandler


class SharedViewModel : ViewModel() {
// this SharedViewModel class stores data for the clicked recycleview element. This is passed and shared by the activity.

    private val FarmData : MutableLiveData<Farm> = MutableLiveData()
    private val Farmers_Followers : MutableLiveData<Int> = MutableLiveData()
    private val firebaseImagehandler : MutableLiveData<FirebaseImagehandler> = MutableLiveData()


    //Setters
    fun setFarmers_Followers(input:Int){
        Farmers_Followers.value = input
    }
    fun setFarmData(List : Farm){
        FarmData.value = List
    }
    fun setFirebaseImageHandler(firebaseImagehandler: FirebaseImagehandler){
        this.firebaseImagehandler.value = firebaseImagehandler
    }


    //Getters
    fun getFarmers_Followers() : LiveData<Int>{
        return Farmers_Followers
    }
    fun getFarmData() : LiveData<Farm> {
        return FarmData
    }
    fun getImageHandler(): LiveData<FirebaseImagehandler>{
        return firebaseImagehandler
    }



}
