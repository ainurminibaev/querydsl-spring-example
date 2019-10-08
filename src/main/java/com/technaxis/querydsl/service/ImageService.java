package com.technaxis.querydsl.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.technaxis.querydsl.utils.img.ImageInformation;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by ainurminibaev on 06.03.18.
 */
@Slf4j
@Service
public class ImageService {

    public byte[] fitImageToSize(byte[] imageData, Integer size, String extension) {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
            double height = img.getHeight();
            double width = img.getWidth();
            if (height > size || width > size) {
                double multiplier = Math.max(height / size, width / size);
                height /= multiplier;
                width /= multiplier;
                int intHeight = (int) height;
                int intWidth = (int) width;
                img = Scalr.resize(img,
                        Scalr.Method.ULTRA_QUALITY,
                        Scalr.Mode.AUTOMATIC,
                        intWidth,
                        intHeight,
                        Scalr.OP_ANTIALIAS);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img, extension, baos);
                return baos.toByteArray();
            }
        } catch (Exception e) {
            log.error("Error resize image. " + e);
        }
        return null;
    }

    private static ImageInformation readImageInformation(byte[] imageFile) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(imageFile));
            Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);

            int orientation = 1;
            orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);

            int width = jpegDirectory.getImageWidth();
            int height = jpegDirectory.getImageHeight();
            return new ImageInformation(orientation, width, height);
        } catch (Exception e) {
            return null;
        }
    }

    private static AffineTransform getExifTransformation(ImageInformation info) {

        AffineTransform t = new AffineTransform();

        switch (info.getOrientation()) {
            case 1:
                break;
            case 2: // Flip X
                t.scale(-1.0, 1.0);
                t.translate(-info.getWidth(), 0);
                break;
            case 3: // PI rotation
                t.translate(info.getWidth(), info.getHeight());
                t.rotate(Math.PI);
                break;
            case 4: // Flip Y
                t.scale(1.0, -1.0);
                t.translate(0, -info.getHeight());
                break;
            case 5: // - PI/2 and Flip X
                t.rotate(-Math.PI / 2);
                t.scale(-1.0, 1.0);
                break;
            case 6: // -PI/2 and -width
                t.translate(info.getHeight(), 0);
                t.rotate(Math.PI / 2);
                break;
            case 7: // PI/2 and Flip
                t.scale(-1.0, 1.0);
                t.translate(-info.getHeight(), 0);
                t.translate(0, info.getWidth());
                t.rotate(3 * Math.PI / 2);
                break;
            case 8: // PI / 2
                t.translate(0, info.getWidth());
                t.rotate(3 * Math.PI / 2);
                break;
        }

        return t;
    }

    private static BufferedImage transformImage(BufferedImage image, AffineTransform transform) {
        try {
            AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            BufferedImage destinationImage = op.createCompatibleDestImage(image, null);
            Graphics2D g = destinationImage.createGraphics();
            g.clearRect(0, 0, destinationImage.getWidth(), destinationImage.getHeight());
            destinationImage = op.filter(image, destinationImage);
            return destinationImage;

        } catch (Exception e) {
            return image;
        }
    }

    public byte[] rotateExif(byte[] data, String extension) {
        try {
            ImageInformation imageInformation = readImageInformation(data);
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
            AffineTransform exifTransformation = getExifTransformation(imageInformation);
            BufferedImage result = transformImage(img, exifTransformation);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(result, extension, baos);
            return baos.toByteArray();
        } catch (Exception e) {
            return data;
        }
    }
}
