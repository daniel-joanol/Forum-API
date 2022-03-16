package com.secondcommit.forum.services.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.secondcommit.forum.exceptions.EmptyFileException;
import com.secondcommit.forum.exceptions.InvalidFileFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

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

    /**
     * Methos that uploads files
     * @param file
     * @return String (url of the uploaded file)
     * @throws EmptyFileException
     * @throws InvalidFileFormatException
     * @throws IOException
     */
    @Override
    public String uploadImage(MultipartFile file) throws EmptyFileException, InvalidFileFormatException, IOException {
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
        Map response = cloudinary.uploader().upload((file.getBytes()),
                ObjectUtils.emptyMap());

        return response.get("secure_url").toString();
    }
}
