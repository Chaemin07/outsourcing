package com.example.outsourcing.menu.repository;

import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.menu.entity.MenuOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface MenuOptionRepository extends JpaRepository<MenuOption, Long> {

    // 특정 메뉴(menu_id)에 속하는 옵션 전체 조회 (삭제 안 된 것만)
    List<MenuOption> findAllByMenuIdAndDeletedAtIsNull(Long menuId);

    // 옵션 하나 조회할 때도 삭제 안 된 것만 조회
    MenuOption findByIdAndDeletedAtIsNull(Long id);

    default MenuOption findByIdOrElseThrow(Long menuOpitonId) {
        return findById(menuOpitonId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다"));

    }
}
