package com.classapplication.utils;

import com.classapplication.data.BasicData;
import com.classapplication.data.CalendarData;
import com.classapplication.data.MyLectureData;
import com.classapplication.data.LectureData;
import com.classapplication.data.StudentData;
import com.classapplication.data.UserData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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


}
