package communitycards.relics;

import com.megacrit.cardcrawl.actions.common.GainGoldAction;

import static communitycards.CommunityCards.makeID;

public class RelicBloodMoney extends BaseRelic {
    public static final String RELIC_ID = makeID("BloodMoney");

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public RelicBloodMoney() {
        super(RELIC_ID, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public int onLoseHpLast(int damageAmount) {
        super.onLoseHpLast(damageAmount);
        activate(damageAmount);
        return damageAmount;
    }

    private void activate(int damageAmount) {
        this.flash();
        addToTop(new GainGoldAction(damageAmount));
    }
}
