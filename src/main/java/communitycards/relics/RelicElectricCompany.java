package communitycards.relics;

import basemod.helpers.RelicType;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.blue.Zap;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PandorasBox;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.ArrayList;
import java.util.Iterator;

import static communitycards.CommunityCards.makeID;

public class RelicElectricCompany extends BaseRelic {
    public static final String RELIC_ID = makeID("ElectricCompany");
    public int count = 0;
    private boolean calledTransform = true;

    public RelicElectricCompany() {
        super(RELIC_ID, RelicTier.UNCOMMON, LandingSound.CLINK);
        relicType = RelicType.BLUE;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        Iterator<AbstractCard> cards = AbstractDungeon.player.masterDeck.group.iterator();
        while (cards.hasNext()) {
            AbstractCard card = cards.next();
            if (card.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
                cards.remove();
                this.count++;
            }
        }

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        if (this.count > 0) {
            for (int i = 0; i < this.count; i++) {
                AbstractCard zap = new Zap();

                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    r.onPreviewObtainCard(zap);
                }

                group.addToBottom(zap);
            }
            AbstractDungeon.gridSelectScreen.openConfirmationGrid(group, this.DESCRIPTIONS[1]);
        }
    }

    public void update() {
        super.update();
        if (!this.calledTransform && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.GRID) {
            this.calledTransform = true;
            AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
        }

    }

}
