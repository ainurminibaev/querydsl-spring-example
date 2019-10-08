package com.technaxis.querydsl.utils.img;

import lombok.Data;

@Data
public class ImageInformation {

    private final int orientation;
    private final int width;
    private final int height;

    public ImageInformation(int orientation, int width, int height) {
        this.orientation = orientation;
        this.width = width;
        this.height = height;
    }

    public String toString() {
        return String.format("%dx%d,%d", this.width, this.height, this.orientation);
    }
}