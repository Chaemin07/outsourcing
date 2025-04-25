package com.example.outsourcing.store.repository;

import com.example.outsourcing.store.entity.Store;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findAllByDeletedAtIsNull();

    default Store findByIdOrElseThrow(Long id) {
        Store store =
            findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다"));

        // 폐업 가게 검증
        if (store.getDeletedAt() != null) {
            throw new ResponseStatusException(HttpStatus.GONE, "이미 폐업한 가게입니다");
        }
        return store;
    }
}