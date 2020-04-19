package com.blogspot.android_czy_java.apps.mgr.main.chat.exception

import io.reactivex.rxjava3.annotations.NonNull

class MessageNotSentException : @NonNull Throwable() {
    override val message = "Podczas wysyłania Twojej wiadomości wystąpił błąd"

}
