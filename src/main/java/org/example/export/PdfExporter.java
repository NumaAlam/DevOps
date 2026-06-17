package org.example.export;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jfree.chart.JFreeChart;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PdfExporter {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public void export(JFreeChart chart, String outputPath) throws IOException {
        new File("output").mkdirs();

        BufferedImage image = chart.createBufferedImage(WIDTH, HEIGHT);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(WIDTH, HEIGHT));
            document.addPage(page);

            PDImageXObject pdImage = LosslessFactory.createFromImage(document, image);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                content.drawImage(pdImage, 0, 0, WIDTH, HEIGHT);
            }

            document.save(outputPath);
        }
    }
}
