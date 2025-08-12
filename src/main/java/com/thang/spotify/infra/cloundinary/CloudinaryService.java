package com.thang.spotify.infra.cloundinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.thang.spotify.infra.configuration.CloudinaryConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(CloudinaryConfig cloudinaryConfig) {
        this.cloudinary = cloudinaryConfig.getCloudinary();
    }

    public String uploadAvatarUserFile(MultipartFile file) {
        try {
            File uploadedFile = convertToFile(file); // convert MultipartFile to File

            Map<String, Object> options = ObjectUtils.asMap(
                    "folder", "spotify-avatar"
            );

            Map<?, ?>uploadResult = cloudinary.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Upload failed: " + e.getMessage(), e);
        }
    }

    public String uploadSongCoverImageFile(MultipartFile file) {
        try {
            File uploadedFile = convertToFile(file); // convert MultipartFile to File

            Map<String, Object> options = ObjectUtils.asMap(
                    "folder", "spotify-cover"
            );

            Map<?, ?>uploadResult = cloudinary.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Upload failed: " + e.getMessage(), e);
        }
    }

    // Convert MultipartFile to File
    private File convertToFile(MultipartFile file) throws IOException {
        File convFile = File.createTempFile("upload", file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile;
    }

}
