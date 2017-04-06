package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author Tejal Shah
 *
 */
public class Event implements Serializable {

    private int eventId;
    private String eventTitle;
    private String eventDescription;
    private String eventLocation;
    private String eventDate;
    private String eventStartTime;
    private String eventEndTime;
    //private String hostName;
    private int eventHostedById;
    private int interestAreaId;

    public Event(){

    }

    public Event(String eTitle,String eDetail,String eLoc, String eOn,String eStart, String eEnd, int eBy){
        eventTitle = eTitle;
        eventDescription = eDetail;
        eventLocation=eLoc;
        eventDate=eOn;
        eventStartTime = eStart;
        eventEndTime = eEnd;
        eventHostedById = eBy;
        //hostName = getUserNameById(eBy);
    }

    public Event(int eId,String title, String description, String location, String eDate, String eStTime, String eEnTime){
        this.eventId = eId;
        this.eventTitle = title;
        this.eventDescription = description;
        this.eventLocation = location;
        this.eventDate = eDate;
        this.eventStartTime = eStTime;
        this.eventEndTime = eEnTime;
    }


    /*public Event(Parcel in) {
        eventId = in.readInt();
        eventTitle = in.readString();
        eventDescription = in.readString();
        eventLocation = in.readString();
        eventDate = in.readString();
        eventStartTime = in.readString();
        eventEndTime = in.readString();
        eventHostedById = in.readInt();
        interestAreaId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(eventId);
        dest.writeString(eventTitle);
        dest.writeString(eventDescription);
        dest.writeString(eventLocation);
        dest.writeString(eventDate);
        dest.writeString(eventStartTime);
        dest.writeString(eventEndTime);
        dest.writeInt(eventHostedById);
        dest.writeInt(interestAreaId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };*/

    public int getEventId() {
        return eventId;
    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    public String getEventTitle() {
        return eventTitle;
    }
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
    public String getEventDescription() {
        return eventDescription;
    }
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
    public String getEventLocation() {
        return eventLocation;
    }
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
    public String getEventDate() {
        return eventDate;
    }
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
    public String getEventStartTime() {
        return eventStartTime;
    }
    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }
    public String getEventEndTime() {
        return eventEndTime;
    }
    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }
    public int getEventHostedById() {
        return eventHostedById;
    }
    public void setEventHostedById(int eventHostedById) {
        this.eventHostedById = eventHostedById;
    }
    public int getInterestAreaId() {
        return interestAreaId;
    }
    public void setInterestAreaId(int interestAreaId) {
        this.interestAreaId = interestAreaId;
    }
    /*public String getUserNameById(int id){
        return "Amu";
    }*/

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", eventTitle='" + eventTitle + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", eventLocation='" + eventLocation + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", eventStartTime='" + eventStartTime + '\'' +
                ", eventEndTime='" + eventEndTime + '\'' +
                ", eventHostedById=" + eventHostedById +
                ", interestAreaId=" + interestAreaId +
                '}';
    }
}

