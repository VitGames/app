package heads

class SonyHead: IHead {
    private var price = 100
    override fun speek() {
        println("Говорит голова Sony")
    }

    override fun getPrice(): Int {
        return price
    }
}