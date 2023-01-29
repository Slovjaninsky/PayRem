package com.payrem.backend.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.payrem.backend.entities.*

fun jsonToApplicationUser(jsonBody: String): ApplicationUser {
    return Gson().fromJson(jsonBody, ApplicationUser::class.java)
}

fun jsonArrayToApplicationUsers(jsonBody: String): List<ApplicationUser> {
    val typeToken = object : TypeToken<List<ApplicationUser>>() {}.type
    return Gson().fromJson(jsonBody, typeToken)
}


fun jsonToGroup(jsonBody: String): Group {
    return Gson().fromJson(jsonBody, Group::class.java)
}

fun jsonArrayToGroups(jsonBody: String): List<Group> {
    val typeToken = object : TypeToken<List<Group>>() {}.type
    return Gson().fromJson(jsonBody, typeToken)
}


fun jsonToReminder(jsonBody: String): Reminder {
    return Gson().fromJson(jsonBody, Reminder::class.java)
}

fun jsonArrayToReminders(jsonBody: String): ArrayList<Reminder> {
    val typeToken = object : TypeToken<ArrayList<Reminder>>() {}.type
    return Gson().fromJson(jsonBody, typeToken)
}
