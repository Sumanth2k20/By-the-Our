package com.bytheour.useCases

import com.bytheour.data.response.LoginNewResponse
import com.bytheour.utils.BEARER_TOKEN
import com.bytheour.utils.DEFAULT_ADDRESS
import com.bytheour.utils.KEY_LAT
import com.bytheour.utils.KEY_LNG
import com.bytheour.utils.NEW_BOOKING_ID
import com.bytheour.utils.PAYMENT_METHOD
import com.bytheour.utils.TYPE_CUSTOMER
import com.bytheour.utils.USER_INFO
import com.bytheour.utils.VERIFICATION_FIREBASE_ID
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json

class SharedPreferenceUseCase(private val settings: Settings) {


    fun putToken(token: String){
        settings.putString(BEARER_TOKEN, token)
    }

    fun getToken(): String? {
        return settings.getString(BEARER_TOKEN,"")
    }

    fun putUser(usertype:String){
        settings.putString(TYPE_CUSTOMER,usertype)
    }

    fun getUser():String{
        return settings.getString(TYPE_CUSTOMER,"")
    }

    fun putVerificationId(id: String){
        settings.putString(VERIFICATION_FIREBASE_ID, id)
    }

    fun getVerificationId():String?{
        return settings.getString(VERIFICATION_FIREBASE_ID, "")
    }

    fun putDefaultAddress(address:String){
        settings.putString(DEFAULT_ADDRESS, address)
    }
    fun getDefaultAddress():String{
        return settings.getString(DEFAULT_ADDRESS, "")
    }


    fun clearSharedPrefs(){
        settings.clear()
        println("settings------------------${getToken()}")
    }

//    fun putUserName(name:String){
//        return settings.putString(USER_NAME, name)
//    }
//
//    fun putUserProfile(Image:String){
//        return settings.putString(USER_PROFILE, Image)
//
//    }
//
//    fun getUseName():String{
//        return settings.getString(USER_NAME, "")
//    }
//
//    fun getUserProfile():String{
//        return settings.getString(USER_PROFILE, "")
//    }
    fun putIsAdmin(isAdmin: Boolean){
        settings.putBoolean("isAdmin", isAdmin)
    }

    fun getIsAdmin():Boolean{
        return settings.getBoolean("isAdmin", false)
    }

    fun putProfileInfo(isIncomplete: Boolean) {
        val currentUserInfo = getUserInfo()
        currentUserInfo?.flags?.profileIncomplete = isIncomplete
        val json = Json.encodeToString(currentUserInfo)
        settings.putString(USER_INFO, json)
    }

    fun putUserInfo(userInfo: LoginNewResponse.Data) {
        val json = Json.encodeToString(userInfo)
        settings.putString(USER_INFO, json)
    }

    fun getUserInfo(): LoginNewResponse.Data? {
        val json = settings.getString(USER_INFO,"{}")
        return json.let {
            try {
                Json.decodeFromString<LoginNewResponse.Data>(it)
            } catch (e: Exception) {
                null
            }
        }
    }
    fun getAvilabilityStatus():Boolean{
        return settings.getBoolean("Avilability", false)
    }
    fun putAvilabilityStatus(notify:Boolean){
         settings.putBoolean("Avilability", notify)
    }
    fun getNotificationStatus():Boolean{
        return settings.getBoolean("Notification", false)
    }
    fun putNotificationStatus(notify:Boolean){
         settings.putBoolean("Notification", notify)
    }

    fun putPaymentMethod(paymentMethod: String){
        settings.putString(PAYMENT_METHOD, paymentMethod)
    }

    fun getPaymentMethod(): String{
        return settings.getString(PAYMENT_METHOD, "")
    }
    fun putLocation(lat: Double, lng: Double) {
        settings.putString(KEY_LAT, lat.toString())
        settings.putString(KEY_LNG, lng.toString())
    }

    fun getLat(): Double {
        return settings.getString(KEY_LAT, "0.0").toDouble()
    }

    fun getLng(): Double {
        return settings.getString(KEY_LNG, "0.0").toDouble()
    }
//
//    fun getLocation(): Pair<Double, Double> {
//        val lat = settings.getString(KEY_LAT, "0.0").toDouble()
//        val lng = settings.getString(KEY_LNG, "0.0").toDouble()
//        return Pair(lat, lng)
//    }

    fun putNewBookingId(id :String){
        settings.putString(NEW_BOOKING_ID,id)
    }

    fun getNewBookingId():String{
        return settings.getString(NEW_BOOKING_ID, "")
    }

}