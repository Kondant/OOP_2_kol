package com.example.serwer2023;

import java.awt.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImageProcessor {
    public static BufferedImage applyBoxBlur(File inputFile, int radius) throws IOException {
        BufferedImage input = ImageIO.read(inputFile);
        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage output = new BufferedImage(width, height, input.getType());

        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(cores);
        int chunkHeight = height / cores;

        for (int i = 0; i < cores; i++) {
            int startY = i * chunkHeight;
            int endY = (i == cores - 1) ? height : startY + chunkHeight;

            executor.submit(() -> {
                for (int y = startY; y < endY; y++) {
                    for (int x = 0; x < width; x++) {
                        applyKernel(input, output, x, y, radius);
                    }
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return output;
    }

    private static void applyKernel(BufferedImage input, BufferedImage output, int x, int y, int radius) {
        int size = radius;
        int sumR = 0, sumG = 0, sumB = 0, count = 0;

        for (int dy = -size / 2; dy <= size / 2; dy++) {
            for (int dx = -size / 2; dx <= size / 2; dx++) {
                int nx = x + dx, ny = y + dy;
                if (nx >= 0 && ny >= 0 && nx < input.getWidth() && ny < input.getHeight()) {
                    int rgb = input.getRGB(nx, ny);
                    Color c = new Color(rgb);
                    sumR += c.getRed();
                    sumG += c.getGreen();
                    sumB += c.getBlue();
                    count++;
                }
            }
        }

        Color newColor = new Color(sumR / count, sumG / count, sumB / count);
        output.setRGB(x, y, newColor.getRGB());
    }
}
