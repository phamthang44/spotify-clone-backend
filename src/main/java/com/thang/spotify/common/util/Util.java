package com.thang.spotify.common.util;

import com.thang.spotify.exception.InvalidDataException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class Util {

    public static String normalizePhoneNumber(String raw, String countryCode) {
        if (raw == null || countryCode == null) return null;

        String cleaned = raw.replaceAll("[^0-9]", "");

        if (cleaned.startsWith("0")) {
            cleaned = cleaned.substring(1);
        }

        switch (countryCode.toUpperCase()) {
            case "VN":
                return "+84" + cleaned;
            case "UK":
                return "+44" + cleaned;
            default:
                return null;
        }
    }

    public static boolean isInvalidEmailFormat(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return !email.matches(emailRegex);
    }

    public static boolean isInvalidPhoneNumberFormat(String phoneNumber) {
        String phoneRegex = "^0\\d{9,10}$";
        return !phoneNumber.matches(phoneRegex);
    }

    public static boolean isInvalidPasswordFormat(String password) {
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return !password.matches(passwordRegex);
    }

    public static boolean isInvalidFullNameFormat(String fullName) {
        String fullNameRegex = "^[\\p{L} '-]{2,50}$";
        return !fullName.matches(fullNameRegex);
    }

    public static boolean isInvalidEmailLength(String email) {
        return email.length() >= 255; //nếu invalid thì trả về true
    }

    /**
     * Checks if a string is null or empty (empty or contains only whitespace).
     * for java 8, use isEmpty() method.
     * @param str the string to check
     * @return true if the string is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNullOrEmpty(Object obj) {
        return obj == null || (obj instanceof String && ((String) obj).trim().isEmpty());
    }

    /**
     * Checks if a string is null or blank (empty or contains only whitespace).
     * for java 11 and later, use isBlank() method.
     * @param str the string to check
     * @return true if the string is null or blank, false otherwise
     */
    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }

    public static int getPageNo(Integer pageNo) {
        return pageNo > 0 ? pageNo - 1 : 0; // Convert to zero-based index
    }

    public static void isIntegerNumber(Integer num) {
        if (num == null) {
            throw new InvalidDataException("Invalid data");
        }
        try {
            String str = String.valueOf(num);
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(num + " is not a integer number");
        }
    }

    public static void isDoubleNumber(Double num) {
        if (num == null) {
            throw new InvalidDataException("Invalid data");
        }
        try {
            String str = String.valueOf(num);
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(num + " is not a double number");
        }
    }

    public static void isLongNumber(Long num) {
        if (num == null) {
            throw new InvalidDataException("Invalid data");
        }
        try {
            String str = String.valueOf(num);
            Long.parseLong(str);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(num + " is not a long number");
        }
    }

    public static void validatePageNoPageSize(Integer pageNo, Integer pageSize) {
        if (pageNo == null || pageSize == null || pageNo < 0 || pageSize < 1) {
            throw new InvalidDataException("Invalid page number or page size");
        }
    }

    public static Pageable getPageable(Integer pageNo, Integer pageSize) {
        validatePageNoPageSize(pageNo, pageSize);
        return PageRequest.of(getPageNo(pageNo), pageSize);
    }
}
