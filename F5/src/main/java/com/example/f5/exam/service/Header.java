package com.example.f5.exam.service;

import com.example.f5.util.FileUrl;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class Header implements IEventHandler {
    //    private String header;
    private String day;
    private String date;
    private String examName;
    private String topLine;
    private Color mainLineColor;
    private float mainLineWidth;
    private Color subLineColor;
    private float subLineWidth;

    private int fontSizeS = 10;
    private int fontSizeM = 20;
    private int fontSizeL = 30;
    private Color fontColor = new DeviceRgb(0, 0, 0);

    private String DEST;
    private String PDF_URL = "pdf_file";

    @Value("${windows.file.pdfDir}")
    private String widowsFileDir;

    @Value("${linux.file.pdfDir}")
    private String linuxFileDir;

    public Header(String day, String date, String examName, String topLine, Color mainLineColor, float mainLineWidth, Color subLineColor, float subLineWidth) {
        this.day = day;
        this.date = date;
        this.examName = examName;
        this.topLine = topLine;
        this.mainLineColor = mainLineColor;
        this.mainLineWidth = mainLineWidth;
        this.subLineColor = subLineColor;
        this.subLineWidth = subLineWidth;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();

        PdfPage page = docEvent.getPage();
        Rectangle pageSize = page.getPageSize();

        PdfFont font;
        try {
            FileUrl fileUrl = new FileUrl();
            DEST = fileUrl.selectUrl() + PDF_URL;

            font = PdfFontFactory.createFont(DEST + "HYGothic-Medium-Regular.ttf", PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PdfCanvas pdfCanvas = new PdfCanvas(page);
        Canvas canvas = new Canvas(new PdfCanvas(page), page.getPageSize());

        // Add date
        canvas.showTextAligned(date, pageSize.getWidth() - 20, pageSize.getTop() - 40, TextAlignment.RIGHT)
                .setFont(font)
                .setFontColor(mainLineColor)
                .setFontSize(50)
                .setTextAlignment(TextAlignment.RIGHT);

        // Add examName
        canvas.showTextAligned(examName, pageSize.getWidth() / 2, pageSize.getTop() - 100, TextAlignment.CENTER)
                .setFont(font)
                .setFontColor(fontColor)
                .setFontSize(fontSizeM)
                .setTextAlignment(TextAlignment.CENTER);

        // Add topLine
        canvas.showTextAligned(topLine, pageSize.getWidth() / 2, pageSize.getTop() - 140, TextAlignment.CENTER)
                .setFont(font)
                .setFontColor(fontColor)
                .setFontSize(fontSizeS)
                .setTextAlignment(TextAlignment.CENTER);

        canvas.close();

        // Draw horizontal line
        PdfCanvas lineCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdf);
        lineCanvas.setStrokeColor(mainLineColor);
        lineCanvas.setLineWidth(mainLineWidth);
        lineCanvas.moveTo(pageSize.getLeft() + 10, pageSize.getTop() - 160);
        lineCanvas.lineTo(pageSize.getRight() - 10, pageSize.getTop() - 160);
        lineCanvas.stroke();

        // Draw virtical line
        PdfCanvas vlineCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdf);
        vlineCanvas.setStrokeColor(subLineColor);
        vlineCanvas.setLineWidth(subLineWidth);
        vlineCanvas.moveTo(pageSize.getRight() / 2, pageSize.getTop() - 160);
        vlineCanvas.lineTo(pageSize.getRight() / 2, pageSize.getBottom() + 60);
        vlineCanvas.stroke();

    }
}
