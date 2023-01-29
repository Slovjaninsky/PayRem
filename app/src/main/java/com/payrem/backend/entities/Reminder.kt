package com.payrem.backend.entities

class Reminder {

    private var id: Long = -1

    private var title: String = ""
    private var description: String = ""
    private var frequency: Int = 0
    private var dateStamp: String = "2000-01-01"
    private var timeStamp: String = "00:00"
    private var isGroupReminder: Boolean = false
    private var groupId: Long = -1

    constructor()

    constructor(
        id: Long,
        title: String,
        description: String,
        frequency: Int,
        dateStamp: String,
        timeStamp: String
    ) {
        this.id = id
        this.title = title
        this.description = description
        this.frequency = frequency
        this.dateStamp = dateStamp
        this.timeStamp = timeStamp
    }

    constructor(
        title: String,
        description: String,
        frequency: Int,
        dateStamp: String,
        timeStamp: String
    ) {
        this.title = title
        this.description = description
        this.frequency = frequency
        this.dateStamp = dateStamp
        this.timeStamp = timeStamp
    }

    constructor(
        id: Long,
        title: String,
        description: String,
        frequency: Int,
        dateStamp: String,
        timeStamp: String,
        isGroupReminder: Boolean,
        groupId: Long
    ) {
        this.id = id
        this.title = title
        this.description = description
        this.frequency = frequency
        this.dateStamp = dateStamp
        this.timeStamp = timeStamp
        this.isGroupReminder = isGroupReminder
        this.groupId = groupId
    }

    fun getTitle(): String {
        return title
    }

    fun getDescription(): String {
        return description
    }

    fun getDateStamp(): String {
        return dateStamp
    }

    fun getId(): Long {
        return id
    }

    fun getTimeStamp(): String {
        return timeStamp
    }

    fun getFrequency(): Int {
        return frequency
    }

    fun isGroupReminder(): Boolean {
        return isGroupReminder
    }

    fun getGroupId(): Long {
        return groupId
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }
}