package omnia.string

interface Escapist {
    fun escape(string: String?): String?
    fun unescape(string: String?): String?
}