package io.techmeskills.an02onl_plannerapp.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notes")
open class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    var text: String,
    var date: String? = null,
) : Parcelable
