package com.example.f5.exam.service;

import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import java.io.IOException;
import java.util.Set;

public class ImageSavingListener implements IEventListener {
    @Override
    public void eventOccurred(IEventData data, EventType type) {
        if (type == EventType.RENDER_IMAGE) {
            ImageRenderInfo imageInfo = (ImageRenderInfo) data;
            PdfImageXObject imageObject = imageInfo.getImage();
            // Save the image to a ByteArrayOutputStream
            imageObject.getImageBytes();
            // Write the image bytes to the ByteArrayOutputStream
            // Replace this with your actual image-saving logic
        }
    }

    @Override
    public Set<EventType> getSupportedEvents() {
        return null;
    }
}
