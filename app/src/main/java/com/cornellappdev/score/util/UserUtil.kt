package com.cornellappdev.score.util

import com.cornellappdev.score.auth.AuthUser

fun AuthUser.toWelcomeName(): String? =
    name.trim()
        .split(Regex("\\s+"))
        .firstOrNull()
        ?.takeIf { it.isNotBlank() }
