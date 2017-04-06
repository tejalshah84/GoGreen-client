package model;

import java.io.Serializable;
import java.sql.Date;

public class Notification implements Serializable{
    private int notificationId;
    private GreenEntry postNote;
    private Event eventNote;
    private String notificationMessage;
    private String notificationDate;
    private int greenEntryId;
    private int eventId;
    private String byUserName;
    private String userPic;

    public Notification(){

    }

    public Notification(int notificationId, int greenEntryId, int eventId, String notificationMessage, Date notificationDate, String byUserName, String userPic) {
        this.notificationId = notificationId;
        this.greenEntryId = greenEntryId;
        this.eventId = eventId;
        this.notificationMessage = notificationMessage;
        this.notificationDate = notificationDate.toString();
        this.byUserName=byUserName;
        this.userPic = userPic;

    }

    public String getByUserName() {
        return byUserName;
    }

    public void setByUserName(String byUserName) {
        this.byUserName = byUserName;
    }


    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public GreenEntry getPostNote() {
        return postNote;
    }
    public void setPostNote(GreenEntry postNote) {
        this.postNote = postNote;
    }

    public Event getEventNote() {
        return eventNote;
    }
    public void setEventNote(Event eventNote) {
        this.eventNote = eventNote;
    }

    public int getGreenEntryId() {
        return greenEntryId;
    }

    public void setGreenEntryId(int id) {
        this.greenEntryId = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int id) {
        this.eventId = id;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }
    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }
    public String getNotificationDate() {
        return notificationDate;
    }
    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", postNote=" + postNote +
                ", eventNote=" + eventNote +
                ", notificationMessage='" + notificationMessage + '\'' +
                ", notificationDate='" + notificationDate + '\'' +
                ", greenEntryId=" + greenEntryId +
                ", eventId=" + eventId +
                ", byUserName='" + byUserName + '\'' +
                ", userPic='" + userPic + '\'' +
                '}';
    }
}
