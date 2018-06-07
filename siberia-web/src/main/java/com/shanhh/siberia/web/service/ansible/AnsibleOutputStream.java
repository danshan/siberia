package com.shanhh.siberia.web.service.ansible;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.apache.commons.exec.LogOutputStream;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * @author Dan
 * @since 2016-06-22 19:16
 */
public abstract class AnsibleOutputStream extends LogOutputStream {

    private final String filename;
    private final File file;
    private final DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");


    public AnsibleOutputStream(String filename) throws IOException {
        this.filename = filename;
        this.file = new File(filename);

        setFile();
    }

    private synchronized void setFile() throws IOException {
        // Check directory
        String fileName = filename;
        File file = new File(fileName);
        fileName = file.getCanonicalPath();
        file = new File(fileName);
        File dir = new File(file.getParent());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!dir.isDirectory() || !dir.canWrite()) {
            throw new IOException("Cannot write log directory " + dir);
        }

        if (file.exists() && !file.canWrite()) {
            throw new IOException("Cannot write log file " + file);
        }

    }

    protected void appendToFile(String line) {
        try {
            String time = DateTime.now().toString(format);
            Files.append(String.format("%s %s\n", time, line), file, Charsets.UTF_8);
        } catch (IOException e) {
            return;
        }
    }

}
