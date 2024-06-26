package org.zerock.todolist.exception

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException

open class CustomSizeLimitExceededException(
    message: String, actual: Long, permitted: Long
) : SizeLimitExceededException(message, actual, permitted) {

    override val message: String
        get() = "업로드 가능한 이미지의 전체 크기는 ${permittedSize / (1024 * 1024)}MB 입니다."
}