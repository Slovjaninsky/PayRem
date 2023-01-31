package com.payrem.backend.entities

class Group {
    var id: Long = -1
    var name: String = ""
    var description: String = ""

    constructor(id: Long, name: String, description: String) {
        this.id = id
        this.name = name
        this.description = description
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Group

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }

    override fun toString(): String {
        return "ReminderGroup(id=$id, name='$name', description='$description')"
    }
}
