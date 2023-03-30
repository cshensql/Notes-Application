package business
import kotlinx.serialization.*

// Copyright (c) 2023. Andy Yang, Benjamin Du, Charles Shen, Yuying Li
@Serializable
data class WindowConfig (
    var positionX: Double,
    var positionY: Double,
    var width: Double,
    var height: Double
)