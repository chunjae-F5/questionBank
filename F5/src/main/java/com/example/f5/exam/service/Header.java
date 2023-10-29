package com.example.f5.exam.service;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.properties.TextAlignment;

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
            font = PdfFontFactory.createFont("D:/pdf_file/HYGothic-Medium-Regular.ttf", PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Canvas canvas = new Canvas(new PdfCanvas(page), page.getPageSize());

        // Add day
        canvas.showTextAligned(day, pageSize.getWidth() / 2, pageSize.getTop(), TextAlignment.CENTER)
                .setFont(font)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER);

        // Add date
        canvas.showTextAligned(date, pageSize.getWidth() - 20, pageSize.getTop() - 40, TextAlignment.RIGHT)
                .setFont(font)
                .setFontSize(50)
                .setTextAlignment(TextAlignment.RIGHT);

        // Add examName
        canvas.showTextAligned(examName, pageSize.getWidth() / 2, pageSize.getTop() - 100, TextAlignment.CENTER)
                .setFont(font)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER);

        // Add topLine
        canvas.showTextAligned(topLine, pageSize.getWidth() / 2, pageSize.getTop() - 140, TextAlignment.CENTER)
                .setFont(font)
                .setFontSize(30)
                .setTextAlignment(TextAlignment.CENTER);

        canvas.close();

        // Draw horizontal line
        PdfCanvas lineCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdf);
        lineCanvas.setStrokeColor(mainLineColor);
        lineCanvas.setLineWidth(mainLineWidth);
        lineCanvas.moveTo(pageSize.getLeft()+10, pageSize.getTop() - 160);
        lineCanvas.lineTo(pageSize.getRight()-10, pageSize.getTop() - 160);
        lineCanvas.stroke();

        // Draw virtical line
        PdfCanvas vlineCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdf);
        vlineCanvas.setStrokeColor(subLineColor);
        vlineCanvas.setLineWidth(subLineWidth);
        vlineCanvas.moveTo(pageSize.getRight()/2, pageSize.getTop() - 160);
        vlineCanvas.lineTo(pageSize.getRight()/2, pageSize.getBottom()+60);
        vlineCanvas.stroke();

    }
}
