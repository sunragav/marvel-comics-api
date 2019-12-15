package com.sunragav.indiecampers.home.presentation.mapper


interface Mapper<T, E> {

    fun from(model: E): T

    fun to(entity: T): E

}
