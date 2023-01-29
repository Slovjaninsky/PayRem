package com.payrem.backend.entities

class Reminder {
    var id: Long = -1
    var name: String = ""
    var description: String = ""
    var date: String = "2000-01-01"
    var period: Long = 0
    var time: String = "00:00"
    var userId: Long = -1
    var groupId: Long = -1

    constructor(
        id: Long,
        name: String,
        description: String,
        date: String,
        period: Long,
        time: String,
        userId: Long,
        groupId: Long
    ) {
        this.id = id
        this.name = name
        this.description = description
        this.date = date
        this.period = period
        this.time = time
        this.userId = userId
        this.groupId = groupId
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Reminder

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (date != other.date) return false
        if (period != other.period) return false
        if (time != other.time) return false
        if (userId != other.userId) return false
        if (groupId != other.groupId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + period.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + groupId.hashCode()
        return result
    }

    override fun toString(): String {
        return "Reminder(id=$id, name='$name', description='$description', date='$date', period=$period, time='$time', userId=$userId, groupId=$groupId)"
    }
}
