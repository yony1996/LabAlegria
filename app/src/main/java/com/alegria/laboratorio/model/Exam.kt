package com.alegria.laboratorio.model

data class Exam(val id: Int, val name: String){
    override fun toString(): String {
        return name
    }
}