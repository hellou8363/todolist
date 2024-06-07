//package org.zerock.todolist.domain.comment.repository
//
//import org.junit.jupiter.api.DisplayName
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.data.repository.findByIdOrNull
//import org.springframework.test.annotation.Commit
//import org.springframework.transaction.annotation.Transactional
//import org.zerock.todolist.domain.comment.model.Comment
//import org.zerock.todolist.domain.comment.model.toResponse
//import org.zerock.todolist.domain.exception.ModelNotFoundException
//import org.zerock.todolist.domain.todo.model.toResponse
//import org.zerock.todolist.domain.todo.repository.TodoRepository
//
//@SpringBootTest
//class CommentRepositoryTests {
//
//    @Autowired
//    private lateinit var todoRepository: TodoRepository
//
//    @Autowired
//    private lateinit var commentRepository: CommentRepository
//
//    @Test
//    @DisplayName("댓글 입력 테스트")
//    @Transactional
//    @Commit
//    fun insertTest() {
//        val todoId = 123L
//        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
//        println("Todo =======> ${todo.toResponse()}")
//
//        for (i in 1..1) {
//            val comment = Comment(
//                content = "Comment...$i",
//                writer = "Writer...$i",
//                password = "1234",
//                todo = todo,
//                user =
//            )
//
//            commentRepository.save(comment)
//
//            println("comment ========> ${comment.toResponse()}")
//        }
//    }
//
//    @Test
//    @DisplayName("댓글 조회 테스트")
//    @Transactional
//    fun selectAllTest() {
//        val todoId = 123L
//        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
//        println("Todo =======> ${todo.toResponse()}")
//        println("Todo =======> ${todo.comments.map { it.toResponse() }}")
//    }
//
//    @Test
//    @DisplayName("댓글 삭제 테스트")
//    @Transactional
//    @Commit
//    fun deleteTest() {
//        val todoId = 123L
//        val commentId = 10L
//        val comment = commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw ModelNotFoundException("Comment", commentId)
//        commentRepository.delete(comment)
//    }
//
//    @Test
//    @DisplayName("댓글 수정 테스트")
//    @Transactional
//    @Commit
//    fun updateTest() {
//        val todoId = 123L
//        val commentId = 12L
//        val comment = commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw ModelNotFoundException("Comment", commentId)
//        comment.content = "댓글 수정 테스트!!"
//        commentRepository.save(comment)
//    }
//}