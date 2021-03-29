package hands

class SamsungHand : IHand {
    private var price = 100
    override fun upHand() {
        println("Машет рукой от Самсунг")
    }

    override fun getPrice(): Int {
        return price
    }
}