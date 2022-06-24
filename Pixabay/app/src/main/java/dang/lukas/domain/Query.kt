package dang.lukas.domain

sealed interface Query

object Recent : Query

data class Search(val queryText: String) : Query