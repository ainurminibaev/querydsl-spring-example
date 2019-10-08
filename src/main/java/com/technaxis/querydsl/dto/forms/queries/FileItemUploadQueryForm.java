package com.technaxis.querydsl.dto.forms.queries;

import com.technaxis.querydsl.model.enums.TechnaxisProjectModule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Vitaly on 13.02.2018.
 */
@Data
public class FileItemUploadQueryForm {

    public static final String FILE_FIELD = "file";
    public static final String MODULE_FIELD = "module";
    public static final String ENTITY_NAME_FIELD = "entity_name";

    @ApiModelProperty(FILE_FIELD)
    private MultipartFile file;

    @ApiModelProperty(MODULE_FIELD)
    private TechnaxisProjectModule module;

    @ApiModelProperty(ENTITY_NAME_FIELD)
    private String entityName;
}
