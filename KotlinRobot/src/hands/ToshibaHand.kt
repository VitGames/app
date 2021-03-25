package hands

class ToshibaHand: IHand {
    private var price = 90
    override fun upHand() {
        println("Машет рукой от Тошиба")
    }

    override fun getPrice(): Int {
        return price
    }
}