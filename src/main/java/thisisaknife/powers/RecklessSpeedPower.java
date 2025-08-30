package thisisaknife.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static thisisaknife.ThisIsAKnife.makeID;

public class RecklessSpeedPower extends BasePower {
    public static final String POWER_ID = makeID("RecklessSpeedPower");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public RecklessSpeedPower(AbstractCreature owner, int newAmount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, newAmount);
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        if (card.type != AbstractCard.CardType.ATTACK) {
            return;
        }

        this.addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, -this.amount), -this.amount, true, AbstractGameAction.AttackEffect.NONE));

        if (!owner.hasPower("Artifact")) {
            this.addToBot(new ApplyPowerAction(owner, owner, new GainStrengthPower(owner, this.amount), this.amount, true, AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        addToBot(new GainEnergyAction(this.amount));
    }
}
