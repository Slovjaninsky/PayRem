package com.payrem.backend.api

import com.payrem.backend.exceptions.ServerException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

fun sendGet(toWhere: String) : String {
    var res = ""
    val url = URL(toWhere)
    with(url.openConnection() as HttpURLConnection) {
        requestMethod = "GET"
//        println("Sent 'GET' request to URL : $url; Response Code : $responseCode")
        if(responseCode == 409 || responseCode == 400 || responseCode == 404){
            throw ServerException(responseMessage)
        }
        inputStream.bufferedReader().use {
            it.lines().forEach { line -> res = res.plus(line + "\n") }
        }
    }
    return res
}

fun sendPost(toWhere: String, requestBody: String) : String {
    var res = ""
    val url = URL(toWhere)
    with(url.openConnection() as HttpURLConnection) {
        requestMethod = "POST"
        doInput = true
        setRequestProperty("Content-Type", "application/json")
        val outputStreamWriter = OutputStreamWriter(outputStream)
        outputStreamWriter.write(requestBody)
        outputStreamWriter.flush()
//        println("Sent 'POST' request to URL : $url, with body : $requestBody; Response Code : $responseCode")
        if(responseCode == 409 || responseCode == 400 || responseCode == 404){
            throw ServerException(responseMessage)
        }
        inputStream.bufferedReader().use {
            it.lines().forEach { line -> res = res.plus(line + "\n") }
        }
    }
    return res
}

fun sendPut(toWhere: String, requestBody: String) : String {
    var res = ""
    val url = URL(toWhere)
    with(url.openConnection() as HttpURLConnection) {
        requestMethod = "PUT"
        doInput = true
        setRequestProperty("Content-Type", "application/json")
        val outputStreamWriter = OutputStreamWriter(outputStream)
        outputStreamWriter.write(requestBody)
        outputStreamWriter.flush()
//        println("Sent 'PUT' request to URL : $url, with body : $requestBody; Response Code : $responseCode")
        if(responseCode == 409 || responseCode == 400 || responseCode == 404){
            throw ServerException(responseMessage)
        }
        inputStream.bufferedReader().use {
            it.lines().forEach { line -> res = res.plus(line + "\n") }
        }
    }
    return res
}

fun sendDelete(toWhere: String) : String {
    var res = ""
    val url = URL(toWhere)
    with(url.openConnection() as HttpURLConnection) {
        requestMethod = "DELETE"
        doInput = true
        setRequestProperty("Content-Type", "application/json")
        val outputStreamWriter = OutputStreamWriter(outputStream)
        outputStreamWriter.flush()
//        println("Sent 'DELETE' request to URL : $url; Response Code : $responseCode")
        if(responseCode == 409 || responseCode == 400 || responseCode == 404){
            throw ServerException(responseMessage)
        }
        inputStream.bufferedReader().use {
            it.lines().forEach { line -> res = res.plus(line + "\n") }
        }
    }
    return res
}
