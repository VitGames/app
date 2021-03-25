package legs

class ToshibaLeg:ILeg {
    private val price = 110
    override fun step() {
        print("Шагает нога Toshiba")
    }

    override fun getPrice(): Int {
       return price
    }
}