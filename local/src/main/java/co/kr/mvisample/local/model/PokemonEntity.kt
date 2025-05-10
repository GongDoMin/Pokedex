package co.kr.mvisample.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imgUrl: String,
    val isDiscovered: Boolean,
    val isCaught: Boolean
)