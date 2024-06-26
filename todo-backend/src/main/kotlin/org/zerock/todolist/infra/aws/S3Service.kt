package org.zerock.todolist.infra.aws

import io.awspring.cloud.s3.ObjectMetadata
import io.awspring.cloud.s3.S3Operations
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile


@Service
class S3Service(
    private val s3Operations: S3Operations,
    @Value("\${spring.cloud.aws.s3.bucket}")
    private val bucket: String,
) {

    @Transactional
    fun upload(file: MultipartFile, key: String): String {
        // 업로드할 이미지 확장자 목록 정의
        val imageTypes = listOf("jpg", "jpeg", "png", "gif", "bmp")

        // contentType의 확장자 부분만 추출(예: image/png -> png)해 미리 정의한 확장자와 일치하는지 확인
        if (!imageTypes.contains(file.contentType.toString().split("/")[1])) {
            throw IllegalArgumentException("이미지 파일만 업로드가 가능합니다.") // 일치하지 않을 경우 예외 발생
        }

        file.inputStream.use { it -> // use: 블록 내 코드 실행 후 예외 발생 여부에 관계없이 close
            return s3Operations.upload(
                // 버킷명, key(버킷 업로드 시 적용되는 파일명), 업로드할 파일(InputStream), 파일의 메타데이터(ObjectMetadata)
                bucket, key, it,
                ObjectMetadata.builder().contentType(file.contentType).build()
            ).url.toString() // 업로드된 URL을 반환
        }
    }
}