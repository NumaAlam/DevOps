package org.example.ui;

import org.example.entity.SocialMediaPost;
import org.example.export.JsonExporter;
import org.example.export.PdfExporter;
import org.example.repository.SocialMediaPostRepository;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {

    private JFreeChart chart;
    private List<SocialMediaPost> posts;

    public MainFrame() {
        setTitle("Social Media Analytics");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        loadData();
        initUI();
    }

    private void loadData() {
        posts = new SocialMediaPostRepository().findAll();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        chart = buildChart();
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(880, 580));
        add(chartPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton pdfButton = new JButton("Export as PDF");
        pdfButton.addActionListener(e -> exportPdf());

        JButton jsonButton = new JButton("Export");
        jsonButton.addActionListener(e -> exportJson());

        buttonPanel.add(pdfButton);
        buttonPanel.add(jsonButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JFreeChart buildChart() {
        Map<String, Integer> likesByPlatform = posts.stream()
                .collect(Collectors.groupingBy(
                        SocialMediaPost::getPlatform,
                        Collectors.summingInt(SocialMediaPost::getLikes)));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        likesByPlatform.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> dataset.addValue(e.getValue(), "Likes", e.getKey()));

        return ChartFactory.createBarChart(
                "Total Likes by Platform",
                "Platform",
                "Total Likes",
                dataset
        );
    }

    private void exportPdf() {
        try {
            new PdfExporter().export(chart, "output/chart.pdf");
            JOptionPane.showMessageDialog(this, "Chart exported to output/chart.pdf");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "PDF export failed: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportJson() {
        try {
            new JsonExporter().export(posts, "output/data.json");
            JOptionPane.showMessageDialog(this, "Data exported to output/data.json");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "JSON export failed: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
