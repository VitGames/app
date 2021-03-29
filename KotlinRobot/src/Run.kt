import hands.SamsungHand
import hands.SonyHand
import hands.ToshibaHand
import heads.SamsungHead
import heads.SonyHead
import heads.ToshibaHead
import legs.SonyLeg
import legs.ToshibaLeg

fun main(args: Array<String>) {
    val getMaxPriceofRobot = GetMaxPriceofRobot()
    val samsungHand = SamsungHand()
    val samsungHead = SamsungHead()
    val sonyLeg = SonyLeg()
    val sonyHead = SonyHead()
    val sonyHand = SonyHand()
    val toshibaHead = ToshibaHead()
    val toshibaHand = ToshibaHand()
    val toshibaLeg = ToshibaLeg()
    val robot1 = Robot(samsungHead, samsungHand, sonyLeg)
    val robot2 = Robot(toshibaHead, toshibaHand, sonyLeg)
    val robot3 = Robot(sonyHead, sonyHand, toshibaLeg)
    val robots: MutableList<Robot> = mutableListOf()

    robots.add(robot1)
    robots.add(robot2)
    robots.add(robot3)

    println("Первый робот:")
    robot1.action()
    println("\nВторой робот:")
    robot2.action()
    println("\nТретий робот:")
    robot3.action()
    getMaxPriceofRobot.calcprice(robots)
}
