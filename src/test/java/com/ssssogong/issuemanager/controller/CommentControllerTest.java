package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.security.SecurityConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;

@WebMvcTest(CommentController.class)
@ContextConfiguration(classes = {CommentController.class, SecurityConfig.class})
class CommentControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CommentService commentService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//
//    @Test
//    @WithMockUser
//    void createComment() throws Exception {
//        Long issueId = 1L;
//        Long commentId = 1L;
//        CommentRequestDto commentRequestDto = new CommentRequestDto();
//        commentRequestDto.setContent("This is a comment");
//
//
//
//        MockMultipartFile contentFile = new MockMultipartFile("commentRequestDto", "", "application/json",
//                new ObjectMapper().writeValueAsBytes(commentRequestDto));
//        MockMultipartFile imageFile = new MockMultipartFile("commentImageRequestDto", "image.jpg", "image/jpeg", "image data".getBytes());
//
//        //Mockito.when().thenReturn(commentId);
//
//        commentService.createComment(Mockito.eq(issueId), Mockito.any());
//        mockMvc.perform(multipart("/issues/{iid}/comments", issueId)
//                        .file(contentFile)
//                        .file(imageFile))
//                .andExpect(status().isOk())
//                .andExpect(content().string(String.valueOf(commentId)));
//    }
//
//
//    @Test
//    public void testGetComment() throws Exception {
//        Long issueId = 1L;
//        Long commentId = 1L;
//        CommentResponseDto commentResponseDto = new CommentResponseDto();
//        commentResponseDto.setId(commentId);
//        commentResponseDto.setContent("This is a comment");
//
//        Mockito.when(commentService.getComment(commentId)).thenReturn(commentResponseDto);
//
//        mockMvc.perform(get("/issues/{iid}/comments/{cid}", issueId, commentId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(commentId))
//                .andExpect(jsonPath("$.content").value("This is a comment"));
//    }
//
//    @Test
//    public void testUpdateComment() throws Exception {
//        Long issueId = 1L;
//        Long commentId = 1L;
//        String content = "Updated comment content";
//        MockMultipartFile contentFile = new MockMultipartFile("content", "",
//                "application/json", content.getBytes());
//        MockMultipartFile imageFile = new MockMultipartFile("images", "image.jpg",
//                "image/jpeg", "image data".getBytes());
//
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/issues/{iid}/comments/{cid}", issueId, commentId)
//                        .file(contentFile)
//                        .file(imageFile)
//                        .with(request -> {
//                            request.setMethod("PUT");
//                            return request;
//                        }))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(commentId)));
//
//        ArgumentCaptor<Long> commentIdCaptor = ArgumentCaptor.forClass(Long.class);
//        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<List> imagesCaptor = ArgumentCaptor.forClass(List.class);
//
//        verify(commentService, Mockito.times(1))
//                .updateComment(commentIdCaptor.capture(), contentCaptor.capture(), imagesCaptor.capture());
//
//        assertEquals(commentId, commentIdCaptor.getValue());
//        assertEquals(content, contentCaptor.getValue());
//        assertEquals(1, imagesCaptor.getValue().size()); // Assuming one image is uploaded
//    }
//    @Test
//    public void testDeleteComment() throws Exception {
//        Long issueId = 1L;
//        Long commentId = 1L;
//
//        Mockito.doNothing().when(commentService).deleteComment(commentId);
//
//        mockMvc.perform(delete("/issues/{iid}/comments/{cid}", issueId, commentId))
//                .andExpect(status().isOk());
//    }
}
