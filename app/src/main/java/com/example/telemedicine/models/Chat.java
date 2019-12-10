package com.example.telemedicine.models;
import java.util.ArrayList;
public class  Chat {
    //ChatID used for categorizing the messages by conversation.

    private String chatId, doctorId, doctorName, patientId, patientName;
    //status allows the user to delete chats from their device but does not delete from DB.
    private boolean status;
    public Chat(){
        this.status = true;
    }
    public Chat(String chatID, String doctorId, String doctorName, String patientId, String patientName) {
        this.chatId = chatID;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.status = true;
    }
    //add messages to the chat.
    //public void addMessage(Message m){messages.add(m);}

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String p) {
        this.doctorId = p;
    }
    public String getDoctorName(){
        return this.doctorName;
    }
    public void setDoctorName(String d){
        this.doctorName = d;
    }

    public String getPatientId() {
        return patientId;
    }
    public String getPatientName() { return patientName;}
    public void setPatientName(String p){
        this.patientName = p;
    }
    public void setPatientId(String p) {
        this.patientId = p;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

//    public ArrayList<Message> getMessages() {
//        return messages;
//    }
//
//    public void setMessages(ArrayList<Message> messages) {
//        this.messages = messages;
//    }
}
