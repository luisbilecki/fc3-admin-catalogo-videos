package com.fullcycle.admin.catalog.domain.exceptions

import com.fullcycle.admin.catalog.domain.validation.handler.Notification

class NotificationException(message: String?, notification: Notification) :
    DomainException(message, notification.getErrors())