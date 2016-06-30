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

package com.ucai.test.attribute;

import java.util.HashMap;
import java.util.UUID;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 * 这个类包括一小部分GATT服务属性用于演示目的。
 */
public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";//心率检测
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    //TODO 体重服务
    public static String WEIGHT_MEASUREMENT_SERVICE = "f433bd80-75b8-11e2-97d9-0002a5d5c51b";//体重测量服务
   // public static String WEIGHT_MEASUREMENT_SERVICE = "FFE4";//体重测量服务
    //public static String WEIGHT_MEASUREMENT_SERVICE = "0000ffe4-0000-1000-8000-00805f9b34fb";//体重测量服务
    public static String UUID_CCC_STRING = "00002902-0000-1000-8000-00805f9b34fb";

    public static String DEVICE_NAME_TEMPERATURE = "BLT_MODT";
    public static String TEMPERATURE_STRING = "0000ffe1-0000-1000-8000-00805f9b34fb";//AA 06 11 00 38 0B 00 5A//28.75
    public static String TEMPERATURE_BEGIN_STRING_INDEX_0 = "AA";
    public static String TEMPERATURE_BEGIN_STRING_INDEX_1 = "06";

    public static String DEVICE_NAME_WEIGHT = "VScale";
    //TODO 体重测量的UUID
    //public static String WEIGHT_MEASUREMENT = "1a2ea400-75b9-11e2-be05-0002a5d5c51b";
    public static String WEIGHT_MEASUREMENT = "0000ffe4-0000-1000-8000-00805f9b34fb";//在适配器中log出来的uuid
    //public static String WEIGHT_MEASUREMENT = "FFE4";
    public static final UUID UUID_WEIGHT_MEASUREMENT = UUID.fromString(WEIGHT_MEASUREMENT);

    public static String DEVICE_NAME_YUNMAI_WEIGHT = "YUNMAI-SIGNAL-CW";
    public static String DEVICE_NAME_YUNMAI_WEIGHT_PREFIX1 = "0D 15 0B 01 55";//0D 15 0B 01 55 79 58 46 19 27 13--8,9
    public static String DEVICE_NAME_YUNMAI_WEIGHT_PREFIX2 = "0D 15 12 02 00 55";//0D 15 12 02 00 55 79 58 46 06 01 FA 65 19 27 00 00 91--13,14
    public static final UUID UUID_YUNMAI_WEIGHT_MEASUREMENT_00 = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_YUNMAI_WEIGHT_MEASUREMENT_01 = UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_YUNMAI_WEIGHT_MEASUREMENT_02 = UUID.fromString("00002a02-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_YUNMAI_WEIGHT_MEASUREMENT_03 = UUID.fromString("00002a03-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_YUNMAI_WEIGHT_MEASUREMENT_04 = UUID.fromString("00002a04-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_YUNMAI_WEIGHT_MEASUREMENT_10 = UUID.fromString("00002a05-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_YUNMAI_WEIGHT_MEASUREMENT_20 = UUID.fromString("0000ffe4-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_YUNMAI_WEIGHT_MEASUREMENT_30 = UUID.fromString("0000ffe9-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_YUNMAI_WEIGHT_MEASUREMENT_40 = UUID.fromString("d618d001-6000-1000-8000-000000000000");
    public static final UUID UUID_YUNMAI_WEIGHT_MEASUREMENT_41 = UUID.fromString("d618d002-6000-1000-8000-000000000000");
    //TODO 体重测量
    public static final UUID UUID_HUMIDITY_SERVICE = UUID.fromString(WEIGHT_MEASUREMENT_SERVICE);
    public static final UUID UUID_HUMIDITY_DATA = UUID.fromString(WEIGHT_MEASUREMENT);
    public static final UUID UUID_HUMIDITY_CONF = UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG);
    public static final UUID UUID_CCC = UUID.fromString(UUID_CCC_STRING);
    public static final UUID UUID_TEMPERATURE = UUID.fromString(TEMPERATURE_STRING);

    public static final byte[] ENABLE_SENSOR = {0x01};

    static {
        //样品服务
        // Sample Services.
        attributes.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate Service");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        //样品特性
        // Sample Characteristics.
        attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
        attributes.put(WEIGHT_MEASUREMENT_SERVICE, "Weight Measurement Service");
        attributes.put(WEIGHT_MEASUREMENT, "Weight Measurement");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
