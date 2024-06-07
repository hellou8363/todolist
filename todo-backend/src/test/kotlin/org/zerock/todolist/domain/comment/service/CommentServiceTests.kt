//package org.zerock.todolist.domain.comment.service
//
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.zerock.todolist.domain.comment.dto.CreateAndUpdateCommentRequest
//import org.zerock.todolist.domain.comment.dto.DeleteCommentRequest
//
//@SpringBootTest
//class CommentServiceTests {
//
//    @Autowired
//    private lateinit var commentService: CommentService
//
//    @Test
//    fun `전체 댓글 조회 테스트` () {
//        val result = commentService.getAllCommentList()
//        println(result)
//    }
//
//    @Test
//    fun `댓글 등록 테스트` () {
//        val todoId = 120L
//        val createrequest = CreateAndUpdateCommentRequest(content = "댓글 테스트", writer = "Frog", password = "1234")
//        val result = commentService.createComment(todoId, createrequest)
//        println(result)
//    }
//
//    @Test
//    fun `댓글 삭제 테스트` () {
//        val todoId = 120L
//        val commentId = 18L
//        val deleteRequest = DeleteCommentRequest(writer = "Frog", password = "1234")
//        val result = commentService.deleteComment(todoId, commentId, deleteRequest)
//        println(result)
//    }
//
//    @Test
//    fun `댓글 수정 테스트` () {
//        val todoId = 120L
//        val commentId = 18L
//        val updateRequest = UpdateCommentRequest(content = "댓글 수정 테스트", writer = "Frog", password = "1234")
//        val result = commentService.updateComment(todoId, commentId, updateRequest)
//        println(result)
//    }
//}