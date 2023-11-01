package com.example.f5.exam.service;

import com.example.f5.util.FileUrl;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.MalformedURLException;

public class Footer implements IEventHandler {
    protected PdfFormXObject placeholder;
    protected float side = 20;
    protected float x = 300;
    protected float y = 25;
    protected float space = 4.5f;
    protected float descent = 3;

    private Color subLineColor;
    private float subLineWidth;
    private Color fontColor = new DeviceRgb(0, 0, 20);

    private Image logo;

    private String PDF_URL = "pdf_file";
    private String DEST;

    @Value("${windows.file.pdfDir}")
    private String widowsFileDir;

    @Value("${linux.file.pdfDir}")
    private String linuxFileDir;

    public Footer(Color subLineColor, float subLineWidth) {
        placeholder = new PdfFormXObject(new Rectangle(0, 0, side, side));
        this.subLineColor = subLineColor;
        this.subLineWidth = subLineWidth;

        FileUrl fileUrl = new FileUrl();
        DEST = fileUrl.selectUrl(widowsFileDir, linuxFileDir) + PDF_URL;
        if (DEST.contains("C:")) {
            DEST = DEST + "\\";

        } else {
            DEST = DEST + "/";
        }

        try {
            logo = new Image(ImageDataFactory.create(DEST + "f5.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        int pageNumber = pdf.getPageNumber(page);
        Rectangle pageSize = page.getPageSize();

        // Creates drawing canvas
        PdfCanvas pdfCanvas = new PdfCanvas(page);
        Canvas canvas = new Canvas(pdfCanvas, pageSize);

        // Draw horizontal line
        pdfCanvas.setStrokeColor(subLineColor);
        pdfCanvas.setLineWidth(subLineWidth);
        pdfCanvas.moveTo(pageSize.getLeft() + 10, pageSize.getBottom() + 60);
        pdfCanvas.lineTo(pageSize.getRight() - 10, pageSize.getBottom() + 60);
        pdfCanvas.stroke();

        float logoWidth = logo.getImageWidth() / 4;
        float logoHeight = logo.getImageHeight() / 4;
        float center = (pageSize.getLeft() + pageSize.getRight()) / 2;

        try {

            FileUrl fileUrl = new FileUrl();
            DEST = fileUrl.selectUrl(widowsFileDir, linuxFileDir) + PDF_URL;
            if (DEST.contains("C:")) {
                DEST = DEST + "\\";

            } else {
                DEST = DEST + "/";
            }

            Image logoImage = new Image(ImageDataFactory.create(DEST + "f5.png"));
            logoImage.scaleToFit(logoWidth, logoHeight);

            // 가운데에 이미지 배치
            float imageX = center - (logoWidth / 2);
            float imageY = pageSize.getBottom() + 12;

            // 오른쪽 끝에 페이지 번호와 총 페이지 수 배치
            float rightX = pageSize.getRight() - 70;

            canvas.add(logoImage.setFixedPosition(pageNumber, imageX, imageY));

            // 페이지 번호와 총 페이지 수
            Paragraph p = new Paragraph()
                    .add("Page ")
                    .add(String.valueOf(pageNumber))
                    .add(" of ")
                    .add(Integer.toString(pdf.getNumberOfPages()))
                    .setFontColor(fontColor);
            canvas.showTextAligned(p, x + 250, y, TextAlignment.CENTER);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        canvas.close();

    }

}
