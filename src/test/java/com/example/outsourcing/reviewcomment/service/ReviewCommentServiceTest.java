package com.example.outsourcing.reviewcomment.service;

import com.example.outsourcing.common.config.PasswordEncoder;
import com.example.outsourcing.common.exception.BaseException;
import com.example.outsourcing.common.exception.ErrorCode;
import com.example.outsourcing.order.entity.Order;
import com.example.outsourcing.review.entity.Review;
import com.example.outsourcing.review.repository.ReviewRepository;
import com.example.outsourcing.reviewcomment.dto.request.ReviewCommentRequestDto;
import com.example.outsourcing.reviewcomment.dto.response.ReviewCommentResponseDto;
import com.example.outsourcing.reviewcomment.entity.ReviewComment;
import com.example.outsourcing.reviewcomment.repository.ReviewCommentRepository;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.user.entity.Role;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewCommentServiceTest {

    @InjectMocks
    private ReviewCommentService reviewCommentService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewCommentRepository reviewCommentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;
    private Store store;
    private Order order;
    private Review review;
    private ReviewComment reviewComment;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        User storeOwner = new User();
        storeOwner.setId(1L);
        storeOwner.setPassword("storeOwnerPassword");

        user = new User();
        user.setId(22L);
        user.setPassword("userPassword");

        store = new Store();
        store.setId(1L);
        store.setUser(storeOwner);

        order = new Order();
        order.setId(1L);

        review = new Review();
        review.setOrder(order);
        review.setStoreId(store.getId());

        reviewComment = new ReviewComment("test", review);
    }

    @Test
    void saveReviewComment_Owner() {

        // Given
        ReviewCommentRequestDto dto = new ReviewCommentRequestDto("new test");
        user.setRole(Role.OWNER);
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.of(user));
        when(reviewRepository.findByOrderId(review.getOrder().getId())).thenReturn(java.util.Optional.of(review));
        when(storeRepository.findById(store.getId())).thenReturn(java.util.Optional.of(store));
        when(reviewCommentRepository.save(any(ReviewComment.class))).thenReturn(reviewComment);

        // When
        ReviewCommentResponseDto response = reviewCommentService.saveReviewComment(user.getId(), review.getOrder().getId(), dto);

        // Then
        assertNotNull(response);
        assertEquals("new test", response.getContent());
    }

    @Test
    void saveReviewComment_user() {

        // Given
        ReviewCommentRequestDto dto = new ReviewCommentRequestDto("new test");
        user.setRole(Role.USER);
        user.setId(22L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(reviewRepository.findByOrderId(review.getOrder().getId())).thenReturn(Optional.of(review));
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

        // When & Then
        BaseException exception = assertThrows(BaseException.class, () ->
                reviewCommentService.saveReviewComment(user.getId(), review.getOrder().getId(), dto)
        );
        assertEquals(ErrorCode.UNAUTHORIZED_USER_ID, exception.getErrorCode());
    }

    @Test
    void updateReviewComment_Owner() {

        // Given
        ReviewCommentRequestDto dto = new ReviewCommentRequestDto("update test");
        user.setRole(Role.OWNER);
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.of(user));
        when(reviewRepository.findByOrderId(review.getOrder().getId())).thenReturn(java.util.Optional.of(review));
        when(storeRepository.findById(store.getId())).thenReturn(java.util.Optional.of(store));
        when(reviewCommentRepository.findById(review.getOrder().getId())).thenReturn(java.util.Optional.of(reviewComment));

        // When
        ReviewCommentResponseDto response = reviewCommentService.updateReviewComment(user.getId(), review.getOrder().getId(), dto);

        // Then
        assertNotNull(response);
        assertEquals("update test", response.getContent());
    }

    @Test
    void updateReviewComment_user() {

        // Given
        ReviewCommentRequestDto dto = new ReviewCommentRequestDto("update test");
        user.setRole(Role.USER);
        user.setId(22L);

        when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.of(user));
        when(reviewRepository.findByOrderId(review.getOrder().getId())).thenReturn(java.util.Optional.of(review));
        when(storeRepository.findById(store.getId())).thenReturn(java.util.Optional.of(store));
        when(reviewCommentRepository.findById(review.getOrder().getId())).thenReturn(java.util.Optional.of(reviewComment));

        // When & Then
        BaseException exception = assertThrows(BaseException.class, () ->
                reviewCommentService.updateReviewComment(user.getId(), review.getOrder().getId(), dto)
        );
        assertEquals(ErrorCode.UNAUTHORIZED_USER_ID, exception.getErrorCode());
    }

    @Test
    void deleteReviewComment_Owner() {

        // Given
        String password = "hashedPassword";
        user.setRole(Role.OWNER);  // OWNER 역할을 부여
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.of(user));
        when(reviewRepository.findByOrderId(review.getOrder().getId())).thenReturn(java.util.Optional.of(review));
        when(storeRepository.findById(store.getId())).thenReturn(java.util.Optional.of(store));
        when(reviewCommentRepository.findById(review.getOrder().getId())).thenReturn(java.util.Optional.of(reviewComment));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        // When
        reviewCommentService.deleteReviewComment(user.getId(), review.getOrder().getId(), password);

        // Then
        verify(reviewCommentRepository, times(1)).deleteById(review.getOrder().getId());  // 삭제가 호출됐는지 확인
    }

    @Test
    void deleteReviewComment_user() {

        // Given
        String password = "hashedPassword";
        user.setRole(Role.USER);  // USER 역할을 부여
        user.setId(22L);  // 유저 ID 설정

        when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.of(user));
        when(reviewRepository.findByOrderId(review.getOrder().getId())).thenReturn(java.util.Optional.of(review));
        when(storeRepository.findById(store.getId())).thenReturn(java.util.Optional.of(store));
        when(reviewCommentRepository.findById(review.getOrder().getId())).thenReturn(java.util.Optional.of(reviewComment));

        // When & Then
        BaseException exception = assertThrows(BaseException.class, () ->
                reviewCommentService.deleteReviewComment(user.getId(), review.getOrder().getId(), password)
        );
        assertEquals(ErrorCode.UNAUTHORIZED_USER_ID, exception.getErrorCode());
    }

    @Test
    void deleteReviewComment_invalidPassword() {

        // Given
        String invalidPassword = "wrongPassword";
        user.setRole(Role.OWNER);  // OWNER 역할을 부여
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.of(user));
        when(reviewRepository.findByOrderId(review.getOrder().getId())).thenReturn(java.util.Optional.of(review));
        when(storeRepository.findById(store.getId())).thenReturn(java.util.Optional.of(store));
        when(reviewCommentRepository.findById(review.getOrder().getId())).thenReturn(java.util.Optional.of(reviewComment));
        when(passwordEncoder.matches(invalidPassword, user.getPassword())).thenReturn(false);

        // When & Then
        BaseException exception = assertThrows(BaseException.class, () ->
                reviewCommentService.deleteReviewComment(user.getId(), review.getOrder().getId(), invalidPassword)
        );
        assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }
}
