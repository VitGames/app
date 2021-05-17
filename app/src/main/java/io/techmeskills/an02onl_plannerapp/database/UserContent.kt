package io.techmeskills.an02onl_plannerapp.database

import androidx.room.Embedded
import androidx.room.Relation

class UserContent(

    @Embedded
    val user: User,

    @Relation(
        parentColumn = "name",
        entityColumn = "userName"
    )
    val notes: List<Note>
)