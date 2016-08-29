[![Duit logo](http://api.diuit.com/images/logo.png)](http://api.diuit.com/)

# Android: Building Chatting APP with Diuit API


在這次教學中，我們將帶大家透過Diuit API快速在自己的APP也能擁有一個聊天的功能。


# Requirement

- 安裝 [Android Studio](https://developer.android.com/studio/index.html)  
- 註冊一組Diuit帳號 ([Get one for free](https://developer.diuit.com/login))
- 有一個合法的authToken([Tutorial](https://github.com/Diuit/DUChatServerDemo))



# Agenda 


在這次教學中，我們將完成以下幾個目標：  

- 建立一個新的Android Project以及安裝Diuit SDK
- 使用合法的authToken登入API  
- 列出所有聊天列表
- 列出聊天列表中所有的聊天紀錄  


# Create A New Project

在安裝好或是已經安裝Android Studio之後，點擊**Start a new Android Studio project**.  

<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-23.09.05.png" alt="android-tutorial-1" class="alignnone size-full wp-image-171" />  


會看到預設的Application name，也可以改成自己想要的Application name跟Domain，完成之後點選Next  

<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-23.13.32.png" alt="android-tutorial-2" class="alignnone size-full wp-image-172" />
  
這裡是需要選擇APP可以Run在哪些Android平台上，勾選**Phone and Tablet(default)**，除非有特殊想符合的規格，目前API 14已支援大部分的平台。選好之後點選**Next**

<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-23.21.27.png" alt="android-tutorial-3"/>  

最後點選**Empty Activity(default)**之後點擊**Next**就可以準備開始Coding囉！


> 在最後還會看到Activity Name = MainActivity，這裡可以無需修改直接點擊Finish

<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-23.30.10.png" alt="android-tutorial-4" />  


<br>

# Installation


安裝整個流程的部分可以參考[Github文件](https://github.com/diuitAPI/diuit.uikit.demo.android#prerequisites)  
在完成上述動作之後，一開始進去會看到  

<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-23.32.18.png" alt="android-tutorial-4"/>  

點擊左邊側欄Gradle Scripts底下的build.gradle(Module: app)  

<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-23.32.18.png" alt="android-tutorial-5"/>  


填上紅色框框裡面的文字(也可以從[Github文件](https://github.com/diuitAPI/diuit.uikit.demo.android#prerequisites)文件中複製貼上)

完成之後點擊右上角黃色標起的部分Sync Now  
點擊完之後，右下角會顯示從網路上抓取Diuit Android SDK下來的進度，完成之後就裝好Android SDK了  



<br>

# Authorization with valid authToken


回到左邊側欄點擊**app > java > MainActivity**可以看到  

<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-23.40.46.png" alt="android-tutorial-6"/>

在MainActivity底下，新增    


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
完成之後點擊上方menu bar **Run**   

<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-23.51.27.png" alt="android-tutorial-10" />  


點擊底下的**Android Monitor**就可以看到結果    

<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-27-at-00.00.06.png" alt="android-tutorial-11"/>


<br>


# Chat list UI

在登入成功之後，接著想列出該使用者底下所有的聊天清單，Diuit提供了[UIKit](https://github.com/diuitAPI/diuit.uikit.demo.android) ，讓使用者在開發app上更快速。  


打開專案左側欄**res > layout > activity_main.xml**. 將**<Text ..."Hello World!"/>**移除，貼上  

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

並且在\<RelativeLayout ... \>中新增 **xmlns:app="http://schemas.android.com/apk/res-auto"**  

```
<RelativeLayout
    .....
    xmlns:app="http://schemas.android.com/apk/res-auto">
```

回到原本**MainActivity.java**中貼上:  

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


設置好UI環境之後，我們透過listChats這個API來取得該User底下所有的chatroom `DiuitMessagingAPI.loginWithAuthToken`的`onSuccess`中加入:  

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

再執行一次`Run`，成功之後會在Console裡面看到所有的聊天列表  
這時候在DiuitMessagingAPI.listChats的onSuccess中加入  
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

> (註) 在完成上述步驟之後bindChats也許可能會出現警告，這時候記得在onSuccess的參數`ArrayList result`改為`final ArrayList result`即可  

這時候再重新**Run**過一次，就可以看到畫面有不一樣的變化了！   


# Go To Message list

在取得聊天列表之後，我們想要取得單一聊天室的對話列表，在點擊某一個聊天室之後，`new DiuitChatsRecyclerViewAdapter.OnItemClickListener()`的`onItemClick`會被觸發，相對應的DiuitChat就是對應到被點擊的聊天室  
 

## Add New XML Page  

在左側欄**res > layout**點擊右鍵新增xml檔案並且命名為`activity_message_list`  

<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-27-at-00.58.37.png" alt="android-tutorial-13" /> 

在 `activity_message_list.xml`加上DiuitMessagesListView  


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

完成設置Message List UI的部分。  


## Add MessageListActivity

在左側欄點擊右鍵新增java檔案  


<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-27-at-00.54.09.png" alt="android-tutorial-12"/> 

Name輸入`MessageListActivity`，完成之後可以看到新的java檔案在MainActivity之下，打開MessageListActivity檔案後，將原本**public class MessageListActivity {}**改成為：  


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

倒數第二個步驟，回到原本MainActivity之中，在**onItemClick**加入  
   
```
@Override
public void onItemClick(DiuitChat diuitChat) {
    MessageListActivity.setCurrentChat(diuitChat);
    Intent intent = new Intent(MainActivity.this, MessageListActivity.class);
    startActivity(intent);
}
```

當下次我們點擊chat item之後，就會直接幫我們轉跳到MessageListActivity的頁面了。


## Update AndroidManifest.xml

最後，我們要跟系統宣告我們新增了一個Activity，打開左側欄**app>manifests>AndroidManifest.xml**，在底下新增:  

```
<activity android:name=".MessageListActivity"/>
```


<img src="http://zxcvbnius.tw/wp-content/uploads/2016/07/Screen-Shot-2016-07-27-at-01.08.11.png" alt="android-tutorial-14"/>


After finishing above steps, click **Run** to rebuild app into your Android phone. Click 'chat item', app will show messages list. You can type messages and click the send button, and others in the chat will receive your messages!  

DONE!!!
