package co.kr.mvisample.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "pokemon-local",
    foreignKeys = [
        ForeignKey(
            entity = PokemonEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PokemonLocalEntity(
    @PrimaryKey val id: Int = 0,
    val iconUrl: String = "",
    val isCaught: Boolean = false,
    val order: Int? = null
)