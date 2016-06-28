package de.rohitmisra.planningpoker.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.MissingServletRequestParameterException;

import de.rohitmisra.planningpoker.pojo.ErrorResponse;
import de.rohitmisra.planningpoker.pojo.IGenericResponse;

public class BaseController {
    public static String serializeToJson(final IGenericResponse serviceResponse, final String callback) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        ByteArrayOutputStream jsonOutputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(jsonOutputStream, serviceResponse);
        if (callback != null && callback.trim().length() > 0) {
            return callback.concat("(").concat(jsonOutputStream.toString("UTF-8")).concat(")");
        }
        return jsonOutputStream.toString("UTF-8");
    }
    
    public Double maxDiff(Double[] arr, int n){
    	double maxDiff = -1; // Initialize Result
    	 
        double maxRight = arr[n-1]; // Initialize max element from right side
     
        for (int i = n-2; i >= 0; i--)
        {
            if (arr[i] > maxRight)
                maxRight = arr[i];
            else
            {
            	double diff = maxRight - arr[i];
                if (diff > maxDiff)
                {
                    maxDiff = diff;
                }
            }
        }
        return maxDiff;
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> missingParameterError(MissingServletRequestParameterException ex, HttpServletRequest request)
    {
        final ErrorResponse r = new ErrorResponse(ex.getLocalizedMessage(), 400);
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        String responseBody = "";
        try {
            String callback = request.getParameter("callback");
            if (callback != null && callback.trim().length() > 0) {
                statusCode = HttpStatus.OK; // in case of callback the returning status is 200 and inside the error code is set
            }
            responseBody = serializeToJson(r, callback);
        }
        catch (IOException e) {
            // if we end up in a serialization error here, the service is basically dead
            responseBody = statusCode.getReasonPhrase();
        }
        return new ResponseEntity<String>(responseBody, statusCode);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> missingParameterError(IllegalArgumentException ex, HttpServletRequest request)
    {
        final ErrorResponse r = new ErrorResponse(ex.getLocalizedMessage(), 400);
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        String responseBody = "";
        try {
            String callback = request.getParameter("callback");
            if (callback != null && callback.trim().length() > 0) {
                statusCode = HttpStatus.OK; // in case of callback the returning status is 200 and inside the error code is set
            }
            responseBody = serializeToJson(r, callback);
        }
        catch (IOException e) {
            // if we end up in a serialization error here, the service is basically dead
            responseBody = statusCode.getReasonPhrase();
        }
        return new ResponseEntity<String>(responseBody, statusCode);
    }
}
