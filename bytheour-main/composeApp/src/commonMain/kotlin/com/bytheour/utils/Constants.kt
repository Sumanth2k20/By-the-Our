package com.bytheour.utils

var TERMS_AND_CONDITIONS_WEB_VIEW_URL = "http://3.13.228.170/terms-and-conditions"
var PRIVACY_POLICY_WEB_VIEW_URL = "http://3.13.228.170/privacy-policy"
const val INFO_URL = "info@bytheour.com"



const val TYPE_GENERAL = "general"
const val TYPE_PROVIDER = "worker"
const val TYPE_CUSTOMER = "user"
const val API_VERSION = "/v1/"
const val PAGINATION_LIMIT = 10
const val STRIPE_VERSION = "2020-03-02"
const val PAGINATION_LIMIT_30 = "30"
const val PAGINATION_LIMIT_15 = 15
const val PENDING = "Pending"
const val ONGOING = "Ongoing"
const val UPCOMING = "Upcoming"
const val COMPLETED = "Completed"
const val Cancelled = "Cancelled"

const val HOUR = "hour"
const val FIXED = "fixed"

const val BEARER_TOKEN = "bearer_token"
const val VERIFICATION_FIREBASE_ID = "verification_firebase_id"

const val CANCEL_AFTER_START_TIME = "n"

// change the country also when you change the country code as country is used for registration
const val COUNTRY_CODE = "1"
const val COUNTRY = "US"
const val REFERRAL_CODE = ""
const val SOLE_PROPRIETOR = "sole_proprietor"
const val COMPANY_LLC_TITLE = "company_llc_title"

const val service_amount = "50% of the service amount"
const val Decline = "Decline"

const val API_KEY = "AIzaSyAz2rBfVsFoshnKwb0ZP4VOZOaKS5QZPzM"

const val DEFAULT_ADDRESS = "default_address"
const val USER_INFO = "user_info"
const val NEW_BOOKING_ID = "1"



//const val PUBLISHABLE_KEY= "pk_test_51JZG2ZJ0AnXOXIEO2toOOUGa1iQjDfVHjFABUW1tewfMuXVS2ErpsRtIUBXemg3EFFAU4XV2usqk9gaP1dMECFeD00gbGNggIE"

const val PUBLISHABLE_KEY = "pk_live_51JZG2ZJ0AnXOXIEO9i1QltSDSIDIibqTdUVinUqFKwhE8xBqu04EQRKxzLIQ3KSwaWLier8usXObVLks0fgh2MJW00sSprKmT0"


const val PAYMENT_METHOD = "payment_method"

object Strings {
    const val MEDIATION_MSG = "%1\$s Mediation charges will be deducted from the refund amount"
    const val MEDIATION_TITLE_APPROVED = "Refunded %s, which will be credited with in 7 working days"
    const val MEDIATION_TITLE_REQUESTED = "Mediation request raised - %s"
    const val MEDIATION_DESCRIPTION_APPROVED = "Provider has been accepted your mediation."
    const val MEDIATION_DESCRIPTION_REQUESTED = "Mediation has been initiated for the amount of %s, citing concerns regarding the quality of service received."
    const val MEDIATION_ACCEPTED_PROVIDER= "You have accepted the mediation to refund %s of the amount "
}

const val MOBILE_MECHANIC = "Mobile Mechanics"
const val MOBILE_MECHANIC_ID = "mQMVsOA211cN"

const val AUTO_DETAILING = "Mobile Auto Detailing"
const val AUTO_DETAILING_ID = "YdOHWRQWsnnw"

const val ROAD_SIDE_ASSISTANCE = "RoadSide Assistance"
const val ROAD_SIDE_ASSISTANCE_Id = "Pj6EmItV1Kwh"



const val EQUIPMENT_RENTALS = "Equipment Rentals"
const val EQUIPMENT_RENTALS_ID = "gq1mNfHcG2tB"

const val FACILITY_RENTALS = "Facility Rentals"
const val FACILITY_RENTALS_ID = "y7a3aEdSNSvX"

const val KEY_LAT = "KEY_LAT"
const val KEY_LNG = "KEY_LNG"

const val INSPECTION = "inspection"

enum class SheetMode { ADD, EDIT }

object AppFeatures {
    const val TRENDING_SERVICES_HOME = false
    const val TRENDING_SERVICES_SEARCH = true
    const val POPULAR_SEARCHES = true
    const val INSPECTION_RATE = false
}