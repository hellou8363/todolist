package org.zerock.todolist.exception

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException

class CustomFileSizeLimitExceededException(
    message: String, actual: Long, permitted: Long
) :
    FileSizeLimitExceededException(message, actual, permitted) {

    override val message: String
        get() = "업로드 가능한 이미지는 최대 크기는 ${permittedSize / (1024 * 1024)}MB 입니다."
}