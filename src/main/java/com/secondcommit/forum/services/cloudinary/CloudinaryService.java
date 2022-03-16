package com.secondcommit.forum.services.cloudinary;

import org.springframework.web.multipart.MultipartFile;

/**
 * Interface that defines the methods for Cloudinary (to upload and store files in the cloud)
 */
public interface CloudinaryService {
    String uploadImage(MultipartFile file) throws Exception;
}
