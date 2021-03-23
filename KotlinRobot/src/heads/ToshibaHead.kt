package heads

class ToshibaHead: IHead {
    private var price = 100
    override fun speek() {
        println("Говорит голова Toshiba")
    }

    override fun getPrice(): Int {
        return price
    }
}