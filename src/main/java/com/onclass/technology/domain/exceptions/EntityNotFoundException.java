package com.onclass.technology.domain.exceptions;

import com.onclass.technology.domain.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(TechnicalMessage technicalMessage) {
        super(technicalMessage);
    }
}
