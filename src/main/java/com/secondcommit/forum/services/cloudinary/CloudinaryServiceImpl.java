package com.secondcommit.forum.services.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.secondcommit.forum.entities.File;
import com.secondcommit.forum.exceptions.EmptyFileException;
import com.secondcommit.forum.exceptions.InvalidFileFormatException;
import com.secondcommit.forum.repositories.FileRepository;
import com.secondcommit.forum.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Implements the methods specified on CloudinaryService (to upload and store files in the cloud)
 */
@Service
public class CloudinaryServiceImpl implements CloudinaryService{

    private final String cloudName = System.getenv("CLOUDINARY_CLOUD_NAME");
    private final String apiKey = System.getenv("CLOUDINARY_API_KEY");
    private final String apiSecret = System.getenv("CLOUDINARY_API_SECRET");
    private final Map params = ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret
    );

    Logger log = LoggerFactory.getLogger(CloudinaryServiceImpl.class);

    Cloudinary cloudinary = new Cloudinary(params);

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    public CloudinaryServiceImpl(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    /**
     * Method to upload files
     * @param file
     * @return File
     * @throws EmptyFileException
     * @throws InvalidFileFormatException
     * @throws IOException
     */
    @Override
    public File uploadImage(MultipartFile file) throws EmptyFileException, InvalidFileFormatException, IOException {

        log.warn(file.getContentType());

        if (file.isEmpty()) {
            String message = "Error: El archivo está vacío";
            log.error(message);
            throw new EmptyFileException(message);

        } else if (!file.getContentType().equalsIgnoreCase("image/jpeg") &&
                !file.getContentType().equalsIgnoreCase("image/jpg")){
            String message = "Error: El formato del archivo es incorrecto. Formatos admitidos 'jpg' y '.jpeg'";
            log.error(message);
            throw new InvalidFileFormatException(message);
        }

        File newFile = new File();

        //Saves url and Cloudinary ID
        Map response = cloudinary.uploader().upload((file.getBytes()), ObjectUtils.emptyMap());
        response.forEach((key, value) -> {
            if (Objects.equals(key, "secure_url"))  newFile.setUrl((String) value);
            if (Objects.equals(key, "public_id")) newFile.setCloudinaryId((String) value);
        });

        return newFile;
    }

    /**
     * Method to remove a file from Cloudinary
     * @param cloudinaryId (String)
     * @return Boolean
     * @throws IOException
     */
    @Override
    public Boolean deleteFile(String cloudinaryId) throws IOException {

        if (fileRepository.existsByCloudinaryId(cloudinaryId)) {
            cloudinary.uploader().destroy(cloudinaryId, ObjectUtils.emptyMap());
            fileRepository.delete(fileRepository.findByCloudinaryId(cloudinaryId));

            return true;
        }

        return false;
    }
}
