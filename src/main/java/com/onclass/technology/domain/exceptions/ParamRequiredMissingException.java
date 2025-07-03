package com.onclass.technology.domain.exceptions;

import com.onclass.technology.domain.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class ParamRequiredMissingException extends BusinessException {

    public ParamRequiredMissingException(TechnicalMessage technicalMessage) {
        super(technicalMessage);
    }


}
