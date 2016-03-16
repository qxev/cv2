# CocoaMySQL dump
# Version 0.7b5
# http://cocoamysql.sourceforge.net
#
# Host: localhost (MySQL 5.1.23-rc)
# Database: cv2Development
# Generation Time: 2008-06-20 11:43:17 +0800
# ************************************************************

# Dump of table account
# ------------------------------------------------------------

CREATE TABLE `Account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `deleted` int(1) DEFAULT '0',
  `verified` int(1) DEFAULT '0',
  `bankName` varchar(255) DEFAULT NULL,
  `cardNumber` varchar(255) DEFAULT NULL,
  `idCardNumber` varchar(255) DEFAULT NULL,
  `postcode` int(11) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexAccountOnUserId` (`userId`),
  KEY `indexAccountOnDeleted` (`deleted`),
  KEY `indexAccountOnVerified` (`verified`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table accountSite
# ------------------------------------------------------------

CREATE TABLE `AccountSite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `siteId` int(11) DEFAULT NULL,
  `accountId` int(11) DEFAULT NULL,
  `defaultAccount` int(1) DEFAULT '0',
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexAccountSiteOnDefaultAccount` (`defaultAccount`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table advertise
# ------------------------------------------------------------

CREATE TABLE `Advertise` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ruleGroupId` int(11) DEFAULT NULL,
  `landingPageId` int(11) DEFAULT NULL,
  `bannerId` int(11) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexAdvertiseOnRuleGroupId` (`ruleGroupId`),
  KEY `indexAdvertiseOnLandingPageId` (`landingPageId`),
  KEY `indexAdvertiseOnBannerId` (`bannerId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table advertiseAffiliate
# ------------------------------------------------------------

CREATE TABLE `AdvertiseAffiliate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `advertiseId` int(11) DEFAULT NULL,
  `siteId` int(11) DEFAULT NULL,
  `trackCode` int(32) NOT NULL,
  `actived` int(1) DEFAULT '0',
  `status` int(1) DEFAULT '0',
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexAdvertiseAffiliateOnAdvertiseId` (`advertiseId`),
  KEY `indexAdvertiseAffiliateOnSiteId` (`siteId`),
  KEY `indexAdvertiseAffiliateOnTrackCode` (`trackCode`),
  KEY `indexAdvertiseAffiliateOnActived` (`actived`),
  KEY `indexAdvertiseAffiliateOnStatus` (`status`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table advertiserCategory
# ------------------------------------------------------------

CREATE TABLE `AdvertiserCategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table advertiserCategorySite
# ------------------------------------------------------------

CREATE TABLE `AdvertiserCategorySite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `advertiserCategoryId` int(11) DEFAULT NULL,
  `siteId` int(11) DEFAULT NULL,
  `categoryOrder` int(1) DEFAULT '0',
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `advCateUniqueIndex` (`advertiserCategoryId`,`siteId`),
  KEY `indexAdvertiserCategorySiteOnCategoryOrder` (`categoryOrder`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table affiliateCategory
# ------------------------------------------------------------

CREATE TABLE `AffiliateCategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table affiliateCategorySite
# ------------------------------------------------------------

CREATE TABLE `AffiliateCategorySite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `affiliateCategoryId` int(11) DEFAULT NULL,
  `siteId` int(11) DEFAULT NULL,
  `categoryOrder` int(1) DEFAULT '0',
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `affCateUniqueIndex` (`affiliateCategoryId`,`siteId`),
  KEY `indexAffiliateCategorySiteOnCategoryOrder` (`categoryOrder`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table banner
# ------------------------------------------------------------

CREATE TABLE `Banner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `campaignId` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `bannerType` int(1) DEFAULT '0',
  `actived` int(1) DEFAULT '0',
  `deleted` int(1) DEFAULT '0',
  `verified` int(1) DEFAULT '0',
  `description` varchar(255) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexBannerOnCampaignId` (`campaignId`),
  KEY `indexBannerOnActived` (`actived`),
  KEY `indexBannerOnDeleted` (`deleted`),
  KEY `indexBannerOnVerified` (`verified`),
  KEY `indexBannerOnWidth` (`width`),
  KEY `indexBannerOnHeight` (`height`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table bonus
# ------------------------------------------------------------

CREATE TABLE `Bonus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `affiliateId` int(11) DEFAULT NULL,
  `accountId` int(11) DEFAULT NULL,
  `bonusValue` int(11) DEFAULT '0',
  `description` varchar(255) DEFAULT NULL,
  `paid` int(1) DEFAULT '0',
  `paidDate` date DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexBonusOnAffiliateId` (`affiliateId`),
  KEY `indexBonusOnAccountId` (`accountId`),
  KEY `indexBonusOnPaid` (`paid`),
  KEY `indexBonusOnPaidDate` (`paidDate`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table campaign
# ------------------------------------------------------------

CREATE TABLE `Campaign` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `siteId` int(11) DEFAULT NULL,
  `actived` int(1) DEFAULT '0',
  `deleted` int(1) DEFAULT '0',
  `verified` int(1) DEFAULT '0',
  `cookieMaxage` int(11) DEFAULT NULL,
  `description` text,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexCampaignOnName` (`name`),
  KEY `indexCampaignOnUserId` (`userId`),
  KEY `indexCampaignOnSiteId` (`siteId`),
  KEY `indexCampaignOnDeleted` (`deleted`),
  KEY `indexCampaignOnActived` (`actived`),
  KEY `indexCampaignOnVerified` (`verified`),
  KEY `indexCampaignOnStartDate` (`startDate`),
  KEY `indexCampaignOnEndDate` (`endDate`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table campaignSite
# ------------------------------------------------------------

CREATE TABLE `CampaignSite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `siteId` int(11) DEFAULT NULL,
  `campaignId` int(11) DEFAULT NULL,
  `verified` int(1) DEFAULT '0',
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexCampaignSiteOnSiteId` (`siteId`),
  KEY `indexCampaignSiteOnCampaignId` (`campaignId`),
  KEY `indexCampaignSiteOnVerified` (`verified`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table commissionRule
# ------------------------------------------------------------

CREATE TABLE `CommissionRule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `campaignId` int(11) DEFAULT NULL,
  `commissionType` int(1) DEFAULT '0',
  `commissionValue` int(11) DEFAULT NULL,
  `actionType` int(1) DEFAULT '0',
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexCommissionRuleOnCommissionType` (`commissionType`),
  KEY `indexCommissionRuleOnCampaignId` (`campaignId`),
  KEY `indexCommissionRuleOnActionType` (`actionType`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table commissionRuleGroup
# ------------------------------------------------------------

CREATE TABLE `CommissionRuleGroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ruleGroupId` int(11) DEFAULT NULL,
  `commissionRuleId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueCommissionRuleGroup` (`ruleGroupId`,`commissionRuleId`),
  KEY `indexCommissionRuleGroupOnCommissionRuleId` (`commissionRuleId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table cpmTrackData
# ------------------------------------------------------------

CREATE TABLE `CpmTrackData` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `advertiseAffiliateId` int(11) DEFAULT NULL,
  `trackCode` varchar(255) DEFAULT NULL,
  `campaignId` int(11) DEFAULT NULL,
  `ruleGroupId` int(11) DEFAULT NULL,
  `bannerId` int(11) DEFAULT NULL,
  `landingPageId` int(11) DEFAULT NULL,
  `affiliateId` int(11) DEFAULT NULL,
  `advertiserId` int(11) DEFAULT NULL,
  `siteId` int(11) DEFAULT NULL,
  `trackType` int(1) DEFAULT '4',
  `trackStatus` int(1) DEFAULT '0',
  `trackStep` int(2) DEFAULT '0',
  `orderid` varchar(1024) DEFAULT NULL,
  `amounts` int(11) DEFAULT '0',
  `commission` int(11) DEFAULT '0',
  `commissionMatched` int(1) DEFAULT '0',
  `commissionConfirmed` int(1) DEFAULT '0',
  `trackIp` varchar(255) DEFAULT NULL,
  `referrerUrl` varchar(2048) DEFAULT NULL,
  `checked` int(1) DEFAULT '0',
  `checkedDate` date DEFAULT NULL,
  `paid` int(1) DEFAULT '0',
  `paidDate` date DEFAULT NULL,
  `trackTime` datetime DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cpmTrackDataUniqueIndex` (`advertiseAffiliateId`,`trackType`,`trackStep`,`trackIp`,`trackTime`),
  KEY `indexCpmTrackDataOnTrackType` (`trackType`),
  KEY `indexCpmTrackDataOnTrackStep` (`trackStep`),
  KEY `indexCpmTrackDataOnTrackTime` (`trackTime`),
  KEY `indexCpmTrackDataOnTrackIp` (`trackIp`),
  KEY `indexCpmTrackDataOnTrackStatus` (`trackStatus`),
  KEY `indexCpmTrackDataOnChecked` (`checked`),
  KEY `indexCpmTrackDataOnCheckedDate` (`checkedDate`),
  KEY `indexCpmTrackDataOnPaid` (`paid`),
  KEY `indexCpmTrackDataOnPaidDate` (`paidDate`),
  KEY `indexCpmTrackDataOnCommissionConfirmed` (`commissionConfirmed`),
  KEY `indexCpmTrackDataOnAffiliateId` (`affiliateId`),
  KEY `indexCpmTrackDataOnAdvertiserId` (`advertiserId`),
  KEY `indexCpmTrackDataOnCampaignId` (`campaignId`),
  KEY `indexCpmTrackDataOnRuleGroupId` (`ruleGroupId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table darwinProfit
# ------------------------------------------------------------

CREATE TABLE `DarwinProfit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `campaignId` int(11) DEFAULT NULL,
  `profitMonth` varchar(255) DEFAULT NULL,
  `profitType` int(1) DEFAULT '0',
  `profitValue` int(11) DEFAULT NULL,
  `description` date DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexDarwinProfitOnCampaignId` (`campaignId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table deposit
# ------------------------------------------------------------

CREATE TABLE `Deposit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `advertiserId` int(11) DEFAULT NULL,
  `accountId` int(11) DEFAULT NULL,
  `depositType` int(1) DEFAULT '0',
  `depositValue` int(11) DEFAULT NULL,
  `depositDate` date DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexDepositOnAdvertiserId` (`advertiserId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table groupRight
# ------------------------------------------------------------

CREATE TABLE `GroupRight` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) DEFAULT NULL,
  `groupLevel` int(1) DEFAULT '0',
  `privRead` int(1) DEFAULT '0',
  `privCreate` int(1) DEFAULT '0',
  `privDelete` int(1) DEFAULT '0',
  `privUpdate` int(1) DEFAULT '0',
  `description` varchar(255) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexGroupRightOnGroupId` (`groupId`),
  KEY `indexGroupRightOnGroupLevel` (`groupLevel`),
  KEY `indexGroupRightOnPrivRead` (`privRead`),
  KEY `indexGroupRightOnPrivCreate` (`privCreate`),
  KEY `indexGroupRightOnPrivDelete` (`privDelete`),
  KEY `indexGroupRightOnPrivUpdate` (`privUpdate`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table landingPage
# ------------------------------------------------------------

CREATE TABLE `LandingPage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `campaignId` int(11) DEFAULT NULL,
  `actived` int(1) DEFAULT '0',
  `deleted` int(1) DEFAULT '0',
  `verified` int(1) DEFAULT '0',
  `url` varchar(1024) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexLandingPageOnCampaignId` (`campaignId`),
  KEY `indexLandingPageOnActived` (`actived`),
  KEY `indexLandingPageOnDeleted` (`deleted`),
  KEY `indexLandingPageOnVerified` (`verified`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table monthSummary
# ------------------------------------------------------------

CREATE TABLE `MonthSummary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `advertiseAffiliateId` int(11) DEFAULT NULL,
  `summaryMonth` varchar(255) DEFAULT NULL,
  `confirmedOmmission` int(11) DEFAULT '0',
  `paid` int(1) DEFAULT '0',
  `paidDate` date DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `monthSummaryUniqueIndex` (`advertiseAffiliateId`,`summaryMonth`),
  KEY `indexMonthSummaryOnSummaryMonth` (`summaryMonth`),
  KEY `indexMonthSummaryOnPaid` (`paid`),
  KEY `indexMonthSummaryOnPaidDate` (`paidDate`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table ruleGroup
# ------------------------------------------------------------

CREATE TABLE `RuleGroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `verified` int(1) DEFAULT '0',
  `deleted` int(1) DEFAULT '0',
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexRuleGroupOnVerified` (`verified`),
  KEY `indexRuleGroupOnDeleted` (`deleted`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table site
# ------------------------------------------------------------

CREATE TABLE `Site` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `deleted` int(1) DEFAULT '0',
  `verified` int(1) DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `url` varchar(2048) DEFAULT 'http://www.',
  `logo` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexSiteOnUserId` (`userId`),
  KEY `indexSiteOnDeleted` (`deleted`),
  KEY `indexSiteOnVerified` (`verified`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table summary
# ------------------------------------------------------------

CREATE TABLE `Summary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `advertiseAffiliateId` int(11) DEFAULT NULL,
  `summaryDate` date DEFAULT NULL,
  `clicks` int(11) DEFAULT '0',
  `leads` int(11) DEFAULT '0',
  `validLeads` int(11) DEFAULT '0',
  `commission` int(11) DEFAULT '0',
  `confirmedCommission` int(11) DEFAULT '0',
  `checked` int(1) DEFAULT '0',
  `checkedDate` date DEFAULT NULL,
  `paid` int(1) DEFAULT '0',
  `paidDate` date DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `indexSummaryOnAdvertiseAffiliateIdAndSummaryDate` (`advertiseAffiliateId`,`summaryDate`),
  KEY `indexSummaryOnSummaryDate` (`summaryDate`),
  KEY `indexSummaryOnChecked` (`checked`),
  KEY `indexSummaryOnCheckedDate` (`checkedDate`),
  KEY `indexSummaryOnPaid` (`paid`),
  KEY `indexSummaryOnPaidDate` (`paidDate`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table summaryDimension
# ------------------------------------------------------------

CREATE TABLE `SummaryDimension` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `advertiseAffiliateId` int(11) DEFAULT NULL,
  `trackCode` varchar(32) DEFAULT NULL,
  `campaignId` int(11) DEFAULT NULL,
  `campaignName` varchar(255) DEFAULT NULL,
  `ruleGroupId` int(11) DEFAULT NULL,
  `ruleGroupName` varchar(255) DEFAULT NULL,
  `bannerId` int(11) DEFAULT NULL,
  `bannerDescription` varchar(255) DEFAULT NULL,
  `landingPageId` int(11) DEFAULT NULL,
  `landingPageUrl` varchar(255) DEFAULT NULL,
  `affiliateId` int(11) DEFAULT NULL,
  `affiliateName` varchar(255) DEFAULT NULL,
  `advertiserId` int(11) DEFAULT NULL,
  `advertiserName` varchar(255) DEFAULT NULL,
  `siteId` int(11) DEFAULT NULL,
  `siteName` varchar(255) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `indexSummaryDimensionOnAdvertiseAffiliateId` (`advertiseAffiliateId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table trackData
# ------------------------------------------------------------

CREATE TABLE `TrackData` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `advertiseAffiliateId` int(11) DEFAULT NULL,
  `trackCode` varchar(255) DEFAULT NULL,
  `campaignId` int(11) DEFAULT NULL,
  `ruleGroupId` int(11) DEFAULT NULL,
  `bannerId` int(11) DEFAULT NULL,
  `landingPageId` int(11) DEFAULT NULL,
  `affiliateId` int(11) DEFAULT NULL,
  `advertiserId` int(11) DEFAULT NULL,
  `siteId` int(11) DEFAULT NULL,
  `trackType` int(11) DEFAULT NULL,
  `trackStatus` int(1) DEFAULT '0',
  `trackStep` int(2) DEFAULT '0',
  `orderid` varchar(1024) DEFAULT NULL,
  `amounts` int(11) DEFAULT '0',
  `commission` int(11) DEFAULT '0',
  `commissionMatched` int(1) DEFAULT '0',
  `commissionConfirmed` int(1) DEFAULT '0',
  `trackIp` varchar(255) DEFAULT NULL,
  `referrerUrl` varchar(2048) DEFAULT NULL,
  `checked` int(1) DEFAULT '0',
  `checkedDate` date DEFAULT NULL,
  `paid` int(1) DEFAULT '0',
  `paidDate` date DEFAULT NULL,
  `trackTime` datetime DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `trackDataUniqueIndex` (`advertiseAffiliateId`,`trackType`,`trackStep`,`trackIp`,`trackTime`),
  KEY `indexTrackDataOnTrackType` (`trackType`),
  KEY `indexTrackDataOnTrackStep` (`trackStep`),
  KEY `indexTrackDataOnTrackTime` (`trackTime`),
  KEY `indexTrackDataOnTrackIp` (`trackIp`),
  KEY `indexTrackDataOnTrackStatus` (`trackStatus`),
  KEY `indexTrackDataOnChecked` (`checked`),
  KEY `indexTrackDataOnCheckedDate` (`checkedDate`),
  KEY `indexTrackDataOnPaid` (`paid`),
  KEY `indexTrackDataOnPaidDate` (`paidDate`),
  KEY `indexTrackDataOnCommissionConfirmed` (`commissionConfirmed`),
  KEY `indexTrackDataOnAffiliateId` (`affiliateId`),
  KEY `indexTrackDataOnAdvertiserId` (`advertiserId`),
  KEY `indexTrackDataOnCampaignId` (`campaignId`),
  KEY `indexTrackDataOnRuleGroupId` (`ruleGroupId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table user
# ------------------------------------------------------------

CREATE TABLE `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `nickName` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `userGroupId` int(11) DEFAULT NULL,
  `deleted` int(1) DEFAULT '0',
  `actived` int(1) DEFAULT '0',
  `activedAt` datetime DEFAULT NULL,
  `deletedAt` datetime DEFAULT NULL,
  `loginedAt` datetime DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `indexUserOnName` (`name`),
  UNIQUE KEY `indexUserOnNickName` (`nickName`),
  KEY `indexUserOnEmail` (`email`),
  KEY `indexUserOnGroupId` (`userGroupId`),
  KEY `indexUserOnDeleted` (`deleted`),
  KEY `indexUserOnActived` (`actived`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table userGroup
# ------------------------------------------------------------

CREATE TABLE `UserGroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexUserGroupOnName` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table userInfo
# ------------------------------------------------------------

CREATE TABLE `UserInfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `postcode` int(11) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `fax` varchar(255) DEFAULT NULL,
  `mobile` int(11) DEFAULT NULL,
  `qq` int(11) DEFAULT NULL,
  `msn` varchar(255) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexUserInfoOnUserId` (`userId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



