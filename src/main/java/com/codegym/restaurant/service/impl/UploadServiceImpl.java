package com.codegym.restaurant.service.impl;

import com.cloudinary.Cloudinary;
import com.codegym.restaurant.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Map upload(MultipartFile multipartFile, Map options) throws IOException {
        return cloudinary.uploader().upload(multipartFile.getBytes(), options);
    }

    @Override
    public Map destroy(String publicId, Map options) throws IOException {
        return cloudinary.uploader().destroy(publicId, options);
    }
}
