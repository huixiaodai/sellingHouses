package com.sellinghouses.salescontrol.module.upload.service;

import com.sellinghouses.salescontrol.module.upload.vo.UploadImageVO;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    /**
     * Uploads an image to object storage and returns the public image URL.
     *
     * @param file image file
     * @return image upload result
     */
    UploadImageVO uploadImage(MultipartFile file);
}
