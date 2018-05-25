package com.vdcrx.rest.api.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

public class SignatureTestHelper {

    private static File file = null;

    public SignatureTestHelper() throws URISyntaxException, NullPointerException {
        final String resource = "static/images/dog.jpg";
        final URL url = getClass().getClassLoader().getResource(resource);
        file = new File(url.toURI());
    }

    public static byte[] getBytes() {

        byte bytes [] = null;

        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }

        return bytes;
    }
}
