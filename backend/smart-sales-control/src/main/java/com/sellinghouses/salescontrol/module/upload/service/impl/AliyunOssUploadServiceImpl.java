package com.sellinghouses.salescontrol.module.upload.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.sellinghouses.salescontrol.common.config.AliyunOssProperties;
import com.sellinghouses.salescontrol.common.exception.BusinessException;
import com.sellinghouses.salescontrol.common.exception.ErrorCode;
import com.sellinghouses.salescontrol.module.upload.service.UploadService;
import com.sellinghouses.salescontrol.module.upload.vo.UploadImageVO;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliyunOssUploadServiceImpl implements UploadService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png", "image/webp", "image/gif");

    private static final DateTimeFormatter DATE_PATH_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final AliyunOssProperties aliyunOssProperties;

    @Override
    public UploadImageVO uploadImage(MultipartFile file) {
        validateFile(file);
        validateProperties();

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() == null ? "image" : file.getOriginalFilename());
        String extension = resolveExtension(originalFilename, file.getContentType());
        String objectKey = buildObjectKey(extension);

        OSS ossClient = new OSSClientBuilder().build(
                aliyunOssProperties.getEndpoint(),
                aliyunOssProperties.getAccessKeyId(),
                aliyunOssProperties.getAccessKeySecret());
        try (InputStream inputStream = file.getInputStream()) {
            ossClient.putObject(aliyunOssProperties.getBucketName(), objectKey, inputStream);
        } catch (IOException e) {
            log.error("Read upload image failed, originalFilename={}", originalFilename, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "图片读取失败");
        } catch (RuntimeException e) {
            log.error("Upload image to Aliyun OSS failed, objectKey={}", objectKey, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "图片上传失败");
        } finally {
            ossClient.shutdown();
        }

        return UploadImageVO.builder()
                .url(buildPublicUrl(objectKey))
                .objectKey(objectKey)
                .originalFilename(originalFilename)
                .build();
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "请选择要上传的图片");
        }
        String contentType = file.getContentType();
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "仅支持 JPG、PNG、WEBP、GIF 图片");
        }
        long maxBytes = aliyunOssProperties.getMaxSizeMb() * 1024L * 1024L;
        if (file.getSize() > maxBytes) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "图片大小不能超过" + aliyunOssProperties.getMaxSizeMb() + "MB");
        }
    }

    private void validateProperties() {
        if (!StringUtils.hasText(aliyunOssProperties.getEndpoint())
                || !StringUtils.hasText(aliyunOssProperties.getAccessKeyId())
                || !StringUtils.hasText(aliyunOssProperties.getAccessKeySecret())
                || !StringUtils.hasText(aliyunOssProperties.getBucketName())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "OSS配置未完成");
        }
    }

    private String resolveExtension(String originalFilename, String contentType) {
        String extension = StringUtils.getFilenameExtension(originalFilename);
        if (StringUtils.hasText(extension)) {
            return extension.toLowerCase(Locale.ROOT);
        }
        if ("image/png".equals(contentType)) {
            return "png";
        }
        if ("image/webp".equals(contentType)) {
            return "webp";
        }
        if ("image/gif".equals(contentType)) {
            return "gif";
        }
        return "jpg";
    }

    private String buildObjectKey(String extension) {
        String uploadDir = StringUtils.hasText(aliyunOssProperties.getUploadDir())
                ? aliyunOssProperties.getUploadDir()
                : "selling-houses";
        String normalizedDir = uploadDir.replaceAll("^/+", "").replaceAll("/+$", "");
        return normalizedDir + "/" + LocalDate.now().format(DATE_PATH_FORMATTER) + "/" + UUID.randomUUID() + "." + extension;
    }

    private String buildPublicUrl(String objectKey) {
        if (StringUtils.hasText(aliyunOssProperties.getPublicUrlPrefix())) {
            return aliyunOssProperties.getPublicUrlPrefix().replaceAll("/+$", "") + "/" + objectKey;
        }
        return "https://" + aliyunOssProperties.getBucketName() + "." + aliyunOssProperties.getEndpoint() + "/" + objectKey;
    }
}
