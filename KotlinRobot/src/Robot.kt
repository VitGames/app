import hands.IHand
import heads.IHead
import legs.ILeg

class Robot(var head: IHead, var hand: IHand, var leg: ILeg) : IRobot {


    override fun action() {
        head.speek()
        hand.upHand()
        leg.step()
    }

    override fun getPrice(): Int {
        return head.getPrice() + hand.getPrice() + leg.getPrice()
    }

}

