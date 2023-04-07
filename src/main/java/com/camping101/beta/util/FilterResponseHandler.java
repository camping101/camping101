package com.camping101.beta.util;

import com.camping101.beta.web.domain.member.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class FilterResponseHandler {

    public static void sendFilterExceptionResponse(HttpServletResponse response,
                                                   String errorMessage, int httpStatus) throws IOException {

        response.setStatus(httpStatus);
        response.setContentType((APPLICATION_JSON_VALUE));
        response.setCharacterEncoding("utf-8");
        ErrorResponse errorResponse = new ErrorResponse(String.valueOf(httpStatus), errorMessage);
        new ObjectMapper().writeValue(response.getWriter(), errorResponse);

    }

    public static void sendSuccessResponse(HttpServletResponse response,
                                           Map<String, String> responseMap)
                                                       throws IOException {

        new ObjectMapper().writeValue(response.getWriter(), responseMap);
    }

}
