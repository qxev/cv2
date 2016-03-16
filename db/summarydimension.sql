DROP TABLE IF EXISTS `cv2`.`summarydimension`;
CREATE TABLE `cv2`.`summarydimension` (
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

  `advertiseAffiliateId` int(11) default 0,
  `bannerId` int(11) default 0,
  `bannerDescription` varchar(255) default NULL,
  `landingPageId` int(11) default 0,
  `landingPageUrl` varchar(1024) default NULL,
  
  `createdAt` datetime default NULL,
  `updatedAt` datetime default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `sd_indexSummaryDimensionOnAdvertiseAffiliateId` (`advertiseAffiliateId`)
) ENGINE=MyISAM CHARSET=utf8;