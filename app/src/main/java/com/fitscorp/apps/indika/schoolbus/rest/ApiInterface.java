package com.fitscorp.apps.indika.schoolbus.rest;

import com.fitscorp.apps.indika.schoolbus.model.*;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;


public interface ApiInterface {

@GET("api/RetrofitAndroidImageResponse")
Call<ResponseBody> getImageDetails();


   @GET("api/RetrofitAndroidImageResponses")
   Call<ResponseBody> getImageDetailsOld(
           @Field("method") String method,
           @Field("input_type") String JSON,
           @Field("response_type") String JSON2,
           @Field("rest_data") String jsonStr
   );

//   @POST("getMedicalExpertsNew")@FormUrlEncoded
//   Call<MainResponse> getDoctors(
//           @Field("method") String method,
//           @Field("input_type") String JSON,
//           @Field("response_type") String JSON2,
//           @Field("rest_data") String jsonStr
//   );

   @POST("pickGoal")@FormUrlEncoded
   Call<Object> getTimeLineData(
           @Field("method") String method,
           @Field("input_type") String JSON,
           @Field("response_type") String JSON2,
           @Field("rest_data") String jsonStr
   );

   @POST("pickGoal")@FormUrlEncoded
   Call<Object> pickAGoal(
           @Field("method") String method,
           @Field("input_type") String JSON,
           @Field("response_type") String JSON2,
           @Field("rest_data") String jsonStr
   );

   @POST("homePageTiles")@FormUrlEncoded
   Call<Object> callMainService(
           @Field("method") String method,
           @Field("input_type") String JSON,
           @Field("response_type") String JSON2,
           @Field("rest_data") String jsonStr
   );
     @GET("posts?")
     Call<Object> getAllPost(
             @Header("Authorization") String authorization,
             @Query("page") int page_number,
             @Query("offset") int offset,
             @Query("max_post_id") String post_id);
 //  https://livehappy.ayubo.life/api.ayubo.life/public/api/v1/posts?page=1&offset=25&max_post_id
 //  http://businesscrmdev.cloudapp.net/API/public/api/v1/post/7


    @GET("post/{post_id}")
    Call<Object> getSinglePost(
            @Header("Authorization") String authorization,
            @Path("post_id") Integer var);


   @GET("post/{post_id}/comments")
   Call<Object> getAllComment(
           @Header("Authorization") String authorization,
           @Path("post_id") String post_id);


   @GET("post/{post_id}/liked_users")
   Call<Object> getLikedUsers(
           @Header("Authorization") String authorization,
           @Path("post_id") String post_id);

   @POST("getbuslocation")@FormUrlEncoded
   Call<GPSMainResponse> getBusLocations(
           @Field("bus_id") String bus_id
   );

    @POST("getbusstatus")@FormUrlEncoded
    Call<String> getBusStatus(
            @Field("bus_id") String bus_id
    );

//   @GET("getschoollist")
//   Call<SchoolListMainResponse> getSchoolList();

   @POST("getschoollist")@FormUrlEncoded
   Call<SchoolListMainResponse> getSchoolList(
           @Field("district") String district
   );

   @POST("getchildbyparentid")@FormUrlEncoded
   Call<ChildMainResponse> getChildByParentId(
           @Field("parent_id") String parent_id
   );


   @POST("getmybusrequests")@FormUrlEncoded
   Call<SchoolListMainResponse> getMyBusRequests(
           @Field("bus_id") String bus_id
   );

   @POST("getallbuses")@FormUrlEncoded
   Call<BusListMainResponse> getBusesForSchoolList(
           @Field("schoolID") String schoolID
   );


   @POST("assignbustoparent")@FormUrlEncoded
   Call<String> assignBusToParent(
           @Field("bus_id") String bus_id,
           @Field("parent_id") String parent_id,
           @Field("school_id") String school_id,
           @Field("child_id") String childID


   );

   @POST("gettripstatus")@FormUrlEncoded
   Call<List<BusMainListData>> getMyBusStatus(
           @Field("bus_id") String bus_id
   );

   @POST("getmybus")@FormUrlEncoded
   Call<BusListMainResponse> getMyBus(
           @Field("bus_id") String bus_id
   );

   @GET("gettradings")
   Call<Object> getAllTradings();

   @POST("addUser")@FormUrlEncoded
   Call<Object> addUser(
           @Field("name") String name,
           @Field("email") String email,
           @Field("address") String address
   );

   @POST("getparentbyparentid")@FormUrlEncoded
   Call<ParentsDataModel> isParentRegistered(
           @Field("id") String bus_id

   );
   @POST("getbusbybusnumber")@FormUrlEncoded
   Call<BusMainResponse> isBusRegistered(
           @Field("bus_number") String bus_id

   );
   @POST("registernewschool")@FormUrlEncoded
   Call<String> registerNewSchool(
           @Field("address") String address

   );

   @POST("getcities")@FormUrlEncoded
   Call<List<City>> getCities(
           @Field("msg") String msg

   );

   @POST("sendmessage")@FormUrlEncoded
   Call<String> sendmessage(
           @Field("msg") String msg,
           @Field("user_id") String user_id
   );

   @POST("saveattendence")@FormUrlEncoded
   Call<String> saveAttendence(
           @Field("bus_id") String bus_id,
           @Field("parent_id") String parent_id
   );
   @POST("setabsencebybusid")@FormUrlEncoded
   Call<String> setAbsenceByBusid(
           @Field("bus_id") String bus_id,
           @Field("parent_id") String parent_id,
           @Field("date") String date
   );


   @POST("sendmessagetodriver")@FormUrlEncoded
   Call<String> sendMessageToDriver(
           @Field("bus_id") String bus_id,
           @Field("parent_id") String parent_id,
           @Field("text") String text
   );

   @GET("getappversion")
   Call<String> getAppVersion();


   @POST("saveBusUser")@FormUrlEncoded
   Call<ParentsDataModel> registerUser(
           @Field("name") String name,
           @Field("phone") String phone,
           @Field("email") String email,
           @Field("city") String city,
           @Field("birthday") String birth,
           @Field("gender") String gender,
           @Field("district") String district

   );


   @POST("registerchild")@FormUrlEncoded
   Call<ChildMainResponse> registerChild(
           @Field("name") String name,
           @Field("parent_id") String parent_id,
           @Field("grade") String grade,
           @Field("school") String school,
           @Field("birthday") String birthday,
           @Field("gender") String district
   );


   @POST("addBusLocation")@FormUrlEncoded
   Call<Object> addBusLocation(
           @Field("lat") String lat,
           @Field("longi") String longit,
           @Field("bus_id") String bus_id
   );
   @POST("saveBusLocation")@FormUrlEncoded
   Call<Object> saveBusLocation(
           @Field("lat") String lat,
           @Field("longi") String longit,
           @Field("bus_id") String bus_id
   );

   @POST("addlocationchildbyid")@FormUrlEncoded
   Call<Object> getChildLocationsByID(@Field("location_id") String method);


   @POST("gethotels")@FormUrlEncoded
   Call<Object> getHotelsByLocation(@Field("location_id") String method);
   //getthingstodo
   @POST("getthingstodo")@FormUrlEncoded
   Call<Object> getThingsToDoByLocation(@Field("location_id") String method);

   @POST("dashboard_details")@FormUrlEncoded
   Call<Object> getWellnessDashboardDetails(
           @Header("Authorization") String authorization,
           @Field("duration") String date,
           @Field("average_category_id") String average_category_id);



   @GET("steps_history")
   Call<Object> getDashboardStepHistory(
           @Header("Authorization") String authorization);

   @POST("dashboard")@FormUrlEncoded
   Call<Object> getWellnessDashboard(
           @Header("Authorization") String authorization,
           @Field("date") String date,
           @Field("average_category_id") String average_category_id);


   @PUT("post/{post_id}/like")@FormUrlEncoded
   Call<Object> LikeAPost(
           @Header("Authorization") String authorization,
           @Path("post_id") String var,
           @Field("like") int like);

   @POST("auth/login")@FormUrlEncoded
   Call<Object> getToken(
           @Header("Authorization") String authorization,
           @Field("user_id") String user_id);


   @DELETE("post/{post_id}/delete")
   Call<Object> DeleteAPost(
           @Header("Authorization") String authorization,
           @Path("post_id") String var);
///v1/post/7/comment/1/delete
   @DELETE("post/{post_id}/comment/{comment_id}/delete")
   Call<Object> DeleteAComment(
           @Header("Authorization") String authorization,
           @Path("post_id") String post_id,
           @Path("comment_id") String comment_id);

   @POST("goalStatus")@FormUrlEncoded
   Call<Object> goalStatus(
           @Field("method") String method,
           @Field("input_type") String JSON,
           @Field("response_type") String JSON2,
           @Field("rest_data") String jsonStr
   );

   @Multipart
   @POST("upload")
   Call<ResponseBody> uploadImage(
           @Part("str_oner_name") RequestBody str_oner_name1,
           @Part("str_oner_phone") RequestBody str_oner_phone1,
           @Part("str_oner_email") RequestBody str_oner_email1,
           @Part("str_oner_address") RequestBody str_oner_address1,
           @Part("str_bus_number") RequestBody str_bus_number1,
           @Part("str_driver_name") RequestBody str_driver_name1,
           @Part("str_driver_phone") RequestBody str_driver_phone1,
           @Part("start_address") RequestBody start_address1,
           @Part("end_address") RequestBody end_address1,
           @Part("cross_cities") RequestBody cross_cities1,
           @Part("time_desc") RequestBody time_desc1,
           @Part MultipartBody.Part file
   );



   @POST("savebus")@FormUrlEncoded
   Call<Object> saveBus(
           @Field("owner_name") String owner_name,
           @Field("owner_address") String owner_address,
           @Field("owner_phone") String owner_phone,
           @Field("owner_email") String owner_email,
           @Field("driver_name") String driver_name,
           @Field("driver_phone") String driver_phone,
           @Field("bus_number") String bus_number,
           @Field("bus_photo") String bus_photo,
           @Field("bus_number_photo") String bus_number_photo,
           @Field("start_address") String start_address,
           @Field("end_address") String end_address,
           @Field("cross_cities") String cross_cities,
           @Field("time_desc") String time_desc
   );



      @Multipart
      @POST("video_post")
      Call<Object> makeVideoPost(@Header("Authorization") String authorization,
                                 @Part("heading") RequestBody file,
                                 @Part("body") RequestBody body,
                                 @Part("community_ids") RequestBody community_ids,
                                 @Part("video") RequestBody video,
                                 @Part("thumbnail") RequestBody thumbnail);


   @POST("achieveGoal")@FormUrlEncoded
   Call<Object> achieveGoal(
           @Field("method") String method,
           @Field("input_type") String JSON,
           @Field("response_type") String JSON2,
           @Field("rest_data") String jsonStr
   );



   @POST("getLifePoints")@FormUrlEncoded
   Call<Object> getLifePoints(
           @Field("method") String method,
           @Field("input_type") String JSON,
           @Field("response_type") String JSON2,
           @Field("rest_data") String jsonStr
   );


   @POST("viewGoals")@FormUrlEncoded
   Call<Object> viewGoals(
           @Field("method") String method,
           @Field("input_type") String JSON,
           @Field("response_type") String JSON2,
           @Field("rest_data") String jsonStr
   );
   @POST("goalDelete")@FormUrlEncoded
   Call<Object> goalDelete(
           @Field("method") String method,
           @Field("input_type") String JSON,
           @Field("response_type") String JSON2,
           @Field("rest_data") String jsonStr
   );
   @POST("addGoal")@FormUrlEncoded
   Call<Object> goalAdd(
           @Field("method") String method,
           @Field("input_type") String JSON,
           @Field("response_type") String JSON2,
           @Field("rest_data") String jsonStr
   );
   @POST("editGoal")@FormUrlEncoded
   Call<Object> editGoal(
           @Field("method") String method,
           @Field("input_type") String JSON,
           @Field("response_type") String JSON2,
           @Field("rest_data") String jsonStr
   );

   @POST("getGoalImages")@FormUrlEncoded
   Call<Object> getGoalCategories(
           @Field("method") String method,
           @Field("input_type") String JSON,
           @Field("response_type") String JSON2,
           @Field("rest_data") String jsonStr
   );


   @POST("getGoalImages")@FormUrlEncoded
   Call<Object> addAGoalResult(
           @Field("method") String method,
           @Field("input_type") String JSON,
           @Field("response_type") String JSON2,
           @Field("rest_data") String jsonStr
   );

   @POST("getGoalImages")@FormUrlEncoded
   Call<Object> getGoalImages(
           @Field("method") String method,
           @Field("input_type") String JSON,
           @Field("response_type") String JSON2,
           @Field("rest_data") String jsonStr
   );

   @POST("getHistoryCategoryList")@FormUrlEncoded
   Call<Object> getReports(
           @Field("method") String method,
           @Field("input_type") String JSON,
           @Field("response_type") String JSON2,
           @Field("rest_data") String jsonStr
   );


   @POST("loginVerficationWithMobile")@FormUrlEncoded
Call<Object> callLoginVerficationWithMobile(
           @Field("method") String method,
           @Field("input_type") String JSON,
           @Field("response_type") String JSON2,
           @Field("rest_data") String jsonStr
   );
    @POST("createAMobileUser")@FormUrlEncoded
    Call<Object> callCreateAMobileUser(
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

//    @POST("loginVerficationWithMobile")@FormUrlEncoded
//    Call<Login> callLoginVerficationWithMobile(
//    @Query("method") String methodName,
//    @Query("input_type") String input_type,
//    @Query("response_type") String response_type,
//    @Query("rest_data") String rest_data);

    @POST("scheduleFormData")
    Call<Object> getscheduleFormData(
            @Query("method") String methodName,
            @Query("input_type") String input_type,
            @Query("response_type") String response_type,
            @Query("rest_data") String rest_data);


    @POST("session_logout")
    Call<String> getUserLogout(

            @Query("method") String methodName,
            @Query("input_type") String input_type,
            @Query("response_type") String response_type,
            @Query("rest_data") String rest_data);

}
