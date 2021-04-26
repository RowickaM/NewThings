package com.rowicka.newthings.recyclerViewSection.model

import java.time.LocalDateTime

data class BinItem(
    val id: Int = 0,
    val productName: String,
    val removedAt: LocalDateTime,
    val amount: Int
)