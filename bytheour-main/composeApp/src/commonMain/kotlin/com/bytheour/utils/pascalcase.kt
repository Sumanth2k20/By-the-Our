package com.bytheour.utils

fun String.toPascalCase(): String {
    return this.split(" ", "_")
        .filter { it.isNotBlank() }
        .joinToString(" ") { part ->
            part.lowercase().replaceFirstChar { it.uppercase() }
        }
}
