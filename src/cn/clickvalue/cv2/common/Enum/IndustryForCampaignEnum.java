package cn.clickvalue.cv2.common.Enum;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

public enum IndustryForCampaignEnum {
	
	
	CLOTHING, SHOES_AND_HATS(CLOTHING), JEWELRY(CLOTHING), SUB_CLOTHING(CLOTHING),
	TRAVELING, TOUR(TRAVELING), HOTEL(TRAVELING), AIRLINE_TICKET(TRAVELING),
	OUTDOOR, OUTDOOR_ACTIVITY(OUTDOOR), FITNESS(OUTDOOR), SPORT(OUTDOOR), AUTO(OUTDOOR), 
	COMMERCE, FINANCE(COMMERCE), MARKETING(COMMERCE), MERCHANTS(COMMERCE), 
	REWARD, RESEARCH(REWARD), POINTS(REWARD), PRIZE(REWARD),  
	EDUCATION, LEARNING(EDUCATION), TRAINING(EDUCATION), RECRUITMENT(EDUCATION), 
	LITERATURE_AND_ART, BOOK(LITERATURE_AND_ART), LITERATURE(LITERATURE_AND_ART), PHOTOGRAPHY(LITERATURE_AND_ART),  
	ENTERTAINMENT, MUSIC(ENTERTAINMENT), FILM(ENTERTAINMENT), 	
	DATING, EMOTION(DATING), LIFE(DATING), MARRIAGE(DATING), SUB_DATING(DATING), 
	GAME, COMPETITION(GAME), MODEL(GAME), 
	FURNITURE, GIFT(FURNITURE), FLOWER(FURNITURE), HOUSEHOLD(FURNITURE), 
	HEALTH, MEDICAL(HEALTH), MEDICINE(HEALTH), ADULT_PRODUCTS(HEALTH), 
	WOMEN, FASHION(WOMEN), BEAUTY(WOMEN), 
	IT_DIGITAL, NETWORK(IT_DIGITAL), SOFTWARE_AND_HARDWARE(IT_DIGITAL), ELECTRONICS(IT_DIGITAL), 
	DOWNLOAD, RESOURCES(DOWNLOAD), 
	OTHERS;
	
	private IndustryForCampaignEnum parent;

	private List<IndustryForCampaignEnum> children;

	private static final List<IndustryForCampaignEnum> parentValues = CollectionFactory.newList();
	private static final List<IndustryForCampaignEnum> childValues = CollectionFactory.newList();
	private static final List<IndustryForCampaignEnum> otherValues = CollectionFactory.newList();

	static {
		for (IndustryForCampaignEnum industry : IndustryForCampaignEnum.values()) {
			if (industry.getChildren() != null && industry.getChildren().size() > 0) {
				parentValues.add(industry);
			} else if (industry.getParent() == null) {
				otherValues.add(industry);
			} else {
				childValues.add(industry);
			}
		}
	}

	IndustryForCampaignEnum(IndustryForCampaignEnum... parent) {
		if (parent != null && parent.length > 0) {
			this.parent = parent[0];
			parent[0].addChild(this);
		}
	}
	
	public IndustryForCampaignEnum getParent() {
		return parent;
	}

	public void setParent(IndustryForCampaignEnum parent) {
		this.parent = parent;
	}

	public List<IndustryForCampaignEnum> getChildren() {
		return children;
	}

	public void setChildren(List<IndustryForCampaignEnum> children) {
		this.children = children;
	}

	public void addChild(IndustryForCampaignEnum child) {
		if (this.getChildren() == null) {
			this.setChildren(new ArrayList<IndustryForCampaignEnum>());
		}
		this.getChildren().add(child);
	}

	public static List<IndustryForCampaignEnum> getParentValues() {
		return CollectionFactory.newList(parentValues);
	}

	public static List<IndustryForCampaignEnum> getChildValues() {
		return CollectionFactory.newList(childValues);
	}

	public static List<IndustryForCampaignEnum> getOtherValues() {
		return CollectionFactory.newList(otherValues);
	}

}
