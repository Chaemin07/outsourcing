package com.example.outsourcing.favorite.service;

import com.example.outsourcing.common.exception.BaseException;
import com.example.outsourcing.common.exception.ErrorCode;
import com.example.outsourcing.favorite.dto.reponse.FavoriteResponseDto;
import com.example.outsourcing.favorite.entity.Favorite;
import com.example.outsourcing.favorite.repository.FavoriteRepository;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private User user;
    private Store store;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);

        store = new Store();
        store.setId(1L);
    }

    @Test
    public void testFavoriteStore_Success() {

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(storeRepository.findById(1L)).thenReturn(java.util.Optional.of(store));
        when(favoriteRepository.existsByUserAndStore(user, store)).thenReturn(false);
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(new Favorite(user, store));

        FavoriteResponseDto response = favoriteService.favoriteStore(1L, 1L);

        assertEquals("즐겨찾기가 완료되었습니다.", response.getMessage());
        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }

    @Test
    public void testFavoriteStore_Failed() {

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(storeRepository.findById(1L)).thenReturn(java.util.Optional.of(store));
        when(favoriteRepository.existsByUserAndStore(user, store)).thenReturn(true);

        BaseException exception = assertThrows(BaseException.class, () -> {
            favoriteService.favoriteStore(1L, 1L);
        });
        assertEquals(ErrorCode.CONFLICT_STATUS, exception.getErrorCode());
    }

    @Test
    public void  testDeleteFavorite_Success() {

        Favorite favorite = new Favorite(user, store);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(storeRepository.findById(1L)).thenReturn(java.util.Optional.of(store));
        when(favoriteRepository.findByUserAndStore(user, store)).thenReturn(java.util.Optional.of(favorite));

        favoriteService.deleteFavorite(1L, 1L);

        verify(favoriteRepository, times(1)).delete(favorite);
    }

    @Test
    public void testDeleteFavorite_Failed() {

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(storeRepository.findById(1L)).thenReturn(java.util.Optional.of(store));
        when(favoriteRepository.findByUserAndStore(user, store)).thenReturn(java.util.Optional.empty());

        BaseException exception = assertThrows(BaseException.class, () -> {
            favoriteService.deleteFavorite(1L, 1L);
        });
        assertEquals(ErrorCode.NOT_FAVORITE_STORE, exception.getErrorCode());
    }
}