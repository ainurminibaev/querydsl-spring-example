package com.technaxis.querydsl.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

/**
 * @author Dmitry Sadchikov
 */
@Slf4j
public class WebUtils {

    public static void writeFileByteArrayToResponse(HttpServletResponse response, byte[] fileData,
                                                    String fileName, String contentType) {
        try (OutputStream os = response.getOutputStream()) {
            IOUtils.write(fileData, os);
            os.flush();

            response.setStatus(HttpServletResponse.SC_OK);
            response.addHeader(CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            response.addHeader(CONTENT_TYPE, contentType);
        } catch (Exception e) {
            log.error("Can't decode and write file", e);
            throw new RuntimeException();
        }
    }
}
