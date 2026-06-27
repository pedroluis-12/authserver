package com.pedroluis.authserver.users

import com.pedroluis.authserver.exceptions.BadRequestException

enum class SortDir {
    ASC, DESC;

    companion object {
        fun findOrNull(sortDir: String) = entries.find { it.name == sortDir.uppercase() }
        fun find(sortDir: String) = SortDir.findOrNull(sortDir) ?: throw BadRequestException("Invalid sort dir")
    }
}