package com.payrem.backend.exceptions

class FailedLoginException: RuntimeException {
    constructor() : super("Login failed")
    constructor(message: String) : super(message)
}