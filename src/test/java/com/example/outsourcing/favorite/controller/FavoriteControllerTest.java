package com.example.outsourcing.favorite.controller;

import com.example.outsourcing.favorite.dto.reponse.FavoriteResponseDto;
import com.example.outsourcing.favorite.service.FavoriteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FavoriteService favoriteService;

    @InjectMocks
    private FavoriteController favoriteController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(favoriteController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testFavoriteStore() throws Exception {
        Long storeId = 1L;
        Long userId = 1L;

        FavoriteResponseDto responseDto = new FavoriteResponseDto(userId, storeId, "message");

        when(favoriteService.favoriteStore(userId, storeId)).thenReturn(responseDto);

        mockMvc.perform(post("/stores/{storeId}/favorites", storeId)
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("즐겨찾기 추가가 완료되었습니다."))
                .andExpect(jsonPath("$.data.storeId").value(storeId))
                .andExpect(jsonPath("$.data.userId").value(userId));

        verify(favoriteService, times(1)).favoriteStore(userId, storeId);
    }

    @Test
    public void testDeleteFavorite() throws Exception {
        Long storeId = 1L;
        Long userId = 1L;

        mockMvc.perform(delete("/stores/{storeId}/favorites", storeId)
                        .param("userId", String.valueOf(userId))) // userId를 직접 전달
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("즐겨찾기 추가가 완료되었습니다."));

        verify(favoriteService, times(1)).deleteFavorite(userId, storeId);
    }
}
