package com.javadocking.util;

import javax.swing.*;
import java.awt.*;

public class Chart extends JPanel {

    private JPanel chartPanel = null;
    private static int chartCount = 0;

    public JPanel get_chart_panel() {
        return chartPanel;
    }

    public Chart() {
        super(new BorderLayout());

        // Create the chart.
        chartCount++;

        chartPanel = new JPanel();
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 4, 4, 4),
                BorderFactory.createLineBorder(Color.black)));
        add(chartPanel);

    }
}