package com.technaxis.querydsl.model.enums;

/**
 * Created by Vitaly on 27.02.2018.
 */
public enum MediaFormat {
    MPEG4(0), IMAGE_400(500);

    private int size = 0;

    MediaFormat(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
