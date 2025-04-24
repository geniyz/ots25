package site.geniyz.ots.rim

import kotlinx.serialization.Serializable

@Serializable
data class ReceiveData(
    val game: Int = -1,
    val obj: String = "",
    val actions: List<ReceiveAction>
)

@Serializable
data class ReceiveAction(
    val id: String = "",
    val args: List<String> = emptyList(),
)

