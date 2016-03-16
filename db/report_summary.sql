DROP TABLE IF EXISTS `cv2`.`report_summary`;
CREATE TABLE `cv2`.`report_summary` (
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

  `cpm_count_old` int(11) default 0,
  `cpm_count_new` int(11) default 0,
  `cpm_sitecommision_old` float(12,3) default 0,
  `cpm_sitecommision_new` float(12,3) default 0,
  `cpm_darwcommision_old` float(12,3) default 0,
  `cpm_darwcommision_new` float(12,3) default 0,

  `cpc_count_old` int(11) default 0,
  `cpc_count_new` int(11) default 0,
  `cpc_sitecommision_old` float(12,3) default 0,
  `cpc_sitecommision_new` float(12,3) default 0,
  `cpc_darwcommision_old` float(12,3) default 0,
  `cpc_darwcommision_new` float(12,3) default 0,

  `cpl_count_old` int(11) default 0,
  `cpl_count_new` int(11) default 0,
  `cpl_sitecommision_old` float(12,3) default 0,
  `cpl_sitecommision_new` float(12,3) default 0,
  `cpl_darwcommision_old` float(12,3) default 0,
  `cpl_darwcommision_new` float(12,3) default 0,

  `cps_count_old` int(11) default 0,
  `cps_count_new` int(11) default 0,
  `cps_sitecommision_old` float(12,3) default 0,
  `cps_sitecommision_new` float(12,3) default 0,
  `cps_darwcommision_old` float(12,3) default 0,
  `cps_darwcommision_new` float(12,3) default 0,

  `sitecommision_total_old` float(12,3) default 0,
  `sitecommision_total_new` float(12,3) default 0,
  `darwcommision_total_old` float(12,3) default 0,
  `darwcommision_total_new` float(12,3) default 0,

  `createdAt` datetime default NULL,
  `updatedAt` datetime default NULL,

  PRIMARY KEY  (`id`),
  UNIQUE KEY `rs_indexSummaryOnAdvertiseAffiliateIdAndSummaryDate` (`advertiseAffiliateId`,`subSiteId`,`summaryDate`),
  KEY `rs_indexSummaryOnSummaryDate` (`summaryDate`),
  KEY `rs_indexOfadvertiserId` (`advertiserId`),
  KEY `rs_indexOfcampaignId` (`campaignId`),
  KEY `rs_indexOfaffiliateId` (`affiliateId`),
  KEY `rs_indexOfsiteId` (`siteId`),
  KEY `rs_indexOfsubSiteId` (`subSiteId`),
  KEY `rs_indexOfadvertiseAffiliateId` (`advertiseAffiliateId`),
  KEY `rs_indexOfbannerId` (`bannerId`),
  KEY `rs_indexOfbalanceId` (`landingPageId`)
) ENGINE=InnoDB CHARSET=utf8;