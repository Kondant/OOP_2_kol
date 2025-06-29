package com.example.serwer2023;

import javax.swing.*;
import java.awt.*;

public class FilterGUI {
    private final JSlider slider;
    private final JLabel label;

    public FilterGUI() {
        JFrame frame = new JFrame("Box Blur Filter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        slider = new JSlider(1, 15, 3);
        slider.setMajorTickSpacing(2);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.setPaintLabels(true);

        label = new JLabel("Promień: 3");

        slider.addChangeListener(e -> {
            int val = slider.getValue();
            if (val % 2 == 0) val++; // wymusza nieparzystość
            slider.setValue(val);
            label.setText("Promień: " + val);
        });

        frame.setLayout(new FlowLayout());
        frame.add(slider);
        frame.add(label);
        frame.setSize(300, 100);
        frame.setVisible(true);
    }

    public int getCurrentRadius() {
        return slider.getValue();
    }
}
