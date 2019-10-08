package com.technaxis.querydsl.utils;

/**
 * @author Dmitry Sadchikov
 */
public interface HttpResponseStatus {

    String CAPTCHA_FAILS = "captcha-fails";
    String EMPTY_PARAM = "empty-param";
    String OK = "ok";
    String INVALID_PARAM = "invalid-param";
    String MISSING_PARAMETERS = "missing-parameters";
    String PARSE_EXCEPTION = "parse-exception";
    String ORDERED_CONSULTATION = "ordered-consultation";

    String NO_PERMISSIONS = "no-permissions";
    String NOT_AUTHORIZED = "not-authorized";
    String NOT_ACCEPTABLE = "not-acceptable";
    String REQUEST_FAILED = "request-failed";

    String NOT_FOUND = "not-found";
    String USER_NOT_FOUND = "user-not-found";

    String ALREADY_REGISTERED = "already-registered";
    String ALREADY_USED = "already-used";
    String ALREADY_PAID = "already-paid";
    String NOT_AVAILABLE = "not-available";

    String DUPLICATE_ACCOUNT = "duplicate-account";
    String DUPLICATE_EMAIL = "duplicate-email";
    String DUPLICATE_PHONE = "duplicate-phone";
    String DUPLICATE_CATEGORY = "duplicate-category";

    String INVALID_FILE_TYPE = "invalid-file-type";
    String TOO_BIG_FILE_SIZE = "too-big-file-size";
    String FORBIDDEN = "forbidden";
    String REQUEST_ANOTHER_CODE = "request-another-code";
    String INVALID_STATE = "invalid-state";
    String EXIST_DEPENDENCIES = "exist-dependencies";

    String TIME_EXPIRED = "time_expired";
    String GONE = "gone";
    String NEGATIVE = "negative";
}
