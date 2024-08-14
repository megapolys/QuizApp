package com.example.servingwebcontent.persistence;

import com.example.servingwebcontent.model.Role;

public interface RolePersistence {
	Role findRoleByName(String name);
}
