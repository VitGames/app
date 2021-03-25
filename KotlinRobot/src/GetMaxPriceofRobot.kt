import kotlin.math.max

class GetMaxPriceofRobot {
    fun calcprice(robots: List<Robot>) {
        var max = robots[0].getPrice()
        for (robot in robots) {
            print("\n" + robot.getPrice())
            max = max(max, robot.getPrice())
        }
        println("\nСамый дорогой робот стоит:  $max")
    }
}