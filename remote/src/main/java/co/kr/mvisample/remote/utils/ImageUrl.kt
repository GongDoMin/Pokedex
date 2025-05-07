package co.kr.mvisample.remote.utils

import co.kr.mvisample.remote.BuildConfig

fun getImageUrl(id: Int) = BuildConfig.POKEMON_IMAGE_URL + "$id.png"