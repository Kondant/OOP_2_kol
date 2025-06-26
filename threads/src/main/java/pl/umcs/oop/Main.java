package pl.umcs.oop;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ImageHandler ih = new ImageHandler();

        ih.loadImage("C:\\Users\\konra\\Desktop\\test.png");
        long start = System.currentTimeMillis();
        ih.increaseBrightness(0x77);
        long end = System.currentTimeMillis();
        ih.saveImage("out.png");
        System.out.println("czas na 1 wątku: "+(end-start) + "ms");

        ih.loadImage("C:\\Users\\konra\\Desktop\\test.png");
        long start2 = System.currentTimeMillis();
        ih.increaseBrightnessMultiThreaded(0x77);
        long end2 = System.currentTimeMillis();
        ih.saveImage("out.png");
        System.out.println("czas na wielu wątkach: "+(end2-start2) + "ms");

        ih.loadImage("C:\\Users\\konra\\Desktop\\test.png");
        long start3 = System.currentTimeMillis();
        ih.increaseBrightnessThreadPool(0x77);
        long end3 = System.currentTimeMillis();
        ih.saveImage("out.png");
        System.out.println("czas na wielu wątkach: "+(end3-start3) + "ms");

        ih.loadImage("C:\\Users\\konra\\Desktop\\test.png");
        int[] histogram = ih.makeHistogram("R");
        for (int i = 0; i < histogram.length; i++) {
            System.out.println("R[" + i + "] = " + histogram[i]);
        }
        BufferedImage histImage = ImageHandler.HistogramRenderer.generateHistogramImage(histogram);
        ImageIO.write(histImage, "png", new File("histogram_output.png"));

    }
}