package com.classapplication.utils;

import com.classapplication.data.BasicData;
import com.classapplication.data.CalendarData;
import com.classapplication.data.MyLectureData;
import com.classapplication.data.LectureData;
import com.classapplication.data.StudentData;
import com.classapplication.data.UserData;
import com.classapplication.data.VideoData;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Services {

    @FormUrlEncoded
    @POST("/signin")
    Call<UserData>Login(@Field("email")String email,
                        @Field("passwd")String passwd);

    @FormUrlEncoded
    @POST("/signup")
    Call<UserData>Resiger(@Field("name")String name ,
                          @Field("passwd")String passwd,
                          @Field("email")String email,
                          @Field("isProfessor")Boolean isProfessor);

    @GET("/allLecture")
    Call<LectureData> LectureList();

    @FormUrlEncoded
    @POST("/searchLecture")
    Call<LectureData>SearchLecture(@Field("courseTitle")String courseTitle);

    @FormUrlEncoded
    @POST("/lectureList")
    Call<MyLectureData>Lecture(@Field("token")String token);


    @FormUrlEncoded
    @POST("/student")
    Call<StudentData>Student(@Field("token")String token);

    @FormUrlEncoded
    @POST("/attendance")
    Call<BasicData>Attendance(@Field("token")String token,
                           @Field("date")String date,
                           @Field("index")String index);

    @FormUrlEncoded
    @POST("/joinLecture")
    Call<BasicData>Lecturejoin(@Field("lectureToken")String lectureToken,
                           @Field("token")String token);

    @FormUrlEncoded
    @POST("/lectureExist")
    Call<BasicData>LectureExist(@Field("token")String token,
                                @Field("lectureToken")String lectureToken);

    @FormUrlEncoded
    @POST("/chkAttendance")
    Call<CalendarData>CalnedarAttendance(@Field("token")String token,
                                    @Field("lectureToken")String lectureToken);

    @FormUrlEncoded
    @POST("/signdel")
    Call<BasicData>SignDel(@Field("token")String token);

    @FormUrlEncoded
    @POST("/signmodify")
    Call<UserData>SignModify(@Field("token")String token,
                              @Field("name")String name,
                              @Field("passwd")String passwd);

    @FormUrlEncoded
    @POST("/leaveLecture")
    Call<BasicData>leaveLecture(@Field("lectureToken")String lectureToken,
                                @Field("token")String token);

    @Multipart
    @POST("/addVideo")
    Call<BasicData>addVideo(@Part MultipartBody.Part video,
                            @Part("token") RequestBody token,
                            @Part("title") RequestBody title,
                            @Part("date") RequestBody date);

    @FormUrlEncoded
    @POST("/videoList")
    Call<VideoData>VideoList(@Field("token")String token);


    @FormUrlEncoded
    @POST("/delVideo")
    Call<BasicData>delVideo(@Field("token")String token,
                            @Field("title")String title);

    @FormUrlEncoded
    @POST("/newLecture")
    Call<BasicData>newLecture(@Field("courseTitle")String courseTitle,
                            @Field("professor")String professor,
                            @Field("token")String token,
                            @Field("category")String category,
                            @Field("date")String date,
                            @Field("time")String time,
                            @Field("maxMember")int maxMember,
                            @Field("term")String term);
    @FormUrlEncoded
    @POST("/setLecture")
    Call<BasicData>setLecture(@Field("courseTitle")String courseTitle,
                              @Field("professor")String professor,
                              @Field("token")String token,
                              @Field("category")String category,
                              @Field("date")String date,
                              @Field("time")String time,
                              @Field("maxMember")int maxMember,
                              @Field("term")String term,
                              @Field("lectureToken")String lectureToken);

    @FormUrlEncoded
    @POST("/delLecture")
    Call<BasicData>delLecture(@Field("token")String token);

    @FormUrlEncoded
    @POST("/getPro")
    Call<MyLectureData>getPro(@Field("token")String token);

    @FormUrlEncoded
    @POST("/getWeb")
    Call<MyLectureData>getWeb(@Field("token")String token);

    @FormUrlEncoded
    @POST("/getNet")
    Call<MyLectureData>getNet(@Field("token")String token);


}
