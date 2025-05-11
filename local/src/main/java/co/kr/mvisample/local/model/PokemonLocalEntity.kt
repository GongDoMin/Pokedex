package co.kr.mvisample.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon-local")
data class PokemonLocalEntity(
    @PrimaryKey val id: Int,
    val isDiscovered: Boolean,
    val isCaught: Boolean
)