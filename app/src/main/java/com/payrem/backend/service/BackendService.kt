package com.payrem.backend.service

import android.os.StrictMode
import com.google.gson.Gson
import com.payrem.backend.api.*
import com.payrem.PreferencesData
import com.payrem.backend.entities.Reminder
import com.payrem.backend.entities.ReminderGroup
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class BackendService(
    private val preferences: PreferencesData
) {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun getPersonal(dateFrom: LocalDate, dateTo: LocalDate): List<ReminderItem> {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val list = ArrayList<ReminderItem>()

        return list
    }

    //TODO: change to entities
    fun getGroups(): List<ReminderGroup> {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val jsonBodyGroups = sendGet(
            "http://${preferences.serverIp}/users/${preferences.userId}/groups"
        )
        val groups: List<ReminderGroup> = jsonArrayToExpenseGroups(jsonBodyGroups)
        println(groups)

        //todo https://stackoverflow.com/questions/6343166/how-can-i-fix-android-os-networkonmainthreadexception#:~:text=Implementation%20summary
        return groups
    }

    fun getGroupById(id: Long): ReminderGroup {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val jsonBodyGroup = sendGet(
            "http://${preferences.serverIp}/groups/${id}"
        )

        //todo https://stackoverflow.com/questions/6343166/how-can-i-fix-android-os-networkonmainthreadexception#:~:text=Implementation%20summary
        return jsonToExpenseGroup(jsonBodyGroup)
    }

    fun getGroupList(groupId: Long): List<Reminder> {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val responseGroupList = if (groupId == -1L) "[]" else sendGet(
            //todo change to group choice
            "http://${preferences.serverIp}/groups/${groupId}/expenses"
        )

        return jsonArrayToExpenses(responseGroupList).sortedBy {
            LocalDate.parse(it.getDateStamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd")) }.reversed()
    }

    fun addPersonalReminder(name: String, desc: String, freq: Int, period: String, date: String, time: String) {
        val freqMap = mapOf("Day" to 1, "Month" to -1, "Year" to -12)
        Thread {
            try {
                val jsonBodyPOST = sendPost(
                    "http://${preferences.serverIp}/user/${preferences.userId}/add/reminder",
                    Gson().toJson(
                        Reminder(name, desc, freq * freqMap[period]!!, date, time)
                    )
                )
            }
            catch (e: Exception) {
                print(e.stackTrace)
            }
        }

    }
}

data class ReminderItem(
    var text: String,
    var expenses: ArrayList<Reminder>
) {
    constructor(reminder: Reminder) : this(
        reminder.getTitle(),
        arrayListOf(reminder)
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReminderItem

        if (text != other.text) return false

        return true
    }

    override fun hashCode(): Int {
        return text.hashCode()
    }
}
//endregion

