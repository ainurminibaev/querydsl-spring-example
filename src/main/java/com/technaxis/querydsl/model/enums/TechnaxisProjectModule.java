package com.technaxis.querydsl.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Dmitry Sadchikov
 */
@AllArgsConstructor
// TODO: rename for your project
public enum TechnaxisProjectModule {

    // TODO ...
    MODULE_NAME("module-name"), PROFILE("profile");

    @Getter
    private String moduleName;
}
