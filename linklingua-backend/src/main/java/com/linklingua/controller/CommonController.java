package com.linklingua.controller;

import com.linklingua.common.BusinessException;
import com.linklingua.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Common API (file upload, etc.).
 *
 * @author LinkLingua
 */
@Tag(name = "Common module", description = "Shared utility endpoints")
@RestController
@RequestMapping("/api/common")
@Slf4j
public class CommonController {

    /** Local directory where uploaded files are stored. */
    @Value("${linklingua.upload.dir}")
    private String uploadDir;

    /** Public URL prefix mapped to the upload directory. */
    @Value("${linklingua.upload.url-prefix}")
    private String uploadUrlPrefix;

    /**
     * Host part prepended to returned file URLs. Defaults to the local dev host;
     * set to an empty string in production so URLs are returned relative
     * (e.g. {@code /uploads/xxx}) and resolved via the reverse proxy.
     */
    @Value("${linklingua.upload.public-base:http://localhost:8080}")
    private String uploadPublicBase;

    @PostMapping("/upload")
    @Operation(summary = "Upload file", description = "Upload an image file and return its accessible URL")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("Upload file must not be empty");
        }
        log.info("Uploading file: {}, size={} bytes", file.getOriginalFilename(), file.getSize());

        // Build a unique target file name, preserving the original extension.
        String originalName = file.getOriginalFilename();
        String extension = (originalName != null && originalName.contains("."))
                ? originalName.substring(originalName.lastIndexOf('.'))
                : "";
        String fileName = UUID.randomUUID().toString().replace("-", "") + extension;

        try {
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);
            file.transferTo(dir.resolve(fileName).toAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to store uploaded file", e);
            throw new BusinessException("Failed to store uploaded file");
        }

        // Return the public URL that maps to the stored file.
        String url = uploadPublicBase +
                (uploadUrlPrefix.endsWith("/") ? uploadUrlPrefix : uploadUrlPrefix + "/") + fileName;
        return Result.success(url);
    }
}
