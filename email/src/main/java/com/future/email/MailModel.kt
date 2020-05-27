package com.future.email

import java.util.*

data class MailModel(
    var mailServerHost: String = "", // email server ip
    var mailServerPort: String = "", // send email server port
    var fromAddress: String = "", // sender address
    var toAddress: String = "",    // receive address
    var userName: String = "", // send email username
    var password: String = "", // send email pwd
    var isValidate: Boolean = true,
    var subject: String = "", // email subject
    var content: String = "", // email content
    val properties: Properties = Properties()
) {
    companion object {

        private const val MAIL_SMTP_HOST = "mail.smtp.host"
        private const val MAIL_SMTP_PORT = "mail.smtp.port"
        private const val MAIL_SMTP_AUTH = "mail.smtp.auth"

        private const val TRUE = "true"
        private const val FALSE = "false"
    }

    fun init() {
        properties.setProperty(MAIL_SMTP_HOST, mailServerHost)
        properties.setProperty(MAIL_SMTP_PORT, mailServerPort)
        properties.setProperty(MAIL_SMTP_AUTH, if (isValidate) TRUE else FALSE)
    }
}