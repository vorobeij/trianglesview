package io.github.jdiemke.triangulation

class NotEnoughPointsException : Exception {

    constructor()

    constructor(s: String) : super(s)

    companion object {
        private const val serialVersionUID = 7061712854155625067L
    }
}