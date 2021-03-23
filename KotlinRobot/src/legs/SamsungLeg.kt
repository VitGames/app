package legs

class SamsungLeg: ILeg {
    private val price = 110
    override fun step() {
        print("Шагает нога Samsung")
    }

    override fun getPrice(): Int {
        return price
    }
}