[![Duit logo](http://api.diuit.com/images/logo.png)](http://api.diuit.com/)

# Android: Building Chatting APP with Diuit API

This tutorial will guide you to integrate with our UIKit. You will have a real time message app which is capable of displaying all required information and sending multimedia messages.


# Requirement

- Install [Android Studio](https://developer.android.com/studio/index.html)  
- A Diuit acount ([Get one for free](https://developer.diuit.com/login))
- A valid Diuit API AuthToken([Tutorial](https://github.com/Diuit/DUChatServerDemo))



# Agenda 


A couple of targets sould be done in this tutorial.

- Create an Android Project and install Diuit SDK
- Login with authToken
- Show chats list after logging
- send messages to someone or send to a group


# Create A New Project

Open Android Studio and click**Start a new Android Studio project**.

<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-23.09.05.png" alt="android-tutorial-1" class="alignnone size-full wp-image-171" />  


You will see the deault name of the application. You also can change the name and domain. Then, click **Next**.


<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-23.13.32.png" alt="android-tutorial-2" class="alignnone size-full wp-image-172" />
  
Choose **Phone and Tablet(default)** and click **Next**

<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-23.21.27.png" alt="android-tutorial-3"/>  

Choose **Empty Activity(default)** and click **Next**.


Finally, Click **Finish** and you will finish setting up your project.  

<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-23.30.10.png" alt="android-tutorial-4" />  


<br>

# Installation


- We do not support Java outside of Android at the moment.
- A recent version of the Android SDK
- We support all Diuit SDK versions since Diuit API 1.1.3+ (Android 1.1.3 & above).

**Maven**

1.  Navigate to your build.gradle file at the app level (not project level) and ensure that you include the following:

```java
repositories {
    maven {
      url "https://dl.bintray.com/duolc/maven"
    }
}
```

2. Add `compile 'com.duolc.diuitapi:messageui:0.1.9'` to the dependencies of your project
3. In the Android Studio Menu: Tools -> Android -> Sync Project with Gradle Files  


<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-23.32.18.png" alt="android-tutorial-5"/>  



<br>

# Authorization with valid authToken


In the left menu, click **app > java > MainActivity** and you will see  

<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-23.40.46.png" alt="android-tutorial-6"/>

Open MainActivity.java, add copy and paste:  


```java
DiuitMessagingAPI.loginWithAuthToken("PUT_YOUR_TOKEN", new DiuitMessagingAPICallback<JSONObject>() {
    @Override
    public void onSuccess(JSONObject result) {
        Log.d("Diuit", "result: Success");
    }

    @Override
    public void onFailure(int code, JSONObject result) {
        Log.d("Diuit", "code: " + code + ", result:" + result.toString());
    }
});
```


<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-23.49.28.png" alt="android-tutorial-9"/>
  
Remember replacing **"YOUR_SESSION_TOKEN"** with your valid authToken.  


After finishing the steps above, click **Run** in menu bar.   

<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-23.51.27.png" alt="android-tutorial-10" />  


Click **Android Monitor** and you will see these logging logs.  

<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-27-at-00.00.06.png" alt="android-tutorial-11"/>


<br>


# Chat list UI

After logging successfully, we will list all conversations that the authenticated user ID belongs to. By default, we use [UIKit](https://github.com/diuitAPI/diuit.uikit.demo.android) for us to develop apps easier.   

Go to left menu and open **res > layout > activity_main.xml**. Remove **<Text ..."Hello World!"/>**  and paste the following codes:  

```
<com.duolc.diuitapi.messageui.page.DiuitChatsRecyclerView
    android:id="@+id/diuitChatListView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:colorBackground="@color/white"
    app:diuListDivider="1dp"
    app:diuTitleTextColor="@color/black">
</com.duolc.diuitapi.messageui.page.DiuitChatsRecyclerView>
```

Add in \<RelativeLayout ... \> add **xmlns:app="http://schemas.android.com/apk/res-auto"**  

```
<RelativeLayout
    .....
    xmlns:app="http://schemas.android.com/apk/res-auto">
```

Go back to **MainActivity.java** and paste the following code:  

```
public class MainActivity extends AppCompatActivity {
    private DiuitChatsRecyclerView chatsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatsRecyclerView = (DiuitChatsRecyclerView) this.findViewById(R.id.diuitChatListView);
        .....
    }
    .....
}
```

<br>

# Get Chat list


After setting up UI, we will use **listChats** this api to get all chat rooms whitch the authenticated user ID belongs to. In `DiuitMessagingAPI.loginWithAuthToken` add the following code:  

```
@Override
public void onSuccess(JSONObject result) {
    .....
    DiuitMessagingAPI.listChats(new DiuitMessagingAPICallback<ArrayList<DiuitChat>>() {
        @Override
        public void onSuccess(ArrayList<DiuitChat> result) {
            Log.d("Diuit", "result:" + result.toString());
        }

        @Override
        public void onFailure(int code, JSONObject result) {
            Log.d("Diuit", "code: " + code + ", result:" + result.toString());
        }
    });
}
```

Click `Run` agian and you will see the whole chats in console. 

Add the following code in DiuitMessagingAPI.listChats `onSuccess` callback  

```
@Override
public void onSuccess(ArrayList<DiuitChat> result) {
    Log.d("Diuit", "result:" + result.toString());    
    runOnUiThread(new Runnable() {
	    @Override
	    public void run() {
	        chatsRecyclerView.bindChats(result, new DiuitChatsRecyclerViewAdapter.OnItemClickListener() {
	            @Override
	            public void onItemClick(DiuitChat diuitChat) {
	            }
	        });                                
	    }
	  });
}
```

> (Note) Remember change `onSuccess(ArrayList<DiuitChat> result)` to `onSuccess(final ArrayList<DiuitChat> result)`

Then click **Run** again, you will see the changes in app.   


# Go To Message list

After getting chat list successfully, we want to get messages. We have to create a new Activity and a XML file for showing messages first.  
 

## Add New XML Page  

In left menu, go to **res > layout > new** and click **layout resource file** then naming `activity_message_list`  

<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-27-at-00.58.37.png" alt="android-tutorial-13" /> 

Add the following codes in `activity_message_list.xml`  

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                xmlns:app="http://schemas.android.com/apk/res-auto"
                android:layout_width="match_parent"
                android:layout_height="match_parent">

    <com.duolc.diuitapi.messageui.page.DiuitMessagesListView
        android:id="@+id/diuitMsgListView"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    </com.duolc.diuitapi.messageui.page.DiuitMessagesListView>

</RelativeLayout>
```

Then, we finish ui setion of showing messages list.  


## Add MessageListActivity

Go to left menu and click **add** to add `Java Class`  


<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-27-at-00.54.09.png" alt="android-tutorial-12"/> 

After naming `MessageListActivity`，open MessageListActivity.java，then pasting the following codes:  

```
public class MessageListActivity extends Activity {

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
```


## Switch MainActivity To MessageListActivity

Go back to MainActivity.java，add the following codes in **onItemClick** :   
```
@Override
public void onItemClick(DiuitChat diuitChat) {
    MessageListActivity.setCurrentChat(diuitChat);
    Intent intent = new Intent(MainActivity.this, MessageListActivity.class);
    startActivity(intent);
}
```

While the next time we click 'chat item', app will switch to MessageListActivity.  

## Update AndroidManifest.xml

Final，we have to announce we create a new Activity. Open left menu and click **app>manifests>AndroidManifest.xml**，adding the following code:  

```
<activity android:name=".MessageListActivity"/>
```


<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-27-at-01.08.11.png" alt="android-tutorial-14"/>


After finishing above steps, click **Run** to rebuild app into your Android phone. Click 'chat item', app will show messages list. You can type messages and click the send button, and others in the chat will receive your messages!  

DONE!!!
