package com.diuit.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.duolc.DiuitChat;
import com.duolc.DiuitMessagingAPI;
import com.duolc.DiuitMessagingAPICallback;
import com.duolc.diuitapi.messageui.adapter.DiuitChatsRecyclerViewAdapter;
import com.duolc.diuitapi.messageui.page.DiuitChatsRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private DiuitChatsRecyclerView chatsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.chatsRecyclerView = (DiuitChatsRecyclerView) this.findViewById(R.id.diuitChatListView);

        DiuitMessagingAPI.loginWithAuthToken("DemoDev2", new DiuitMessagingAPICallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d("Diuit", "result: Success");
                DiuitMessagingAPI.listChats(new DiuitMessagingAPICallback<ArrayList<DiuitChat>>() {
                    @Override
                    public void onSuccess(final ArrayList<DiuitChat> result) {
                        Log.d("Diuit", "result:" + result.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chatsRecyclerView.bindChats(result, new DiuitChatsRecyclerViewAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(DiuitChat diuitChat) {

                                        MessageListActivity.setCurrentChat(diuitChat);
                                        Intent intent = new Intent(MainActivity.this, MessageListActivity.class);
                                        startActivity(intent);

                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onFailure(int code, JSONObject result) {
                        Log.d("Diuit", "code: " + code + ", result:" + result.toString());
                    }
                });
            }

            @Override
            public void onFailure(int code, JSONObject result) {
                Log.d("Diuit", "code: " + code + ", result:" + result.toString());
            }
        });

    }
}
