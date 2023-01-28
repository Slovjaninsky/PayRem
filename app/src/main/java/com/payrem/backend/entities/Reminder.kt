package com.payrem.backend.entities

class Reminder {

    private var id: Long = -1

    private var title: String = ""
    private var description: String = ""
    private var frequency: Int = 0
    private var dateStamp: String = "2000-01-01"
    private var timeStamp: String = "00:00"

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

    fun getTitle(): String {
        return title
    }

    fun getDateStamp(): String {
        return dateStamp
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