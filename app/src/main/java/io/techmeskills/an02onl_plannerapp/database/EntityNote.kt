package io.techmeskills.an02onl_plannerapp.database

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notes",
indices =  [Index(value = ["text"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("userName"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
open class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    var text: String,
    var date: String? = null,

    @ColumnInfo(index = true, name = "userName")
    val userName: String,
    val fromCloud: Boolean = false
) : Parcelable
