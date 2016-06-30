package com.ucai.test.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by clawpo on 15/5/29.
 * 账户信息类序列化
 * 实现Parcelable就是为了进行序列化
 */
public class AccountInfo implements Parcelable {

    private int _id;//账户id
    private String accountName;//账户名
    private String password;//密码

    public AccountInfo() {
        super();
    }

    public AccountInfo(String accountName, String password) {
        super();
        this.accountName = accountName;
        this.password = password;
    }

    public AccountInfo(int _id, String accountName, String password) {
        super();
        this._id = _id;
        this.accountName = accountName;
        this.password = password;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private AccountInfo(Parcel in) {
        _id = in.readInt();
        accountName = in.readString();
        password = in.readString();
    }

    /**
     * 重写writeToParcel方法，将对象序列化为一个Parcel对象
     * 即：将类的数据写入外部提供的Parcel中，打包需要传递的数据到Parcel容器保存，以便从 Parcel容器获取数据
     */

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(accountName);
        dest.writeString(password);
    }

    /**
     * Parcelabel的实现，不仅需要implements  Parcelabel，
     * 还需要在类中添加一个静态成员变量CREATOR，这个变量需要实现 Parcelable.Creator 接口。
     */
    public static final Creator<AccountInfo> CREATOR = new Creator<AccountInfo>() {
        public AccountInfo createFromParcel(Parcel in) {
            return new AccountInfo(in);
        }

        public AccountInfo[] newArray(int size) {
            return new AccountInfo[size];
        }
    };
}

