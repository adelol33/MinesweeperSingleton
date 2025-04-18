package com.example.minesweeper.model

interface IRepository<TKey,TEntity> {
    fun read(key:TKey):TEntity
    fun save(entity:TEntity) {
    }
}
