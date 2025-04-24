package com.example.training.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for common application functions
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppUtils {

    private static final Logger log = LoggerFactory.getLogger(AppUtils.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Format a LocalDateTime object to a string
     * @param dateTime the date time to format
     * @return formatted date time string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_FORMATTER);
    }
    
    /**
     * Log sensitive operations for audit purposes
     * @param operation the operation being performed
     * @param username the user performing the operation
     * @param details additional details about the operation
     */
    public static void logAudit(String operation, String username, String details) {
        log.info("AUDIT: Operation=[{}], User=[{}], Details=[{}], Time=[{}]", 
                operation, username, details, formatDateTime(LocalDateTime.now()));
    }
    
    /**
     * Truncate a string to a maximum length
     * @param input the input string
     * @param maxLength the maximum length
     * @return truncated string
     */
    public static String truncate(String input, int maxLength) {
        if (input == null || input.length() <= maxLength) {
            return input;
        }
        return input.substring(0, maxLength) + "...";
    }
}