package pl.umcs.oop;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class ImageHandler {
    private BufferedImage image;

    public void loadImage(String filePath) {
        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.err.println("Bład wczytywania pliku: " + e.getMessage());
        }
    }

    public void saveImage(String filePath) {
        try {
            String format = filePath.substring(filePath.lastIndexOf('.') + 1);
            ImageIO.write(image, format, new File(filePath));
        } catch (IOException e) {
            System.err.println("Bład zapisu: " + e.getMessage());
        }
    }

    public void increaseBrightness(int value) {
        for (int y=0; y < image.getHeight(); y++) {
            for(int x=0; x< image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb);
                int r = Math.clamp(color.getRed() + value, 0, 255);
                int g = Math.clamp(color.getGreen() + value, 0, 255);
                int b = Math.clamp(color.getBlue() + value, 0, 255);
                Color brighter = new Color(r, g, b);

                image.setRGB(x, y, brighter.getRGB());
            }
        }
    }

    public void increaseBrightnessMultiThreaded(int value) {
        int cores = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[cores];

        System.out.println("Dostepne rdzenie: "+cores);

        int totalSize = image.getHeight() * image.getWidth();
        int chunkSize = totalSize / cores;

        for (int i = 0; i < cores; i++) {
            int start = i*chunkSize;
            int end = (i == cores - 1) ? totalSize : (start + chunkSize);

            threads[i] = new Thread(() -> {
                for (int index = start; index < end; index++) {
                    int x = index % image.getWidth();
                    int y = index / image.getWidth();

                    int rgb = image.getRGB(x, y);
                    Color color = new Color(rgb);
                    int r = Math.clamp(color.getRed() + value, 0, 255);
                    int g = Math.clamp(color.getGreen() + value, 0, 255);
                    int b = Math.clamp(color.getBlue() + value, 0, 255);
                    Color brighter = new Color(r, g, b);

                    image.setRGB(x, y, brighter.getRGB());
                }
            });
        }

        for (Thread t: threads) {
            t.start();
        }

        for (Thread t: threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void increaseBrightnessThreadPool(int value) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int y = 0; y < image.getHeight(); y++) {
            final int row = y;
            executor.execute(() -> {
                for(int x=0; x < image.getWidth(); x++) {
                    int rgb = image.getRGB(x, row);
                    Color color = new Color(rgb);
                    int r = Math.clamp(color.getRed() + value, 0, 255);
                    int g = Math.clamp(color.getGreen() + value, 0, 255);
                    int b = Math.clamp(color.getBlue() + value, 0, 255);
                    Color brighter = new Color(r, g, b);

                    image.setRGB(x, row, brighter.getRGB());
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int[] makeHistogram(String channel) {
        if (image == null) {
            throw new IllegalStateException("Brak wczytanego obrazu.");
        }

        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(cores);

        int height = image.getHeight();
        int width = image.getWidth();

        int[][] localHistograms = new int[cores][256];

        for (int i = 0; i < cores; i++) {
            final int threadId = i;
            final int fromY = (height * i) / cores;
            final int toY = (height * (i + 1)) / cores;

            executor.submit(() -> {
                for (int y = fromY; y < toY; y++) {
                    for (int x = 0; x < width; x++) {
                        int rgb = image.getRGB(x, y);
                        Color color = new Color(rgb);
                        int value = switch (channel.toUpperCase()) {
                            case "R" -> color.getRed();
                            case "G" -> color.getGreen();
                            case "B" -> color.getBlue();
                            default -> throw new IllegalArgumentException("Nieprawidłowy kanał: " + channel);
                        };
                        localHistograms[threadId][value]++;
                    }
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        int[] finalHistogram = new int[256];
        for (int i = 0; i < cores; i++) {
            for (int j = 0; j < 256; j++) {
                finalHistogram[j] += localHistograms[i][j];
            }
        }

        return finalHistogram;
    }

    public class HistogramRenderer {

        public static BufferedImage generateHistogramImage(int[] histogram) {
            int width = 512;
            int height = 300;

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();


            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);


            int max = 0;
            for (int value : histogram) {
                if (value > max) {
                    max = value;
                }
            }

            g2d.setColor(Color.BLACK);

            for (int i = 0; i < histogram.length; i++) {
                int barHeight = (int) ((histogram[i] / (float) max) * (height - 20));
                int barWidth = width / histogram.length;

                int x = i * barWidth;
                int y = height - barHeight;

                g2d.fillRect(x, y, barWidth, barHeight);
            }

            g2d.dispose();
            return image;
        }
    }

}
