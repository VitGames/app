package heads

class SamsungHead: IHead {
    private var price = 100
    override fun speek() {
        println("Говорит голова Samsung")
    }

    override fun getPrice(): Int {
        return price
    }
}