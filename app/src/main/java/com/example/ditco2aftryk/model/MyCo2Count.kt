package com.example.ditco2aftryk.model

data class MyCo2Count(val size: Int) {

    override fun toString(): String {
        return "MyCo2Count(size = $size)"
    }
}

// Here we can define a DAO as a public interface