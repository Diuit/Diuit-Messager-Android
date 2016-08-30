package com.diuit.example;

import android.app.Activity;
import android.os.Bundle;

import com.duolc.DiuitChat;
import com.duolc.diuitapi.messageui.page.DiuitMessagesListView;

public class MessageListActivity extends Activity
{
    private DiuitMessagesListView messagesListView;

    private static DiuitChat currentChat;
    public static void setCurrentChat(DiuitChat diuitChat) {
        MessageListActivity.currentChat = diuitChat;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_message_list);
        this.messagesListView = (DiuitMessagesListView) findViewById(R.id.diuitMsgListView);
        this.messagesListView.bindChat(MessageListActivity.currentChat).load();
    }
}
