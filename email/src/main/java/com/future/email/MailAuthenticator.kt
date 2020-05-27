package com.future.email

import javax.mail.Authenticator
import javax.mail.PasswordAuthentication


class MailAuthenticator(
    private val userName: String,
    private val password: String
) : Authenticator() {

    override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(userName, password)
    }
}
