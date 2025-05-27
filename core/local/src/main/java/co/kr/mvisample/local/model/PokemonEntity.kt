package co.kr.mvisample.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int = 0,
    val name: String = "",
    val imgUrl: String = "",
    val page: Int = 0
)