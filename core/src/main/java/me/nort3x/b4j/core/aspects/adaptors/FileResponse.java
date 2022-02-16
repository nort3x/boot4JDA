package me.nort3x.b4j.core.aspects.adaptors;

import net.dv8tion.jda.api.utils.AttachmentOption;

import java.io.*;

public class FileResponse {
    final InputStream inputStream;
    final AttachmentOption[] attachmentOptions;
    final String name;

    public FileResponse(File file, String name, AttachmentOption... options) throws FileNotFoundException {
        this(new FileInputStream(file), name, options);
    }

    public FileResponse(byte[] buffer, String name, AttachmentOption... options) {
        this(new ByteArrayInputStream(buffer), name, options);
    }

    public FileResponse(InputStream inputStream, String name, AttachmentOption... options) {
        this.inputStream = inputStream;
        this.name = name;
        this.attachmentOptions = options;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public AttachmentOption[] getAttachmentOptions() {
        return attachmentOptions;
    }

    public String getName() {
        return name;
    }
}
