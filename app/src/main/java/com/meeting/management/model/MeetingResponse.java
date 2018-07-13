package com.meeting.management.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MeetingResponse {

    @SerializedName("meetings")
    private List<Meetings> meetings;


    public List<Meetings> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meetings> meetings) {
        this.meetings = meetings;
    }
}
