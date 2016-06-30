/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ucai.test.control;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ucai.test.EditActivity;
import com.ucai.test.R;
import com.ucai.test.attribute.SampleGattAttributes;
import com.ucai.test.bean.MedicalData;
import com.ucai.test.constant.Constant;
import com.ucai.test.scan.DeviceScanActivity;
import com.ucai.test.service.BluetoothLeService;
import com.ucai.test.utils.Calculaion;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */

/**
 * 对于一个给定的BLE装置,该活动提供了用户界面连接,显示数据,
 * 和显示关贸总协定服务和设备支持的特征。该活动
 * 与{ @code BluetoothLeService },进而与交互
 * 蓝牙API。
 */
//设备控制
public class DeviceControlActivity extends Activity {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";//连接的设备名
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";//设备地址

    private TextView mConnectionState;//连接状态
    private TextView mDataField;
    private TextView mDeviceDataName;
    private TextView mDeviceDataField;
    private String mDeviceName;
    private String mDeviceAddress;
    // private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    //以下声明的
    private TextView tv_weight_center, tv_bmi, tv_fat, tv_measure_muscle, tv_measure_bone, tv_measure_water,
            tv_measure_metabolize, tv_measure_bodyage;

    private String bmi, fat, measure_muscle, measure_bone, measure_water, measure_metabolize, measure_bodyage;
    private double weight;
    private int sex = 2;
    // Code to manage Service lifecycle.
    //代码去管理Service生命周期
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.e("cst", "---------action=" + action);
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                //clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                autoGetTemperature();
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                displayDeviceData(intent.getStringExtra(BluetoothLeService.DEVICE_DATA));
            }
        }
    };

    // If a given GATT characteristic is selected, check for supported features.  This sample
    // demonstrates 'Read' and 'Notify' features.  See
    // http://d.android.com/reference/android/bluetooth/BluetoothGatt.html for the complete
    // list of supported characteristic features.

    /**
     * 如果给定的GATT特征选择,检查支持功能。这个示例
     * 演示了“读”和“通知”功能。看到
     * http://d.android.com/reference/android/bluetooth/BluetoothGatt.html的完整
     * 支持的特征列表。
     */
   /* private final ExpandableListView.OnChildClickListener servicesListClickListner =
            new ExpandableListView.OnChildClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                            int childPosition, long id) {
                    if (mGattCharacteristics != null) {
                        final BluetoothGattCharacteristic characteristic =
                                mGattCharacteristics.get(groupPosition).get(childPosition);
                        final int charaProp = characteristic.getProperties();
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                            // If there is an active notification on a characteristic, clear
                            // it first so it doesn't update the data field on the user interface.
                            if (mNotifyCharacteristic != null) {
                                mBluetoothLeService.setCharacteristicNotification(
                                        mNotifyCharacteristic, false);
                                mNotifyCharacteristic = null;
                            }
                            mBluetoothLeService.readCharacteristic(characteristic);
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            mNotifyCharacteristic = characteristic;
                            mBluetoothLeService.setCharacteristicNotification(
                                    characteristic, true);
                        }
                        return true;
                    }
                    return false;
                }
            };*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void autoGetTemperature() {
        int groupPosition = 0;
        int childPosition = 0;
        Log.e("cst", "autoGetTemperature,mDeviceName=" + mDeviceName);
        if (SampleGattAttributes.DEVICE_NAME_TEMPERATURE.equals(mDeviceName)) {
            groupPosition = 2;
            childPosition = 2;
            mDeviceDataName.setText(R.string.device_temperature);
        } else if (SampleGattAttributes.DEVICE_NAME_WEIGHT.equals(mDeviceName)) {
            groupPosition = 1;
            childPosition = 0;
            mDeviceDataName.setText(R.string.device_weight);
        } else if (SampleGattAttributes.DEVICE_NAME_YUNMAI_WEIGHT.equals(mDeviceName)) {
            groupPosition = 2;
            childPosition = 0;
            mDeviceDataName.setText(R.string.device_weight);
        } else {
            return;
        }
        if (mGattCharacteristics != null) {
            final BluetoothGattCharacteristic characteristic =
                    mGattCharacteristics.get(groupPosition).get(childPosition);
            final int charaProp = characteristic.getProperties();
            Log.e("cst", "autoGetTemperature,groupPosition=" + groupPosition + ",childPosition=" + childPosition
                    + ",charaProp=" + charaProp + ",PROPERTY_READ=" + BluetoothGattCharacteristic.PROPERTY_READ
                    + ",PROPERTY_NOTIFY=" + BluetoothGattCharacteristic.PROPERTY_NOTIFY);
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                // If there is an active notification on a characteristic, clear
                // it first so it doesn't update the data field on the user interface.
                if (mNotifyCharacteristic != null) {
                    mBluetoothLeService.setCharacteristicNotification(
                            mNotifyCharacteristic, false);
                    mNotifyCharacteristic = null;
                }
                Log.e("cst", "autoGetTemperature,readCharacteristic(" + characteristic + ")");
                mBluetoothLeService.readCharacteristic(characteristic);

//                            Log.e("cst", "OnChildClickListener,setCharacteristicNotification("+characteristic+",true)");
//                            mBluetoothLeService.setCharacteristicNotification(characteristic,true);
            }
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                mNotifyCharacteristic = characteristic;
                mBluetoothLeService.setCharacteristicNotification(
                        characteristic, true);
            }
        }
    }

   /* private void clearUI() {
        mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
        mDataField.setText(R.string.no_data);

        //   mDeviceDataField.setText(R.string.no_data);
//        mDeviceDataName.setText(R.string.device_data);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);
        initView();
        //获得EditActivity传入的值
        Intent intentCon = getIntent();
        sex = intentCon.getIntExtra("sex", 1);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        // Sets up UI references.更新UI
        //TODO 更改
        //((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        //mGattServicesList = (ExpandableListView) findViewById(R.id.gatt_services_list);
        //  mGattServicesList.setOnChildClickListener(servicesListClickListner);
        mConnectionState = (TextView) findViewById(R.id.connection_state);//连接状态
        mDataField = (TextView) findViewById(R.id.data_value);
        mDeviceDataField = (TextView) findViewById(R.id.device_type_value);//测量类型的值
        mDeviceDataName = (TextView) findViewById(R.id.device_type_name);//测量类型

//        getActionBar().setTitle(mDeviceName);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        //TODO 启动服务（绑定）
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);


        //点击开始测量，去测量体重
        tv_weight_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceControlActivity.this, DeviceScanActivity.class);
                new DeviceScanActivity();
//                startActivity(intent);
//                finish();
            }
        });
    }

    //初始化控件
    private void initView() {
        tv_weight_center = (TextView) findViewById(R.id.device_type_value);
        tv_bmi = (TextView) findViewById(R.id.tv_bmi);
        tv_fat = (TextView) findViewById(R.id.tv_fat);
        tv_measure_muscle = (TextView) findViewById(R.id.tv_measure_muscle);
        tv_measure_bone = (TextView) findViewById(R.id.tv_measure_bone);
        tv_measure_water = (TextView) findViewById(R.id.tv_measure_water);
        tv_measure_metabolize = (TextView) findViewById(R.id.tv_measure_metabolize);
        tv_measure_bodyage = (TextView) findViewById(R.id.tv_measure_bodyage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    //选项菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    /**
     * 再点击选项菜单的时候调用的方法
     * 返回值为turn时，menu才有效
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //更新连接状态
    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }

    //展示数据，根据对应的最下面一行的数据，
    private void displayData(String data) {
        if (data != null) {
            mDataField.setText(data);
        }
    }

    //展示体重数据，计算其他·数据
    private void displayDeviceData(String data) {
        if (data != null) {
            //TODO 设置体重值
            mDeviceDataField.setText(data);
            String newdata = data.substring(0, 4);
            weight = Double.valueOf(newdata);
            // Log.e("/weight", weight + "");
            // 数据格式化工具 将数值保留两位小数
            DecimalFormat df = new DecimalFormat("#0.00");
            int height = EditActivity.toHeight();
            int age = EditActivity.toAge();
            //获取值
            bmi = df.format(Calculaion.Bmi(height, weight));
            fat = df.format(Calculaion.Fat(height, weight, age, sex));
            measure_muscle = df.format(Calculaion.Muscle(height, weight, age, sex));
            measure_bone = df.format(Calculaion.Bone(height, weight, age, sex));
            measure_water = df.format(Calculaion.Water(height, weight, age, sex));
            measure_metabolize = df.format(Calculaion.Metabolize(height, weight, age, sex));
            measure_bodyage = df.format(Calculaion.BodyAge(age, sex, Calculaion.Bmi(height, weight)));

            //设置控件显示的值
            tv_bmi.setText(bmi);
            tv_fat.setText(fat + "%");
            tv_measure_muscle.setText(measure_muscle + "%");
            tv_measure_bone.setText(measure_bone + "%");
            tv_measure_water.setText(measure_water + "%");
            tv_measure_metabolize.setText(measure_metabolize + "大卡/天");
            tv_measure_bodyage.setText(measure_bodyage + "岁");

            if (data.endsWith("㎏")) {
                if (data.equals("0.0 ㎏")) {
                    Log.e("cst", "data=0.0,so autoGetTemperature");
                    autoGetTemperature();
                } else {
//                    Log.e("cst","data!=0.0,mBluetoothLeService.disconnect()");
//                    mBluetoothLeService.disconnect();
                    final MedicalData mdata = new MedicalData();
                    //TODO 设置体重
                    mdata.setBodyWeight(Float.valueOf(data.split(" ")[0]));
                    Log.e("cst", "----post,data=" + mdata.getBodyWeight());
                    //sendDataByPost(mdata);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendDataByPost(mdata);//获取数据
                        }
                    }).start();

                }
            }
        }
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.

    /**
     * 演示了如何遍历所支持的GATT服务/特征。
     * 在这个示例中,我们填充数据结构绑定到ExpandableListView在UI。
     *
     * @param gattServices
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        //遍历可用的GATT服务
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();
            //Log.e("/g", "" + uuid);
            // Loops through available Characteristics.
            //遍历可用特性
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
                //TODO 获取到所有的uuid
                Log.e("/t", "" + uuid);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
            Log.e("cst", "load over");
        }

        /*SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                this,
                gattServiceData,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{LIST_NAME, LIST_UUID},
                new int[]{android.R.id.text1, android.R.id.text2},
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{LIST_NAME, LIST_UUID},
                new int[]{android.R.id.text1, android.R.id.text2}

        );
        mGattServicesList.setAdapter(gattServiceAdapter);*/
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    //通过post请求发送数据
    private int sendDataByPost(MedicalData data) {
        if (null == data) {
            return 0;
        }
        boolean DEBUG_SWL = true;
        if (DEBUG_SWL) {
            Log.i(TAG, "run--------sendDataByPost----------");//通过post发送数据
        }
        JSONObject obj = transToJson(data);//将消息转化成json数据
        HttpResponse httpResponse = null;
        HttpPost httpPost = new HttpPost(Constant.URL_USER_DATA);
        HttpClient client = new DefaultHttpClient();

        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000); //设置请求超时
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);    //读取超时

        try {
            httpPost.setEntity(new StringEntity(obj.toString()));
            httpResponse = new DefaultHttpClient().execute(httpPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpResponse) {
            //  Toast.makeText(this, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
            return -1; //网络异常
        }
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            if (DEBUG_SWL) {
                Log.i(TAG,
                        "httpResponse.getStatusLine().getStatusCode() == 200");
            }
            try {
                String result = EntityUtils.toString(httpResponse
                        .getEntity());
                if (DEBUG_SWL) {
                    Log.i(TAG, "result---------------" + result);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 1; //post成功
        } else {
            return 2; //post出错
        }
    }

    //TODO 数据转换成obj类
    private JSONObject transToJson(MedicalData data) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("username", "zhangsan");
            obj.put("PEST", data.getPeStartTime());
            obj.put("PEET", data.getPeEndTime());
            obj.put("height", data.getBodyHeight());
            obj.put("weight", data.getBodyWeight());
            obj.put("heartR", data.getHeartRate());
            obj.put("bloodO", data.getBloodOxy());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    //
    public void toEdit(View view) {
        Intent intentToEdit = new Intent(DeviceControlActivity.this, EditActivity.class);
        startActivity(intentToEdit);
        finish();
    }


}
