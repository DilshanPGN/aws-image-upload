package com.amigoscode.awsimageupload.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStore {
    private final AmazonS3 s3;

    @Autowired  //For dependency injection
    public FileStore(AmazonS3 s3) {
        this.s3 = s3;
    }

    public void save(String path,
                     String fileName,
                     Optional<Map<String, String>> optionalMetadata,
                     InputStream inputStream){

        //Creating ObjectMetadata from Optional Map

        ObjectMetadata metadata = new ObjectMetadata();
        optionalMetadata.ifPresent(map ->{
            if (!map.isEmpty()){
                map.forEach(metadata::addUserMetadata);
                //map.forEach((key, value) -> metadata.addUserMetadata(key, value));
            }
        });


        try {
            s3.putObject(path , fileName , inputStream , metadata);
        }catch (AmazonServiceException e){
            throw new IllegalStateException("Failed to store file to s3", e);
        }
    }
}
