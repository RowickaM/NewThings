package com.rowicka.newthings.recyclerViewSection.model

import java.time.LocalDateTime

sealed class BinType {
    data class Date(val date: LocalDateTime) : BinType()
    data class Bin(val value: BinItem) : BinType()
}