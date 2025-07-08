package com.onclass.technology.infrastructure.entrypoints.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String TECHNOLOGY_ERROR = "Error on Technology - [ERROR]";
    public static final String TECHNOLOGY_CREATED_RS_OK = "Technology create successfully";
    public static final String TECHNOLOGIES_BY_CAPABILITIES_DELETE_RS_OK = "Technologies by capabilities ids are deleted successfully";
    public static final String ASSIGN_TECHNOLOGIES_CREATED_RS_OK = "Technologies assign successfully";
    public static final String UNEXPECTED_ERROR = "Unexpected error occurred";

    public static final String PATH_POST_TECHNOLOGY = "/technology";
    public static final String PATH_POST_ASSIGN_TECHNOLOGIES = "/technology/assign";
    public static final String PATH_GET_TECHNOLOGIES_BY_CAPABILITIES_IDS = "/technology/capabilities_ids";
    public static final String PATH_GET_CAPABILITIES_SORT_BY_TECHNOLOGIES = "/technology/capabilities";
    public static final String PATH_DELETE_TECHNOLOGIES_BY_CAPABILITIES = "/technology/capabilities";

    public final String QUERY_PARAM_CAPABILITIES_IDS = "capabilitiesIds";
    public final String QUERY_PARAM_ORDER_SORT = "sort";
    public final String QUERY_PARAM_PAGE = "page";
    public final String QUERY_PARAM_SIZE = "size";
    public final String DEFAULT_SIZE_PAGINATION = "10";
    public final String DEFAULT_PAGE_PAGINATION = "0";
}
