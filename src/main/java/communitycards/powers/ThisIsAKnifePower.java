package communitycards.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Objects;

import static communitycards.CommunityCards.makeID;

public class ThisIsAKnifePower extends BasePower {
    public static final String POWER_ID = makeID("ThisIsAKnifePower");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public ThisIsAKnifePower(AbstractCreature owner, int newAmount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, newAmount);
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (Objects.equals(card.cardID, "Shiv")) {
            this.flash();
            this.addToBot(new DrawCardAction(amount));
            this.addToTop(new ModifyDamageAction(card.uuid, amount));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }
}
