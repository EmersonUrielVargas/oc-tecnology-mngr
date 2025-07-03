package com.onclass.technology.domain.validators;

import com.onclass.technology.domain.constants.Constants;
import com.onclass.technology.domain.enums.TechnicalMessage;
import com.onclass.technology.domain.exceptions.InvalidFormatParamException;
import com.onclass.technology.domain.exceptions.ParamRequiredMissingException;
import com.onclass.technology.domain.model.Technology;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class Validator {

	public static Mono<Technology> validateTechnology(Technology technology) {
        if (isNullOrEmpty(technology.name()) && isNullOrEmpty(technology.description())){
            return Mono.error(new ParamRequiredMissingException(TechnicalMessage.MISSING_REQUIRED_PARAM));
        }
        if (technology.name().length()  > Constants.TECHNOLOGY_NAME_MAX_SIZE) {
            return Mono.error(new InvalidFormatParamException(TechnicalMessage.TECHNOLOGY_NAME_TOO_LONG));
        }
        if (technology.description().length() > Constants.TECHNOLOGY_DESCRIPTION_MAX_SIZE) {
            return Mono.error(new InvalidFormatParamException(TechnicalMessage.TECHNOLOGY_DESCRIPTION_TOO_LONG));
        }
        return Mono.just(technology);
    }

	public static <T> boolean isNullOrEmpty(T value){
        if (Objects.nonNull(value)){
            if (value instanceof String str) {
                return str.isBlank();
            }else {
                return false;
            }
        }
        return true;
	}
}
