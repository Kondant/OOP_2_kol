package com.example.serwer2023;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static File receiveAndSaveImage(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);

        long size = dis.readLong(); // <- klient wysyła długość jako long

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[8192];
        long totalRead = 0;
        int read;

        while (totalRead < size && (read = dis.read(chunk)) != -1) {
            buffer.write(chunk, 0, read);
            totalRead += read;
        }

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(buffer.toByteArray()));
        if (image == null) {
            throw new IOException("Nie udało się zdekodować obrazu (image == null).");
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        File dir = new File("images");
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, timestamp + ".png");
        ImageIO.write(image, "png", file);

        return file;
    }


    public static File saveImage(BufferedImage image) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss'_SAVED'"));
        File file = new File("images", timestamp + ".png");
        ImageIO.write(image, "png", file);
        return file;
    }

    public static void sendImage(OutputStream out, File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeLong(bytes.length); // zamiast writeInt
        dos.write(bytes);
        dos.flush();

    }
}
