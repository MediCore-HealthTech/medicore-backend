package com.medicore.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.MediaType;

final class SecurityErrorWriter {
    private SecurityErrorWriter() { }
    static void write(HttpServletResponse response, int status, String code, String detail, ObjectMapper mapper, Clock clock) throws IOException {
        response.setStatus(status); response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "about:blank"); body.put("title", status == 401 ? "Unauthorized" : "Forbidden");
        body.put("status", status); body.put("detail", detail); body.put("code", code); body.put("timestamp", OffsetDateTime.now(clock));
        mapper.writeValue(response.getOutputStream(), body);
    }
}
