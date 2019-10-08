package com.technaxis.querydsl.controller;

import com.technaxis.querydsl.dto.FileItemJson;
import com.technaxis.querydsl.dto.Response;
import com.technaxis.querydsl.dto.forms.queries.FileItemUploadQueryForm;
import com.technaxis.querydsl.mappers.FileItemMapper;
import com.technaxis.querydsl.model.FileItem;
import com.technaxis.querydsl.security.CurrentUser;
import com.technaxis.querydsl.service.ContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by Vitaly on 13.02.2018.
 */
@Slf4j
@ApiIgnore
@Api(tags = {"Common.Files"})
@RestController
@RequestMapping(FileController.ROOT_URL)
public class FileController {

    public static final String ROOT_URL = "/v1/files";
    private static final String FILE_ONE_URL = "/{fileId}";
    private static final String UPLOAD_FILE_URL = "/upload";

    @Autowired
    private ContentService contentService;

    @Autowired
    private FileItemMapper fileItemMapper;

    @ApiOperation(value = "Upload new file")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization header",
                    defaultValue = "Bearer %JWTTOKEN%",
                    dataType = "string",
                    paramType = "header"
            )
    })
    @ApiResponses({
            @ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = "Need authorization")
    })
    @RequestMapping(value = UPLOAD_FILE_URL, method = RequestMethod.POST)
    public Response<FileItemJson> uploadFileItem(@ModelAttribute FileItemUploadQueryForm form,
                                                 MultipartFile file,
                                                 @AuthenticationPrincipal CurrentUser currentUser) {
        Long userId = currentUser.getId();
        log.debug("Upload new file by user [{}]", userId != null ? userId : "anonymous");
        FileItem fileItem = contentService.uploadFile(form, userId);
        return new Response<>(fileItemMapper.apply(fileItem));
    }

    @ApiOperation(value = "Get file")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization header",
                    defaultValue = "Bearer %JWTTOKEN%",
                    dataType = "string",
                    paramType = "header"
            )
    })
    @ApiResponses({
            @ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = "Need authorization")
    })
    @RequestMapping(value = FILE_ONE_URL, method = RequestMethod.GET)
    public Response<FileItemJson> getFile(@PathVariable Long fileId) {
        FileItem fileItem = contentService.getFile(fileId);
        return new Response<>(fileItemMapper.apply(fileItem));
    }
}
