package ws.remote;


import java.util.List;

import model.Event;
import model.GreenEntry;
import model.Notification;

import java.lang.reflect.Array;
import java.util.ArrayList;

import model.Event;
import model.Following;
import model.GreenEntry;

import model.Share;
import model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Tejal Shah.
 */
public interface GreenRESTInterface {

    @POST("UserServlet")
    Call<User> createUser(@Body User user);

    @GET("UserServlet")
    Call<User> getUserDetails(@Query("userId") int userId);

    @POST("EditUserServlet")
    Call<User> editUser(@Body User user);


    @GET("TimelineServlet")
    Call<List<GreenEntry>> getQuestions(@Query("opId") int opId);

    @GET("AllNotificationsServlet")
    Call<List<Notification>> getAllNotifications();

    @GET("EventServlet")
    Call<Event> getAnEvent(@Query("eventId") int eventId);

    @GET("AnswerServlet")
    Call<List<GreenEntry>> getAnsForQ(@Query("qId") int qId);

    @POST("UserAuthServlet")
    Call<User> authenticateUser(@Body User user);

    @POST("GreenEntryServlet")
    Call<GreenEntry> createGreenEntry(@Body GreenEntry greenEntry);

    @GET("TimelineServlet")
    Call<ArrayList<GreenEntry>> getTimeline(@Query("opId") int opId);

    @GET("FollowingServlet")
    Call<ArrayList<User>> getFollowingDetails(@Query("userId") int userId, @Query("opId") int opId);

    @POST("FollowingServlet")
    Call<Following> setFollowing(@Body Following f);

    @POST("EventServlet")
    Call<Event> createEvent(@Body Event event);

    @POST("AnswerServlet")
    Call<GreenEntry> createAnswer(@Body GreenEntry ge);

    @POST("NotificationServlet")
    Call<Notification> createNotification(@Body Notification n);

    @POST("ShareServlet")
    Call<Share> setShare(@Body Share s);

}
