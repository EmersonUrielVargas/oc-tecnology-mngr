package com.onclass.technology.domain.exceptions;

import com.onclass.technology.domain.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class InvalidFormatParamException extends BusinessException {

    public InvalidFormatParamException(TechnicalMessage technicalMessage) {
        super(technicalMessage);
    }


}
