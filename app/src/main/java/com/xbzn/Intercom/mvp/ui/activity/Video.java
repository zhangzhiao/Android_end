package com.xbzn.Intercom.mvp.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xbzn.Intercom.R;
import com.xbzn.Intercom.mvp.ui.fragmane.VideoViewFragment;

import java.util.HashSet;
import java.util.Map;

import io.agora.rtm.ErrorInfo;
import io.agora.rtm.LocalInvitation;
import io.agora.rtm.RemoteInvitation;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmCallEventListener;
import io.agora.rtm.RtmCallManager;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.SendMessageOptions;


public class Video extends AppCompatActivity {
    RtmClient rtmClient;
    private static final int PERMISSION_REQ_ID = 22;

    // Permission WRITE_EXTERNAL_STORAGE is not mandatory
    // for Agora RTC SDK, just in case if you wanna save
    // logs to external sdcard.
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    RtmCallManager rtmCallManager;
    EditText editTexts;
    Button button,mess;
    //此处的id是打电话的目标id，当我是被呼叫者的时候这是来电人的id
    String id ="";
    //定义全局变量，方便后面向fragment更新
    private VideoViewFragment fragment;
    private FragmentTransaction transaction;
    private FragmentManager manager;
    //此处id是我的id
    private String myid;
    String TAG ="zzzzzzzzzz";
    private String callId;
    private boolean isCaller =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment =new VideoViewFragment();
        setContentView(R.layout.video_main_lod);
        manager =getSupportFragmentManager();
        transaction =manager.beginTransaction();
        mess =findViewById(R.id.mess);
        button = findViewById(R.id.calls);
        myid=getIntent().getStringExtra("id");
        mess.setVisibility(View.INVISIBLE);
        mess.setOnClickListener(v -> {
            showDialogMessage();
        });
        try {
            rtmClient =RtmClient.createInstance(getApplicationContext(), "f2f80fc4bf964fcaa75acc03dbe9eccb", new RtmClientListener() {
               @Override
               public void onConnectionStateChanged(int i, int i1) {

               }

               @Override
               public void onMessageReceived(RtmMessage rtmMessage, String s) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        Toast.makeText(getApplicationContext(),rtmMessage.getText(),Toast.LENGTH_SHORT).show();
                        fragment.showOutMessage(s+"："+rtmMessage.getText());
                        mess.setVisibility(View.VISIBLE);
                        }
                    });
               }

               @Override
               public void onImageMessageReceivedFromPeer(RtmImageMessage rtmImageMessage, String s) {

               }

               @Override
               public void onFileMessageReceivedFromPeer(RtmFileMessage rtmFileMessage, String s) {

               }

               @Override
               public void onMediaUploadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

               }

               @Override
               public void onMediaDownloadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

               }

               @Override
               public void onTokenExpired() {

               }

               @Override
               public void onPeersOnlineStatusChanged(Map<String, Integer> map) {

               }
           });
            rtmClient.login(null, myid, new ResultCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i(TAG, "Login onSuccess: ");
                }

                @Override
                public void onFailure(ErrorInfo errorInfo) {

                }
            });
             rtmCallManager =rtmClient.getRtmCallManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
        editTexts =findViewById(R.id.edit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callId=id=editTexts.getText().toString();
                HashSet<String> hashSet =new HashSet<>();
                hashSet.add(editTexts.getText().toString());
                rtmClient.queryPeersOnlineStatus(hashSet, new ResultCallback<Map<String, Boolean>>() {
                    @Override
                    public void onSuccess(Map<String, Boolean> stringBooleanMap) {
                        if(stringBooleanMap.get(editTexts.getText().toString())){
                            if(myid.equals(editTexts.getText().toString())){
                               runOnUiThread(()-> Toast.makeText(getApplicationContext(),"不能呼叫自己",Toast.LENGTH_SHORT).show());
                            }else {

                                caller(editTexts.getText().toString());
                            }

                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"用户不在线",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(ErrorInfo errorInfo) {

                    }
                });
            }
        });
        //接受的权限
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
        }
        callee();
    }


    //请求网络
    public void callee(){
        rtmCallManager =rtmClient.getRtmCallManager();
        rtmCallManager.setEventListener(new RtmCallEventListener() {
            @Override
            public void onLocalInvitationReceivedByPeer(LocalInvitation localInvitation) {
                localInvitation.getCalleeId();
            }

            @Override
            public void onLocalInvitationAccepted(LocalInvitation localInvitation, String s) {
                Log.i(TAG, "onLocalInvitationAccepted: ");
            }

            @Override
            public void onLocalInvitationRefused(LocalInvitation localInvitation, String s) {
                Log.i(TAG, "onLocalInvitationRefused: 33333333333");

            }

            @Override
            public void onLocalInvitationCanceled(LocalInvitation localInvitation) {
                Log.i(TAG, "onLocalInvitationCanceled: ");
            }

            @Override
            public void onLocalInvitationFailure(LocalInvitation localInvitation, int i) {
                Log.i(TAG, "onLocalInvitationFailure: ");
            }

            @Override
            public void onRemoteInvitationReceived(RemoteInvitation remoteInvitation) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog(rtmCallManager,remoteInvitation);
                    }
                });
            }

            @Override
            public void onRemoteInvitationAccepted(RemoteInvitation remoteInvitation) {
                //接听之后会回调此方法，跳转fragment
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                           editTexts.setVisibility(View.GONE);
                           mess.setVisibility(View.VISIBLE);

                    }
                });
                //此时是接听者 把拨号者的id赋予id
                id=remoteInvitation.getCallerId();
                isCaller=true;
                reFreshView(fragment,remoteInvitation.getCallerId());

            }

            @Override
            public void onRemoteInvitationRefused(RemoteInvitation remoteInvitation) {
                Log.i(TAG, "onRemoteInvitationRefused:222222222222222 ");

            }

            @Override
            public void onRemoteInvitationCanceled(RemoteInvitation remoteInvitation) {
                Log.i(TAG, "onRemoteInvitationCanceled: ");
            }

            @Override
            public void onRemoteInvitationFailure(RemoteInvitation remoteInvitation, int i) {
                Log.i(TAG, "onRemoteInvitationFailure: ");
            }
        });
    }
    //拨号者：s参数为目标id
    public void caller(String s){


             rtmCallManager =rtmClient.getRtmCallManager();
        rtmCallManager.setEventListener(new RtmCallEventListener() {
            @Override
            public void onLocalInvitationReceivedByPeer(LocalInvitation localInvitation) {
                localInvitation.getCalleeId();

                Log.i("", "onLocalInvitationReceivedByPeer:接听！ ");
            }
            
            @Override
            public void onLocalInvitationAccepted(LocalInvitation localInvitation, String s) {
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         editTexts.setVisibility(View.GONE);
                         mess.setVisibility(View.VISIBLE);
                     }
                 });
                 isCaller=true;
                reFreshView(fragment,myid);
            }

            @Override
            public void onLocalInvitationRefused(LocalInvitation localInvitation, String s) {
                Log.i(TAG, "onLocalInvitationRefused: 111111111111");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"对方拒绝了您的电话",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onLocalInvitationCanceled(LocalInvitation localInvitation) {
                Log.i(TAG, "onLocalInvitationCanceled: ");
            }

            @Override
            public void onLocalInvitationFailure(LocalInvitation localInvitation, int i) {
                Log.i(TAG, "onLocalInvitationFailure: ");
            }

            @Override
            public void onRemoteInvitationReceived(RemoteInvitation remoteInvitation) {
                Log.i(TAG, "onRemoteInvitationReceived: ");
            }

            @Override
            public void onRemoteInvitationAccepted(RemoteInvitation remoteInvitation) {
                Log.i(TAG, "onRemoteInvitationAccepted: ");
            }

            @Override
            public void onRemoteInvitationRefused(RemoteInvitation remoteInvitation) {
                Log.i(TAG, "onRemoteInvitationRefused: ");
            }

            @Override
            public void onRemoteInvitationCanceled(RemoteInvitation remoteInvitation) {
                Log.i(TAG, "onRemoteInvitationCanceled: ");
            }

            @Override
            public void onRemoteInvitationFailure(RemoteInvitation remoteInvitation, int i) {
                Log.i(TAG, "onRemoteInvitationFailure: ");
            }
        });
            final LocalInvitation localInvitation = rtmCallManager.createLocalInvitation(s);
            rtmCallManager.sendLocalInvitation(localInvitation, new ResultCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("zza", "onSuccess: "+"呼叫成功");
                }

                @Override
                public void onFailure(ErrorInfo errorInfo) {

                }
            });

    }
    //来电弹窗 参数为必要接听参数
    public void dialog(final RtmCallManager callManager, final RemoteInvitation remoteInvitation){
        AlertDialog.Builder builder =new AlertDialog.Builder(Video.this);
        builder.setTitle(remoteInvitation.getCallerId()+",来电，是否接听")
                .setNegativeButton("接听", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //如果接听则进入接听者
                        callManager.acceptRemoteInvitation(remoteInvitation, new ResultCallback<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }

                            @Override
                            public void onFailure(ErrorInfo errorInfo) {

                            }
                        });
                    }
                })
                .setPositiveButton("拒接", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callManager.refuseRemoteInvitation(remoteInvitation, new ResultCallback<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }

                            @Override
                            public void onFailure(ErrorInfo errorInfo) {

                            }
                        });
                    }
                })
                .show();

    }
    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }

        return true;
    }
    public void reFreshView(Fragment f, String id){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setVisibility(View.INVISIBLE);
            }
        });
        //跳转指定的fragment 并把通话id传入
        Bundle bundle =new Bundle();
        bundle.putString("id",id);
        f.setArguments(bundle);
        transaction.replace(R.id.alllayout,f);
        transaction.commit();
    }
    public void showDialogMessage(){
        AlertDialog.Builder builder =new AlertDialog.Builder(Video.this);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.messagenput,null);
        EditText editText =view.findViewById(R.id.editMessage);
        builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SendMessageOptions sendMessageOptions =new SendMessageOptions();
                sendMessageOptions.enableOfflineMessaging=false;
                sendMessageOptions.enableHistoricalMessaging=false;
                RtmMessage message =rtmClient.createMessage(editText.getText().toString());

                rtmClient.sendMessageToPeer(id,message, sendMessageOptions, new ResultCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        fragment.showMyMessage("我："+message.getText());
                        Log.i(TAG, "onSuccess: 发送给了："+id);
                    }

                    @Override
                    public void onFailure(ErrorInfo errorInfo) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"发送失败，未知原因",Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "run: "+id);
                            }
                        });
                    }
                });
            }
        }).setNegativeButton("取消",null).setView(view).show();
    }

}
