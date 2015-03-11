package dyehard.Background;

import Engine.BaseCode;
import dyehard.DyehardRectangle;

public class CreditScreen {
    private final DyehardRectangle creditBack;
    private final DyehardRectangle credit;
    private final DyehardRectangle creditFront;

    public CreditScreen() {
        creditFront = new DyehardRectangle();
        creditFront.size.set(BaseCode.world.getWidth(),
                BaseCode.world.getHeight());
        creditFront.center.set(BaseCode.world.getWidth() / 2,
                BaseCode.world.getHeight() / 2);
        creditFront.texture = BaseCode.resources
                .getImage("Textures/UI/DyeHard_CreditsFront.png");
        creditFront.visible = false;

        credit = new DyehardRectangle();
        credit.size.set(BaseCode.world.getWidth() * 0.683f,
                BaseCode.world.getHeight());
        credit.center.set(BaseCode.world.getWidth() / 2,
                BaseCode.world.getHeight() / 2);
        credit.texture = BaseCode.resources
                .getImage("Textures/UI/DyeHard_CreditsScroll.png");
        credit.setPanning(true);
        credit.setPanningSheet(credit.texture, 300, 2, true);
        credit.visible = false;
        credit.overRide = true;
        credit.stopAtEnd = true;

        creditBack = new DyehardRectangle();
        creditBack.size.set(BaseCode.world.getWidth(),
                BaseCode.world.getHeight());
        creditBack.center.set(BaseCode.world.getWidth() / 2,
                BaseCode.world.getHeight() / 2);
        creditBack.texture = BaseCode.resources
                .getImage("Textures/UI/DyeHard_CreditsBack.png");
        creditBack.visible = false;
    }

    public void showScreen(boolean show) {
        if (show) {
            credit.setCurFrame(0);
            BaseCode.resources.moveToFrontOfDrawSet(creditBack);
            BaseCode.resources.moveToFrontOfDrawSet(credit);
            BaseCode.resources.moveToFrontOfDrawSet(creditFront);
        }
        creditFront.visible = show;
        credit.visible = show;
        creditBack.visible = show;
    }

    public boolean isShown() {
        return credit.visible;
    }
}
