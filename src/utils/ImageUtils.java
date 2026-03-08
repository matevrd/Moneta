package utils;

import javafx.scene.image.*;

public class ImageUtils {

    public static Image removeBackground(Image image, int targetColor, int tolerance) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        PixelReader reader = image.getPixelReader();
        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();

        int targetR = (targetColor >> 16) & 0xFF;
        int targetG = (targetColor >> 8) & 0xFF;
        int targetB = targetColor & 0xFF;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = reader.getArgb(x, y);
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                int diff = Math.abs(r - targetR) + Math.abs(g - targetG) + Math.abs(b - targetB);

                if (diff <= tolerance) {
                    writer.setArgb(x, y, 0x00000000);
                } else {
                    writer.setArgb(x, y, argb);
                }
            }
        }
        return result;
    }
}