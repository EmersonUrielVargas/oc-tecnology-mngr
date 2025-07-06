package com.onclass.technology.infrastructure.entrypoints.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String TECHNOLOGY_ERROR = "Error on Technology - [ERROR]";
    public static final String TECHNOLOGY_CREATED_RS_OK = "Technology create successfully";
    public static final String ASSIGN_TECHNOLOGIES_CREATED_RS_OK = "Technologies assign successfully";
    public static final String UNEXPECTED_ERROR = "Unexpected error occurred";

    public static final String PATH_POST_TECHNOLOGY = "/technology";
    public static final String PATH_POST_ASSIGN_TECHNOLOGIES = "/technology/assign";
}
