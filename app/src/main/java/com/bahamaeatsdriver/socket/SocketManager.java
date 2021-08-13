package com.bahamaeatsdriver.socket;

import android.util.Log;

import com.bahamaeats.constant.Constants;
import com.bahamaeatsdriver.activity.Home_Page;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketManager {
    private Socket mSocket;
    private List<Observer> observerList;
    // sockets events
    public static final String CONNECT_USER_EMITTER = "connectUser";
    public static final String UPDATE_LOCATION = "sendLatLng";
    //listener
    public static final String CONNECT_USER_LISTENER = "connectListener";
    public static final String UPDATE_LOCATION_LISTENER = "receiveLatLng";
    public static final String TAKE_ORDER_STATUS = "driver_offline";

    public void initializeSocket() {
        mSocket = getSocket();
        observerList = new ArrayList<>();
        mSocket.connect();
        disconnectAll();
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);

    }

    public Socket getSocket() {
        {
            try {
                mSocket = IO.socket(Constants.SOCKET_BASE_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return mSocket;
    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("Socket", "CONNECTED");
            try {
                if (!Home_Page.Companion.getLoginDiverId().isEmpty()){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", Constants.DRIVER_TYPE);
                    jsonObject.put("userId", Home_Page.Companion.getLoginDiverId());
                    mSocket.emit(CONNECT_USER_EMITTER, jsonObject);
                    mSocket.off(CONNECT_USER_LISTENER);
                    mSocket.on(CONNECT_USER_LISTENER, onConnectListener);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };


    public void disconnectAll() {
        if (mSocket != null) {
            mSocket.off(Socket.EVENT_CONNECT, onConnect);
            mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.off();
        }
    }

    private Emitter.Listener onConnectListener = args -> {
        try {
            JSONObject jsonObject = ((JSONObject) args[0]);
            Log.i("Socket", "onConnectListener" + jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    };



    public void onRegister(Observer observer) {
        if (observerList != null) {
            observerList.add(observer);
        } else {
            observerList = new ArrayList<>();
            observerList.add(observer);
        }
    }


    public void unRegister(Observer observer) {
        if (observerList != null) {
            for (int i = 0; i < observerList.size(); i++) {
                Observer model = observerList.get(i);
                if (model == observer) {
                    observerList.remove(model);
                }
            }
        }
    }


    private Emitter.Listener onDisconnect = args -> Log.i("Socket", "DISCONNECTED");


    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("Socket", "CONNECTION ERROR");
            try {
                JSONObject jsonObject = ((JSONObject) args[0]);
                Log.i("Socket", "onConnectListener" + jsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Observer observer : observerList) {
                observer.onError("errorSocket", args);
            }
        }
    };


    public void saveVendorLocation(JSONObject jsonObject) {
        if (!mSocket.connected()) {
            mSocket.connect();
            mSocket.emit(UPDATE_LOCATION, jsonObject);
            mSocket.off(UPDATE_LOCATION_LISTENER);
            mSocket.on(UPDATE_LOCATION_LISTENER, onSaveVendorLocListener);
        } else {
            mSocket.emit(UPDATE_LOCATION, jsonObject);
            mSocket.off(UPDATE_LOCATION_LISTENER);
            mSocket.on(UPDATE_LOCATION_LISTENER, onSaveVendorLocListener);
        }
    }


    public void getDriverTakeOrderStatus() {
        if (!mSocket.connected()) {
            mSocket.connect();
            mSocket.off(UPDATE_LOCATION_LISTENER);
            mSocket.on(TAKE_ORDER_STATUS, onGetDriverTakeOrderStatusListener);
        } else {
            mSocket.off(UPDATE_LOCATION_LISTENER);
            mSocket.on(TAKE_ORDER_STATUS, onGetDriverTakeOrderStatusListener);
        }
    }

    private Emitter.Listener onSaveVendorLocListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("Socket", "onSaveChatListener");

            for (Observer observer : observerList) {
                observer.onResponse(UPDATE_LOCATION_LISTENER, args);
            }
        }
    };

    private Emitter.Listener onGetDriverTakeOrderStatusListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("Socket", "onSaveChatListener");

            for (Observer observer : observerList) {
                observer.onResponse(TAKE_ORDER_STATUS, args);
            }
        }
    };

    public interface Observer {
        void onError(String event, Object... args);
        void onResponse(String event, Object... args);
    }

}

