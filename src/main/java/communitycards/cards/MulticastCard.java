package communitycards.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import communitycards.powers.MulticastPower;
import communitycards.util.CardStats;

public class MulticastCard extends BaseCard {
    public static final String ID = makeID("MulticastCard");
    private static final CardStats info = new CardStats(
            CardColor.RED, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.POWER, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    private static final int STACK_LIMIT = 1;
    private static final int STACK_LIMIT_UPGRADE = 1;

    public MulticastCard() {
        super(ID, info);
        setMagic(STACK_LIMIT, STACK_LIMIT_UPGRADE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new MulticastPower(p, magicNumber)));
    }
}
