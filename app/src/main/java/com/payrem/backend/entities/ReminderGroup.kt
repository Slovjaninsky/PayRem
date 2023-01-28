package com.payrem.backend.entities

class ReminderGroup {

    private var id: Long = -1

    private var name: String = ""

    private var users: Set<ApplicationUser> = HashSet()

    constructor()

    constructor(id: Long, name: String) {
        this.id = id
        this.name = name
    }

    constructor(name: String) {
        this.name = name
    }

    constructor(id: Long, name: String, usersSet: Set<ApplicationUser>) {
        this.id = id
        this.name = name
        this.users = usersSet
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReminderGroup

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "ExpenseGroup(id=$id, name='$name')"
    }

    fun getId(): Long {
        return this.id
    }

    fun setId(id: Long) {
        this.id = id
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getName(): String {
        return this.name
    }

    fun getUsers(): Set<ApplicationUser> {
        return this.users
    }

    fun setUsers(users: Set<ApplicationUser>) {
        this.users = users
    }
}