package com.github.johnsonmoon.rsaencryptionanddecryption.entity;

/**
 * Create by johnsonmoon at 2018/12/6 10:37.
 */
public class UploadParam {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "UploadParam{" +
                "text='" + text + '\'' +
                '}';
    }
}
