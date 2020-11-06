package com.inas.atroads.services;

public interface APIConstants {


   // String BASE_URL = "http://18.139.34.127:5005/wego/";
   // String BASE_URL = "http://18.138.154.234:5005/atroads/";
    String BASE_URL = "http://13.235.235.58:5005/atroads/";

   // String IMAGE_URL = "http://18.138.154.234/ATROADS/";
    String IMAGE_URL = "http://13.235.235.58/ATROADS/";

    //Login
    String LOGINPage = BASE_URL + "login";

    // Register
    String REGISTERPAGE = BASE_URL + "register";

    //forgot_password
    String ForgotPassword = BASE_URL + "forgot_password";

    //re-send otp
    String resendotp = BASE_URL + "send_otp";

    //verify_otp
    String verify_otp = BASE_URL + "verify_otp";


 //resetpassword
    String resetpassword = BASE_URL + "reset_password";

    //travelling_locations
   // String travelling_locations = "http://18.139.34.127:5005/travelling_locations";
    String travelling_locations = BASE_URL + "travelling_locations";

    //travel_info
    String travel_info = BASE_URL + "travel_info";

    //password_change
    String change_password = BASE_URL + "change_password";

    // user_ride_details
    String user_ride_details = BASE_URL + "get_ride_details_from_users";

    String start_ride = BASE_URL + "start_ride";

    String start_ride_for_paired_users = BASE_URL + "start_ride_for_paired_users";

    String cancel_ride = BASE_URL + "cancel_ride";

    String end_ride = BASE_URL + "end_ride";

    String end_pay = BASE_URL + "paynow";

    String find_pair = BASE_URL + "find_pair";

    String get_user_info = BASE_URL + "get_user_info";

    String edit_user_info = BASE_URL + "edit_user_info";

    String edit_name = BASE_URL + "edit_name";

    String upload_photo = BASE_URL + "upload_photo";

    String help = BASE_URL + "help";

    String get_details_of_ride = BASE_URL + "get_details_of_ride";

    String get_ride_details_update = BASE_URL + "get_ride_details_update";

    String paired_user_details = BASE_URL + "paired_user_details";

    String rides_history = BASE_URL + "rides_history";

    String on_going_rides = BASE_URL + "on_going_rides";

    String meter_calculations = BASE_URL + "meter_calculations";

    String manual_calculations = BASE_URL + "manual_calculations";

    String get_source_dest_details = BASE_URL + "get_source_dest_details";

    String delete_pair = BASE_URL + "delete_pair";

    String paired_details = BASE_URL + "paired_details";

    String paired_details_for_chat = BASE_URL + "paired_details_for_chat";

    String delay = BASE_URL + "delay";

    String get_qrcode = BASE_URL + "get_qrcode";

    String get_notifications = BASE_URL + "get_notifications";


    String googleplacelocation = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyAUkyYWbQREq2dT8pDwV2e7lDSteP1pbCA&types=&input=";

    String getAddressComponentsURL = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyAUkyYWbQREq2dT8pDwV2e7lDSteP1pbCA&place_id=";

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    //Siddu
     //generate_checksumhash(Payment Gateway)
    String generate_checksumhash = BASE_URL + "generate_checksumhash";


    //get_phone_number
    String get_phone_number = BASE_URL + "get_phone_number";

    //get_user_details
    String get_user_details = BASE_URL + "get_user_details";

    //your_bill
    String your_bill = BASE_URL + "your_bill";

    String accept_find_pair = BASE_URL + "accept_find_pair";

    String send_request = BASE_URL + "send_request";

    String get_request = BASE_URL + "get_request";

    String get_request_for_other_user = BASE_URL + "get_request_for_other_user";

    String delete_request = BASE_URL + "delete_request";

    String qrcode_upload = BASE_URL + "qrcode_upload";

    String get_scheduling_ride = BASE_URL + "get_scheduling_ride";

    String scheduling_ride = BASE_URL + "scheduling_ride";

    String scheduling_ride_notification = BASE_URL + "scheduling_ride_notification";

    // Edit Emergency Contacts
    String edit_emergency_contacts = BASE_URL + "edit_emergency_contacts";

    //Add Emergency Contacts
    String emergency_contacts = BASE_URL + "emergency_contacts";

    //Get Emergency Contacts
    String get_emergency_contacts = BASE_URL + "get_emergency_contacts";

   //Get Emergency Contacts
   String calling_Api = BASE_URL + "call";

}
