package com.onclass.technology.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {

    INTERNAL_ERROR("500","Something went wrong, please try again", ""),
    INVALID_REQUEST("400", "Bad Request, please verify data", ""),
    INVALID_PARAMETERS(INVALID_REQUEST.getCode(), "Bad Parameters, please verify data", ""),
    MISSING_REQUIRED_PARAM("400", "Missing required parameters, please verify data ", ""),
    INVALID_MESSAGE_ID("404", "Invalid Message ID, please verify", "messageId"),
    TECHNOLOGY_NAME_TOO_LONG("400", "The technology name is too long", ""),
    TECHNOLOGY_DESCRIPTION_TOO_LONG("400", "The technology description is too long", ""),
    UNSUPPORTED_OPERATION("501", "Method not supported, please try again", ""),
    TECHNOLOGY_CREATED("201", "Technology created successful", ""),
    TECHNOLOGIES_ASSIGN_OK("200", "Technologies assigned successful", ""),
    TECHNOLOGIES_DELETE_BY_CAPABILITIES_OK("200", "Technologies by capabilities ids are deleted successfully", ""),
    TECHNOLOGY_ALREADY_EXISTS("400","The technology already exist." ,"" ),
    ERROR_CREATING_TECHNOLOGY("500","An error occurred qhile creating the technology" ,"" ),
    SOME_TECHNOLOGIES_NOT_FOUND("404","Some of the technologies were not found, please verify data" ,"" );

    private final String code;
    private final String message;
    private final String param;
}