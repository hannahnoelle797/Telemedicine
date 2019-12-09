package com.example.telemedicine.models;
import java.util.Date;
import com.example.telemedicine.models.Chat;
public class Message {
    //creates a message table that connects to the chat table.
    //the message table holds the message content, read and delete status', and date sent.
    private String messageId, chatId, senderName,senderId, recipientId, messageContent;
    private boolean readStatus, mStatus;
    private Date dateSent;

    public Message(){
        this.readStatus = false;
        this.mStatus = true;
    }
    public Message(String messageID, String chatId, String senderId, String senderName, String recipientId, String messageContent, Date dateSent){
        this.messageContent = messageContent;
        this.messageId = messageID;
        this.dateSent = dateSent;
        this.chatId = chatId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.recipientId = recipientId;
        this.readStatus = false;
        this.mStatus = true;

        //add message to correct chatArrayList.
    }
    public void read(){
        this.readStatus = true;
    }
    public boolean readStatus() {return this.readStatus; }

    public boolean getMStatus() { return this.mStatus; }
    public void setMStatus(boolean b) { this.mStatus = b; }

    public Date getDate() {
        return dateSent;
    }
    public void setDate(Date mDate) {
        this.dateSent = mDate;
    }

    public String getMessageId() {
        return messageId;
    }
    public void setMessageId(String id) { this.messageId = id; }

    public String getMessageContent() {
        return messageContent;
    }
    public void setMessageContent(String message) {this.messageContent = message;}

    public String getSenderName() {
        return senderName;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    public String getSenderId(){
        return this.senderId;
    }
    public void setSenderId(String senderId) {this.senderId = senderId;}
    public String getRecipientId() {
        return this.recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }
    public String getChatId(){
        return this.chatId;
    }
}
