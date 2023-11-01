package com.example.f5.util;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ErrorHandler implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.valueOf(status.toString());

            //404
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "html/error";
            }
        }
        return "html/error";
    }

    @GetMapping("/loading")
    @ResponseBody
    public ResponseEntity<?> someEndpoint() {
        // 로직 처리
        return ResponseEntity.ok("Success");
    }
}
