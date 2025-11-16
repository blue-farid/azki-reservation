package com.azki.reservation.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSourceUtil {
    private final MessageSource messageSource;

    public String getMessageIfExist(String key, Object... params) {
        try {
            return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException noSuchMessageException) {
            log.error("the key not found: {}", key);
            return "";
        }
    }
}
