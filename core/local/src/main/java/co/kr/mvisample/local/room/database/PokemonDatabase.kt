package co.kr.mvisample.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import co.kr.mvisample.local.model.PokemonEntity
import co.kr.mvisample.local.model.PokemonLocalEntity
import co.kr.mvisample.local.room.dao.PokemonDao
import co.kr.mvisample.local.room.dao.PokemonLocalDao

@Database(
    version = 1,
    entities = [PokemonEntity::class, PokemonLocalEntity::class]
)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao() : PokemonDao

    abstract fun pokemonLocalDao() : PokemonLocalDao
}