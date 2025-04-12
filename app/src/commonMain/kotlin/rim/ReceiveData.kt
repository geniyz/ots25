package site.geniyz.ots.rim

import kotlinx.serialization.Serializable

@Serializable
data class ReceiveData(
    val game: String = "",
    val obj: String = "",
    val actions: List<ReceiveAction>
)

@Serializable
data class ReceiveAction(
    val id: String = "",
    val args: List<String> = emptyList(),
)

