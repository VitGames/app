package legs

class SonyLeg:ILeg {
    private val price = 110
    override fun step() {
        print("Шагает нога Sony")
    }

    override fun getPrice(): Int {
        return  price
    }
}