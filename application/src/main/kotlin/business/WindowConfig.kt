package business
import kotlinx.serialization.*

@Serializable
data class WindowConfig (
    var positionX: Double,
    var positionY: Double,
    var width: Double,
    var height: Double
)