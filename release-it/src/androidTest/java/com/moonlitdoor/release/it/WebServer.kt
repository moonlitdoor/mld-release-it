package com.moonlitdoor.release.it

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WebServer(val value: String = "null")
