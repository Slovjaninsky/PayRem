package com.payrem.backend.exceptions;

class PasswordMissmatchException : RuntimeException {
    constructor() : super("Passwords are different")
    constructor(message: String) : super(message)
}
