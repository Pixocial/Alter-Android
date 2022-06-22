package com.pixocial.alter.module_base;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {

    private String username;
    private int age;

    public Person(String username, int age) {
        super();
        this.username = username;
        this.age = age;
    }

    public Person(Parcel source) {
        username = source.readString();
        age = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeInt(age);
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }

        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }
    };
}
