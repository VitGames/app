package hands

class SonyHand: IHand {
    private var price = 120
    override fun upHand() {
        println("Машет рукой от Сони")
    }

    override fun getPrice(): Int {
        return  price
    }
}