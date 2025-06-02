package cultureinfo.culture_app.service;

import java.io.IOException;

import cultureinfo.culture_app.exception.CustomException;
import cultureinfo.culture_app.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //파일 업로드
    public String upload(MultipartFile file, Long contentId) {
        try{
            String fileName = contentId + "/" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(file.getContentType()) //jpg만 받기
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return getUrl(fileName);
            // https://app-culture-bucket.s3.ap-southeast-2.amazonaws.com/ + (content_Id)+".jpg"
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
        }


    }

    //파일 삭제
    public void deleteFile(Long contentId) {
        try {
            String fileName = contentId + ".jpg";

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FILE_DELETE_FAILED);
        }
    }

    public String getUrl(String fileName) {
        return "https://" + bucket + ".s3.amazonaws.com/" + fileName;
    }
}
