package com.secondcommit.forum.services.cloudinary;

import com.secondcommit.forum.entities.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Interface that defines the methods for Cloudinary (to upload and store files in the cloud)
 */
public interface CloudinaryService {
    File uploadImage(MultipartFile file) throws Exception;
    Boolean deleteFile(String cloudinaryId) throws IOException;
}
