package com.pedroluis.authserver.users.responses

import com.pedroluis.authserver.users.User

data class UserResponse(
    val id: Long,
    val email: String,
    val name: String,
    val avatarUrl: String?
) {
    constructor(user: User) : this(user.id!!, user.email, user.name, user.avatarUrl)
}