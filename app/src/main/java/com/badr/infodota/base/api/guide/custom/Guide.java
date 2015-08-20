package com.badr.infodota.base.api.guide.custom;

import java.io.Serializable;

/**
 * User: Histler
 * Date: 28.01.14
 */
public class Guide implements Serializable {
    private String Hero;
    private String Title;
    private String GuideRevision;
    private String FileVersion;
    private ItemBuild ItemBuild;
    private AbilityBuild AbilityBuild;

    public Guide() {
    }

    public String getHero() {
        return Hero;
    }

    public void setHero(String hero) {
        Hero = hero;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getGuideRevision() {
        return GuideRevision;
    }

    public void setGuideRevision(String guideRevision) {
        GuideRevision = guideRevision;
    }

    public String getFileVersion() {
        return FileVersion;
    }

    public void setFileVersion(String fileVersion) {
        FileVersion = fileVersion;
    }

    public ItemBuild getItemBuild() {
        return ItemBuild;
    }

    public void setItemBuild(ItemBuild itemBuild) {
        ItemBuild = itemBuild;
    }

    public AbilityBuild getAbilityBuild() {
        return AbilityBuild;
    }

    public void setAbilityBuild(AbilityBuild abilityBuild) {
        AbilityBuild = abilityBuild;
    }
}