package communitycards.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.random.Random;

import java.util.Objects;

import static communitycards.CommunityCards.makeID;

public class MulticastPower extends BasePower {
    public static final String POWER_ID = makeID("MulticastPower");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private static final float BASE_MODIFIER = 0.3f;
    private static final float STRENGTH_MODIFIER = 0.02f;

    private int strength = 0;
    private boolean updateDescAfterTurn = false;

    public MulticastPower(AbstractCreature owner, int newAmount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, newAmount);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.purgeOnUse) {
            return;
        }

        if (card.type != AbstractCard.CardType.SKILL) {
            return;
        }

        int numMulticasts = 0;
        float procChance = getProcChance();

        for (int i = 0; i < this.amount; i++) {
            numMulticasts += new Random().random() <= procChance ? 1 : 0;
        }

        for (int i = 0; i < numMulticasts; i++) {
            AbstractCard tmp = card.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = (float) Settings.HEIGHT / 2.0F;
            if (m != null) {
                tmp.calculateCardDamage(m);
            }

            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
        }
    }

    public void setStrength() {
        AbstractPower strengthPower = owner.getPower("Strength");
        if (strengthPower == null) {
            strength = 0;
            return;
        }

        strength = strengthPower.amount;
    }

    public float getProcChance() {
        setStrength();
        return Math.min(Math.max(0f, BASE_MODIFIER + strength * STRENGTH_MODIFIER), 1f);
    }

    public int getProcChancePercent() {
        return Math.round(getProcChance() * 100f);
    }

    @Override
    public void updateDescription() {
        setStrength();
        this.description = DESCRIPTIONS[0] + getProcChancePercent() + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (Objects.equals(power.ID, StrengthPower.POWER_ID)) {
            updateDescription();
        }
        if (Objects.equals(power.ID, LoseStrengthPower.POWER_ID)) {
            this.updateDescAfterTurn = true;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && updateDescAfterTurn) {
            this.updateDescription();
            this.updateDescAfterTurn = false;
        }
    }
}
