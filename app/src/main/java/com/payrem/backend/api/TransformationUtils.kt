package com.payrem.backend.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.payrem.backend.entities.*

fun jsonArrayToApplicationUsers(jsonBody: String): List<ApplicationUser> {
    val typeToken = object : TypeToken<List<ApplicationUser>>() {}.type
    return Gson().fromJson(jsonBody, typeToken)
}

fun jsonArrayToExpenses(jsonBody: String): ArrayList<Reminder> {
    val typeToken = object : TypeToken<ArrayList<Reminder>>() {}.type
    return Gson().fromJson(jsonBody, typeToken)
}

fun jsonArrayToExpenseGroups(jsonBody: String): List<ReminderGroup> {
    val typeToken = object : TypeToken<List<ReminderGroup>>() {}.type
    return Gson().fromJson(jsonBody, typeToken)
}

fun jsonToApplicationUser(jsonBody: String): ApplicationUser {
    return Gson().fromJson(jsonBody, ApplicationUser::class.java)
}

fun jsonToExpense(jsonBody: String): Reminder {
    return Gson().fromJson(jsonBody, Reminder::class.java)
}

fun jsonToExpenseGroup(jsonBody: String): ReminderGroup {
    return Gson().fromJson(jsonBody, ReminderGroup::class.java)
}


fun jsonToLoginAndPassword(jsonBody: String): LoginAndPassword {
    return Gson().fromJson(jsonBody, LoginAndPassword::class.java)
}

fun jsonArrayToNotification(jsonBody: String): List<Notification> {
    val typeToken = object : TypeToken<List<Notification>>() {}.type
    return Gson().fromJson(jsonBody, typeToken)
}
