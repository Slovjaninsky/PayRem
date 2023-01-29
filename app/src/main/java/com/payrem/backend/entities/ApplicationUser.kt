package com.payrem.backend.entities

class ApplicationUser {
    var id: Long = -1
    var password: String = ""
    var name: String = ""
    var surname: String = ""
    var email: String = ""

    constructor(id: Long, password: String, name: String, surname: String, email: String) {
        this.id = id
        this.password = password
        this.name = name
        this.surname = surname
        this.email = email
    }

    constructor(password: String, name: String, surname: String, email: String) {
        this.password = password
        this.name = name
        this.surname = surname
        this.email = email
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApplicationUser

        if (id != other.id) return false
        if (password != other.password) return false
        if (name != other.name) return false
        if (surname != other.surname) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + surname.hashCode()
        result = 31 * result + email.hashCode()
        return result
    }

    override fun toString(): String {
        return "ApplicationUser(id=$id, password='$password', name='$name', surname='$surname', email='$email')"
    }
}


