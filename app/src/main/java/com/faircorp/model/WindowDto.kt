package com.faircorp.model

enum class Status { OPEN, CLOSED}

data class WindowDto(val id: Long, val name: String, val roomId: Long, val roomName:String ,val windowStatus: Status)