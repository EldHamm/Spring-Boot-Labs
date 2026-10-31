package com.hammatov.MySecurity2DbThemeleaf.repository;

import com.hammatov.MySecurity2DbThemeleaf.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // Возвращает null, если роли с таким названием нет
    Role findByName(String name);

}
