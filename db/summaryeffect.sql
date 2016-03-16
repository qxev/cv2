DROP TABLE IF EXISTS `cv2`.`summaryeffect`;
CREATE TABLE `cv2`.`summaryeffect` (
  `id` int(11) NOT NULL auto_increment,

  `advertiserId` int(11) default 0,
  `advertiserName` varchar(255) default NULL,
  `campaignId` int(11) default 0,
  `campaignName` varchar(255) default NULL,
  `affiliateId` int(11) default 0,
  `affiliateName` varchar(255) default NULL,
  `siteId` int(4) default 0,
  `siteName` varchar(255) default NULL,
  `siteUrl` varchar(255) default NULL,
  `subSiteId` varchar(255) default '',

  `advertiseAffiliateId` int(11) default 0,
  `bannerId` int(11) default 0,
  `bannerDescription` varchar(255) default NULL,
  `landingPageId` int(11) default 0,
  `landingPageUrl` varchar(1024) default NULL,

  `summaryDate` date NOT NULL,
  `trackStep` int(4) NOT NULL default 0,
  `dataTotalOld` int(11) default 0,
  `dataTotalNew` int(11) default 0,
  `createdAt` datetime default NULL,
  `updatedAt` datetime default NULL,

  PRIMARY KEY  (`id`),
  UNIQUE KEY `se_indexSummaryOnAdvertiseAffiliateIdAndSummaryDate` (`advertiseAffiliateId`,`subSiteId`,`summaryDate`,`trackStep`),
  KEY `se_indexSummaryOnSummaryDate` (`summaryDate`),
  KEY `se_indexOfadvertiserId` (`advertiserId`),
  KEY `se_indexOfcampaignId` (`campaignId`),
  KEY `se_indexOfaffiliateId` (`affiliateId`),
  KEY `se_indexOfsiteId` (`siteId`),
  KEY `se_indexOfsubSiteId` (`subSiteId`),
  KEY `se_indexOfadvertiseAffiliateId` (`advertiseAffiliateId`),
  KEY `se_indexOfbannerId` (`bannerId`),
  KEY `se_indexOfbalanceId` (`landingPageId`)
) ENGINE=InnoDB CHARSET=utf8;