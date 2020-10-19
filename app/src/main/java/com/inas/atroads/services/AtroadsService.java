package com.inas.atroads.services;


import com.google.gson.JsonObject;
import com.inas.atroads.views.model.*;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

public interface AtroadsService
{
    //0-fail
    //1-success

    // Login API
    @POST(APIConstants.LOGINPage)
    Observable<LoginResponseModel> LoginResponse(@Body JsonObject data);

    // REGISTER API
    @POST(APIConstants.REGISTERPAGE)
    Observable<RegisterResponseModel> RegisterResponse(@Body JsonObject data);

    // ForgotPassword API
    @POST(APIConstants.ForgotPassword)
    Observable<ForgotPasswordResponseModel> ForgotPasswordResponse(@Body JsonObject data);

    // Re Send OTP
    @POST(APIConstants.resendotp)
    Observable<ResendOTPResponseModel> ResendOTPResponse(@Body JsonObject data);

    // Re Send OTP
    @POST(APIConstants.verify_otp)
    Observable<VerifyOTPResponseModel> VerifyOTPResponse(@Body JsonObject data);


    //Re-set password
    @POST(APIConstants.resetpassword)
    Observable<ResetPasswordResponseModel> ResetPasswordResponse(@Body JsonObject data);


    //password_change
    @POST(APIConstants.change_password)
    Observable<PasswordResponseModel> PasswordResponse(@Body JsonObject data);

//      // user_ride_details
    @POST(APIConstants.user_ride_details)
    Observable<GetRideDetailsResponseModel> GetRideDetailsResponse(@Body JsonObject data);


    @POST(APIConstants.get_ride_details_update)
    Observable<GetRideDetailsUpdateResponseModel> GetRideDetailsUpdateResponse(@Body JsonObject data);


    // start_ride
    @POST(APIConstants.start_ride)
    Observable<StartRideResponseModel> StartRideResponse(@Body JsonObject data);

    // start_ride
    @POST(APIConstants.start_ride_for_paired_users)
    Observable<StartRideForPairedUsersResponseModel> StartRideForPairedUsersResponse(@Body JsonObject data);


    // cancel_ride
    @POST(APIConstants.cancel_ride)
    Observable<CancelRideDetailsResponseModel> CancelRideResponse(@Body JsonObject data);

    // end_ride
    @POST(APIConstants.end_ride)
    Observable<EndRideResponseModel> EndRideResponse(@Body JsonObject data);


    // find_pair
    /*0- you are at farthest distance
    1- other user has to initiate ride
    2- other person in farthest distance
    3-you have to initiate
     */
    @POST(APIConstants.find_pair)
    Observable<FindPairResponseModel> FindPairResponse(@Body JsonObject data);

    @POST(APIConstants.get_user_info)
    Observable<GetUserInfoResponseModel> GetUserInfoResponse(@Body JsonObject data);

    @POST(APIConstants.edit_user_info)
    Observable<EditUserInfoResponseModel> EditUserInfoResponse(@Body JsonObject data);

    @POST(APIConstants.edit_name)
    Observable<EditNameResponseModel> EditNameResponse(@Body JsonObject data);

    @POST(APIConstants.upload_photo)
    Observable<UploadPicResponseModel> UploadPhotoResponse(@Body JsonObject data);

    @POST(APIConstants.help)
    Observable<HelpResponseModel> HelpResponse(@Body JsonObject data);

    @POST(APIConstants.get_details_of_ride)
    Observable<GetDetailsOfRideResponseModel> GetDetailsOfRideResponse(@Body JsonObject data);

    @POST(APIConstants.paired_user_details)
    Observable<PairedUserDetailsResponseModel> PairedUserDetailsResponse(@Body JsonObject data);

    @POST(APIConstants.rides_history)
    Observable<RidesHistoryResponseModel> RidesHistoryResponse(@Body JsonObject data);

    @POST(APIConstants.on_going_rides)
    Observable<OnGoingRidesResponseModel> OnGoingRidesResponse(@Body JsonObject data);

    @POST(APIConstants.meter_calculations)
    Observable<MeterCalculationResponeModel> MeterCalculationResponse(@Body JsonObject data);

    @POST(APIConstants.manual_calculations)
    Observable<ManualCalculationResponseModel> ManualCalculationResponse(@Body JsonObject data);

    @POST(APIConstants.get_source_dest_details)
    Observable<RouteSourceDestResponseModel> RouteSourceDestDetailsResponse(@Body JsonObject data);

    @POST(APIConstants.delete_pair)
    Observable<DeletePairResponseModel> DeletePairResponse(@Body JsonObject data);

    @POST(APIConstants.paired_details)
    Observable<PairedDetailsResponseModel> PairedDetailsResponse(@Body JsonObject data);

    @POST(APIConstants.paired_details_for_chat)
    Observable<PairedDetailsForChatResponseModel> PairedDetailsForChatResponse(@Body JsonObject data);

    @POST(APIConstants.delay)
    Observable<DeletePairResponseModel> delayResponse(@Body JsonObject data);

    @POST(APIConstants.get_qrcode)
    Observable<GetQRResponseModel> GetQRResponse(@Body JsonObject data);


    @POST(APIConstants.get_notifications)
    Observable<NotificationResponseModel> getNotificationListResponse(@Body JsonObject data);

    @GET
    Observable<AddressComponentsResponse> GetAddressComponents(@Url String url);

    // Payment Gateway API
    @POST(APIConstants.generate_checksumhash)
    Observable<PaymentGatewayResponseModel> PaymentGatewayResponse(@Body JsonObject data);

    /**************************************/
    @POST(APIConstants.get_phone_number)
    Observable<GetPhoneNumberResponseModel> GetPhoneNumber(@Body JsonObject data);

    @POST(APIConstants.get_user_details)
    Observable<GetUserDetailsResponseModel> GetUserDetails(@Body JsonObject data);

    @POST(APIConstants.get_scheduling_ride)
    Observable<GetSchedulingRideResponseModel> GetSchedulingRideResponse(@Body JsonObject data);

    @POST(APIConstants.scheduling_ride)
    Observable<SchedulingRideResponseModel> SchedulingRide(@Body JsonObject data);

    @POST(APIConstants.scheduling_ride_notification)
    Observable<ScheduleRideNotifyResponseModel> SchedulingRideNotify(@Body JsonObject data);

    @POST(APIConstants.your_bill)
    Observable<YourBillResponseModel> YourBillResponse(@Body JsonObject data);


    @POST(APIConstants.accept_find_pair)
    Observable<AcceptPairResponseModel> AcceptFindPairResponse(@Body JsonObject data);


    @POST(APIConstants.send_request)
    Observable<SendRequestResponseModel> SendRequestResponse(@Body JsonObject data);


    @POST(APIConstants.get_request)
    Observable<GetRequestResponseModel> GETRequestResponse(@Body JsonObject data);



    @POST(APIConstants.get_request_for_other_user)
    Observable<GetRequestForOtherUserResponseModel> GETRequestForOtherResponse(@Body JsonObject data);


    @POST(APIConstants.delete_request)
    Observable<DeleteRequestResponseModel> DeleteRequestResponse(@Body JsonObject data);



    @POST(APIConstants.qrcode_upload)
    Observable<UploadQRResponseModel> QRUpload(@Body JsonObject data);


    // Add Emergency Contacts
    @POST(APIConstants.emergency_contacts)
    Observable<Res_AddEmergencyContact> addEmergencyContact(@Body Req_AddEmergencyContact data);

    // Get Emergency Contacts
    @POST(APIConstants.get_emergency_contacts)
    Observable<Res_GetEmergencyContacts> getEmergencyContacts(@Body Req_GetEmergencyContacts data);

    // Edit Emergency Contacts
    @POST(APIConstants.edit_emergency_contacts)
    Observable<Res_EditEmergencyContact> editEmergencyContacts(@Body Req_EditEmergencyContact data);

    // calling api
    @POST(APIConstants.calling_Api)
    Observable<ScheduleRideNotifyResponseModel> callingApi(@Body JsonObject data);
}
