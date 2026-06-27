package com.pedroluis.authserver.roles.responses

import com.pedroluis.authserver.roles.Role

data class RoleResponse(
    val name: String,
    val description: String,
) {
    constructor(role: Role) : this(role.name, role.description)
}