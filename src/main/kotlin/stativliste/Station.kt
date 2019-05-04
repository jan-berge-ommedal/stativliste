package stativliste

data class Station(
        val id: String,
        val name: String,
        val availableBikes: Int,
        val availableLocks: Int
)
