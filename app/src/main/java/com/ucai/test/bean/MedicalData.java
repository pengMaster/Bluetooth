package com.ucai.test.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 检查数据的实体类
 * Created by clawpo on 15/5/29.
 */
public class MedicalData implements Parcelable {

    private AccountInfo account;//账户信息对象
    private float bodyHeight; // 身高
    private float bodyWeight;	//体重
    private int heartRate; // 心率
    private float bloodOxy; // 血氧
    private long peStartTime; // 檢測開始時間
    private long peEndTime; // 檢測結束時間

    public MedicalData() {
        super();
    }

    public MedicalData(AccountInfo account, float bodyHeight, int heartRate,
                       int bloodOxy) {
        super();
        this.account = account;
        this.bodyHeight = bodyHeight;
        this.heartRate = heartRate;
        this.bloodOxy = bloodOxy;
    }

    public AccountInfo getAccount() {
        return account;
    }

    public void setAccount(AccountInfo account) {
        this.account = account;
    }

    public float getBodyHeight() {
        return bodyHeight;
    }

    public void setBodyHeight(float bodyHeight) {
        this.bodyHeight = bodyHeight;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public float getBloodOxy() {
        return bloodOxy;
    }

    public void setBloodOxy(float bloodOxy) {
        this.bloodOxy = bloodOxy;
    }

    public long getPeStartTime() {
        return peStartTime;
    }

    public void setPeStartTime(long peStartTime) {
        this.peStartTime = peStartTime;
    }

    public long getPeEndTime() {
        return peEndTime;
    }

    public void setPeEndTime(long peEndTime) {
        this.peEndTime = peEndTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(account, flags);
        dest.writeFloat(bodyHeight);
        dest.writeFloat(bodyWeight);
        dest.writeInt(heartRate);
        dest.writeFloat(bloodOxy);
        dest.writeLong(peStartTime);
        dest.writeLong(peEndTime);
    }

    public MedicalData(Parcel source) {
        account = source.readParcelable(AccountInfo.class.getClassLoader());
        bodyHeight = source.readFloat();
        bodyWeight = source.readFloat();
        heartRate = source.readInt();
        bloodOxy = source.readFloat();
        peStartTime = source.readLong();
        peEndTime = source.readLong();
    }

    public float getBodyWeight() {
        return bodyWeight;
    }

    public void setBodyWeight(float bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

    public static final Creator<MedicalData> CREATOR = new Creator<MedicalData>() {

        @Override
        public MedicalData createFromParcel(Parcel source) {
            return new MedicalData(source);
        }

        @Override
        public MedicalData[] newArray(int size) {
            return new MedicalData[size];
        }

    };
}
