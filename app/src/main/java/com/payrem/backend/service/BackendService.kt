package com.payrem.backend.service

import android.os.StrictMode
import com.google.gson.Gson
import com.payrem.backend.api.*
import com.payrem.PreferencesData
import com.payrem.backend.entities.ApplicationUser
import com.payrem.backend.entities.Reminder
import com.payrem.backend.entities.Group
import com.payrem.backend.exceptions.FailedLoginException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.time.format.DateTimeFormatter

class BackendService(
    private val preferences: PreferencesData
) {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun setPolicy() {
        //todo https://stackoverflow.com/questions/6343166/how-can-i-fix-android-os-networkonmainthreadexception#:~:text=Implementation%20summary
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    //region user
    fun getAllUsers(): List<ApplicationUser> {
        setPolicy()

        val jsonUsers = sendGet("http://${preferences.serverIp}/user")

        return jsonArrayToApplicationUsers(jsonUsers)
    }

    fun getUserById(userId: Long): ApplicationUser {
        setPolicy()

        val jsonUser = sendGet("http://${preferences.serverIp}/user/${userId}")

        return jsonToApplicationUser(jsonUser)
    }

    fun createUser(user: ApplicationUser): ApplicationUser {
        setPolicy()

        val jsonUser = sendPost("http://${preferences.serverIp}/user",
            "{\n" +
                    "    \"password\": \"${user.password}\",\n" +
                    "    \"name\": \"${user.name}\",\n" +
                    "    \"surname\": \"${user.surname}\",\n" +
                    "    \"email\": \"${user.email}\"\n" +
                    "}")

        return jsonToApplicationUser(jsonUser)
    }

    fun login(email: String, password: String): ApplicationUser {
        setPolicy()

        var res = ""
        val url = URL("http://${preferences.serverIp}/user/login")
        val requestBody = "{\n" +
                "    \"password\": \"${password}\",\n" +
                "    \"email\": \"${email}\"\n" +
                "}"
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            doInput = true
            doOutput = true
            setRequestProperty("Content-Type", "application/json")
            val outputStreamWriter = OutputStreamWriter(outputStream)
            outputStreamWriter.write(requestBody)
            outputStreamWriter.flush()
            println("Sent 'POST' request to URL : $url, with body : $requestBody; Response Code : $responseCode")
            if (responseCode == 401 || responseCode == 409 || responseCode == 400) {
                throw FailedLoginException("Invalid password for email: $email")
            }
            inputStream.bufferedReader().use {
                it.lines().forEach { line -> res = res.plus(line + "\n") }
            }
        }
        return jsonToApplicationUser(res)
    }

    fun addUserToGroup(userId: Long, groupId: Long) {
        setPolicy()

        sendPut("http://${preferences.serverIp}/user/${userId}/add/group/${groupId}", "")
    }

    fun getAllGroupOfUser(userId: Long): List<Group> {
        setPolicy()

        val jsonGroups = sendGet("http://${preferences.serverIp}/user/${userId}/groups")

        return jsonArrayToGroups(jsonGroups)
    }

    fun removeUserFromGroup(userId: Long, groupId: Long) {
        setPolicy()

        sendDelete("http://${preferences.serverIp}/user/${userId}/delete/group/${groupId}")
    }

    fun addReminderToUser(userId: Long, reminder: Reminder): Reminder {
        setPolicy()

        val jsonReminder = sendPost("http://${preferences.serverIp}/user/${userId}/add/reminder",
        "{\n" +
                "    \"name\": \"${reminder.name}\",\n" +
                "    \"description\": \"${reminder.description}\",\n" +
                "    \"period\": ${reminder.period},\n" +
                "    \"date\": \"${reminder.date}\",\n" +
                "    \"time\": \"${reminder.time}\"\n" +
                "}")

        return jsonToReminder(jsonReminder)
    }

    fun getAllRemindersOfUser(userId: Long): List<Reminder> {
        setPolicy()

        val jsonReminders = sendGet("http://${preferences.serverIp}/user/${userId}/reminders")

        return jsonArrayToReminders(jsonReminders)
    }

    fun deleteReminderFromUser(userId: Long, reminderId: Long) {
        setPolicy()

        sendDelete("http://${preferences.serverIp}/user/${userId}/delete/reminder/${reminderId}")
    }

    fun deleteUser(userId: Long) {
        setPolicy()

        sendDelete("http://${preferences.serverIp}/user/${userId}")
    }
    //endregion

    //region group
    fun getAllGroups(): List<Group> {
        setPolicy()

        val jsonGroups = sendGet("http://${preferences.serverIp}/group")

        return jsonArrayToGroups(jsonGroups)
    }

    fun getGroupById(groupId: Long): Group {
        setPolicy()

        val jsonGroup = sendGet("http://${preferences.serverIp}/group/${groupId}")

        return jsonToGroup(jsonGroup)
    }

    fun createGroup(group: Group): Group {
        setPolicy()

        val jsonGroup = sendPost("http://${preferences.serverIp}/group",
            "{\n" +
                    "    \"name\": \"${group.name}\",\n" +
                    "    \"description\": \"${group.description}\"\n" +
                    "}")

        return jsonToGroup(jsonGroup)
    }

    fun getAllUsersOfGroup(groupId: Long): List<ApplicationUser> {
        setPolicy()

        val jsonUsers = sendGet("http://${preferences.serverIp}/group/${groupId}/users")

        return jsonArrayToApplicationUsers(jsonUsers)
    }

    fun addReminderToGroup(groupId: Long, reminder: Reminder): Reminder {
        setPolicy()

        val jsonReminder = sendPost("http://${preferences.serverIp}/group/${groupId}/add/reminder",
        "{\n" +
                "    \"name\": \"${reminder.name}\",\n" +
                "    \"description\": \"${reminder.description}\",\n" +
                "    \"period\": ${reminder.period},\n" +
                "    \"date\": \"${reminder.date}\",\n" +
                "    \"time\": \"${reminder.time}\"\n" +
                "}")

        return jsonToReminder(jsonReminder)
    }

    fun getAllRemindersOfGroup(groupId: Long): List<Reminder> {
        setPolicy()

        val jsonReminders = sendGet("http://${preferences.serverIp}/group/${groupId}/reminders")

        return jsonArrayToReminders(jsonReminders)
    }

    fun deleteReminderFromGroup(groupId: Long, reminderId: Long) {
        setPolicy()

        sendDelete("http://${preferences.serverIp}/group/${groupId}/delete/reminder/${reminderId}")
    }

    fun deleteGroup(userId: Long) {
        setPolicy()

        sendDelete("http://${preferences.serverIp}/group/${userId}")
    }
    //endregion

    //region reminder
    fun editReminder(reminder: Reminder): Reminder {
        setPolicy()

        val jsonReminder = sendPut("http://${preferences.serverIp}/reminder",
            "{\n" +
                    "    \"id\": \"${reminder.id}\",\n" +
                    "    \"name\": \"${reminder.name}\",\n" +
                    "    \"description\": \"${reminder.description}\",\n" +
                    "    \"period\": ${reminder.period},\n" +
                    "    \"date\": \"${reminder.date}\",\n" +
                    "    \"time\": \"${reminder.time}\"\n" +
                    "}")

        return jsonToReminder(jsonReminder)
    }
    //endregion
}
