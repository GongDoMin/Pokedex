package co.kr.mvisample.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon-local")
data class PokemonLocalEntity(
    @PrimaryKey val id: Int = 0,
    val iconUrl: String = "",
    val isCaught: Boolean = false,
    val order: Int? = null
)